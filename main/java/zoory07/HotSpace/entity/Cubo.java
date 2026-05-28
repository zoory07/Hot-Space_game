package main.java.zoory07.HotSpace.entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.java.zoory07.HotSpace.imagen.hitbox;





public class Cubo extends Entity {

    private BufferedImage sprite;
    private int width, height;
    private boolean activo;
    private hitbox hb;

    public Cubo(BufferedImage sprite, int ancho, int alto) {
        super(0, 0, ancho, alto);
        this.sprite = sprite;
        this.width  = ancho;
        this.height = alto;
        this.activo = false;
        this.hb = new hitbox(0, 0, ancho, alto);  // AGREGADO
    }

    public void init(int x, int y) {
        this.x = x;
        this.y = y;
        this.activo = true;
        hb.updatePosition(x, y);  // AGREGADO
    }

    @Override
    public void update(int velocidad) {
        if (!activo) return;

        x -= velocidad;
        hb.updatePosition(x, y);  // AGREGADO

        if (x < -width) {
            activo = false;
        }
    }

    @Override
    public void render(Graphics g) {
        if (!activo || sprite == null) return;
        g.drawImage(sprite, x, y, width, height, null);
        // hb.render(g);  // descomentar para debug
    }

    public hitbox getHitbox() { 
        return hb; 
    }
    public Rectangle getBounds() { 
        return hb.getBounds(); 
    }

    public void setActivo(boolean b) { 
        this.activo = b; 
    }

    public boolean isActivo() { 
        return activo; 
    }

    public int getAncho() { 
        return width; 
    }
    public int getAlto() { 
        return height; 
    }
}