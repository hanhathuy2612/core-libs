package com.hnh.enterprise.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hnh.enterprise.core.constants.Constants;
import com.hnh.enterprise.core.enumeration.AuthProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "app_user")
public class User extends AbstractAuditingEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;
    
    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;
    
    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;
    
    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;
    
    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @NotNull
    @Column(nullable = false)
    @Builder.Default
    private boolean activated = false;
    
    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;
    
    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;
    
    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;
    
    @Column(name = "reset_date")
    @Builder.Default
    private Instant resetDate = null;
    
    @Column(name = "auth_provider", columnDefinition = "default 'DEFAULT'")
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
    
    @Builder.Default
    @Column(name = "two_factor_auth")
    private boolean twoFactorAuth = false;
    
    @Column(name = "password_change_reminder")
    private boolean passwordChangeReminder;
    
    @Column(name = "password_changed_date")
    private LocalDate passwordChangedDate;
    
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "base_user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")}
    )
    @BatchSize(size = 20)
    @ToString.Exclude
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }
    
    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
