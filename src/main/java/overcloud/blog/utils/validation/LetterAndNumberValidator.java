package overcloud.blog.utils.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class LetterAndNumberValidator implements ConstraintValidator<LetterAndNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;
        String regex = "[A-Za-z0-9]+";
        Pattern pattern = Pattern.compile(regex);
        if (StringUtils.hasText(value)) {
            isValid = pattern.matcher(value).matches();
        }

        return isValid;
    }
}
