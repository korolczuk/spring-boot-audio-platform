package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Playlist name cannot be empty!")
    @Size(min = 2, max = 20, message = "Playlist name must be between 2 and 20 characters!")
    private String name;

    @Size(max = 100, message = "Playlist description cannot exceed 100 characters!")
    @Column(length = 100)
    private String description;

    @NotNull(message = "Creation date cannot be null!")
    @PastOrPresent(message = "Creation date cannot be in the future!")
    @Builder.Default
    private LocalDate createDate = LocalDate.now();


    @NotNull(message = "Playlist owner cannot be null!")
    @ManyToOne(optional = false)
    @JoinColumn(name = "app_user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.NONE)
    private AppUser appUser;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("addedDate ASC")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PlaylistEntry> playlistEntry = new HashSet<>();


    public void addEntry(PlaylistEntry playlistEntry) {
        if (playlistEntry == null) {
            throw new IllegalArgumentException("PlaylistEntry cannot be null!");
        }
        this.playlistEntry.add(playlistEntry);

        if (playlistEntry.getPlaylist() != this) {
            playlistEntry.setPlaylist(this);
        }
    }

    /**
     * Adds the specified audio content to the playlist.
     * <p>
     * This method enforces critical business rules:
     * 1. The audio content must be in the PUBLISHED state.
     * 2. If the playlist owner is a minor, explicit content cannot be added.
     * It also ensures the content is not already present in the playlist before creating
     * a new {@link PlaylistEntry}.
     * </p>
     *
     * @param content the audio content (e.g., song or podcast episode) to be added; must not be null
     * @throws IllegalArgumentException if the content is null or if it already exists in the playlist
     * @throws IllegalStateException if the content is not PUBLISHED or if a minor attempts to add explicit content
     */
    public void addAudioContent(AudioContent content) {
        if (content == null) {
            throw new IllegalArgumentException("Audio content cannot be null!");
        }

        if (content.getStatus() != ContentStatus.PUBLISHED) {
            throw new IllegalStateException("You cannot add Unpublished content");
        }

        if (this.appUser != null && this.appUser.isMinor() && content.getIsExplicit()) {
            throw new IllegalStateException("Explicit content is restricted for minors!");
        }

        boolean alreadyExists = this.playlistEntry.stream()
                .anyMatch(entry -> entry.getAudioContent().equals(content));

        if (alreadyExists) {
            throw new IllegalArgumentException("This track is already on the playlist!");
        }

        PlaylistEntry newEntry = PlaylistEntry.builder()
                .build();

        newEntry.setAudioContent(content);

        this.addEntry(newEntry);
    }
}
