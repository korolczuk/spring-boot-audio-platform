package karol.studia.mas_projekt_koncowy.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public abstract class Creator extends SystemUser {

    @NotBlank(message = "Description cannot be empty!")
    @Size(max = 200, message = "Description cannot exceed 200 characters!")
    @Column(length = 200)
    private String description;

    @NotNull(message = "Verification status cannot be null!")
    @Builder.Default
    private Boolean isVerified = false;




    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<AudioContent> audioContent = new HashSet<>();

    protected void addAudioContent(AudioContent content) {
        if (content == null) {
            throw new IllegalArgumentException("Audio content cannot be null!");
        }

        if (!this.audioContent.contains(content)) {
            this.audioContent.add(content);
            content.setCreator(this);
        }
    }

    /**
     * Files an appeal for the specified blocked audio content on behalf of this creator.
     * <p>
     * This method acts as a security proxy. It strictly verifies that the content being appealed
     * is actually owned (authored) by the creator attempting the action. If the ownership check passes,
     * it delegates the action to the content's internal state machine.
     * </p>
     *
     * @param content the blocked audio content to be appealed; must not be null
     * @throws IllegalArgumentException if the content is null
     * @throws SecurityException if the creator attempts to appeal content they do not own
     * @throws IllegalStateException if the underlying content is not in the BLOCKED state
     */
    public void appealContent(AudioContent content) {
        if (content == null) throw new IllegalArgumentException("Content cannot be null");
        if (!this.audioContent.contains(content)) throw new SecurityException("You can only appeal your own content");
        content.appeal();
    }

    /**
     * Resubmits the specified rejected audio content for review on behalf of this creator.
     * <p>
     * Similar to the appeal process, this method acts as a security proxy to verify content ownership
     * before allowing the resubmission process to continue.
     * </p>
     *
     * @param content the unverified/rejected audio content to be resubmitted; must not be null
     * @throws IllegalArgumentException if the content is null
     * @throws SecurityException if the creator attempts to resubmit content they do not own
     * @throws IllegalStateException if the underlying content is not in the UNVERIFIED state
     */
    public void resubmitContent(AudioContent content) {
        if (content == null) throw new IllegalArgumentException("Content cannot be null");
        if (!this.audioContent.contains(content)) throw new SecurityException("You can only resubmit your own content");
        content.resubmit();
    }
}
