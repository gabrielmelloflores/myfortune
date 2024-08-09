package com.gabrielflores.myfortune.dto.pagseguro.dadospessoais;

import com.gabrielflores.myfortune.model.user.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ClientePagSeguro {

    /**
     * Nome do cliente.
     */
    @NotNull
    @JsonProperty("name")
    private String nome;

    /**
     * E-mail do cliente.
     */
    @NotNull
    @JsonProperty("email")
    private String email;

    /**
     * Número do documento do cliente, CPF com 11 dígitos e CNPJ com 14 dígitos
     * numéricos. Não aceita máscaras.
     */
    @NotNull
    @JsonProperty("tax_id")
    private String cpf;

    /**
     * Contém uma lista de telefones do cliente.
     */
    //@NotNull
    @JsonProperty("phones")
    private List<TelefonePagSeguro> telefones;

    /**
     * Algumas APIs pedem somente 1 telefone, então usar essa ou a com lista
     */
    @JsonProperty("phone")
    private TelefonePagSeguro telefone;

    public ClientePagSeguro(final Usuario usuario) {
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.cpf = usuario.getCPFSemFormatacao();
        //this.telefones = List.of(new TelefonePagSeguro(usuario.getTelefone()));
        this.telefone = Optional.ofNullable(usuario.getTelefone()).map(tel -> new TelefonePagSeguro(tel)).orElse(null);
    }

    @JsonIgnore
    public boolean isDadosCompletos() {
        return StringUtils.isNotBlank(this.nome)
                && StringUtils.isNotBlank(this.email)
                && this.cpf != null
                && this.telefone != null;
    }
}
