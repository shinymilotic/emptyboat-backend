package overcloud.blog.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LetterAndNumberValidator.class)
public @interface LetterAndNumber {
    String message() default "The Integer value is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
