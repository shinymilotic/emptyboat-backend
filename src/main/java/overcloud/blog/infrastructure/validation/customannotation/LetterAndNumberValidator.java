package overcloud.blog.infrastructure.validation.customannotation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import overcloud.blog.infrastructure.validation.customannotation.LetterAndNumber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LetterAndNumberValidator implements ConstraintValidator<LetterAndNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "[A-Za-z0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }
}
