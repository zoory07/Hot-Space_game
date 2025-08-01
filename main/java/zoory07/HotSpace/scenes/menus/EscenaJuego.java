package main.java.zoory07.HotSpace.scenes.menus;

import java.awt.Graphics;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.scenes.Escena;
import main.java.zoory07.HotSpace.scenes.evento.tiempo;
import main.java.zoory07.HotSpace.scenes.level.level_00_desierto;



public class EscenaJuego implements Escena {
    private tiempo tiempo;
    private level_00_desierto nivel;

    public EscenaJuego(SpriteSheet ss, teclado t, tiempo tiempoGlobal) throws Exception {
        this.tiempo = tiempoGlobal;
        this.tiempo.iniciar();                
        this.nivel = new level_00_desierto(ss, t, tiempoGlobal);
    }

    @Override
    public void update() {
        nivel.update();
    }

    @Override
    public void render(Graphics g) {
        nivel.render(g);
        // Dibujo del tiempo:
        tiempo.render(g, 10, 20);
    }
}
