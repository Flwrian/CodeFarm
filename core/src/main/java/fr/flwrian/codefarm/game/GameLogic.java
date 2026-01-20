package fr.flwrian.codefarm.game;

import fr.flwrian.codefarm.environment.structures.Base;
import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.action.Action;
import fr.flwrian.codefarm.controller.Controller;
import fr.flwrian.codefarm.controller.KeyboardController;
import fr.flwrian.codefarm.controller.ScriptController;
import fr.flwrian.codefarm.environment.World;
import java.util.*;

public class GameLogic {

    private final List<Player> players = new ArrayList<>();
    private final Map<Player, Controller> controllers = new HashMap<>();
    private final Map<Player, GameContext> contexts = new HashMap<>();
    private final Map<Player, Action> currentActions = new HashMap<>();
    private Player currentPlayer;
    private final World world;
    private final Base base;
    private float tickTimer = 0f;
    private int tickCount = 0;
    private final int ticksPerSecond;
    private final float tickInterval;
    private final boolean debug;

    public GameLogic(boolean debug) {
        this(100, debug);
    }

    public GameLogic(int ticksPerSecond, boolean debug) {
        this.ticksPerSecond = ticksPerSecond;
        this.tickInterval = 1f / ticksPerSecond;
        this.debug = debug;

        // Initialize game objects
        world = new World();
        base = world.getBase();

        Player p1 = new Player(1, 1);
        Player p2 = new Player(3, 3);
        Player p3 = new Player(5, 5);
        players.add(p1);
        players.add(p2);
        players.add(p3);

        for (Player p : players) {
            GameContext ctx = new GameContext(p, world, base);
            contexts.put(p, ctx);
        }

        currentPlayer = p1;
        controllers.put(p1, new KeyboardController(contexts.get(p1)));
        controllers.put(p2, new ScriptController());
        controllers.put(p3, new ScriptController());
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

        for (Player p : players) {
            Controller ctrl = controllers.get(p);
            GameContext ctx = contexts.get(p);
            Action currentAction = currentActions.get(p);

            // Try to get a new action if none is running
            if (currentAction == null) {
                currentAction = ctrl.update();
                if (currentAction != null) {
                    if (!currentAction.canStart(ctx)) {
                        debugLog("⚠️ Action " + currentAction + " cannot start (skipping)");
                        currentAction = null;
                        currentActions.put(p, null);
                        continue;
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
            currentActions.put(p, currentAction);
        }
    }

    private void debugLog(String msg) {
        if (debug)
            System.out.println(msg);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public World getWorld() {
        return world;
    }

    public Base getBase() {
        return base;
    }

    public GameContext getCtx() {
        return contexts.get(currentPlayer);
    }

    public Action getCurrentAction() {
        return currentActions.get(currentPlayer);
    }

    public int getTickCount() {
        return tickCount;
    }

    public int getTicksPerSecond() {
        return ticksPerSecond;
    }

    public void switchToPlayer(Player newPlayer) {
        if (newPlayer == currentPlayer) return;
        controllers.put(currentPlayer, new ScriptController());
        controllers.put(newPlayer, new KeyboardController(contexts.get(newPlayer)));
        currentPlayer = newPlayer;
    }
}