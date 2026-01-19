package fr.flwrian.codefarm;

// ...existing code...

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.flwrian.codefarm.action.Action;
import fr.flwrian.codefarm.controller.Controller;
import fr.flwrian.codefarm.controller.GameContext;
import fr.flwrian.codefarm.controller.KeyboardController;
import fr.flwrian.codefarm.controller.ScriptController;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture playerTex, grassTex, treeTex, stoneTex, baseTex;

    private Player player;
    private World world;
    private Base base;

    private Controller controller;
    private GameContext ctx;

    private OrthographicCamera worldCamera;
    private OrthographicCamera uiCamera;
    private Viewport worldViewport;
    private Viewport uiViewport;

    private BitmapFont font;

    private static final float WORLD_VIEW_WIDTH = 640;
    private static final float WORLD_VIEW_HEIGHT = 480;

    private float tickTimer = 0f;
    private int tickPerSecond = 50;
    private float tickInterval = 1f / tickPerSecond;
    private Action currentAction = null;
    private int ntick = 0;

    private static final boolean DEBUG = true;

    @Override
    public void create() {
        batch = new SpriteBatch();

        playerTex = makeColorTexture(Color.RED);
        grassTex = makeColorTexture(Color.PINK);
        treeTex = makeColorTexture(new Color(0.4f, 0.25f, 0.1f, 1));
        stoneTex = makeColorTexture(Color.GRAY);
        baseTex = makeColorTexture(Color.BROWN);

        world = new World();
        player = new Player(1, 1);
        base = new Base(0, 0);

        ctx = new GameContext(player, world, base);
        controller = new KeyboardController();
        // controller = new ScriptController();

        font = new BitmapFont();

        worldCamera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();

        worldViewport = new FitViewport(WORLD_VIEW_WIDTH, WORLD_VIEW_HEIGHT, worldCamera);
        uiViewport = new ScreenViewport(uiCamera);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private Texture makeColorTexture(Color c) {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(c);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        tickTimer += dt;

        while (tickTimer >= tickInterval) {
            tickTimer -= tickInterval;
            controller.update();
            debugLog("--- TICK " + ntick + " ---");
            runTick();
            ntick++;
        }

        updateCamera(dt);

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Monde
        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        renderWorld();
        renderPlayer();
        batch.end();

        // UI
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        renderUI();
        batch.end();
    }

    private void updateCamera(float dt) {
        float targetX = player.x * world.tileSize + world.tileSize / 2f;
        float targetY = player.y * world.tileSize + world.tileSize / 2f;

        float speed = 8f;
        float alpha = 1f - (float)Math.exp(-speed * dt);
        worldCamera.position.x += (targetX - worldCamera.position.x) * alpha;
        worldCamera.position.y += (targetY - worldCamera.position.y) * alpha;

        float worldW = world.width * world.tileSize;
        float worldH = world.height * world.tileSize;
        float halfW = worldCamera.viewportWidth / 2f;
        float halfH = worldCamera.viewportHeight / 2f;

        if (worldW <= worldCamera.viewportWidth)
            worldCamera.position.x = worldW / 2f;
        else
            worldCamera.position.x = Math.max(halfW, Math.min(worldCamera.position.x, worldW - halfW));

        if (worldH <= worldCamera.viewportHeight)
            worldCamera.position.y = worldH / 2f;
        else
            worldCamera.position.y = Math.max(halfH, Math.min(worldCamera.position.y, worldH - halfH));

        worldCamera.update();
    }

    private void renderWorld() {
        for (int tx = 0; tx < world.width; tx++) {
            for (int ty = 0; ty < world.height; ty++) {
                int tile = world.getTile(tx, ty);
                Texture tex = grassTex;
                if (tile == World.TREE) tex = treeTex;
                if (tile == World.STONE) tex = stoneTex;
                if (tile == World.BASE) tex = baseTex;

                batch.draw(tex,
                        tx * world.tileSize,
                        ty * world.tileSize,
                        world.tileSize,
                        world.tileSize);
            }
        }
    }

    private void renderPlayer() {
        batch.draw(playerTex,
                player.x * world.tileSize,
                player.y * world.tileSize,
                world.tileSize,
                world.tileSize);
    }

    private void renderUI() {
    font.draw(batch,
        "Wood: " + player.wood +
        "  Stone: " + player.stone +
        "  Base Wood: " + base.storedWood +
        "  Base Stone: " + base.storedStone,
        10, uiCamera.viewportHeight - 20);

    // Affichage des FPS et infos utiles en haut à droite
    String info = "FPS: " + Gdx.graphics.getFramesPerSecond()
        + " | Ticks: " + ntick
        + " | Action: " + (currentAction == null ? "none" : currentAction.toString());
    font.draw(batch, info,
        uiCamera.viewportWidth - 320, uiCamera.viewportHeight - 20);
    }

    private void runTick() {
        debugLog("=== TICK START ===");
        debugLog("Current action: " + (currentAction == null ? "none" : currentAction.toString()));

        if (currentAction == null) {
            
            // get an action from the controller
            currentAction = controller.update();
            if (currentAction != null && !currentAction.canStart(ctx)) {
                debugLog("⚠️ Action " + currentAction.toString() + " cannot start (skipping)");
                currentAction = null;
            } else if (currentAction != null) {
                debugLog("Starting: " + currentAction.toString() + " (cost: " + currentAction.totalCost() + ")");
                currentAction.start(ctx);
            }
        }

        if (currentAction != null) {
            currentAction.applyTick(ctx);
            debugLog("Applied tick to " + currentAction.toString() + " (remaining: " + currentAction.remainingCost() + ")");
            if (currentAction.isFinished()) {
                debugLog("Finished: " + currentAction.toString());
                currentAction.finish(ctx);
                currentAction = null;
            }
        }

        debugLog("=== TICK END ===");
    }

    private void debugLog(String msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }

    @Override
    public void resize(int width, int height) {
        worldViewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTex.dispose();
        grassTex.dispose();
        treeTex.dispose();
        stoneTex.dispose();
        baseTex.dispose();
        font.dispose();
    }
}