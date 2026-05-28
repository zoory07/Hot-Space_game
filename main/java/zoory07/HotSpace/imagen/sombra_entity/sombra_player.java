package main.java.zoory07.HotSpace.imagen.sombra_entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;





public class sombra_player {
   public int x, y;
   private int width,height;
   private Image frameActual;
   
   
   public sombra_player( int y, int x, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        
   }
   
  
  
  public void update(){
    
  }
  
 public void render(Graphics g) {
    // Calcular posición de la sombra basada en la posición del jugador
    int offsetX = 16;
    int offsetY = 36;
    int xSombra = x + offsetX;
    int ySombra = y + offsetY;
    int limiteMinX = 18; 
    int limiteMaxX = 895;
    int limiteMinY = 420;
    int limiteMaxY = 960;
    
    
    if (xSombra < limiteMinX) {
        xSombra = limiteMinX;
    } else if (xSombra + width > limiteMaxX) {
        xSombra = limiteMaxX - width;
    }

    if (ySombra < limiteMinY) {
        ySombra = limiteMinY;
    } else if (ySombra + height / 4 > limiteMaxY) {
        ySombra = limiteMaxY - height / 4;
    }

    // Dibujar la sombra
    g.setColor(new Color(0, 0, 0, 100));
    g.fillOval(xSombra, ySombra, width, height / 4);

    // Dibujar al jugador
    g.drawImage(frameActual, x, y, null);
 }




}
    

