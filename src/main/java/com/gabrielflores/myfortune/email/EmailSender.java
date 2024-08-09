package com.gabrielflores.myfortune.email;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;
import com.amazonaws.services.simpleemail.AWSJavaMailTransport;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {

    private static final String TEMPLATE_EMAIL = "velocity/template_email.html";

    private final Environment environment;
    private final VelocityEngine velocityEngine;

    @Value("${sac.aws.access.key}")
    private String AWS_ACCESS_KEY;

    @Value("${sac.aws.access.secret.key}")
    private String AWS_SECRET_ACCESS_KEY;

    @Value("${sac.email.remetente}")
    private String EMAIL_REMETENTE;

    private final String AWS_HOST = "email.sa-east-1.amazonaws.com";

    public void enviaEmailPadrao(final String destinatario, final TipoEmail tipoEmail, final Map<String, Object> velocityParams, final Map<String, String> imageParams) {
        final Template template = velocityEngine.getTemplate(TEMPLATE_EMAIL);
        velocityParams.put("conteudo_email", "velocity/" + tipoEmail.getModelo());
        enviaEmail(destinatario, tipoEmail, velocityParams, imageParams, template);
    }

    public void enviaEmailEspecifico(final String destinatario, final TipoEmail tipoEmail, final Map<String, Object> velocityParams, final Map<String, String> imageParams) {
        final Template template = velocityEngine.getTemplate("velocity/" + tipoEmail.getModelo());
        enviaEmail(destinatario, tipoEmail, velocityParams, imageParams, template);
    }

    private void enviaEmail(final String destinatario, final TipoEmail tipoEmail, final Map<String, Object> velocityParams, final Map<String, String> imageParams, final Template template) {
        final StringWriter writer = new StringWriter();
        final Context context = new VelocityContext(velocityParams);
        template.merge(context, writer);
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "aws");
            props.setProperty("mail.aws.host", AWS_HOST);
            props.setProperty("mail.aws.user", AWS_ACCESS_KEY);
            props.setProperty("mail.aws.password", AWS_SECRET_ACCESS_KEY);

            Session session = Session.getInstance(props);
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(EMAIL_REMETENTE, environment.getProperty("app.name")));
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(destinatario));
            mimeMessage.setSubject(tipoEmail.getAssunto());

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(writer.toString(), "text/html; charset=utf-8");

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            for (Map.Entry<String, String> image : imageParams.entrySet()) {
                MimeBodyPart imageBodyPart = new MimeBodyPart();
                try {
                    File imageFile = new ClassPathResource("/images/" + image.getValue()).getFile();
                    imageBodyPart.attachFile(imageFile);
                    imageBodyPart.setContentID("<" + image.getKey() + ">");
                    imageBodyPart.setDisposition(MimeBodyPart.INLINE);
                    multipart.addBodyPart(imageBodyPart);
                } catch (IOException e) {
                    log.error("Erro ao anexar imagem " + image.getValue(), e);
                }
            }

            mimeMessage.setContent(multipart);

            try (Transport transport = new AWSJavaMailTransport(session, null)) {
                transport.connect();
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            } catch (Exception e) {
                log.error("Erro enviando email com AWS SES " + tipoEmail, e);
            }

        } catch (MessagingException | UnsupportedEncodingException | MailException e) {
            log.error("Erro enviando email " + tipoEmail, e);
        }
    }
}
