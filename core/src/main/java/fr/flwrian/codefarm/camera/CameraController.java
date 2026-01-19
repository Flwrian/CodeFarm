package fr.flwrian.codefarm.camera;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Advanced camera controller with shake, zoom, and pan effects
 */
public class CameraController {
    
    private final GameCamera camera;
    
    // Camera shake
    private float shakeIntensity = 0f;
    private float shakeDuration = 0f;
    private float shakeTimer = 0f;
    private final Vector2 shakeOffset = new Vector2();
    
    // Camera zoom
    private float targetZoom = 1f;
    private float zoomSpeed = 5f;
    
    public CameraController(GameCamera camera) {
        this.camera = camera;
    }

    public void update(float deltaTime) {
        // Update shake
        if (shakeTimer > 0f) {
            shakeTimer -= deltaTime;
            float progress = shakeTimer / shakeDuration;
            float currentIntensity = shakeIntensity * progress;
            
            shakeOffset.x = MathUtils.random(-currentIntensity, currentIntensity);
            shakeOffset.y = MathUtils.random(-currentIntensity, currentIntensity);
            
            camera.getCamera().position.x += shakeOffset.x;
            camera.getCamera().position.y += shakeOffset.y;
        } else {
            shakeOffset.set(0, 0);
        }
        
        // Update zoom (smooth interpolation)
        float currentZoom = camera.getCamera().zoom;
        camera.getCamera().zoom += (targetZoom - currentZoom) * zoomSpeed * deltaTime;
    }

    /**
     * Shake the camera
     */
    public void shake(float intensity, float duration) {
        this.shakeIntensity = intensity;
        this.shakeDuration = duration;
        this.shakeTimer = duration;
    }

    /**
     * Set target zoom level (1.0 = normal, <1.0 = zoom in, >1.0 = zoom out)
     */
    public void setZoom(float zoom) {
        this.targetZoom = Math.max(0.1f, zoom);
    }

    /**
     * Instantly set zoom without interpolation
     */
    public void snapZoom(float zoom) {
        this.targetZoom = Math.max(0.1f, zoom);
        camera.getCamera().zoom = this.targetZoom;
    }

    public void setZoomSpeed(float speed) {
        this.zoomSpeed = speed;
    }

    public float getCurrentZoom() {
        return camera.getCamera().zoom;
    }

    public boolean isShaking() {
        return shakeTimer > 0f;
    }
}
