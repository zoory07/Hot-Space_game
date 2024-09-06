package main.java.zoory07.HotSpace.imagen.sombra_entity;



import java.awt.Color;
import java.awt.Graphics;







public class sombra_player {
   public int x, y;
   private int width,height;

   
   public sombra_player( int y, int x, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
   }
   
  
  
  public void update(){
    
  }
  
  public void render(Graphics g){
    int offsetX = 16; 
    int offsetY = +36;
    g.setColor(new Color(0, 0, 0, 100));
    g.fillOval(x + offsetX, y + offsetY, width, height / 4); 
   
  }


}
    

