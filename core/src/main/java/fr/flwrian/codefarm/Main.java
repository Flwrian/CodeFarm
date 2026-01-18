package fr.flwrian.codefarm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import fr.flwrian.codefarm.controller.Controller;
import fr.flwrian.codefarm.controller.GameContext;
import fr.flwrian.codefarm.controller.KeyboardController;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture playerTex;

    private Player player;
    private World world;
    private Base base;

    private Texture grassTex;
    private Texture treeTex;
    private Texture stoneTex;
    private Texture baseTex;

    private Controller controller;
    private GameContext ctx;


    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerTex = makeColorTexture(Color.RED);
        baseTex = makeColorTexture(Color.BROWN);

        world = new World();
        player = new Player(1, 1);
        font = new BitmapFont();
        base = new Base(0, 0);

        grassTex = makeColorTexture(Color.PINK);
        treeTex = makeColorTexture(new Color(0.4f, 0.25f, 0.1f, 1));
        stoneTex = makeColorTexture(Color.GRAY);

        ctx = new GameContext(player, world, base);
        controller = new KeyboardController();

    }

    private Texture makeColorTexture(Color c) {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(c);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        player.update(dt);

        player.update(dt);
        controller.update(ctx);


        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        // monde
        for (int tx = 0; tx < world.width; tx++) {
            for (int ty = 0; ty < world.height; ty++) {
                int tile = world.getTile(tx, ty);
                Texture tex = grassTex;
                if (tile == World.TREE) tex = treeTex;
                if (tile == World.STONE) tex = stoneTex;
                if (tile == World.BASE) tex = baseTex;


                batch.draw(tex,
                        tx * world.tileSize,
                        ty * world.tileSize,
                        world.tileSize,
                        world.tileSize);
            }
        }

        font.draw(batch,
    "Wood: " + player.wood + "  Stone: " + player.stone,
    10, 470);

        font.draw(batch,
    "Base - Wood: " + base.storedWood + "  Stone: " + base.storedStone,
    10, 450);


        // joueur
        batch.draw(playerTex,
                player.x * world.tileSize,
                player.y * world.tileSize,
                world.tileSize,
                world.tileSize);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTex.dispose();
        grassTex.dispose();
        treeTex.dispose();
        stoneTex.dispose();
        font.dispose();
        baseTex.dispose();
    }
}
