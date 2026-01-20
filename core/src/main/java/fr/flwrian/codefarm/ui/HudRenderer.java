package fr.flwrian.codefarm.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.environment.structures.Base;
import fr.flwrian.codefarm.item.ItemType;
import fr.flwrian.codefarm.action.Action;

public class HudRenderer {
    private BitmapFont font;

    public HudRenderer(BitmapFont font) {
        this.font = font;
    }

    public void render(SpriteBatch batch, Player player, Base base, Action currentAction, int ntick, float uiViewportWidth, float uiViewportHeight) {
        font.draw(batch,
            "Wood: " + player.inventory.get(ItemType.WOOD) +
            "  Stone: " + player.inventory.get(ItemType.STONE) +
            "  Base Wood: " + base.storage.get(ItemType.WOOD) +
            "  Base Stone: " + base.storage.get(ItemType.STONE),
            10, uiViewportHeight - 20);

        String info = "FPS: " + Gdx.graphics.getFramesPerSecond()
            + " | Ticks: " + ntick
            + " | Action: " + (currentAction == null ? "none" : currentAction.toString());
        font.draw(batch, info,
            uiViewportWidth - 320, uiViewportHeight - 20);
    }


}
