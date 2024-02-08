package orme.dominic.canvasfiller.annotations;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = GreaterThanZero.Validator.class)
public @interface GreaterThanZero {
    class Validator implements ConstraintValidator<GreaterThanZero, Integer> {
        @Override
        public boolean isValid(Integer number, ConstraintValidatorContext constraintValidatorContext) {
            if (number < 0.0) {
                constraintValidatorContext.buildConstraintViolationWithTemplate("Must be greater than 0").addConstraintViolation().disableDefaultConstraintViolation();
                return false;
            }
            return true;
        }
    }
}
