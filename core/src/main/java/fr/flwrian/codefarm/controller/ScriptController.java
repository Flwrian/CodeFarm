package fr.flwrian.codefarm.controller;



import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.Gdx;

import fr.flwrian.codefarm.action.Action;
import fr.flwrian.codefarm.action.DepositAction;
import fr.flwrian.codefarm.action.HarvestAction;
import fr.flwrian.codefarm.action.MoveAction;

public class ScriptController implements Controller {
    private Globals globals;

    private LuaValue coroutine;
    private Action nextAction = null;

    public ScriptController() {
        globals = JsePlatform.standardGlobals();
        registerApi();
        // load and execute the script to define functions
        String script = Gdx.files.internal("scripts/player.lua").readString();
        globals.load(script, "player.lua").call();
        // create co routine for update function
        LuaValue updateFunc = globals.get("update");
        if (!updateFunc.isnil()) {
            coroutine = globals.get("coroutine").get("create").call(updateFunc);
        }
    }

    private void registerApi() {
        // yield after each action to return control to Java
        globals.set("move", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                String dir = arg.checkjstring();
                switch (dir) {
                    case "up":    nextAction = new MoveAction(0, 1); break;
                    case "down":  nextAction = new MoveAction(0, -1); break;
                    case "left":  nextAction = new MoveAction(-1, 0); break;
                    case "right": nextAction = new MoveAction(1, 0); break;
                }
                // yield to return control to Java
                return globals.get("coroutine").get("yield").call();
            }
        });

        globals.set("harvest", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                nextAction = new HarvestAction();
                return globals.get("coroutine").get("yield").call();
            }
        });

        globals.set("deposit", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                nextAction = new DepositAction();
                return globals.get("coroutine").get("yield").call();
            }
        });
    }

    @Override
    public Action update() {
        if (coroutine == null) return null;
        nextAction = null;
        LuaValue status = globals.get("coroutine").get("status").call(coroutine);
        if (status.tojstring().equals("suspended")) {
            LuaValue result = globals.get("coroutine").get("resume").call(coroutine);
            if (!result.arg1().toboolean()) {
                System.err.println("Erreur Lua: " + result.arg(2).tojstring());
            }
        } else if (status.tojstring().equals("dead")) {
            System.out.println("Script terminé");
        }
        return nextAction;
    }
}