package fr.flwrian.codefarm.action;

import fr.flwrian.codefarm.Direction;
import fr.flwrian.codefarm.game.GameContext;

public class MoveAction implements Action {
    private final Direction direction;
    private int remaining = 10;

    public MoveAction(Direction direction) {
        this.direction = direction;
    }

    @Override
    public int totalCost() { 
        return 10; 
    }

    @Override
    public int remainingCost() { 
        return remaining;
    }

    @Override
    public boolean canStart(GameContext ctx) {
        return ctx.player.canMove(ctx.world, direction);
    }

    @Override
    public void start(GameContext ctx) {

        ctx.player.move(ctx.world, direction);
    }

    @Override
    public void applyTick(GameContext ctx) {
        remaining--;
    }
    
    @Override
    public boolean isFinished() {
        return remaining <= 0;
    }
    
    @Override
    public void finish(GameContext ctx) {
    }

    @Override
    public String toString() {
        return "Move(" + direction + ")";
    }
}