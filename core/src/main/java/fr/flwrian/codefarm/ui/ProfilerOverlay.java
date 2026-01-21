package fr.flwrian.codefarm.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import fr.flwrian.codefarm.rendering.ImprovedWorldRenderer;

public class ProfilerOverlay {
    private final BitmapFont font;
    public ProfilerOverlay(BitmapFont font) {
        this.font = font;
    }

    public void render(SpriteBatch batch, int fps, int tilesRendered) {
        font.setColor(Color.WHITE);
        font.draw(batch, "Tiles: " + tilesRendered, 10, 20);
    }
}
