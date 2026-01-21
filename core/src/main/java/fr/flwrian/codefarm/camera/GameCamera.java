package fr.flwrian.codefarm.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.MathUtils;

import fr.flwrian.codefarm.Player;
import fr.flwrian.codefarm.environment.World;

public class GameCamera {
    
    private final OrthographicCamera camera;
    private final Viewport viewport;
    
    private final float viewWidth;
    private final float viewHeight;
    private final float followSpeed;
    // Zoom settings
    private float minZoom = 0.5f;
    private float maxZoom = 3.0f;
    private float zoomStep = 0.1f;
    
    private Player target;
    private World world;

    public GameCamera(float viewWidth, float viewHeight) {
        this(viewWidth, viewHeight, 8f);
    }

    public GameCamera(float viewWidth, float viewHeight, float followSpeed) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.followSpeed = followSpeed;
        
        camera = new OrthographicCamera();
        viewport = new FitViewport(viewWidth, viewHeight, camera);
    }

    /**
     * Set the target for the camera to follow
     */
    public void setTarget(Player target, World world) {
        this.target = target;
        this.world = world;
    }

    /**
     * Update camera position with smooth follow
     */
    public void update(float deltaTime) {
        if (target == null || world == null) return;

        // Calculate target position (center of player tile)
        float targetX = target.x * world.tileSize + world.tileSize / 2f;
        float targetY = target.y * world.tileSize + world.tileSize / 2f;

        // Smooth follow using exponential decay
        float alpha = 1f - (float)Math.exp(-followSpeed * deltaTime);
        camera.position.x += (targetX - camera.position.x) * alpha;
        camera.position.y += (targetY - camera.position.y) * alpha;

        // Clamp to world bounds
        clampToWorld();
        
        camera.update();
    }

    /**
     * Instantly snap camera to target without smooth transition
     */
    public void snapToTarget() {
        if (target == null || world == null) return;
        
        float targetX = target.x * world.tileSize + world.tileSize / 2f;
        float targetY = target.y * world.tileSize + world.tileSize / 2f;
        
        camera.position.x = targetX;
        camera.position.y = targetY;
        
        clampToWorld();
        camera.update();
    }

    /**
     * Clamp camera position to world bounds
     */
    private void clampToWorld() {
        if (world == null) return;
        
        float worldWidth = world.width * world.tileSize;
        float worldHeight = world.height * world.tileSize;
        // Account for camera zoom: visible size = viewportSize * zoom
        float visibleWidth = camera.viewportWidth * camera.zoom;
        float visibleHeight = camera.viewportHeight * camera.zoom;
        float halfViewWidth = visibleWidth / 2f;
        float halfViewHeight = visibleHeight / 2f;

        // Clamp X: if world is smaller than visible area, center on world; otherwise clamp to edges
        if (worldWidth <= visibleWidth) {
            camera.position.x = worldWidth / 2f;
        } else {
            camera.position.x = Math.max(halfViewWidth,
                    Math.min(camera.position.x, worldWidth - halfViewWidth));
        }

        // Clamp Y
        if (worldHeight <= visibleHeight) {
            camera.position.y = worldHeight / 2f;
        } else {
            camera.position.y = Math.max(halfViewHeight,
                    Math.min(camera.position.y, worldHeight - halfViewHeight));
        }
    }

    /**
     * Zoom in (closer)
     */
    public void zoomIn() {
        setZoom(camera.zoom - zoomStep);
    }

    /**
     * Zoom out (farther)
     */
    public void zoomOut() {
        setZoom(camera.zoom + zoomStep);
    }

    /**
     * Set absolute zoom with clamping
     */
    public void setZoom(float z) {
        camera.zoom = MathUtils.clamp(z, minZoom, maxZoom);
        clampToWorld();
        camera.update();
    }

    public void setZoomStep(float step) { this.zoomStep = step; }
    public void setZoomLimits(float min, float max) { this.minZoom = min; this.maxZoom = max; }

    /**
     * Resize the viewport
     */
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    /**
     * Shake the camera
     */
    public void shake(float intensity, float duration) {
        throw new UnsupportedOperationException("Camera shake not implemented yet.");
    }

    // Getters
    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public float getX() {
        return camera.position.x;
    }

    public float getY() {
        return camera.position.y;
    }
}