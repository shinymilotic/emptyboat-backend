package overcloud.blog.infrastructure.validation.customannotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LetterAndNumberValidator.class)
public @interface LetterAndNumber {
    public String message() default "The Integer value is invalid";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
