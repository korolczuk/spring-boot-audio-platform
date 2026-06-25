package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"playlist_id", "audio_content_id"}))
public class PlaylistEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Added date cannot be null!")
    @PastOrPresent(message = "Added date cannot be in the future!")
    @Builder.Default
    private LocalDate addedDate = LocalDate.now();


    @NotNull(message = "Playlist cannot be null!")
    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Playlist playlist;

    @NotNull(message = "Audio content cannot be null!")
    @ManyToOne
    @JoinColumn(name = "audio_content_id", nullable = false)
    @ToString.Exclude
    private AudioContent audioContent;


    public void setPlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new IllegalArgumentException("The playlist cannot be null!");
        }
        this.playlist = playlist;

        if (!playlist.getPlaylistEntry().contains(this)) {
            playlist.addEntry(this);
        }
    }
    public void setAudioContent(AudioContent audioContent) {
        if (audioContent == null) {
            throw new IllegalArgumentException("The track (AudioContent) cannot be null!");
        }
        this.audioContent = audioContent;

        if (!audioContent.getPlaylistEntries().contains(this)) {
            audioContent.getPlaylistEntries().add(this);
        }
    }
}
