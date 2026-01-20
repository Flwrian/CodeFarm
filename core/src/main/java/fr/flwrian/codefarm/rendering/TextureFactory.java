package fr.flwrian.codefarm.rendering;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class TextureFactory {
    

    public static Texture createTreeTexture(int size) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        
        // Tronc (rectangle marron au centre-bas)
        pixmap.setColor(0.4f, 0.25f, 0.1f, 1f);
        int trunkW = size / 4;
        int trunkH = size / 3;
        pixmap.fillRectangle(size/2 - trunkW/2, 0, trunkW, trunkH);
        
        // Feuillage (cercle vert en haut)
        pixmap.setColor(0.2f, 0.6f, 0.2f, 1f);
        int radius = size / 3;
        fillCircle(pixmap, size/2, size - radius - 2, radius);
        
        // Highlights
        pixmap.setColor(0.3f, 0.7f, 0.3f, 1f);
        fillCircle(pixmap, size/2 - radius/3, size - radius, radius/2);
        
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    /**
     * Créer une texture de pierre (forme irrégulière)
     */
    public static Texture createStoneTexture(int size) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        
        // Background transparent
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        
        // Pierre (forme polygonale)
        pixmap.setColor(0.5f, 0.5f, 0.5f, 1f);
        
        int[] xs = {size/4, size*3/4, size*3/4, size/4};
        int[] ys = {size/4, size/3, size*2/3, size*3/4};
        fillPolygon(pixmap, xs, ys);
        
        // Ombres
        pixmap.setColor(0.3f, 0.3f, 0.3f, 1f);
        pixmap.fillRectangle(size/4, size/4, size/2, 2);
        
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    /**
     * Créer une texture de joueur (bonhomme simple)
     */
    public static Texture createPlayerTexture(int size) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        
        // Background
        pixmap.setColor(1f, 0.3f, 0.3f, 1f);
        fillCircle(pixmap, size/2, size - size/3, size/3);
        
        // Corps
        pixmap.fillRectangle(size/2 - size/6, size/3, size/3, size/2);
        
        // Yeux
        pixmap.setColor(1f, 1f, 1f, 1f);
        fillCircle(pixmap, size/2 - size/8, size - size/3, size/12);
        fillCircle(pixmap, size/2 + size/8, size - size/3, size/12);
        
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    /**
     * Créer texture de base (maison simple)
     */
    public static Texture createBaseTexture(int size) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        
        // Murs
        pixmap.setColor(0.8f, 0.6f, 0.3f, 1f);
        pixmap.fillRectangle(size/6, size/6, size*2/3, size*2/3);
        
        // Toit (triangle)
        pixmap.setColor(0.6f, 0.3f, 0.2f, 1f);
        int[] xs = {size/6, size/2, size*5/6};
        int[] ys = {size*5/6, size, size*5/6};
        fillPolygon(pixmap, xs, ys);
        
        // Porte
        pixmap.setColor(0.4f, 0.2f, 0.1f, 1f);
        pixmap.fillRectangle(size/2 - size/8, size/6, size/4, size/3);
        
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    // ========================================================================
    // Helper methods
    // ========================================================================

    private static void fillCircle(Pixmap pixmap, int centerX, int centerY, int radius) {
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                if (x*x + y*y <= radius*radius) {
                    pixmap.drawPixel(centerX + x, centerY + y);
                }
            }
        }
    }

    private static void fillPolygon(Pixmap pixmap, int[] xs, int[] ys) {
        // Simple scanline fill (basique)
        int minY = ys[0], maxY = ys[0];
        for (int y : ys) {
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }
        
        for (int y = minY; y <= maxY; y++) {
            int minX = pixmap.getWidth(), maxX = 0;
            for (int i = 0; i < xs.length; i++) {
                int j = (i + 1) % xs.length;
                if ((ys[i] <= y && y < ys[j]) || (ys[j] <= y && y < ys[i])) {
                    int x = xs[i] + (xs[j] - xs[i]) * (y - ys[i]) / (ys[j] - ys[i]);
                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                }
            }
            if (minX < maxX) {
                pixmap.drawLine(minX, y, maxX, y);
            }
        }
    }
}