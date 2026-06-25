package karol.studia.mas_projekt_koncowy.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import karol.studia.mas_projekt_koncowy.model.MusicCreator;
import karol.studia.mas_projekt_koncowy.model.MusicCreatorRole;

public class MusicCreatorAttributesValidator implements ConstraintValidator<ValidMusicCreatorAttributes, MusicCreator> {


    @Override
    public void initialize(ValidMusicCreatorAttributes constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MusicCreator musicCreator, ConstraintValidatorContext constraintValidatorContext) {

        if(musicCreator == null || musicCreator.getRole() == null){
            return true;
        }
        if (!musicCreator.getRole().contains(MusicCreatorRole.Vocalist)) {
            if (musicCreator.getVocalType() != null || musicCreator.getVocalRange() != null) {
                return false;
            }
        }
        if (!musicCreator.getRole().contains(MusicCreatorRole.Instrumentalist)) {
            if (musicCreator.getMainInstrument() != null || musicCreator.getInstrumentFamily() != null) {
                return false;
            }
        }
        return true;
    }
}
