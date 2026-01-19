package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.controller.GameContext;

public class HarvestAction implements Action {
    @Override
    public int cost() { return 5; }

    @Override
    public boolean canExecute(GameContext ctx) {
        return ctx.player.canAct();
    }

    @Override
    public void execute(GameContext ctx) {
        ctx.player.harvest(ctx.world);
    }
}

