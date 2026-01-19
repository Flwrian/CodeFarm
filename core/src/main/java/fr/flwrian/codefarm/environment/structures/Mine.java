package fr.flwrian.codefarm.environment.structures;

public class Mine extends Structure {
    private final int dangerLevel;

    public Mine(String name, int x, int y, int width, int height) {
        this(name, x, y, width, height, 1);
    }

    public Mine(String name, int x, int y, int width, int height, int danger) {
        super(name, x, y, width, height);
        this.dangerLevel = danger;
    }

    public float getStoneBonus() {
        return 2.0f + (dangerLevel * 0.5f);
    }

    public int getDangerLevel() {
        return dangerLevel;
    }
}