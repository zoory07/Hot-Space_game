package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import main.java.zoory07.HotSpace.entity.piedra;
import main.java.zoory07.HotSpace.entity.player;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.imagen.hitbox;
import main.java.zoory07.HotSpace.imagen.sombra_entity.sombra_entity;
import main.java.zoory07.HotSpace.scenes.CollisionManager;





           


public class AlazarCactus {
    
    private List<piedra> cactusList;
    private SpriteSheet spritesheet;
    private CollisionManager collisionManager;
    private int spawnX, minY, maxY;
    private boolean cactiGenerated  = false;
    private VelocidadDeObjecto VelocidadDeCactus;
    private sombra_entity sombra;
    private hitbox hitbox;
    
    public AlazarCactus(SpriteSheet spritesheet, int spawnX, int minY, int maxY, CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
        this.spritesheet = spritesheet;
        this.spawnX = spawnX;
        this.minY = minY;
        this.maxY = maxY;
        this.cactusList = new ArrayList<>();
        this.VelocidadDeCactus = new VelocidadDeObjecto(10, 5, 60);
        
    }

    public void CactusImagen(int x, int y) {
        BufferedImage cactusSprite = spritesheet.getSprite(0, 30, 30, 30);
        if(cactusSprite != null){
          piedra newCactus = new piedra(x, y, cactusSprite);
          collisionManager.addHitbox(newCactus.getHitbox());
          cactusList.add(newCactus);
       }
    }

    public void checkCollisionsWithPlayer(player player) {
        hitbox playerHitbox = player.getHitbox();

        for (piedra c : cactusList) {
            if (playerHitbox.collidesWith(c.getHitbox())) {
                //System.out.println("Colisión detectada con el cactus: " + c);
                // Pon ambas `hitbox` en verde para mostrar la colisión.
                playerHitbox.setColor(Color.GREEN);
                c.getHitbox().setColor(Color.GREEN);
            } else {
                playerHitbox.setColor(Color.RED);
                c.getHitbox().setColor(Color.RED);
            }
        }
    }

    public void update(player player) {
        checkCollisionsWithPlayer(player);
        collisionManager.checkCollisions();
        
        int velocidad = VelocidadDeCactus.calcularVelocidadActual();
        Iterator<piedra> iterator = cactusList.iterator();
        while(iterator.hasNext()){
            piedra c = iterator.next();
            c.update(velocidad);
            if(c.getX() <0){
               iterator.remove();
               collisionManager.removeHitbox(c.getHitbox());
            }
        }
    }

    public void render(Graphics g) {
        for (piedra c : cactusList) {
            c.render(g);
        }
    }
   
    public int getMinY(){
      return minY;
    }

    public void resetGemeracion() {
      cactusList.clear();
      collisionManager.clear();
    }

    public int getSpawnX() {
       return spawnX;
    }

    public int getMaxY() {
       return maxY; 
    }

    public Iterable<piedra> getCactusList() {
        return cactusList;
    }




       
    
 
    
    
    
    
    
}