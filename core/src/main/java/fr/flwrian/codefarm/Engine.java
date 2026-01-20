package fr.flwrian.codefarm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import fr.flwrian.codefarm.camera.GameCamera;
import fr.flwrian.codefarm.camera.UICamera;
import fr.flwrian.codefarm.rendering.ImprovedWorldRenderer;
import fr.flwrian.codefarm.rendering.GridRenderer;
import fr.flwrian.codefarm.rendering.DebugRenderer;
import fr.flwrian.codefarm.ui.HudRenderer;
import fr.flwrian.codefarm.game.GameLogic;

public class Engine extends ApplicationAdapter {

    private SpriteBatch batch;
    private BitmapFont font;
    
    private GameLogic gameLogic;
    private ImprovedWorldRenderer worldRenderer;
    private GridRenderer gridRenderer;
    private DebugRenderer debugRenderer;
    private HudRenderer hudRenderer;

    private GameCamera worldCamera;
    private UICamera uiCamera;

    private static final float WORLD_VIEW_WIDTH = 640;
    private static final float WORLD_VIEW_HEIGHT = 480;
    private static final boolean DEBUG = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        // Initialize game
        gameLogic = new GameLogic(DEBUG);

        // Initialize cameras
        worldCamera = new GameCamera(WORLD_VIEW_WIDTH, WORLD_VIEW_HEIGHT);
        worldCamera.setTarget(gameLogic.getCurrentPlayer(), gameLogic.getWorld());
        worldCamera.snapToTarget();

        uiCamera = new UICamera();

        // Initialize renderers
        worldRenderer = new ImprovedWorldRenderer(gameLogic.getWorld(), gameLogic.getPlayers());
        gridRenderer = new GridRenderer(gameLogic.getWorld());
        debugRenderer = new DebugRenderer(font);
        hudRenderer = new HudRenderer(font);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        // Handle input
        handleInput();

        // Update
        gameLogic.update(dt);
        // update camera current player
        worldCamera.setTarget(gameLogic.getCurrentPlayer(), gameLogic.getWorld());
        worldCamera.update(dt);
        uiCamera.update();

        // Render
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // World
        batch.setProjectionMatrix(worldCamera.getCamera().combined);
        batch.begin();
        worldRenderer.render(batch, gameLogic.getCurrentPlayer());
        batch.end();

        // Grid
        gridRenderer.render(worldCamera.getCamera());

        // Debug shapes
        debugRenderer.render(worldCamera.getCamera(), gameLogic.getCtx());

        // UI
        batch.setProjectionMatrix(uiCamera.getCamera().combined);
        batch.begin();
        hudRenderer.render(batch,
            gameLogic.getCurrentPlayer(),
            gameLogic.getBase(),
            gameLogic.getCurrentAction(),
            gameLogic.getTickCount(),
            uiCamera.getWidth(),
            uiCamera.getHeight());

        // Debug text
        debugRenderer.renderPlayerInfo(batch,
            gameLogic.getCurrentPlayer(),
            gameLogic.getWorld(),
            uiCamera.getWidth(),
            uiCamera.getHeight());

        batch.end();
    }

    private void handleInput() {
        // Toggle grid with G
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            gridRenderer.toggle();
            System.out.println("Grid: " + (gridRenderer.isEnabled() ? "ON" : "OFF"));
        }

        // Toggle debug with F3
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            debugRenderer.toggle();
            System.out.println("Debug: " + (debugRenderer.isEnabled() ? "ON" : "OFF"));
        }

        // Switch player with TAB
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            java.util.List<fr.flwrian.codefarm.Player> players = gameLogic.getPlayers();
            fr.flwrian.codefarm.Player current = gameLogic.getCurrentPlayer();
            int idx = players.indexOf(current);
            int nextIdx = (idx + 1) % players.size();
            fr.flwrian.codefarm.Player nextPlayer = players.get(nextIdx);
            gameLogic.switchToPlayer(nextPlayer);
            System.out.println("Switched to player " + nextIdx);
        }
    }

    @Override
    public void resize(int width, int height) {
        worldCamera.resize(width, height);
        uiCamera.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        worldRenderer.dispose();
        gridRenderer.dispose();
        debugRenderer.dispose();
    }
}