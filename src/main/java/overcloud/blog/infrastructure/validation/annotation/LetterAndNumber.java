package overcloud.blog.infrastructure.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LetterAndNumberValidator.class)
public @interface LetterAndNumber {
    public String message() default "The Integer value is invalid";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
