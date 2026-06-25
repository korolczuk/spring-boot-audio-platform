package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class PodcastCreator extends Creator {

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "podcast_creator_tag", joinColumns = @JoinColumn(name = "creator_id"))
    @NotEmpty(message = "Podcast creator must have at least one tag!")
    @Builder.Default
    private Set<String> tag = new HashSet<>();

    @NotBlank(message = "Main recording language cannot be empty!")
    private String mainRecordingLanguage;

    @NotBlank(message = "Typical upload day cannot be empty!")
    private String typicalUploadDay;

    @Transient
    public Integer getEpisodeCount() {
        if (getAudioContent() == null) {
            return 0;
        }
        return (int) getAudioContent().stream()
                .filter(content -> content instanceof PodcastEpisode)
                .count();
    }

    public void addPodcastEpisode(PodcastEpisode episode) {
        if (episode == null) {
            throw new IllegalArgumentException("Podcast episode cannot be null!");
        }
        super.addAudioContent(episode);
    }

}
