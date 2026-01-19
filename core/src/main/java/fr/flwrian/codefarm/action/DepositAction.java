package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.controller.GameContext;

public class DepositAction implements Action {
    @Override
    public int cost() { return 2; }

    @Override
    public boolean canExecute(GameContext ctx) {
        return ctx.player.canAct() && ctx.base.isPlayerOn(ctx.player);
    }

    @Override
    public void execute(GameContext ctx) {
        ctx.base.deposit(ctx.player);
    }
}
