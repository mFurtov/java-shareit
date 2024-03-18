package ru.practicum.shareit.request.modul;





import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolationException;

public class DescriptionValidator implements ConstraintValidator<ItemRequestValid, String> {


    @Override
    public void initialize(ItemRequestValid itemRequestValid) {
    }

    @Override
    public boolean isValid(String description, ConstraintValidatorContext constraintValidatorContext) {
        return description != null;
    }
}
