package main.java.zoory07.HotSpace.entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.java.zoory07.HotSpace.imagen.hitbox;



public class ArbustoMuerto extends Entity {

    private BufferedImage sprite;
    private boolean activo;

    public ArbustoMuerto(BufferedImage sprite, int ancho, int alto) {
        super(0, 0, ancho, alto);
        this.sprite = sprite;
        this.width  = ancho;
        this.height = alto;
        this.activo = false;
    }

    public void init(int x, int y) {
        this.x = x;
        this.y = y;
        this.activo = true;
        if (hitbox != null) hitbox.updatePosition(x, y);
    }

    public void inicializarHitbox() {
        int hbW = (int)(width  * 0.8);
        int hbH = (int)(height * 0.8);
        int hbX = x + (width  - hbW) / 2;
        int hbY = y + (height - hbH) / 2;
        this.hitbox = new hitbox(hbX, hbY, hbW, hbH);
    }

    @Override
    public void update(int velocidad) {
        if (!activo) return;

        x -= velocidad;
        if (hitbox != null) hitbox.updatePosition(x, y);

        if (x < -width) {
            activo = false;
        }
    }

    @Override
    public void render(Graphics g) {
        if (!activo || sprite == null) return;
        g.drawImage(sprite, x, y, width, height, null);

        if (mostrarHitbox && hitbox != null) {
            hitbox.render(g);
        }
    }

    public boolean isActivo() { 
        return activo; 
    }
    public void setActivo(boolean b) { 
        this.activo = b; 
    }
    public Rectangle getBounds() { 
        return hitbox != null ? hitbox.getBounds() : new Rectangle(x, y, width, height); 
    }
    public hitbox getHitbox() { 
        return hitbox; 
    }
}
