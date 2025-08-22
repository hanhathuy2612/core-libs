package com.hnh.enterprise.core.dto;

import com.hnh.enterprise.core.constants.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Set;

/**
 * A DTO representing a user, with his authorities.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends AuditingDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Builder.Default
    private boolean activated = false;

    @Size(min = 2, max = 10)
    private String langKey;

    private String phoneNumber;

    private Set<String> authorities;

    private boolean twoFactorAuth;

    private boolean passwordChangeReminder;

    private LocalDate passwordChangedDate;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
