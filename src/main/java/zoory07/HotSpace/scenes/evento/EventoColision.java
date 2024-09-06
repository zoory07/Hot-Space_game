package main.java.zoory07.HotSpace.scenes.evento;



import java.util.List;
import main.java.zoory07.HotSpace.entity.piedra;
import main.java.zoory07.HotSpace.entity.player;
import main.java.zoory07.HotSpace.imagen.hitbox;



//Evento que pausa el juego como colision con cactus, cualquier cosa.......

public class EventoColision {
    private player player;
    private List<piedra> cactusList;
    private tiempo tiempo;
    private boolean gameOver;

    public EventoColision(player player, List<piedra> cactusList, tiempo tiempo) {
        this.player = player;
        this.cactusList = cactusList;
        this.tiempo = tiempo;
        this.gameOver = false;
    }

    public void checkColision() {
        hitbox playerHitbox = player.getHitbox();
        for (piedra c : cactusList) {
            if (playerHitbox.collidesWith(c.getHitbox())) {
                gameOver = true;
                tiempo.pausar(); // Pausar el temporizador
                break;
            }
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }
}