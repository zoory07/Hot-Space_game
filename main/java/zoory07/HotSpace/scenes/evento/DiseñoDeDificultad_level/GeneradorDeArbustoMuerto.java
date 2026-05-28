package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import main.java.zoory07.HotSpace.entity.ArbustoMuerto;
import main.java.zoory07.HotSpace.scenes.evento.poolobjeto.PoolArbustoMuerto;


public class GeneradorDeArbustoMuerto {

    private final PoolArbustoMuerto pool;
    private int timer;
    private int intervalo;
    private final int screenW;
    private final int screenH;
    private final int tamArbusto;
    private static final int CUBOS_SUELO = 3;
    
    public GeneradorDeArbustoMuerto(BufferedImage sprite, int ancho, int alto, int screenW, int screenH, int intervalo, GeneradorDeCubos generadorDeCubos) {
        this.pool = new PoolArbustoMuerto(sprite, ancho, alto, 20);
        this.screenW = screenW;
        this.screenH = screenH;
        this.intervalo = intervalo;
        this.tamArbusto = ancho;
        this.timer = 0;
    }

    public void update(int velocidad) {
        timer++;
        if (timer >= intervalo) {
            timer = 0;
            spawnArbusto();
        }
        pool.updateAll(velocidad);
    }

    private void spawnArbusto() {
       int alturaSuelo = CUBOS_SUELO * 30; // 30 = tamCubo
       int y = screenH - alturaSuelo - tamArbusto;
       pool.obtain(screenW, y);
    }

    public void render(Graphics g)  { pool.renderAll(g); }

    public void reiniciar() {
        pool.reiniciar();
        timer = 0;
    }

    public void setIntervalo(int intervalo) { 
        this.intervalo = intervalo; 
    }
    public List<ArbustoMuerto> getActivos() { 
        return pool.getActivos(); 
    }
    public boolean hayActivos() { 
        return pool.hayActivos(); 
    }
}
