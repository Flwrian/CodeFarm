package fr.flwrian.codefarm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import fr.flwrian.codefarm.ui.CraftingMenu;
import fr.flwrian.codefarm.recipe.RecipeManager;
import fr.flwrian.codefarm.ui.InventoryMenu;

import fr.flwrian.codefarm.camera.GameCamera;
import fr.flwrian.codefarm.camera.UICamera;
import fr.flwrian.codefarm.rendering.ImprovedWorldRenderer;
import fr.flwrian.codefarm.rendering.GridRenderer;
import fr.flwrian.codefarm.rendering.DebugRenderer;
import fr.flwrian.codefarm.ui.HudRenderer;
import fr.flwrian.codefarm.ui.ProfilerOverlay;
import fr.flwrian.codefarm.game.GameLogic;

public class Engine extends ApplicationAdapter {
    // UI
    private Stage uiStage;
    private Skin uiSkin;
    private CraftingMenu craftingMenu;
    private boolean craftingMenuVisible = false;
    private RecipeManager recipeManager;
    private InputProcessor previousInputProcessor;
    private InventoryMenu inventoryMenu;
    private boolean inventoryMenuVisible = false;

    private SpriteBatch batch;
    private BitmapFont font;
    
    private GameLogic gameLogic;
    private ImprovedWorldRenderer worldRenderer;
    private GridRenderer gridRenderer;
    private DebugRenderer debugRenderer;
    private HudRenderer hudRenderer;
    private ProfilerOverlay profilerOverlay;

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

        // Initialize RecipeManager and register recipes
        recipeManager = new RecipeManager();
        fr.flwrian.codefarm.recipe.Recipes.registerAll(recipeManager);

        // UI Stage & Skin (assume assets/ui/uiskin.json and uiskin.atlas exist)
        uiStage = new Stage(new ScreenViewport());
        try {
            uiSkin = new Skin(Gdx.files.internal("ui/plain-james-ui.json"));
        } catch (Exception e) {
            System.err.println("[WARN] Could not load ui/uiskin.json. Place a libGDX skin in assets/ui/ !");
            e.printStackTrace();
            uiSkin = null;
            try {
                com.badlogic.gdx.files.FileHandle[] found = Gdx.files.internal("ui").list();
                System.err.println("Files available in assets/ui/:");
                for (com.badlogic.gdx.files.FileHandle fh : found) {
                    System.err.println(" - " + fh.name());
                }
            } catch (Exception ex) {
                System.err.println("Could not list assets/ui/: " + ex.getMessage());
            }
        }
        craftingMenu = null;

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
    profilerOverlay = new ProfilerOverlay(font);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        // Handle input
        handleInput();
        if (craftingMenuVisible && craftingMenu != null) {
            uiStage.act(Gdx.graphics.getDeltaTime());
        }

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
        worldRenderer.render(batch, gameLogic.getCurrentPlayer(), worldCamera.getCamera());
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

        // profiler overlay
        profilerOverlay.render(batch, Gdx.graphics.getFramesPerSecond(), worldRenderer.getLastRenderedTileCount());

        // Debug text
        debugRenderer.renderPlayerInfo(batch,
            gameLogic.getCurrentPlayer(),
            gameLogic.getWorld(),
            uiCamera.getWidth(),
            uiCamera.getHeight());
        batch.end();

        // UI Stage (any UI menu)
        if ((craftingMenuVisible && craftingMenu != null) || (inventoryMenuVisible && inventoryMenu != null)) {
            uiStage.getViewport().apply();
            uiStage.act(Gdx.graphics.getDeltaTime());
            uiStage.draw();
        }
    }

    private void handleInput() {
        // Toggle Crafting Menu with C
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            System.out.println("Toggle Crafting Menu");
            if (craftingMenuVisible) {
                System.out.println("Hide Crafting Menu");
                if (craftingMenu != null) {
                    System.out.println("Removing crafting menu from stage");
                    craftingMenu.remove();
                    craftingMenu = null;
                }
                // restore previous input processor
                if (previousInputProcessor != null) {
                    Gdx.input.setInputProcessor(previousInputProcessor);
                    previousInputProcessor = null;
                } else {
                    Gdx.input.setInputProcessor(null);
                }
                craftingMenuVisible = false;
            } else {
                System.out.println("Show Crafting Menu");
                if (uiSkin != null) {
                    // If inventory is open, close it first
                    if (inventoryMenuVisible && inventoryMenu != null) {
                        inventoryMenu.remove();
                        inventoryMenu = null;
                        inventoryMenuVisible = false;
                    }
                    System.out.println("Sounding crafting menu");
                    craftingMenu = new CraftingMenu(uiSkin, recipeManager, gameLogic.getCurrentPlayer());
                    craftingMenu.setModal(true);
                    craftingMenu.setMovable(true);
                    uiStage.addActor(craftingMenu);
                    craftingMenu.toFront();
                    uiStage.setKeyboardFocus(craftingMenu);
                    // set stage as input processor, saving previous if not already saved
                    if (previousInputProcessor == null) previousInputProcessor = Gdx.input.getInputProcessor();
                    Gdx.input.setInputProcessor(uiStage);
                    craftingMenuVisible = true;
                }
            }
        }

        // Toggle Inventory with E
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (inventoryMenuVisible) {
                if (inventoryMenu != null) inventoryMenu.remove();
                inventoryMenu = null;
                // restore previous input processor
                if (previousInputProcessor != null) {
                    Gdx.input.setInputProcessor(previousInputProcessor);
                    previousInputProcessor = null;
                } else {
                    Gdx.input.setInputProcessor(null);
                }
                inventoryMenuVisible = false;
            } else {
                if (uiSkin != null) {
                    // If crafting is open, close it first
                    if (craftingMenuVisible && craftingMenu != null) {
                        craftingMenu.remove();
                        craftingMenu = null;
                        craftingMenuVisible = false;
                    }
                    inventoryMenu = new InventoryMenu(uiSkin, gameLogic.getCurrentPlayer());
                    uiStage.addActor(inventoryMenu);
                    inventoryMenu.toFront();
                    uiStage.setKeyboardFocus(inventoryMenu);
                    if (previousInputProcessor == null) previousInputProcessor = Gdx.input.getInputProcessor();
                    Gdx.input.setInputProcessor(uiStage);
                    inventoryMenuVisible = true;
                    // refresh contents in case inventory changed
                    inventoryMenu.updateContents();
                }
            }
        }
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
        if (uiStage != null) {
            uiStage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        worldRenderer.dispose();
        gridRenderer.dispose();
        debugRenderer.dispose();
        if (uiStage != null) uiStage.dispose();
        if (uiSkin != null) uiSkin.dispose();
        // Ensure the application and JVM exit when the window is closed.
        // This prevents `./gradlew run` from staying alive after the window is closed.
        try {
            if (Gdx.app != null) Gdx.app.exit();
        } catch (Exception e) {
            // ignore
        }
        // Force JVM exit as a final measure to avoid zombie processes.
        System.exit(0);
    }
}