package com.gabrielflores.myfortune.dto.pagseguro.transacao;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Documento {

    /**
     * CPF ou CNPJ
     */
    @XmlElement(name = "type")
    private String tipo;

    /**
     * Número do documento
     */
    @XmlElement(name = "value")
    private String numero;
}
