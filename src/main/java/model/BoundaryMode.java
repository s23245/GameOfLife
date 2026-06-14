package model;

public enum BoundaryMode {
    TOROIDAL("Toroidal"),
    FIXED_DEAD("Fixed dead");

    private final String displayName;

    BoundaryMode(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
