package main.java.zoory07.HotSpace.imagen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;




public class hitbox {
    
    private int x;     
    private int y;
    private int width;  
    private int height;
    private Color color;
    
    // Rectangle reutilizable para evitar crear objetos nuevos
    private Rectangle bounds;
 
    public hitbox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = Color.RED;
        this.bounds = new Rectangle(x, y, width, height);
    }
    
    public void updatePosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        bounds.setLocation(newX, newY);
    }
    
    public void updateSize(int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
        bounds.setSize(newWidth, newHeight);
    }
    
    public void update(int newX, int newY, int newWidth, int newHeight) {
        this.x = newX;
        this.y = newY;
        this.width = newWidth;
        this.height = newHeight;
        bounds.setBounds(newX, newY, newWidth, newHeight);
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public void render(Graphics g) {
        g.setColor(this.color);  
        g.drawRect(x, y, width, height); 
    }
    
    // Colisión usando Rectangle (más eficiente)
    public boolean collidesWith(hitbox other) {
        if (other == null) return false;
        return this.bounds.intersects(other.getBounds());
    }
    
    // Ahora devuelve Rectangle correctamente
    public Rectangle getBounds() {
        return bounds;
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Color getColor() { return color; }
    
    // Setters
    public void setX(int x) { 
        this.x = x; 
        bounds.x = x;
    }
    
    public void setY(int y) { 
        this.y = y; 
        bounds.y = y;
    }
    
    public void setWidth(int width) { 
        this.width = width; 
        bounds.width = width;
    }
    
    public void setHeight(int height) { 
        this.height = height;
        bounds.height = height;
    }
}