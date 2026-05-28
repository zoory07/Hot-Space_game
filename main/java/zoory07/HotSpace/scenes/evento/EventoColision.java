package main.java.zoory07.HotSpace.scenes.evento;

import java.util.ArrayList;
import java.util.List;
import main.java.zoory07.HotSpace.entity.piedra;
import main.java.zoory07.HotSpace.entity.player;
import main.java.zoory07.HotSpace.imagen.hitbox;



public class EventoColision {
    
    private player player;
    private List<piedra> cactusList;
    private tiempo tiempo;
    private boolean gameOverTriggered;
    
    public EventoColision(player player, List<piedra> cactusList, tiempo tiempo) {
        this.player = player;
        this.cactusList = cactusList != null ? cactusList : new ArrayList<>();
        this.tiempo = tiempo;
        this.gameOverTriggered = false;
    }
    
    public void checkColision() {
        if (gameOverTriggered) return;
        
        hitbox playerHitbox = player.getHitbox();
        if (playerHitbox == null) return;
        
        for (piedra cactus : cactusList) {
            if (cactus != null && cactus.isActiva()) {
                hitbox cactusHitbox = cactus.getHitbox();
                
                if (cactusHitbox != null && playerHitbox.collidesWith(cactusHitbox)) {
                    gameOverTriggered = true;
                    tiempo.pausar();
                    return;
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
}