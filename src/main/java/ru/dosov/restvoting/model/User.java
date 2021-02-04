package ru.dosov.restvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.util.StringUtils;
import ru.dosov.restvoting.model.AbstractEntity.NamedEntity;
import ru.dosov.restvoting.util.JsonDeserializers;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Entity
@Table(name = "users")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends NamedEntity {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotEmpty
    @Size(max = 100)
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonDeserialize(using = JsonDeserializers.PasswordDeserializer.class)
    private String password;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "roles"}, name = "user_roles_unique")})
    @Column(name = "roles")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User(
            Integer id,
            @NotNull String name,
            @Email @NotBlank @Size(max = 100) String email,
            @NotBlank @Size(min = 5, max = 100) String password,
            Set<Role> roles
    ) {
        super(id, name, true);
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = StringUtils.hasText(email) ? email.toLowerCase() : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    public Boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
