package main.java.zoory07.HotSpace.imagen;



import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;





public class SpriteSheet {
    private BufferedImage sheet;

    public SpriteSheet(BufferedImage sheet) {
        this.sheet = sheet;
    }

    public BufferedImage getSprite(int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }

    public List<BufferedImage> getAnimationFrames(int startX, int startY, int frameWidth, int frameHeight, int numFrames) {
        List<BufferedImage> frames = new ArrayList<>();
        for (int i = 0; i < numFrames; i++) {
            int x = startX + (i * frameWidth);
            int y = startY;
            frames.add(getSprite(x, y, frameWidth, frameHeight));
        }
        return frames;
    }
    
  

 
}

