package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.UniqueElements;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "sys_user")
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name cannot be empty!")
    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 characters!")
    @Pattern(regexp = "^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+$", message = "First name can only contain letters!")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty!")
    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters!")
    @Pattern(regexp = "^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+$", message = "Last name can only contain letters!")
    private String lastName;

    @NotBlank(message = "Username cannot be empty!")
    @Column(unique = true)
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters!")
    private String userName;

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Email format is invalid!")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password cannot be empty!")
    @Size(min = 4, max = 16, message = "Password must be between 4 and 16 characters!")
    private String hashPassword;
}
