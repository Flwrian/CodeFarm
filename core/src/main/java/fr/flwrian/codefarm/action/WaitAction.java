package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.controller.GameContext;

public class WaitAction implements Action {
    private int remaining;

    public WaitAction(int ticks) {
        this.remaining = ticks;
    }

    @Override public int totalCost() { return remaining; }
    @Override public int remainingCost() { return remaining; }

    @Override
    public boolean canStart(GameContext ctx) {
        return true;
    }

    @Override
    public void start(GameContext ctx) {}

    @Override
    public void applyTick(GameContext ctx) {
        remaining--;
    }

    @Override
    public boolean isFinished() {
        return remaining <= 0;
    }

    @Override
    public void finish(GameContext ctx) {}
}
