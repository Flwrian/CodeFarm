package fr.flwrian.codefarm;

public enum Direction {
    UP(0, 1, 90),
    DOWN(0, -1, 270),
    LEFT(-1, 0, 180),
    RIGHT(1, 0, 0);

    public final int dx;
    public final int dy;
    public final float angle; // Pour la rotation graphique

    Direction(int dx, int dy, float angle) {
        this.dx = dx;
        this.dy = dy;
        this.angle = angle;
    }

    /**
     * Obtenir la direction depuis des coordonnées
     */
    public static Direction fromDelta(int dx, int dy) {
        for (Direction dir : values()) {
            if (dir.dx == dx && dir.dy == dy) {
                return dir;
            }
        }
        return RIGHT; // Default
    }

    /**
     * Obtenir la direction depuis un nom
     */
    public static Direction fromString(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return RIGHT; // Default
        }
    }

    /**
     * Rotation de 90° dans le sens horaire
     */
    public Direction rotateClockwise() {
        switch (this) {
            case UP: return RIGHT;
            case RIGHT: return DOWN;
            case DOWN: return LEFT;
            case LEFT: return UP;
            default: return this;
        }
    }

    /**
     * Rotation de 90° dans le sens anti-horaire
     */
    public Direction rotateCounterClockwise() {
        switch (this) {
            case UP: return LEFT;
            case LEFT: return DOWN;
            case DOWN: return RIGHT;
            case RIGHT: return UP;
            default: return this;
        }
    }

    /**
     * Direction opposée
     */
    public Direction opposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return this;
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}