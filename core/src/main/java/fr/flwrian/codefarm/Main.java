package fr.flwrian.codefarm;

import fr.flwrian.codefarm.camera.GameCamera;
import fr.flwrian.codefarm.camera.UICamera;
import fr.flwrian.codefarm.ui.HudRenderer;
import fr.flwrian.codefarm.rendering.WorldRenderer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import fr.flwrian.codefarm.game.GameLogic;

public class Main extends ApplicationAdapter {

    // Rendering
    private SpriteBatch batch;
    private BitmapFont font;
    private WorldRenderer worldRenderer;
    private HudRenderer hudRenderer;

    // Game logic
    private GameLogic gameLogic;

    // Cameras
    private GameCamera worldCamera;
    private UICamera uiCamera;

    // Config
    private static final float WORLD_VIEW_WIDTH = 640;
    private static final float WORLD_VIEW_HEIGHT = 480;
    private static final boolean DEBUG = true;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        // Initialize game
        gameLogic = new GameLogic(DEBUG);
        
        // Initialize cameras
        worldCamera = new GameCamera(WORLD_VIEW_WIDTH, WORLD_VIEW_HEIGHT);
        worldCamera.setTarget(gameLogic.getPlayer(), gameLogic.getWorld());
        worldCamera.snapToTarget(); // Start at player position
        
        uiCamera = new UICamera();

        // Initialize renderers
        worldRenderer = new WorldRenderer(gameLogic.getWorld(), gameLogic.getPlayer());
        hudRenderer = new HudRenderer(font);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        // Update game logic
        gameLogic.update(dt);

        // Update cameras
        worldCamera.update(dt);
        uiCamera.update();

        // Render
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        renderWorld();
        renderUI();
    }

    private void renderWorld() {
        batch.setProjectionMatrix(worldCamera.getCamera().combined);
        batch.begin();
        worldRenderer.render(batch);
        batch.end();
    }

    private void renderUI() {
        batch.setProjectionMatrix(uiCamera.getCamera().combined);
        batch.begin();
        hudRenderer.render(batch, 
            gameLogic.getPlayer(), 
            gameLogic.getBase(),
            gameLogic.getCurrentAction(),
            gameLogic.getTickCount(),
            uiCamera.getWidth(), 
            uiCamera.getHeight());
        batch.end();
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
    }
}