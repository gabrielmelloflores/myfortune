package com.gabrielflores.myfortune.model.user;

import com.gabrielflores.myfortune.model.entity.EntidadeBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

/** 
* @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
* @Date: 2024-08-07 09:38:49  
*/
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Audited
@DynamicUpdate
@Table(name = "usuarios")
@NamedEntityGraph(name = "usuarioPerfis", attributeNodes = @NamedAttributeNode("perfis"))
public class Usuario extends EntidadeBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "seq_usuarios", allocationSize = 1)
    private Long id;

    @NotEmpty
    @ToString.Include
    @Column(name = "nome")
    private String nome;

    @NotEmpty
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "email")
    private String email;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "dt_nascimento")
    private LocalDate nascimento;

    @Column(name = "genero")
    @Convert(converter = Genero.Converter.class)
    private Genero genero;

    @NotEmpty
    @Column(name = "senha")
    private String senha;

    @Column(name = "url_foto")
    private String foto;

    @NotNull
    @Column(name = "provider")
    @Convert(converter = Provider.Converter.class)
    private Provider provider;

    @NotNull
    @ToString.Include
    @Column(name = "ativo")
    private Boolean ativo;

    @NotNull
    @ToString.Include
    @Column(name = "confirmado")
    private Boolean confirmado;
    
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuarios_perfis",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_perfil"))
    private Set<Perfil> perfis;

    public Usuario addPerfil(Perfil perfil) {
        if (this.perfis == null) {
            this.perfis = new HashSet<>();
        }
        this.perfis.add(perfil);
        return this;
    }

    public Usuario removePerfil(Perfil perfil) {
        if (this.perfis == null) {
            this.perfis = new HashSet<>();
        }
        this.perfis.removeIf(p -> p.getNome().equals(perfil.getNome()));
        return this;
    }

    public boolean hasPerfil(String nomePerfil) {
        return this.perfis.stream().anyMatch(p -> p.getNome().equals(nomePerfil));
    }

    public boolean isLocal() {
        return Provider.LOCAL.equals(this.provider);
    }

    public String getDataNascimento() {
        return Optional.ofNullable(this.nascimento)
                .map(data -> data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .orElse(null);
    }

    public String getCPFSemFormatacao() {
        return Optional.ofNullable(this.cpf).map(doc -> doc.replaceAll("[^\\d]", "")).orElse(null);
    }

    public boolean isAdm() {
        return hasPerfil("ADMIN");
    }
}
