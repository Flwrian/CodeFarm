package fr.flwrian.codefarm.controller;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import fr.flwrian.codefarm.environment.structures.Base;
import fr.flwrian.codefarm.Player;

import fr.flwrian.codefarm.action.Action;
import fr.flwrian.codefarm.action.DepositAction;
import fr.flwrian.codefarm.action.HarvestAction;
import fr.flwrian.codefarm.action.MoveAction;

public class KeyboardController implements Controller {

    @Override
    public Action update() {

        if (Gdx.input.isKeyPressed(Input.Keys.W)) return new MoveAction(0, 1);
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) return new MoveAction(0, -1);
        else if (Gdx.input.isKeyPressed(Input.Keys.A)) return new MoveAction(-1, 0);
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) return new MoveAction(1, 0);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            return new HarvestAction();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            return new DepositAction();
        }
        return null;
    }
    }
