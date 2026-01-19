package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.controller.GameContext;

public class DepositAction implements Action {
    private int remaining = 10;

    public int totalCost() { return 10; }
    public int remainingCost() { return remaining; }

    public boolean canStart(GameContext ctx) {
        return ctx.base.isPlayerOn(ctx.player);
    }

    public void start(GameContext ctx) {}

    public void applyTick(GameContext ctx) {
        remaining--;
    }

    public boolean isFinished() {
        return remaining <= 0;
    }

    public void finish(GameContext ctx) {
        ctx.base.deposit(ctx.player);
    }
}
