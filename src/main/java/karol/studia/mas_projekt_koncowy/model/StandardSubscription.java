package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
public class StandardSubscription extends Subscription{

    @NotNull(message = "Download limit cannot be null!")
    @Min(value = 0, message = "Download limit cannot be negative!")
    private Integer downloadLimit;

    @NotNull(message = "Max active devices cannot be null!")
    @Min(value = 1, message = "Must have at least 1 active device!")
    private Integer maxActiveDevices;

    /**
     * Calculates and returns the final cost of the standard subscription.
     * <p>
     * The total cost is dynamically calculated by adding an extra charge for offline listening capabilities
     * to the base subscription price. The system charges an additional 100 grosze (1 PLN)
     * for every unit of the declared offline download limit.
     * </p>
     *
     * @param config the system configuration containing the base subscription price; must not be null
     * @return the calculated total cost of the standard subscription in grosze
     * @throws IllegalArgumentException if the provided system configuration is null
     */
    @Override
    public Integer getCost(SystemConfig config){
        if (config == null) {
            throw new IllegalArgumentException("System configuration cannot be null!");
        }
        return config.getSubscriptionBasePrice() + 100 * this.downloadLimit;
    }
}
