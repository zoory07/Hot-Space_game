package main.java.zoory07.HotSpace.scenes.menus;

import java.awt.Graphics;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.scenes.Escena;
import main.java.zoory07.HotSpace.scenes.evento.tiempo;
import main.java.zoory07.HotSpace.scenes.level.ModosDeJuegos.EscenaArcade;
import main.java.zoory07.HotSpace.scenes.level.ModosDeJuegos.EscenaContratiempo;
import main.java.zoory07.HotSpace.scenes.level.ModosDeJuegos.EscenaSurvival;


public class EscenaJuego implements Escena {
    private tiempo tiempo;
    private Escena modoActual;

    public EscenaJuego(SpriteSheet ss, teclado t, tiempo tiempoGlobal, int modo) throws Exception {
        this.tiempo = tiempoGlobal;
        this.tiempo.iniciar();

        switch (modo) {
            case 0:
                modoActual = new EscenaArcade(ss, t, tiempoGlobal);
                break;
            case 1:
                modoActual = new EscenaSurvival(ss, t, tiempoGlobal);
                break;
            case 2:
                modoActual = new EscenaContratiempo(ss, t, tiempoGlobal);
                break;
            default:
                modoActual = new EscenaArcade(ss, t, tiempoGlobal);
                break;
        }
    }

    @Override
    public void update() {
        modoActual.update();
    }

    @Override
    public void render(Graphics g) {
        modoActual.render(g);

        // Solo dibuja el cronómetro en Arcade y Survival
        if (!(modoActual instanceof EscenaContratiempo)) {
        tiempo.render(g, 10, 20);
      }
    }
}
