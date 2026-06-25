package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@ToString(callSuper = true)
public class AppUser extends SystemUser {

    @Valid
    @NotNull(message = "Address cannot be null!")
    @Embedded
    private Address address;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past!")
    private LocalDate birthDate;

    @NotNull(message = "Admin status cannot be null!")
    @Builder.Default
    private Boolean isAdmin = false;

    @Transient
    public Integer getAdsPerHour(SystemConfig config) {
        if (config == null) throw new IllegalArgumentException("System configuration cannot be null!");
        if (this.subscription != null) {
            return 0;
        }
        return config.getFreeTierAdsPerHour();
    }

    @Transient
    public Integer getSkipLimitPerHour(SystemConfig config) {
        if (config == null) throw new IllegalArgumentException("System configuration cannot be null!");
        if (this.subscription != null) {
            return -1;
        }
        return config.getFreeTierSkipLimit();
    }

    @Transient
    public Boolean isMinor() {
        int age = Period.between(this.birthDate, LocalDate.now()).getYears();
        return age < 18;
    }

    @Transient
    public Integer getMySubscriptionCost(SystemConfig config) {

        if (config == null) {
            throw new IllegalArgumentException("System configuration cannot be null!");
        }
        if (this.subscription != null) {
            return this.subscription.getCost(config);
        }
        return 0;
    }


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "subscription_id", referencedColumnName = "id")
    private Subscription subscription;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Playlist> playlists = new HashSet<>();


    public void assignSubscription(Subscription subscription) {
        if (subscription == null) {
            throw new IllegalArgumentException("Subscription cannot be null!");
        }

        this.subscription = subscription;

        if (subscription.getAppUser() != this) {
            subscription.setAppUser(this);
        }
    }

    public void addPlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist cannot be null!");
        }
        this.playlists.add(playlist);

        if (playlist.getAppUser() != this) {
            playlist.setAppUser(this);
        }
    }

    /**
     * Adds the specified audio content to a user's playlist.
     * <p>
     * This method ensures that the playlist belongs to the user executing the action
     * before delegating the operation to the {@link Playlist} object, which enforces
     * additional business rules (e.g., age restrictions and content publication status).
     * </p>
     *
     * @param content the audio content (e.g., song or podcast episode) to be added; must not be null
     * @param playlist the target playlist where the content will be added; must not be null
     * @throws IllegalArgumentException if either the content or the playlist is null
     * @throws SecurityException if the user attempts to modify a playlist they do not own
     */
    public void addAudioContentToPlaylist(AudioContent content, Playlist playlist) {
        if (content == null || playlist == null) {
            throw new IllegalArgumentException("Audio content and playlist cannot be null!");
        }

        if (!this.playlists.contains(playlist)) {
            throw new SecurityException("You cannot edit a playlist that does not belong to you!");
        }

        playlist.addAudioContent(content);
    }

}
