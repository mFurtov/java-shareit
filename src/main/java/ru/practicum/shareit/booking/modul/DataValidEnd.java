package ru.practicum.shareit.booking.modul;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DataValidEnd implements ConstraintValidator<DataValidEndtAnnotation, LocalDateTime> {
    private static final LocalDateTime now = LocalDateTime.now();

    @Override
    public void initialize(DataValidEndtAnnotation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        boolean a = value.isAfter(now);
        return a;
    }
}
