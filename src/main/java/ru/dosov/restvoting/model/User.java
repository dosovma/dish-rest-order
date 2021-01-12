package ru.dosov.restvoting.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static org.hibernate.validator.constraints.SafeHtml.WhiteListType.NONE;

@Entity
@Table(name = "users")
public class User extends NamedEntity {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Vote> votes;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User(
            Integer id,
            @NotNull @Length(min = 2, max = 256) String name,
            @Email @NotBlank @Size(max = 100) String email,
            @NotBlank @Size(min = 5, max = 100) String password,
            Set<Vote> votes,
            Set<Role> roles
    ) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.votes = votes;
        this.roles = roles;
    }

    public User() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", votes=" + votes +
                ", roles=" + roles +
                '}';
    }
}
