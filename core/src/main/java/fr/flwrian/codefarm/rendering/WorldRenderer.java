package fr.flwrian.codefarm.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.World;

public class WorldRenderer {
    
    private final World world;
    private final Player player;
    
    private final Texture playerTex;
    private final Texture grassTex;
    private final Texture treeTex;
    private final Texture stoneTex;
    private final Texture baseTex;

    public WorldRenderer(World world, Player player) {
        this.world = world;
        this.player = player;
        
        // Create textures
        playerTex = createColorTexture(Color.RED);
        grassTex = createColorTexture(Color.PINK);
        treeTex = createColorTexture(new Color(0.4f, 0.25f, 0.1f, 1));
        stoneTex = createColorTexture(Color.GRAY);
        baseTex = createColorTexture(Color.BROWN);
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
        batch.draw(playerTex,
            player.x * world.tileSize,
            player.y * world.tileSize,
            world.tileSize,
            world.tileSize);
    }

    private Texture getTextureForTile(int tile) {
        switch (tile) {
            case World.TREE: return treeTex;
            case World.STONE: return stoneTex;
            case World.BASE: return baseTex;
            default: return grassTex;
        }
    }

    private Texture createColorTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void dispose() {
        playerTex.dispose();
        grassTex.dispose();
        treeTex.dispose();
        stoneTex.dispose();
        baseTex.dispose();
    }
}