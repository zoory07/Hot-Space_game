package main.java.zoory07.HotSpace.entity;

import java.awt.Graphics;
import main.java.zoory07.HotSpace.imagen.hitbox;



public abstract class Entity {
    
    public int x;
    public int y;
    protected int width, height;
    protected int velocidad;
    protected boolean activa = true;
    protected hitbox hitbox;
    protected boolean mostrarHitbox = false; // ---> Player
    
    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    // Metodos abstractos
    public abstract void update(int velocidad1);
    public abstract void render(Graphics g);
    
    // Colisión entre entidades
    public boolean colisionaCon(Entity otra) {
        if (this.hitbox == null || otra.hitbox == null) return false;
        return this.hitbox.getBounds().intersects(otra.hitbox.getBounds());
    }
    
    // Toggle hitbox
    public void toggleHitboxVisibility() {
        this.mostrarHitbox = !mostrarHitbox;
    }
    
    public void setMostrarHitbox(boolean mostrar) {
        this.mostrarHitbox = mostrar;
    }
    
    
    public int getX() { 
       return x; 
    }
    public int getY() { 
        return y; 
    }
    public void setX(int x) { 
        this.x = x; 
    }
    public void setY(int y) { 
        this.y = y; 
    }
    public int getWidth() { 
        return width; 
    }
    public int getHeight() { 
        return height; 
    }
    public boolean isActiva() { 
        return activa; 
    }
    public void setActiva(boolean activa) { 
        this.activa = activa; 
    }
    public hitbox getHitbox() { 
        return hitbox; 
    }

}
