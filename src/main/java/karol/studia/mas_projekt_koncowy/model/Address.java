package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Address {

    @NotBlank(message = "City cannot be empty!")
    private String city;

    @NotBlank(message = "Country cannot be empty!")
    private String country;
}
