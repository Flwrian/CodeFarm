package fr.flwrian.codefarm.environment.structures;

import fr.flwrian.codefarm.Player;

public abstract class Structure {
    protected final int x, y;
    protected final int width, height;
    protected final String name;

    public Structure(String name, int x, int y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getName() { return name; }

    /**
     * Vérifie si une position est dans cette structure
     */
    public boolean contains(int tileX, int tileY) {
        return tileX >= x && tileX < x + width 
            && tileY >= y && tileY < y + height;
    }

    /**
     * Vérifie si le joueur est dans cette structure
     */
    public boolean containsPlayer(Player player) {
        return contains(player.x, player.y);
    }
}