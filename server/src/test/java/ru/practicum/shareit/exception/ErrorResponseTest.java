package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {
    @Test
    public void testGetError() {
        String expectedError = "Test error message";

        ErrorResponse errorResponse = new ErrorResponse(expectedError);

        assertEquals(expectedError, errorResponse.getError());
    }
}