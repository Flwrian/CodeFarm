package fr.flwrian.codefarm.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;

import fr.flwrian.codefarm.environment.World;

public class GridRenderer {
    
    private final ShapeRenderer shapeRenderer;
    private final World world;
    
    private boolean enabled = true;
    private Color gridColor = new Color(1f, 1f, 1f, 0.1f);
    private Color chunkColor = new Color(1f, 1f, 0f, 0.3f);

    public GridRenderer(World world) {
        this.world = world;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void render(OrthographicCamera camera) {
        if (!enabled) return;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Calculate visible tile range
        int startX = Math.max(0, (int)(camera.position.x - camera.viewportWidth / 2) / world.tileSize - 1);
        int endX = Math.min(world.width, (int)(camera.position.x + camera.viewportWidth / 2) / world.tileSize + 2);
        int startY = Math.max(0, (int)(camera.position.y - camera.viewportHeight / 2) / world.tileSize - 1);
        int endY = Math.min(world.height, (int)(camera.position.y + camera.viewportHeight / 2) / world.tileSize + 2);

        shapeRenderer.setColor(gridColor);

        // Vertical lines
        for (int x = startX; x <= endX; x++) {
            float worldX = x * world.tileSize;
            shapeRenderer.line(worldX, startY * world.tileSize, worldX, endY * world.tileSize);
        }

        // Horizontal lines
        for (int y = startY; y <= endY; y++) {
            float worldY = y * world.tileSize;
            shapeRenderer.line(startX * world.tileSize, worldY, endX * world.tileSize, worldY);
        }

        // Draw chunk lines (every 10 tiles for example)
        shapeRenderer.setColor(chunkColor);
        int chunkSize = 10;
        
        for (int x = 0; x <= world.width; x += chunkSize) {
            if (x >= startX && x <= endX) {
                float worldX = x * world.tileSize;
                shapeRenderer.line(worldX, startY * world.tileSize, worldX, endY * world.tileSize);
            }
        }

        for (int y = 0; y <= world.height; y += chunkSize) {
            if (y >= startY && y <= endY) {
                float worldY = y * world.tileSize;
                shapeRenderer.line(startX * world.tileSize, worldY, endX * world.tileSize, worldY);
            }
        }

        shapeRenderer.end();
    }

    public void toggle() {
        enabled = !enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setGridColor(Color color) {
        this.gridColor = color;
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}