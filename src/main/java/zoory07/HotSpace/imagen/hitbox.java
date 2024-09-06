package main.java.zoory07.HotSpace.imagen;




import java.awt.Color;
import java.awt.Graphics;





public class hitbox {


    public int x;     
    public int y;
    public int width;  
    public int height;
    private Color color;
    private boolean hitbox;
 
    public hitbox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = Color.RED;
    }


    public void updatePosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }


    public void update(int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
    }
    
    public void setColor(Color color){
      this.color = color;
    
    }
    
    public void render(Graphics g) {
        g.setColor(this.color);  
        g.drawRect(this.x, this.y, this.width, this.height); 
        //System.out.println("Dibujando hitbox en: " + this.x + ", " + this.y + ", width: " + this.width + ", height: " + this.height);
    }

    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean collidesWith(hitbox other) {
       if(other ==null){
         return false;
       
       }
   
     return this.x < other.getX() + other.getWidth() &&
            this.x + this.width > other.getX() &&
            this.y < other.getY() + other.getHeight() &&
            this.y + this.height > other.getY();
               
    }
    
    


}