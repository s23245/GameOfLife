package model;

import java.util.List;

public final class PatternLibrary {
    private static final List<CellPattern> PATTERNS = List.of(
            new CellPattern("Glider", new boolean[][]{
                    {false, true, false},
                    {false, false, true},
                    {true, true, true}
            }),
            new CellPattern("Blinker", new boolean[][]{
                    {true, true, true}
            }),
            new CellPattern("Beacon", new boolean[][]{
                    {true, true, false, false},
                    {true, true, false, false},
                    {false, false, true, true},
                    {false, false, true, true}
            }),
            new CellPattern("Lightweight spaceship", new boolean[][]{
                    {false, true, false, false, true},
                    {true, false, false, false, false},
                    {true, false, false, false, true},
                    {true, true, true, true, false}
            })
    );

    private PatternLibrary() {
    }

    public static List<CellPattern> getPatterns() {
        return PATTERNS;
    }
}
