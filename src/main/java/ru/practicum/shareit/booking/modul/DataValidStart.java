package ru.practicum.shareit.booking.modul;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DataValidStart implements ConstraintValidator<DataValidStartAnnotation, LocalDateTime> {
    private static final LocalDateTime now = LocalDateTime.now();

    @Override
    public void initialize(DataValidStartAnnotation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        boolean a = value.isAfter(now) || value == now;
        return a;
    }
}
