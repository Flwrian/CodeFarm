package fr.flwrian.codefarm.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.flwrian.codefarm.environment.structures.Base;
import fr.flwrian.codefarm.environment.structures.Forest;
import fr.flwrian.codefarm.environment.structures.Mine;
import fr.flwrian.codefarm.environment.structures.Shop;
import fr.flwrian.codefarm.environment.structures.Structure;

public class World {
    public int width = 200;
    public int height = 150;
    public int tileSize = 32;

    // Tile types
    public static final int EMPTY = 0;
    public static final int GRASS = 1;
    public static final int TREE = 2;
    public static final int STONE = 3;
    public static final int BASE = 4;
    public static final int FOREST = 5;
    public static final int MINE = 6;
    public static final int SHOP = 7;

    private int[][] tiles;
    
    // Map des structures par position
    private Map<String, Structure> structures;

    public World() {
        tiles = new int[width][height];
        structures = new HashMap<>();
        generate();
    }

    private void generate() {
        Random rand = new Random();

        // Remplir d'herbe
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = GRASS;
            }
        }

        // Générer des arbres aléatoires
        for (int i = 0; i < 500; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            if (tiles[x][y] == GRASS) {
                tiles[x][y] = TREE;
            }
        }

        // Générer des pierres aléatoires
        for (int i = 0; i < 300; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            if (tiles[x][y] == GRASS) {
                tiles[x][y] = STONE;
            }
        }

        // Créer la base
        createBase(10, 10);

        // Créer des zones spéciales
        createForest("Dark Forest", 50, 50, 20, 15);
        createMine("Iron Mine", 100, 80, 15, 10);
        createShop("Trading Post", 15, 15);
    }

    // ========================================================================
    // Gestion des tiles
    // ========================================================================

    public int getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return -1;
        }
        return tiles[x][y];
    }

    public void setTile(int x, int y, int tileType) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        tiles[x][y] = tileType;
    }

    // ========================================================================
    // Création de structures
    // ========================================================================

    private void createBase(int x, int y) {
        // Marquer les tiles
        for (int dx = 0; dx < 3; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                setTile(x + dx, y + dy, BASE);
            }
        }

        // Créer l'objet Base
        Base base = new Base(x, y, 3, 3);
        structures.put(posKey(x, y), base);
    }

    private void createForest(String name, int x, int y, int width, int height) {
        // Remplir la zone d'arbres
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                if (getTile(x + dx, y + dy) == GRASS) {
                    setTile(x + dx, y + dy, TREE);
                }
            }
        }

        // Marquer les coins comme FOREST pour identification
        setTile(x, y, FOREST);
        setTile(x + width - 1, y + height - 1, FOREST);

        // Créer l'objet Forest
        Forest forest = new Forest(name, x, y, width, height);
        structures.put(posKey(x, y), forest);
    }

    private void createMine(String name, int x, int y, int width, int height) {
        // Remplir la zone de pierres
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                if (getTile(x + dx, y + dy) == GRASS) {
                    setTile(x + dx, y + dy, STONE);
                }
            }
        }

        setTile(x, y, MINE);
        Mine mine = new Mine(name, x, y, width, height);
        structures.put(posKey(x, y), mine);
    }

    private void createShop(String name, int x, int y) {
        setTile(x, y, SHOP);
        Shop shop = new Shop(name, x, y);
        structures.put(posKey(x, y), shop);
    }

    // ========================================================================
    // Requêtes de structures
    // ========================================================================

    /**
     * Trouve la structure à cette position (coin supérieur gauche)
     */
    public Structure getStructureAt(int x, int y) {
        return structures.get(posKey(x, y));
    }

    /**
     * Trouve n'importe quelle structure qui contient cette position
     */
    public Structure getStructureContaining(int x, int y) {
        for (Structure struct : structures.values()) {
            if (struct.contains(x, y)) {
                return struct;
            }
        }
        return null;
    }

    /**
     * Trouve la base (il n'y en a qu'une)
     */
    public Base getBase() {
        for (Structure struct : structures.values()) {
            if (struct instanceof Base) {
                return (Base) struct;
            }
        }
        return null;
    }

    private String posKey(int x, int y) {
        return x + "," + y;
    }

    // ========================================================================
    // Helpers
    // ========================================================================

    public boolean isWalkable(int x, int y) {
        int tile = getTile(x, y);
        return tile == GRASS || tile == BASE || tile == FOREST || 
               tile == MINE || tile == SHOP;
    }

    public boolean isHarvestable(int x, int y) {
        int tile = getTile(x, y);
        return tile == TREE || tile == STONE;
    }
}