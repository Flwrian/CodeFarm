package fr.flwrian.codefarm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;

    private float x = 300;
    private float y = 200;

    private float speed = 200;

    private World world;
    private Texture grassTex;
    private Texture treeTex;
    private Texture stoneTex;


    @Override
    public void create() {
        batch = new SpriteBatch();
        image = makeColorTexture(Color.BLUE);

        world = new World();
        grassTex = makeColorTexture(Color.GREEN);
        treeTex = makeColorTexture(new Color(0.4f, 0.25f, 0.1f, 1));
        stoneTex = makeColorTexture(Color.GRAY);


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

        

        if (Gdx.input.isKeyPressed(Input.Keys.W)) y += speed * dt;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) y -= speed * dt;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) x -= speed * dt;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) x += speed * dt;

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        for (int tx = 0; tx < world.width; tx++) {
            for (int ty = 0; ty < world.height; ty++) {
                int tile = world.getTile(tx, ty);
                Texture tex = grassTex;
                if (tile == World.TREE) tex = treeTex;
                if (tile == World.STONE) tex = stoneTex;

                batch.draw(tex,
                    tx * world.tileSize,
                    ty * world.tileSize,
                    world.tileSize,
                    world.tileSize);
            }
        }

        // perso par-dessus
        batch.draw(image, x, y, 32, 32);

        batch.end();

    }


    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        grassTex.dispose();
        treeTex.dispose();
        stoneTex.dispose();

    }
}
