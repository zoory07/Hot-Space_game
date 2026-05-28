package main.java.zoory07.HotSpace.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.java.zoory07.HotSpace.game.Main;
import main.java.zoory07.HotSpace.imagen.hitbox;
import main.java.zoory07.HotSpace.imagen.sombra_entity.sombra_entity;


public class piedra extends Entity {
    
    private final BufferedImage sprite;
    private sombra_entity sombra;
    
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
    
   
       int sombraW = (int) (width * 0.9);  
       int sombraH = 15;  // Altura fija para elipse
       this.sombra = new sombra_entity(0, 0, sombraW, sombraH);
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
        actualizarSombra(); 
    }
    
    private void actualizarHitbox() {
        int hbW = hitbox.getWidth();
        int hbH = hitbox.getHeight();
        int offsetX = (width - hbW) / 2;
        int offsetY = (height - hbH) / 2;
        
        hitbox.updatePosition(x + offsetX + HITBOX_OFFSET_X, y + offsetY + HITBOX_OFFSET_Y);
    }
    

    private void actualizarSombra() {
        if (sombra == null) return;
    
        int sombraX = x + (width / 2) - (sombra.width / 2) + 25;
        int sombraY = y + (int)(height * 0.7);
    
        sombra.x = sombraX;
        sombra.y = sombraY;
        sombra.update();
    }
    
    @Override
    public void update(int velocidad) {
        if (!activa) return;
        
        y += velocidad;
        actualizarHitbox();
        actualizarSombra(); 
        
        if (y > Main.BASE_HEIGHT) {
            activa = false;
        }
    }
    
    @Override
    public void render(Graphics g) {
        if (!activa) return;
        
        // Dibujar sombra PRIMERO (debajo del sprite)
        if (sombra != null) {
            sombra.render(g);
        }
        
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