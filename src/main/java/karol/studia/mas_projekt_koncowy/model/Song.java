package karol.studia.mas_projekt_koncowy.model;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@ToString(callSuper = true)
public class Song extends AudioContent{

    @NotEmpty(message = "Song must have at least one genre!")
    @ElementCollection(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<String> genre = new HashSet<>();

    @NotBlank(message = "Label cannot be empty!")
    private String label;

}
