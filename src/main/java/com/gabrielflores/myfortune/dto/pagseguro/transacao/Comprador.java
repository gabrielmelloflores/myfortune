package com.gabrielflores.myfortune.dto.pagseguro.transacao;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import java.util.Collection;
import lombok.Data;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Comprador {

    /**
     * E-mail do comprador. Informa o e-mail do comprador que realizou a
     * transação. <br>
     * Presença: Obrigatória.<br>
     * Tipo: Texto.<br>
     * Formato: um e-mail válido (p.e., usuario@site.com.br), com no máximo 60
     * caracteres.
     */
    @XmlElement(name = "name")
    private String nome;

    /**
     * Nome completo do comprador. Informa o nome completo do comprador que
     * realizou o pagamento.<br>
     * Presença: Opcional.<br>
     * Tipo: Texto.<br>
     * Formato: No mínimo duas sequências de caracteres, com o limite total de
     * 50 caracteres.
     */
    @XmlElement(name = "email", required = true)
    private String email;

    /**
     * Documentos pessoais de identificação do comprador
     */
    @XmlElementWrapper(name = "documents")
    @XmlElement(name = "document")
    private Collection<Documento> documentos;
}
