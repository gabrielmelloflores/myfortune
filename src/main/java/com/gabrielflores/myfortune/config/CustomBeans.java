package com.gabrielflores.myfortune.config;

import com.gabrielflores.myfortune.password.ConfigurablePasswordChecker;
import com.gabrielflores.myfortune.password.PasswordChecker;
import com.gabrielflores.myfortune.password.rules.BlacklistRule;
import com.gabrielflores.myfortune.password.rules.DigitRule;
import com.gabrielflores.myfortune.password.rules.LengthRule;
import com.gabrielflores.myfortune.password.rules.LetterRule;
import com.gabrielflores.myfortune.password.rules.PreviousPasswordRule;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class CustomBeans {

    public static final Locale PT_BR_LOCALE = Locale.of("pt", "BR");

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:Mensagens");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Bean(name = "dateFormat")
    public DateFormat dateFormat() {
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        return dateFormat;
    }

    @Bean(name = "dateTimeFormat")
    public DateFormat dateTimeFormat() {
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateFormat.setLenient(false);
        return dateFormat;
    }

    @Bean(name = "timeFormat")
    public DateFormat timeFormat() {
        final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setLenient(false);
        return dateFormat;
    }

    @Bean(name = "localDateFormat")
    public DateTimeFormatter localDateFormat() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    @Bean(name = "localDateTimeFormat")
    public DateTimeFormatter localDateTimeFormat() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    }

    @Bean(name = "localTimeFormat")
    public DateTimeFormatter localTimeFormat() {
        return DateTimeFormatter.ofPattern("HH:mm:ss");
    }

    @Bean(name = "locale")
    public Locale locale() {
        return PT_BR_LOCALE;
    }

    @Bean(name = "velocityEngine")
    public VelocityEngine velocityEngine() {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loaders", "file,classpath");
        velocityEngine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
        return velocityEngine;
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "passwordChecker")
    public PasswordChecker passwordChecker() {
        return new ConfigurablePasswordChecker()
                .addRule(new LengthRule(8, 32))
                .addRule(new BlacklistRule())
                .addRule(new DigitRule())
                .addRule(new LetterRule())
//                .addRule(new LowercaseRule())
//                .addRule(new UppercaseRule())
//                .addRule(new SymbolRule())
//                .addRule(new DadosPessoaisRule())
                .addRule(new PreviousPasswordRule(passwordEncoder()));
    }

}
