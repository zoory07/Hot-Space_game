package main.java.zoory07.HotSpace.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.java.zoory07.HotSpace.imagen.hitbox;



public class puntatierra extends Entity {

    private BufferedImage sprite;
    private boolean activo;
    private int direccion;

    public puntatierra(BufferedImage sprite, int ancho, int alto) {
        super(0, 0, ancho, alto);
        this.sprite = sprite;
        this.width  = ancho;
        this.height = alto;
        this.activo = false;
    }

    /**
     * direccion: 1 = cuelga del techo | -1 = sale del suelo
     */
    public void init(int x, int y, int direccion) {
        this.x         = x;
        this.y         = y;
        this.direccion = direccion;
        this.activo    = true;
        if (hitbox != null) hitbox.updatePosition(x, y);
    }

    @Override
    public void update(int velocidad) {
        if (!activo) return;

        x -= velocidad; // se mueve con el terreno hacia la izquierda

        if (hitbox != null) hitbox.updatePosition(x, y);

        if (x < -width) {
            activo = false;
        }
    }

    @Override
    public void render(Graphics g) {
     if (!activo || sprite == null) return;

     Graphics2D g2d = (Graphics2D) g;

     if (direccion == -1) {
        // Suelo — normal, punta hacia arriba
        g2d.drawImage(sprite, x, y, width, height, null);
     } else {
        // Techo — voltear verticalmente, punta hacia abajo
        g2d.drawImage(sprite, x, y + height, width, -height, null);
    }
  }

    public void inicializarHitbox() {
        // Hitbox más angosta que el sprite para que sea justa
        int hbW = (int)(width  * 0.4);
        int hbH = (int)(height * 0.85);
        int hbX = x + (width - hbW) / 2;
        int hbY = (direccion == 1) ? y : y + (int)(height * 0.15);
        this.hitbox = new hitbox(hbX, hbY, hbW, hbH);
    }

    public boolean isActivo() { 
        return activo; 
    }
    public void setActivo(boolean b) { 
        this.activo = b; 
    }
    public int getDireccion() { 
        return direccion; 
    }
    public Rectangle getBounds() { 
        return hitbox != null ? hitbox.getBounds() : new Rectangle(x, y, width, height); 
    }
    public hitbox getHitbox() { 
        return hitbox; 
    }
}
