package com.gabrielflores.myfortune.dto.pagseguro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@JsonInclude(Include.NON_NULL)
public class PagSeguroObject {

    /**
     * ID no PagSeguro. Disponível somente no response
     */
    @JsonProperty("id")
    protected String id;
    /**
     * Opcional. Identificador na sua aplicação. Até 65 caracteres com espaços
     * ignorados
     */
    @JsonProperty("reference_id")
    protected String idInterno;

    /**
     * Para ser usado como chave de idem potência
     *
     * @return hash para identificar a transação como única
     */
    @JsonIgnore
    public final String getMD5() {
        return Optional.ofNullable(getMD5StringBuilder()).map(builder -> DigestUtils.md5Hex(builder)).orElse(null);
    }

    /**
     * Descendentes precisam implementar esse método para retonar os campos que
     * compoem o MD5
     *
     * @return campos que fazem parte do identificar único
     */
    @JsonIgnore
    protected String getMD5StringBuilder() {
        return null;
    }
}
