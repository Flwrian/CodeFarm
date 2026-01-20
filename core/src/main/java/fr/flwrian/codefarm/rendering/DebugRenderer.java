package fr.flwrian.codefarm.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.environment.structures.Structure;
import fr.flwrian.codefarm.game.GameContext;
import fr.flwrian.codefarm.item.ItemType;
import fr.flwrian.codefarm.environment.World;

public class DebugRenderer {
    
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    
    private boolean enabled = false;
    private boolean showStructureBounds = true;
    private boolean showPlayerDirection = true;
    private boolean showTileCoords = false;
    private boolean showPlayerInfo = true;

    public DebugRenderer(BitmapFont font) {
        this.shapeRenderer = new ShapeRenderer();
        this.font = font;
    }

    public void render(OrthographicCamera camera, GameContext ctx) {
        if (!enabled) return;

        if (showStructureBounds) {
            renderStructureBounds(camera, ctx.world);
        }

        if (showPlayerDirection) {
            renderPlayerDirection(camera, ctx.player, ctx.world);
        }

        if (showTileCoords) {
            renderTileCoords(camera, ctx.world);
        }
    }

    private void renderStructureBounds(OrthographicCamera camera, World world) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        
        for (int x = 0; x < world.width; x++) {
            for (int y = 0; y < world.height; y++) {
                Structure struct = world.getStructureAt(x, y);
                if (struct != null) {
                    shapeRenderer.setColor(Color.YELLOW);
                    shapeRenderer.rect(
                        struct.getX() * world.tileSize,
                        struct.getY() * world.tileSize,
                        struct.getWidth() * world.tileSize,
                        struct.getHeight() * world.tileSize
                    );
                }
            }
        }
        
        shapeRenderer.end();
    }

    private void renderPlayerDirection(OrthographicCamera camera, Player player, World world) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        float centerX = player.x * world.tileSize + world.tileSize / 2f;
        float centerY = player.y * world.tileSize + world.tileSize / 2f;
        float targetX = centerX + player.direction.dx * world.tileSize * 0.4f;
        float targetY = centerY + player.direction.dy * world.tileSize * 0.4f;

        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rectLine(centerX, centerY, targetX, targetY, 3);
        
        shapeRenderer.triangle(
            targetX, targetY,
            targetX - player.direction.dy * 5, targetY + player.direction.dx * 5,
            targetX + player.direction.dy * 5, targetY - player.direction.dx * 5
        );
        
        shapeRenderer.end();
    }

    private void renderTileCoords(OrthographicCamera camera, World world) {
        // Calculate visible tile range
        int startX = Math.max(0, (int)(camera.position.x - camera.viewportWidth / 2) / world.tileSize);
        int endX = Math.min(world.width, (int)(camera.position.x + camera.viewportWidth / 2) / world.tileSize + 1);
        int startY = Math.max(0, (int)(camera.position.y - camera.viewportHeight / 2) / world.tileSize);
        int endY = Math.min(world.height, (int)(camera.position.y + camera.viewportHeight / 2) / world.tileSize + 1);
    }

    public void renderPlayerInfo(SpriteBatch batch, Player player, World world, float screenWidth, float screenHeight) {
        if (!enabled || !showPlayerInfo) return;

        Structure currentStruct = world.getStructureContaining(player.x, player.y);
        String structName = currentStruct != null ? currentStruct.getName() : "None";

        font.setColor(Color.WHITE);
        font.draw(batch, "Position: (" + player.x + ", " + player.y + ")", 10, screenHeight - 40);
        font.draw(batch, "Direction: (" + player.direction.dx + ", " + player.direction.dy + ")", 10, screenHeight - 60);
        font.draw(batch, "Location: " + structName, 10, screenHeight - 80);
        // font.draw(batch, "Inventory: " + player.inventory.toString(), 10, screenHeight - 100);
    }

    // Toggles
    public void toggle() {
        enabled = !enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggleStructureBounds() {
        showStructureBounds = !showStructureBounds;
    }

    public void togglePlayerDirection() {
        showPlayerDirection = !showPlayerDirection;
    }

    public void toggleTileCoords() {
        showTileCoords = !showTileCoords;
    }

    public void togglePlayerInfo() {
        showPlayerInfo = !showPlayerInfo;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}