package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Card ID cannot be empty!")
    @Pattern(regexp = "^\\d{24}$", message = "Card ID must be exactly 24 digits!")
    private String cardID;

    @Transient
    public abstract Integer getCost(SystemConfig config);


    @OneToOne(mappedBy = "subscription")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AppUser appUser;


    public void setAppUser(AppUser appUser) {
        if (appUser == null) {
            throw new IllegalArgumentException("AppUser cannot be null!");
        }

        this.appUser = appUser;

        if (appUser.getSubscription() != this) {
            appUser.assignSubscription(this);
        }
    }

}
