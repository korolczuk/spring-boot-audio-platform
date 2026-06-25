package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import karol.studia.mas_projekt_koncowy.validation.ValidMusicCreatorAttributes;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@ToString(callSuper = true)
@ValidMusicCreatorAttributes
public class MusicCreator extends Creator {

    @NotBlank(message = "Main genre cannot be empty!")
    private String mainGenre;

    @ElementCollection(targetClass = MusicCreatorRole.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "music_creator_role", joinColumns = @JoinColumn(name = "creator_id"))
    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "Music creator must have at least one role!")
    @Builder.Default
    private Set<MusicCreatorRole> role = new HashSet<>();


    private String vocalType;

    private String vocalRange;


    private String mainInstrument;

    private String instrumentFamily;

    @Transient
    public Integer getSongCount() {
        if (getAudioContent() == null) {
            return 0;
        }
        return (int) getAudioContent().stream()
                .filter(content -> content instanceof Song)
                .count();
    }

    public void addSong(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("Song cannot be null!");
        }
        super.addAudioContent(song);
    }

}
