package fr.flwrian.codefarm.environment.structures;

public class Forest extends Structure {
    private final float harvestBonus;

    public Forest(String name, int x, int y, int width, int height) {
        this(name, x, y, width, height, 1.5f);
    }

    public Forest(String name, int x, int y, int width, int height, float bonus) {
        super(name, x, y, width, height);
        this.harvestBonus = bonus;
    }

    public float getHarvestBonus() {
        return harvestBonus;
    }
}