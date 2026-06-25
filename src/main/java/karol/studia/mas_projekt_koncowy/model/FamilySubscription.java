package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
public class FamilySubscription extends Subscription{

    @NotNull(message = "Number of members cannot be null!")
    @Min(value = 1, message = "Family must have at least 1 member!")
    private Integer numberOfMembers;

    /**
     * Calculates and returns the final cost of the family subscription.
     * <p>
     * The total cost is dynamically calculated by adding a fixed fee per family member to the base subscription price.
     * To prevent abuse, the number of billed members is strictly capped at a maximum limit defined in the system configuration.
     * The additional fee is set to 1000 grosze (10 PLN) per verified member.
     * </p>
     *
     * @param config the system configuration containing the base subscription price and the maximum allowed number of family members; must not be null
     * @return the calculated total cost of the family subscription in grosze
     * @throws IllegalArgumentException if the provided system configuration is null
     */
    @Override
    public Integer getCost(SystemConfig config){
        if (config == null) {
            throw new IllegalArgumentException("System configuration cannot be null!");
        }
        Integer price = 0;
        Integer verificatedNumberOfMembers = this.numberOfMembers;

        if(numberOfMembers > config.getFamilyMaxMembers()){
            verificatedNumberOfMembers = config.getFamilyMaxMembers();
        }

        price += (1000 * verificatedNumberOfMembers);


        return config.getSubscriptionBasePrice() + price;
    }
}
