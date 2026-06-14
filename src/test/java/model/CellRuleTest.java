package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CellRuleTest {
    @Test
    void parsesConwayRule() {
        CellRule rule = CellRule.parse("B3/S23");

        assertTrue(rule.isBirth(3));
        assertFalse(rule.isBirth(2));
        assertTrue(rule.isSurvival(2));
        assertTrue(rule.isSurvival(3));
        assertFalse(rule.isSurvival(4));
        assertEquals("B3/S23", rule.toString());
    }

    @Test
    void parsesRuleWithEmptySurvivalCounts() {
        CellRule rule = CellRule.parse("B2/S");

        assertTrue(rule.isBirth(2));
        assertFalse(rule.isSurvival(2));
        assertEquals("B2/S", rule.toString());
    }

    @Test
    void normalizesLowercaseAndUnsortedCounts() {
        CellRule rule = CellRule.parse("b63/s32");

        assertEquals("B36/S23", rule.toString());
    }

    @Test
    void rejectsInvalidRuleStrings() {
        String[] invalidRules = {
                "",
                "B3",
                "3/S23",
                "B3/S23/X",
                "B9/S23",
                "B33/S23",
                "B3/S2A"
        };

        for (String invalidRule : invalidRules) {
            assertThrows(IllegalArgumentException.class, () -> CellRule.parse(invalidRule));
        }
    }
}
