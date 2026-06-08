package main.java.zoory07.HotSpace.imagen.sombra_entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;





public class sombra_player {
   public int x, y;
   private int width, height;
   private Image frameActual;
   private int limiteMinY = 420;  
   private boolean fijarEnSuelo = false;  

   public sombra_player(int y, int x, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
   }

   public void update() {
   }

   public void setLimiteSuelo(int y) {
        this.limiteMinY = y;
   }

   public int getLimiteSuelo() {
        return limiteMinY;
   }

   
   public void setFijarEnSuelo(boolean fijar) {
        this.fijarEnSuelo = fijar;
   }

   public void render(Graphics g) {
        int offsetX = 16;
        int xSombra = x + offsetX;
        int ySombra;

        if (fijarEnSuelo) {
            // Survival — sombra siempre en el suelo
            ySombra = limiteMinY;
        } else {
            // Arcade y Contrarreloj — sombra sigue al player
            int offsetY = 36;
            ySombra = y + offsetY;

            int limiteMinYLocal = 420;
            int limiteMaxY = 960;

            if (ySombra < limiteMinYLocal) {
                ySombra = limiteMinYLocal;
            } else if (ySombra + height / 4 > limiteMaxY) {
                ySombra = limiteMaxY - height / 4;
            }
        }

        int limiteMinX = 18;
        int limiteMaxX = 895;

        if (xSombra < limiteMinX) {
            xSombra = limiteMinX;
        } else if (xSombra + width > limiteMaxX) {
            xSombra = limiteMaxX - width;
        }

        g.setColor(new Color(0, 0, 0, 100));
        g.fillOval(xSombra, ySombra, width, height / 4);
        g.drawImage(frameActual, x, y, null);
   }
}