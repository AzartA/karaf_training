package ru.training.karaf.rest.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PictureTypeValidatorImpl implements ConstraintValidator<PictureType, String> {
    @Override
    public void initialize(PictureType pictureType) {

    }

    @Override
    public boolean isValid(String type, ConstraintValidatorContext constraintValidatorContext) {
        Set<String> pictureTypes = new HashSet<>(Arrays.asList(
                "image/gif", "image/jpeg", "image/png", "image/svg+xml",
                "image/tiff", "image/webp", "image/vnd.wap.wbmp"));
        return pictureTypes.contains(type);
    }
}
