package karol.studia.mas_projekt_koncowy.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MusicCreatorAttributesValidator.class)
public @interface ValidMusicCreatorAttributes {
    String message() default "Attributes don't match the creator's roles!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
