package main.java.zoory07.HotSpace.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.java.zoory07.HotSpace.game.Main;
import main.java.zoory07.HotSpace.imagen.hitbox;



public class piedra extends Entity {
    
    private final BufferedImage sprite;
    
    
    // Offsets del hitbox
    private static final int HITBOX_OFFSET_X = 50;
    private static final int HITBOX_OFFSET_Y = 55;
    
    public piedra(BufferedImage sprite, int width, int height) {
        super(0, 0, width, height);
        this.sprite = sprite;
        this.activa = false;
        
        inicializarHitbox();
    }
    
    private void inicializarHitbox() {
       int hbW = (int) (width * 0.50);
       int hbH = (int) (height * 0.50);
       this.hitbox = new hitbox(0, 0, hbW, hbH);
    
   

       
    }
    
    /**
     * Inicializa o reinicia la piedra para reciclarla desde el pool
     */
    public void init(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.activa = true;
        this.mostrarHitbox = false;
        
        actualizarHitbox();
        
    }
    
    private void actualizarHitbox() {
        int hbW = hitbox.getWidth();
        int hbH = hitbox.getHeight();
        int offsetX = (width - hbW) / 2;
        int offsetY = (height - hbH) / 2;
        
        hitbox.updatePosition(x + offsetX + HITBOX_OFFSET_X, y + offsetY + HITBOX_OFFSET_Y);
    }
    


    
    @Override
    public void update(int velocidad) {
        if (!activa) return;
        
        y += velocidad;
        actualizarHitbox();
       
        
        if (y > Main.BASE_HEIGHT) {
            activa = false;
        }
    }
    
    @Override
    public void render(Graphics g) {
        if (!activa) return;
        

        
        // Dibujar sprite
        int adjX = x + (width - sprite.getWidth()) / 2;
        int adjY = y + (height - sprite.getHeight()) / 2;
        g.drawImage(sprite, adjX, adjY, width, height, null);
        
        if (mostrarHitbox && hitbox != null) {
            hitbox.render(g);
        }
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
}