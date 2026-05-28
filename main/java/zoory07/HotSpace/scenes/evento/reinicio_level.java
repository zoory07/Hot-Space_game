package main.java.zoory07.HotSpace.scenes.evento;

import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.scenes.level.ModosDeJuegos.EscenaArcade;
import main.java.zoory07.HotSpace.scenes.level.ModosDeJuegos.EscenaContratiempo;
import main.java.zoory07.HotSpace.scenes.level.ModosDeJuegos.EscenaSurvival;



public class reinicio_level {
    private EscenaArcade nivel;
    private EscenaSurvival nivelSurvival;
    private EscenaContratiempo nivelContraTiempo;  // AGREGADO
    private SpriteSheet spriteSheet;
    private teclado teclado;
    private tiempo tiempo;
    
    public reinicio_level(SpriteSheet spriteSheet, teclado teclado, tiempo tiempo) {
        this.spriteSheet = spriteSheet;
        this.teclado = teclado;
        this.tiempo = tiempo;
    }

    public void setNivel(EscenaArcade nivel) {
        this.nivel = nivel;
    }

    public void setNivelSurvival(EscenaSurvival nivel) {
        this.nivelSurvival = nivel;
    }

    public void setNivelContraTiempo(EscenaContratiempo nivel) {  // AGREGADO
        this.nivelContraTiempo = nivel;
    }
    
    public void reiniciar() {
        if (teclado.enter) {
            if (nivel != null) {
                nivel.reiniciarNivel();
            } else if (nivelSurvival != null) {
                nivelSurvival.reiniciarNivel();
            } else if (nivelContraTiempo != null) {  // AGREGADO
                nivelContraTiempo.reiniciarNivel();
            }
            tiempo.reanudar();
            teclado.enter = false;
        }
    }
}