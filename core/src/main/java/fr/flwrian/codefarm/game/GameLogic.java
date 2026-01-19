package fr.flwrian.codefarm.game;

import fr.flwrian.codefarm.environment.structures.Base;
import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.action.Action;
import fr.flwrian.codefarm.controller.Controller;
import fr.flwrian.codefarm.controller.GameContext;
import fr.flwrian.codefarm.controller.KeyboardController;
import fr.flwrian.codefarm.controller.ScriptController;
import fr.flwrian.codefarm.environment.World;

public class GameLogic {

    private final Player player;
    private final World world;
    private final GameContext ctx;
    private final Base base;
    private Controller controller;

    private Action currentAction = null;
    private float tickTimer = 0f;
    private int tickCount = 0;

    private final int ticksPerSecond;
    private final float tickInterval;
    private final boolean debug;

    public GameLogic(boolean debug) {
        this(50, debug);
    }

    public GameLogic(int ticksPerSecond, boolean debug) {
        this.ticksPerSecond = ticksPerSecond;
        this.tickInterval = 1f / ticksPerSecond;
        this.debug = debug;

        // Initialize game objects
        world = new World();
        player = new Player(1, 1);
        base = world.getBase();
        ctx = new GameContext(player, world, base);

        // Initialize controller
        controller = new ScriptController();
        controller = new KeyboardController();
    }

    public void update(float dt) {
        tickTimer += dt;

        while (tickTimer >= tickInterval) {
            tickTimer -= tickInterval;
            tick();
            tickCount++;
        }
    }

    private void tick() {
        if (debug)
            System.out.println("\n=== TICK " + tickCount + " ===");

        // Try to get a new action if none is running
        if (currentAction == null) {
            currentAction = controller.update();

            if (currentAction != null) {
                if (!currentAction.canStart(ctx)) {
                    debugLog("⚠️ Action " + currentAction + " cannot start (skipping)");
                    currentAction = null;
                    return;
                }

                debugLog("Starting: " + currentAction + " (cost: " + currentAction.totalCost() + ")");
                currentAction.start(ctx);
            }
        }

        // Process current action
        if (currentAction != null) {
            currentAction.applyTick(ctx);
            debugLog("Applied tick to " + currentAction + " (remaining: " + currentAction.remainingCost() + ")");

            if (currentAction.isFinished()) {
                debugLog("Finished: " + currentAction);
                currentAction.finish(ctx);
                currentAction = null;
            }
        }
    }

    private void debugLog(String msg) {
        if (debug)
            System.out.println(msg);
    }

    // Getters
    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public Base getBase() {
        return base;
    }

    public Action getCurrentAction() {
        return currentAction;
    }

    public int getTickCount() {
        return tickCount;
    }

    public int getTicksPerSecond() {
        return ticksPerSecond;
    }
}