package main.java.zoory07.HotSpace.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.java.zoory07.HotSpace.imagen.hitbox;
import main.java.zoory07.HotSpace.imagen.sombra_entity.sombra_entity;




public class piedra {

  public static int length;
  public int x, y;
  private int width, height;
  private BufferedImage sprite;
  private hitbox hitbox;
  private double scale = 2.0;
  private boolean mostrarHitbox = false; 
  public sombra_entity sombra;
  
  
  public piedra(int x, int y, BufferedImage sprite){
     this.sprite = sprite;
     this.x = x;
     this.y = y;
      
     int hitboxWidth = (int) (sprite.getWidth() * scale * 0.89);
     int hitboxHeight = (int) (sprite.getWidth() * scale * 1.1);
     int hitboxOffsetX = (int) ((sprite.getWidth() * scale - hitboxWidth) / 2);
     int hitboxOffsetY = (int) ((sprite.getHeight() * scale - hitboxHeight) / 2);
    
     this.hitbox = new hitbox(x + hitboxOffsetX, y + hitboxOffsetY, hitboxWidth, hitboxHeight);
     //this.sombra = new sombra_entity(x, y, hitboxWidth, hitboxHeight / 2); 
  
  }


  public void update(int velocidad){
    this.y += velocidad;    
    if (this.hitbox != null) {
      this.hitbox.updatePosition(x, y);
    }
    if(this.sombra != null){
       this.sombra.x = this.x;
       this.sombra.y = this.y + height;
       this.sombra.update();
    }
  }
  
  public void toggleHitboxVisibility(){
    this.mostrarHitbox = !mostrarHitbox;
  }
  
  public void render(Graphics g){
     /*if(sombra != null){
        sombra.render(g);
     }*/
      
    if(mostrarHitbox && hitbox != null){
        hitbox.render(g);
    }
    
    //modificasion de tamaño
    int scale = 4;
    int newWidth = sprite.getWidth() * scale;
    int newHeight = sprite.getHeight() * scale;
    
    //Ajuste x e y para centrar el sprite
    int adjustedX = x - (newWidth - sprite.getWidth()) / 3;
    int adjustedY = y - (newHeight - sprite.getHeight()) / 2;
    
    if(sprite != null){
      g.drawImage(sprite, adjustedX, adjustedY, newWidth, newHeight, null);
    }
  }

    public hitbox getHitbox(){
      return hitbox;
    }



    public void setX(int x){
       this.x = x;  
    }

    public int getX() {
       return this.x;
    }

    public int getWidth() {
       return this.width;
    }



 
  
}
