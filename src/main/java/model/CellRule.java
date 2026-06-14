package model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public final class CellRule {
    private final Set<Integer> birthCounts;
    private final Set<Integer> survivalCounts;

    private CellRule(Set<Integer> birthCounts, Set<Integer> survivalCounts) {
        this.birthCounts = Collections.unmodifiableSet(new TreeSet<>(birthCounts));
        this.survivalCounts = Collections.unmodifiableSet(new TreeSet<>(survivalCounts));
    }

    public static CellRule conway() {
        return parse("B3/S23");
    }

    public static CellRule of(Set<Integer> birthCounts, Set<Integer> survivalCounts) {
        Objects.requireNonNull(birthCounts, "birthCounts must not be null");
        Objects.requireNonNull(survivalCounts, "survivalCounts must not be null");
        validateCounts(birthCounts, "birth");
        validateCounts(survivalCounts, "survival");
        return new CellRule(birthCounts, survivalCounts);
    }

    public static CellRule parse(String ruleText) {
        if (ruleText == null) {
            throw new IllegalArgumentException("Rule must not be null.");
        }

        String normalized = ruleText.trim().toUpperCase();
        String[] parts = normalized.split("/", -1);
        if (parts.length != 2 || !parts[0].startsWith("B") || !parts[1].startsWith("S")) {
            throw new IllegalArgumentException("Use rule format B.../S..., for example B3/S23.");
        }

        Set<Integer> birthCounts = parseCounts(parts[0].substring(1), "birth");
        Set<Integer> survivalCounts = parseCounts(parts[1].substring(1), "survival");
        return new CellRule(birthCounts, survivalCounts);
    }

    public boolean isBirth(int neighborCount) {
        return birthCounts.contains(neighborCount);
    }

    public boolean isSurvival(int neighborCount) {
        return survivalCounts.contains(neighborCount);
    }

    public Set<Integer> getBirthCounts() {
        return birthCounts;
    }

    public Set<Integer> getSurvivalCounts() {
        return survivalCounts;
    }

    @Override
    public String toString() {
        return "B" + joinCounts(birthCounts) + "/S" + joinCounts(survivalCounts);
    }

    private static Set<Integer> parseCounts(String countText, String label) {
        Set<Integer> counts = new LinkedHashSet<>();
        for (int index = 0; index < countText.length(); index++) {
            char character = countText.charAt(index);
            if (!Character.isDigit(character)) {
                throw new IllegalArgumentException("Only digits from 0 to 8 are allowed in " + label + " counts.");
            }

            int count = Character.digit(character, 10);
            if (count < 0 || count > 8) {
                throw new IllegalArgumentException("Neighbor counts must be between 0 and 8.");
            }
            if (!counts.add(count)) {
                throw new IllegalArgumentException("Duplicate " + label + " count: " + count + ".");
            }
        }
        return counts;
    }

    private static void validateCounts(Set<Integer> counts, String label) {
        for (Integer count : counts) {
            if (count == null || count < 0 || count > 8) {
                throw new IllegalArgumentException("Invalid " + label + " count: " + count + ".");
            }
        }
    }

    private static String joinCounts(Set<Integer> counts) {
        StringBuilder builder = new StringBuilder();
        for (int count : counts) {
            builder.append(count);
        }
        return builder.toString();
    }
}
