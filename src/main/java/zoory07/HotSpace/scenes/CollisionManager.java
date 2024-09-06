package main.java.zoory07.HotSpace.scenes;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import main.java.zoory07.HotSpace.imagen.hitbox;









public class CollisionManager {
  private List<hitbox> hitboxes;
  private hitbox playerHitbox;  

  
    public CollisionManager() {
        this.hitboxes = new ArrayList<>();
    }

    public void setPlayerHitbox(hitbox playerHitbox) {
        this.playerHitbox = playerHitbox;  
    }

    public void addHitbox(hitbox box) {
        if (box != playerHitbox) {
            this.hitboxes.add(box); 
        }
    }

    public void checkCollisions() {
       
        for (hitbox hb : hitboxes) {
            if (hb != null) {
                hb.setColor(Color.RED);
            }
        }

      
        if (playerHitbox != null) {
            for (hitbox hb : hitboxes) {
                if (playerHitbox.collidesWith(hb)) {
                    playerHitbox.setColor(Color.GREEN);
                    hb.setColor(Color.GREEN);
                    System.out.println("Colisión entre jugador y " + hb);
                }
            }
        }

       
        for (int i = 0; i < hitboxes.size(); i++) {
            hitbox box1 = hitboxes.get(i);
            if (box1 == null) continue;

            for (int j = i + 1; j < hitboxes.size(); j++) {
                hitbox box2 = hitboxes.get(j);
                if (box2 == null) continue;

                if (box1.collidesWith(box2)) {
                    box1.setColor(Color.GREEN);
                    box2.setColor(Color.GREEN);
                    //System.out.println("Colision entre cactus");
                }
            }
        }
    }

  
  
  public void clear() {
    this.hitboxes.clear();
  }

  public void removeHitbox(hitbox hitbox) {
    hitboxes.remove(hitbox);
  }
    

  




}
