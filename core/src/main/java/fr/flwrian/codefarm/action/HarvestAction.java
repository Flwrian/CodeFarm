package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.controller.GameContext;

public class HarvestAction implements Action {
    private int remaining = 50;

    public int totalCost() { return 50; }
    public int remainingCost() { return remaining; }

    public boolean canStart(GameContext ctx) {
        return true;
    }

    public void start(GameContext ctx) {}

    public void applyTick(GameContext ctx) {
        remaining--;
    }

    public boolean isFinished() {
        return remaining <= 0;
    }

    public void finish(GameContext ctx) {
        ctx.player.harvest(ctx.world);
    }

    @Override
    public String toString() {
        return "HarvestAction";
    }
}


