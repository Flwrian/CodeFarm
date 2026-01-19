
package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.controller.GameContext;

public class MoveAction implements Action {
    private int dx, dy;
    private int remaining = 5;

    public MoveAction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int totalCost() { return 5; }
    public int remainingCost() { return remaining; }

    public boolean canStart(GameContext ctx) {
        return ctx.player.canMove(ctx.world, dx, dy);
    }

    public void start(GameContext ctx) {}

    public void applyTick(GameContext ctx) {
        remaining--;
    }

    public boolean isFinished() {
        return remaining <= 0;
    }

    public void finish(GameContext ctx) {
        ctx.player.move(ctx.world, dx, dy);
    }


    @Override
    public String toString() {
        String direction;
        if (dx == 1 && dy == 0) direction = "right";
        else if (dx == -1 && dy == 0) direction = "left";
        else if (dx == 0 && dy == 1) direction = "up";
        else if (dx == 0 && dy == -1) direction = "down";
        else direction = "unknown";
        return "MoveAction(" + direction + ")";
    }
}

