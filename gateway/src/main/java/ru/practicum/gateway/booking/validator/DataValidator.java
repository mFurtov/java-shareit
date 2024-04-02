package ru.practicum.gateway.booking.validator;


import ru.practicum.gateway.booking.dto.BookingDtoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DataValidator implements ConstraintValidator<StartAndBeforeValid, BookingDtoRequest> {
    private final LocalDateTime now = LocalDateTime.now();

    @Override
    public void initialize(StartAndBeforeValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingDtoRequest bookingDto, ConstraintValidatorContext constraintValidatorContext) {

        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (start == null || end == null || !end.isAfter(now) || !start.isAfter(now) || start.isAfter(end) || start.isEqual(end)) {
            return false;
        }
        return true;
    }
}