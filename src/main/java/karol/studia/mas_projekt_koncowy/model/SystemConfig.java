package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InterruptedIOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SystemConfig {

    @Id
    private Long id;

    @NotNull(message = "Subscription base price cannot be null!")
    @Min(value = 0, message = "Subscription base price cannot be negative!")
    @Builder.Default
    private Integer subscriptionBasePrice = 3000;

    @NotNull(message = "Family max members cannot be null!")
    @Min(value = 1, message = "Family max members must be at least 1!")
    @Builder.Default
    private Integer familyMaxMembers = 6;

    @NotNull(message = "Free tier ads per hour limit cannot be null!")
    @Min(value = 0, message = "Ads limit cannot be negative!")
    @Builder.Default
    private Integer freeTierAdsPerHour = 5;

    @NotNull(message = "Free tier skip limit cannot be null!")
    @Min(value = 0, message = "Skip limit cannot be negative!")
    @Builder.Default
    private Integer freeTierSkipLimit = 6;
}
