package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
public class PodcastEpisode extends AudioContent{

    @NotEmpty(message = "Episode must have at least one tag!")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "podcast_episode_tag", joinColumns = @JoinColumn(name = "audio_content_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<String> tag = new HashSet<>();

    @NotBlank(message = "Episode description cannot be empty!")
    @Size(max = 200, message = "Episode description cannot exceed 200 characters!")
    @Column(length = 200)
    private String episodeDescription;
}
