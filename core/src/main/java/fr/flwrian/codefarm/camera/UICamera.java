package fr.flwrian.codefarm.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UICamera {
    
    private final OrthographicCamera camera;
    private final Viewport viewport;

    public UICamera() {
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
    }

    public void update() {
        camera.update();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public float getWidth() {
        return camera.viewportWidth;
    }

    public float getHeight() {
        return camera.viewportHeight;
    }
}