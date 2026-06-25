package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AudioContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty!")
    private String title;

    @NotNull(message = "Duration cannot be null!")
    @Min(value = 0, message = "Duration cannot be negative!")
    @Max(value = 6000, message = "Duration cannot exceed 6000 seconds!")
    private Integer duration;

    @NotNull(message = "Release date cannot be null!")
    @PastOrPresent(message = "Release date cannot be in the future!")
    private LocalDate releaseDate;

    @NotNull(message = "Explicit flag cannot be null!")
    private Boolean isExplicit;



    @NotNull(message = "Content status cannot be null!")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ContentStatus status = ContentStatus.SUBMITTED;

    /**
     * Converts the raw audio duration in seconds into a user-friendly string format (e.g., "3:45").
     * <p>
     * This method calculates the total minutes and remaining seconds, ensuring that single-digit
     * seconds are correctly padded with a leading zero for proper display.
     * </p>
     *
     * @return the formatted duration string in "MM:SS" format
     */
    public String getFormatedDuration(){
        Integer minutes = (int)this.duration/60;
        Integer seconds = (int)this.duration%60;
        if(seconds < 10){
            return minutes + ":0" + seconds;
        }else {
            return minutes + ":" + seconds;
        }
    }


    /**
     * Approves the submitted audio content and changes its status to PUBLISHED.
     * <p>
     * This action transitions the content's lifecycle state. It requires administrator privileges
     * and can only be performed if the content is currently in the SUBMITTED state.
     * </p>
     *
     * @param adminUser the user attempting to approve the content; must have administrator privileges
     * @throws SecurityException if the provided user does not have administrator privileges
     * @throws IllegalStateException if the content is not in the SUBMITTED state
     */
    public void approve(AppUser adminUser) {
        if (!adminUser.getIsAdmin()) {
            throw new SecurityException("Only an administrator can approve content!");
        }
        //Submitted -> Published
        if (this.status != ContentStatus.SUBMITTED) {
            throw new IllegalStateException("Content must be in SUBMITTED state to be approved.");
        }
        this.status = ContentStatus.PUBLISHED;
    }

    /**
     * Rejects the submitted audio content and changes its status to UNVERIFIED.
     * <p>
     * This action typically occurs when the content fails to meet the platform's terms or quality standards
     * during the initial review. It requires administrator privileges and can only be performed
     * if the content is currently in the SUBMITTED state.
     * </p>
     *
     * @param adminUser the user attempting to reject the content; must have administrator privileges
     * @throws SecurityException if the provided user does not have administrator privileges
     * @throws IllegalStateException if the content is not in the SUBMITTED state
     */
    public void reject(AppUser adminUser) {
        if (!adminUser.getIsAdmin()) {
            throw new SecurityException("Only an administrator can reject content!");
        }
        //Submitted -> Unverified
        if (this.status != ContentStatus.SUBMITTED) {
            throw new IllegalStateException("Content must be in SUBMITTED state to be rejected.");
        }
        this.status = ContentStatus.UNVERIFIED;
    }

    /**
     * Bans the currently published audio content and changes its status to BLOCKED.
     * <p>
     * This action is taken when live content is found to violate platform rules after publication.
     * It requires administrator privileges and can only be performed if the content
     * is currently in the PUBLISHED state.
     * </p>
     *
     * @param adminUser the user attempting to ban the content; must have administrator privileges
     * @throws SecurityException if the provided user does not have administrator privileges
     * @throws IllegalStateException if the content is not in the PUBLISHED state
     */
    public void ban(AppUser adminUser) {
        if (!adminUser.getIsAdmin()) {
            throw new SecurityException("Only an administrator can ban content!");
        }
        //Published -> Blocked
        if (this.status != ContentStatus.PUBLISHED) {
            throw new IllegalStateException("Content must be in PUBLISHED state to be banned.");
        }
        this.status = ContentStatus.BLOCKED;
    }

    /**
     * Archives the published audio content and changes its status to ARCHIVED.
     * <p>
     * This action is typically executed by the system's scheduled tasks to clean up old content.
     * It requires administrator privileges and can only be performed if the content
     * is currently in the PUBLISHED state.
     * </p>
     *
     * @param adminUser the user (or system account) attempting to archive the content; must have administrator privileges
     * @throws SecurityException if the provided user does not have administrator privileges
     * @throws IllegalStateException if the content is not in the PUBLISHED state
     */
    public void archive(AppUser adminUser) {
        if (!adminUser.getIsAdmin()) {
            throw new SecurityException("Only an administrator can archive content!");
        }
        //Published -> Archived
        if (this.status != ContentStatus.PUBLISHED) {
            throw new IllegalStateException("Content must be in PUBLISHED state to be archived.");
        }
        this.status = ContentStatus.ARCHIVED;
    }

    /**
     * Resubmits previously rejected audio content back for administrative review.
     * <p>
     * This action allows creators to fix issues and send the content back to the SUBMITTED state.
     * It can only be performed if the content is currently in the UNVERIFIED (rejected) state.
     * </p>
     *
     * @throws IllegalStateException if the content is not in the UNVERIFIED state
     */
    public void resubmit() {
        //Unverified -> Submitted
        if (this.status != ContentStatus.UNVERIFIED) {
            throw new IllegalStateException("Content must be in UNVERIFIED state to be resubmitted.");
        }
        this.status = ContentStatus.SUBMITTED;
    }

    /**
     * Submits a formal appeal for audio content that has been previously blocked.
     * <p>
     * This action changes the content's status from BLOCKED back to SUBMITTED,
     * allowing administrators to re-evaluate the ban decision.
     * </p>
     *
     * @throws IllegalStateException if the content is not in the BLOCKED state
     */
    public void appeal() {
        //Blocked -> Submitted
        if (this.status != ContentStatus.BLOCKED) {
            throw new IllegalStateException("Content must be in BLOCKED state to appeal.");
        }
        this.status = ContentStatus.SUBMITTED;
    }



    @NotNull(message = "Creator cannot be null!")
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Creator creator;

    @OneToMany(mappedBy = "audioContent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PlaylistEntry> playlistEntries = new HashSet<>();

}
