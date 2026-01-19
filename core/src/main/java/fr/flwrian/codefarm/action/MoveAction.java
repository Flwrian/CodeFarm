
package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.controller.GameContext;

public class MoveAction implements Action {
    private int dx, dy;

    public MoveAction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public int cost() { return 1; }

    @Override
    public boolean canExecute(GameContext ctx) {
        return ctx.player.canAct() && ctx.player.canMove(ctx.world, dx, dy);
    }

    @Override
    public void execute(GameContext ctx) {
        ctx.player.move(ctx.world, dx, dy);
    }
}
