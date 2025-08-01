package main.java.zoory07.HotSpace.scenes.evento;



import java.util.ArrayList;
import java.util.List;
import main.java.zoory07.HotSpace.entity.piedra;
import main.java.zoory07.HotSpace.entity.player;
import main.java.zoory07.HotSpace.imagen.hitbox;



// Evento que pausa el juego como colision con piedra, cualquier cosa.......

public class EventoColision {
    private player player;
    private List<piedra> cactusList;
    private tiempo tiempo;
    private boolean gameOverTriggered; // Cambiamos el nombre para claridad
    private int x, y, width, height;

    public EventoColision(player player, List<piedra> cactusList, tiempo tiempo) {
        this.player = player;
        this.cactusList = cactusList != null ? cactusList : new ArrayList<>();
        this.tiempo = tiempo;
        this.gameOverTriggered = false;
    }

    public void checkColision() {
        
        if (gameOverTriggered) {
            return;
        }
        
        hitbox playerHitbox = player.getHitbox();
        if (playerHitbox == null) return;
        
        // Verificar colisiones con cactus activos
        for (piedra c : cactusList) {
            if (c != null && c.isActiva()) {
                hitbox cactusHitbox = c.getHitbox();
                if (cactusHitbox != null && playerHitbox.collidesWith(cactusHitbox)) {
                    // ACTIVAR GAME OVER SOLO UNA VEZ
                    gameOverTriggered = true;
                    tiempo.pausar();
                    return; // Salir inmediatamente
                }
            }
        }
    }
    
    public void setListaCactus(List<piedra> cactusList) {
        this.cactusList = cactusList != null ? cactusList : new ArrayList<>();
    }
    
    public boolean isGameOver() {
        return gameOverTriggered;
    }
    
    public void reiniciar() {
        gameOverTriggered = false;
    }

    public int GetX(){
       return this.x;
    }
    
    public int GetY(){
       return this.y;
    }
    
    public int getWidth(){
       return width;
    } 
    
    public int getHeight(){
       return height;
    }

    public int setX(int escenaX) {
      return height;
    }
    
    public int setY(int escenaX) {
      return width;
    }
}