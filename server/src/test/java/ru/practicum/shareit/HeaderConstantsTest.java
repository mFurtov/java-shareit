package ru.practicum.shareit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeaderConstantsTest {

    @Test
    public void testXSharerUserIdConstant() {
        assertEquals("X-Sharer-User-Id", HeaderConstants.X_SHARER_USER_ID);
    }
}