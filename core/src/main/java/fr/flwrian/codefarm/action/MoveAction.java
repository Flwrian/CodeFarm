
package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.controller.GameContext;

public class MoveAction implements Action {
    private int dx, dy;
    private boolean done = false;

    public MoveAction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int totalCost() { return 1; }
    public int remainingCost() { return done ? 0 : 1; }

    public boolean canStart(GameContext ctx) {
        return ctx.player.canMove(ctx.world, dx, dy);
    }

    public void start(GameContext ctx) {}

    public void applyTick(GameContext ctx) {
        ctx.player.move(ctx.world, dx, dy);
        done = true;
    }

    public boolean isFinished() {
        return done;
    }

    public void finish(GameContext ctx) {}
}

