package fr.flwrian.codefarm.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.environment.World;

public class ImprovedWorldRenderer {
    
    private final World world;
    private final Player player;
    
    private final Texture playerTex;
    private final Texture grassTex;
    private final Texture treeTex;
    private final Texture stoneTex;
    private final Texture baseTex;
    private final Texture forestTex;
    private final Texture mineTex;
    private final Texture shopTex;
    
    private final Texture arrowTex;
    
    private static final Color GRASS_COLOR = new Color(0.4f, 0.7f, 0.4f, 1f);
    private static final Color TREE_COLOR = new Color(0.2f, 0.5f, 0.2f, 1f);
    private static final Color STONE_COLOR = new Color(0.5f, 0.5f, 0.5f, 1f);
    private static final Color BASE_COLOR = new Color(0.8f, 0.6f, 0.3f, 1f);
    private static final Color PLAYER_COLOR = new Color(1f, 0.3f, 0.3f, 1f);
    private static final Color FOREST_COLOR = new Color(0.15f, 0.4f, 0.15f, 1f);
    private static final Color MINE_COLOR = new Color(0.3f, 0.3f, 0.3f, 1f);
    private static final Color SHOP_COLOR = new Color(0.9f, 0.7f, 0.2f, 1f);

    public ImprovedWorldRenderer(World world, Player player) {
        this.world = world;
        this.player = player;
        
        playerTex = createTileTexture(PLAYER_COLOR, true);
        grassTex = createTileTexture(GRASS_COLOR, false);
        treeTex = createTileTexture(TREE_COLOR, true);
        stoneTex = createTileTexture(STONE_COLOR, true);
        baseTex = createTileTexture(BASE_COLOR, true);
        forestTex = createTileTexture(FOREST_COLOR, true);
        mineTex = createTileTexture(MINE_COLOR, true);
        shopTex = createTileTexture(SHOP_COLOR, true);
        
        arrowTex = createArrowTexture();
    }

    public void render(SpriteBatch batch) {
        // Draw tiles
        for (int tx = 0; tx < world.width; tx++) {
            for (int ty = 0; ty < world.height; ty++) {
                Texture tex = getTextureForTile(world.getTile(tx, ty));
                batch.draw(tex, 
                    tx * world.tileSize, 
                    ty * world.tileSize,
                    world.tileSize, 
                    world.tileSize);
            }
        }

        // Draw player
        float px = player.x * world.tileSize;
        float py = player.y * world.tileSize;
        
        batch.draw(playerTex, px, py, world.tileSize, world.tileSize);
        
        // Draw direction arrow
        float angle = player.direction.angle;
        batch.draw(
            arrowTex,
            px + world.tileSize / 2f - arrowTex.getWidth() / 2f,
            py + world.tileSize / 2f - arrowTex.getHeight() / 2f,
            arrowTex.getWidth() / 2f,
            arrowTex.getHeight() / 2f,
            arrowTex.getWidth(),
            arrowTex.getHeight(),
            1f,
            1f,
            angle,
            0,
            0,
            arrowTex.getWidth(),
            arrowTex.getHeight(),
            false,
            false
        );
    }

    private Texture getTextureForTile(int tile) {
        switch (tile) {
            case World.TREE: return treeTex;
            case World.STONE: return stoneTex;
            case World.BASE: return baseTex;
            case World.FOREST: return forestTex;
            case World.MINE: return mineTex;
            case World.SHOP: return shopTex;
            default: return grassTex;
        }
    }

    private Texture createTileTexture(Color color, boolean withBorder) {
        int size = 32;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        
        pixmap.setColor(color);
        pixmap.fill();
        
        if (withBorder) {
            pixmap.setColor(0f, 0f, 0f, 0.3f);
            pixmap.drawRectangle(0, 0, size, size);
        }
        
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createArrowTexture() {
        int size = 16;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        
        pixmap.setColor(Color.WHITE);
        
        int[] xs = {size - 2, 2, 2};
        int[] ys = {size/2, size - 2, 2};

        // Fill the triangle
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (isInsideTriangle(x, y, xs, ys)) {
                    pixmap.drawPixel(x, y);
                }
            }
        }
        
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private boolean isInsideTriangle(int px, int py, int[] xs, int[] ys) {
        float d1 = sign(px, py, xs[0], ys[0], xs[1], ys[1]);
        float d2 = sign(px, py, xs[1], ys[1], xs[2], ys[2]);
        float d3 = sign(px, py, xs[2], ys[2], xs[0], ys[0]);
        
        boolean hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);
        
        return !(hasNeg && hasPos);
    }

    private float sign(int px, int py, int x1, int y1, int x2, int y2) {
        return (px - x2) * (y1 - y2) - (x1 - x2) * (py - y2);
    }

    public void dispose() {
        playerTex.dispose();
        grassTex.dispose();
        treeTex.dispose();
        stoneTex.dispose();
        baseTex.dispose();
        forestTex.dispose();
        mineTex.dispose();
        shopTex.dispose();
        arrowTex.dispose();
    }
}