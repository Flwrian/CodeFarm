package fr.flwrian.codefarm.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import fr.flwrian.codefarm.Base;
import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.World;

public class KeyboardController implements Controller {
    @Override
    public void update(GameContext ctx) {
        Player p = ctx.player;
        World w = ctx.world;
        Base b = ctx.base;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) p.move(w, 0, 1);
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) p.move(w, 0, -1);
        else if (Gdx.input.isKeyPressed(Input.Keys.A)) p.move(w, -1, 0);
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) p.move(w, 1, 0);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            p.harvest(w);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (b.isPlayerOn(p)) {
                b.deposit(p);
            }
        }
    }
}
