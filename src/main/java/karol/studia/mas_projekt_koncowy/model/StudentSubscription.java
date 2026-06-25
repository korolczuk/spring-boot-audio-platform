package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
public class StudentSubscription extends Subscription{
    @NotBlank(message = "Student number cannot be empty!")
    @Column(unique = true)
    private String studentNumber;

    @NotBlank(message = "University name cannot be empty!")
    private String universityName;

    @NotNull(message = "Last verification date cannot be null!")
    @PastOrPresent(message = "Verification date cannot be in the future!")
    private LocalDate lastVerificationDate;

    /**
     * Calculates and returns the final cost of the student subscription.
     * <p>
     * The cost is dynamically calculated based on the student's active verification status.
     * A constant discount of 1000 grosze (10 PLN) is applied to the base subscription price if, and only if, two conditions are met simultaneously:
     * 1. The last verification date is less than 6 months old.
     * 2. The declared university name contains one of the authorized keywords ("uniwersytet", "akademia", "uczelnia", or "politechnika").
     * If these conditions are not met, the standard base price is charged without any discounts.
     * </p>
     *
     * @param config the system configuration containing the base subscription price; must not be null
     * @return the calculated final cost of the student subscription in grosze
     * @throws IllegalArgumentException if the provided system configuration is null
     */
    @Override
    public Integer getCost(SystemConfig config){
        if (config == null) {
            throw new IllegalArgumentException("System configuration cannot be null!");
        }
        boolean isVerificated = ChronoUnit.MONTHS.between(this.lastVerificationDate, LocalDate.now()) < 6;

        boolean isUniversityAllowed = universityName.toLowerCase().contains("uniwersytet")
                || universityName.toLowerCase().contains("akademia")
                || universityName.toLowerCase().contains("uczelnia")
                || universityName.toLowerCase().contains("politechnika");

        Integer price = 0;

        price += config.getSubscriptionBasePrice();
        if(isUniversityAllowed && isVerificated){
            price -= 1000;
        }
        return price;
    }

}
