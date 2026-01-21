package fr.flwrian.codefarm.rendering;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;

import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.environment.TileType;
import fr.flwrian.codefarm.environment.World;

public class ImprovedWorldRenderer {
    
    private final World world;
    private final List<Player> players;
    
    private final Texture playerTex;
    private final Texture currentPlayerTex;
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
    private static final Color CURRENT_PLAYER_COLOR = new Color(0.3f, 0.3f, 1f, 1f);
    private static final Color FOREST_COLOR = new Color(0.15f, 0.4f, 0.15f, 1f);
    private static final Color MINE_COLOR = new Color(0.3f, 0.3f, 0.3f, 1f);
    private static final Color SHOP_COLOR = new Color(0.9f, 0.7f, 0.2f, 1f);
    private int lastRenderedTileCount = 0;

    public ImprovedWorldRenderer(World world, java.util.List<Player> players) {
        this.world = world;
        this.players = players;

        playerTex = createTileTexture(PLAYER_COLOR, true);
        currentPlayerTex = createTileTexture(CURRENT_PLAYER_COLOR, true);
        grassTex = createTileTexture(GRASS_COLOR, false);
        treeTex = createTileTexture(TREE_COLOR, true);
        stoneTex = createTileTexture(STONE_COLOR, true);
        baseTex = createTileTexture(BASE_COLOR, true);
        forestTex = createTileTexture(FOREST_COLOR, true);
        mineTex = createTileTexture(MINE_COLOR, true);
        shopTex = createTileTexture(SHOP_COLOR, true);
        
        arrowTex = createArrowTexture();
    }

    public void render(SpriteBatch batch, Player currentPlayer, OrthographicCamera camera) {
        // Calculate visible tile range from camera
        int startX = Math.max(0, (int)((camera.position.x - camera.viewportWidth / 2f) / world.tileSize) - 1);
        int endX = Math.min(world.width - 1, (int)((camera.position.x + camera.viewportWidth / 2f) / world.tileSize) + 1);
        int startY = Math.max(0, (int)((camera.position.y - camera.viewportHeight / 2f) / world.tileSize) - 1);
        int endY = Math.min(world.height - 1, (int)((camera.position.y + camera.viewportHeight / 2f) / world.tileSize) + 1);

        // Draw only visible tiles
        int rendered = 0;
        for (int tx = startX; tx <= endX; tx++) {
            for (int ty = startY; ty <= endY; ty++) {
                Texture tex = getTextureForTile(world.getTile(tx, ty));
                batch.draw(tex,
                    tx * world.tileSize,
                    ty * world.tileSize,
                    world.tileSize,
                    world.tileSize);
                rendered++;
            }
        }
        lastRenderedTileCount = rendered;

        // Draw players only if visible
        for (Player player : players) {
            int pxTile = player.x;
            int pyTile = player.y;
            if (pxTile < startX - 1 || pxTile > endX + 1 || pyTile < startY - 1 || pyTile > endY + 1) continue;

            float px = pxTile * world.tileSize;
            float py = pyTile * world.tileSize;

            Texture texToDraw = (player == currentPlayer) ? currentPlayerTex : playerTex;
            batch.draw(texToDraw, px, py, world.tileSize, world.tileSize);

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
    }

    public int getLastRenderedTileCount() {
        return lastRenderedTileCount;
    }

    private Texture getTextureForTile(TileType tile) {
        switch (tile) {
            case TREE: return treeTex;
            case STONE: return stoneTex;
            case BASE: return baseTex;
            case FOREST: return forestTex;
            case MINE: return mineTex;
            case SHOP: return shopTex;
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