package main.java.zoory07.HotSpace.imagen.animacion.entity_a.player;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class Player_animacion_corriendo {
    private List<BufferedImage> frames;
    private int currentFrameIndex;
    private long lastFrameTime;
    private long frameDuracion;

    public Player_animacion_corriendo(List<BufferedImage> frames, long frameDuracion) {
        this.frames = frames;
        this.frameDuracion = frameDuracion;
        this.currentFrameIndex = 0;
        this.lastFrameTime = System.currentTimeMillis();
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= frameDuracion) {
            currentFrameIndex = (currentFrameIndex + 1) % frames.size();
            lastFrameTime = currentTime;
        }
    }

    public void render(Graphics g, int x, int y, int width, int height) {
        if (!frames.isEmpty()) {
            BufferedImage frame = frames.get(currentFrameIndex);
            g.drawImage(frame, x, y, width, height, null);
        }
    }
}
