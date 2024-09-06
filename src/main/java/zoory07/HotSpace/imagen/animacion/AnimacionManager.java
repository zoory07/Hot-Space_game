package main.java.zoory07.HotSpace.imagen.animacion;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





public class AnimacionManager {
    private Map<String,List<BufferedImage>> animations;
    private String currentAnimation;
    private int currentFrameIndex;
    private long lastFrameTime;
    private long frameDuration;

    public AnimacionManager(long frameDuration) {
        this.animations = new HashMap<>();
        this.currentFrameIndex = 0;
        this.lastFrameTime = 0;
        this.frameDuration = frameDuration;
    }

    public void addFrame(String animationName, BufferedImage frame) {
        if (!animations.containsKey(animationName)) {
            animations.put(animationName, new ArrayList<>());
        }
        animations.get(animationName).add(frame);
    }

    public void addAnimation(String animationName, List<BufferedImage> frames) {
        animations.put(animationName, new ArrayList<>(frames));
    }

    public void setAnimation(String animationName) {
        if (!animationName.equals(currentAnimation)) {
            currentAnimation = animationName;
            currentFrameIndex = 0;
            lastFrameTime = 0;
        }
    }

    public void update(long currentTime) {
        if (currentAnimation == null || !animations.containsKey(currentAnimation)) {
            return;
        }

        if (currentTime - lastFrameTime >= frameDuration) {
            currentFrameIndex = (currentFrameIndex + 1) % animations.get(currentAnimation).size();
            lastFrameTime = currentTime;
        }
    }

    public void render(Graphics g, int x, int y) {
        if (currentAnimation == null || !animations.containsKey(currentAnimation)) {
            return;
        }

        BufferedImage frame = animations.get(currentAnimation).get(currentFrameIndex);
        g.drawImage(frame, x, y, null);
    }




}
