package main.java.zoory07.HotSpace.scenes.evento;


import java.io.IOException;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.scenes.level.level_00_desierto;





public class reinicio_level {
    private level_00_desierto nivel;
    private SpriteSheet spriteSheet;
    private teclado teclado;
    private tiempo tiempo;
    
    
    public reinicio_level(SpriteSheet spriteSheet, teclado teclado, tiempo tiempo) {
        this.spriteSheet = spriteSheet;
        this.teclado = teclado;
        this.tiempo = tiempo;
        
    }

    public void setNivel(level_00_desierto nivel) {
        this.nivel = nivel;
    }

    public void reiniciar() {
        if (teclado.enter) {
            System.out.println("Reiniciando nivel..."); 
            try {
                nivel.reiniciarNivel();
                tiempo.reanudar();
                teclado.enter = false; 
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
