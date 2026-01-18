package fr.flwrian.codefarm;

public class World {
    int width = 20;
    int height = 15;
    int tileSize = 32;

    public static final int EMPTY = 0;
    public static final int GRASS = 1;
    public static final int TREE = 2;
    public static final int STONE = 3;
    public static final int BASE = 4;


    int[][] tiles = new int[width][height];
    // 0 = vide, 1 = herbe, 2 = arbre, 3 = pierre, 4 = base

    public World() {
        // Initialiser le monde avec des tiles d'herbe
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = 1; // herbe
            }
        }
        // Ajouter quelques arbres et pierres pour l'exemple
        tiles[5][5] = 2; // arbre
        tiles[10][8] = 3; // pierre
        tiles[0][0] = 4; // base
    }

    public int getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return -1; // hors limites
        }
        return tiles[x][y];
    }

    public void setTile(int x, int y, int tileType) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return; // hors limites
        }
        tiles[x][y] = tileType;
    }
}
