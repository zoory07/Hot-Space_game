package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import main.java.zoory07.HotSpace.entity.puntatierra;
import main.java.zoory07.HotSpace.scenes.evento.poolobjeto.PoolPuntaTierra;








public class GeneradorDePuntas {

    private final PoolPuntaTierra pool;
    private int timer;
    private int intervalo;
    private final int screenW;
    private final int screenH;
    private final int tamPunta;

    public GeneradorDePuntas(BufferedImage sprite, int ancho, int alto, int screenW, int screenH, int intervalo) {
        this.pool      = new PoolPuntaTierra(sprite, ancho, alto, 200);
        this.screenW   = screenW;
        this.screenH   = screenH;
        this.intervalo = intervalo;
        this.tamPunta  = ancho;
        this.timer     = intervalo - 1;
        preLlenar();
    }

    public void update(int velocidad) {
        timer++;
        if (timer >= intervalo) {
            timer = 0;
            spawnPuntas();
        }
        pool.updateAll(velocidad);
    }

    private void spawnPuntas() {
        // 50% de chance de generar punta en techo
        if (Math.random() < 0.5) {
            int y = calcularYTecho();
            pool.obtain(screenW, y, 1);
        }

        // 50% de chance de generar punta en suelo
        if (Math.random() < 0.5) {
            int y = calcularYSuelo();
            pool.obtain(screenW, y, -1);
        }
    }

    private void preLlenar() {
        // Intervalo más grande para no saturar la zona inicial
        int espaciado = tamPunta * 4;

        for (int x = screenW / 2; x <= screenW; x += espaciado) {
            if (Math.random() < 0.5) {
                pool.obtain(x, calcularYTecho(), 1);
            }
            if (Math.random() < 0.5) {
                pool.obtain(x, calcularYSuelo(), -1);
            }
        }
    }

    /**
     * Calcula Y para punta de techo basándose en GeneradorDeCubos.
     * Recibe el Y donde termina el techo de cubos.
     */
    public void spawnEnBordeTecho(int yFinTecho) {
        pool.obtain(screenW, yFinTecho - tamPunta, 1);
    }

    /**
     * Calcula Y para punta de suelo basándose en GeneradorDeCubos.
     * Recibe el Y donde empieza el suelo de cubos.
     */
    public void spawnEnBordeSuelo(int yInicioSuelo) {
        pool.obtain(screenW, yInicioSuelo, -1);
    }

    // Y por defecto si no tenés referencia al generador de cubos
    private int calcularYTecho() {
        int maxCubosTecho = 4 * tamPunta; // ALTURA_MAX del GeneradorDeCubos
        return (int)(Math.random() * maxCubosTecho);
    }

    private int calcularYSuelo() {
        int maxCubosSuelo = 4 * tamPunta;
        return screenH - maxCubosSuelo - tamPunta;
    }

    public void render(Graphics g)  { pool.renderAll(g); }

    public void reiniciar() {
        pool.reiniciar();
        timer = intervalo - 1;
        preLlenar();
    }

    public void setIntervalo(int intervalo) { 
        this.intervalo = intervalo; 
    }
    public List<puntatierra> getActivos() { 
        return pool.getActivos(); 
    }
    public boolean hayActivos() { 
        return pool.hayActivos(); 
    }
}
