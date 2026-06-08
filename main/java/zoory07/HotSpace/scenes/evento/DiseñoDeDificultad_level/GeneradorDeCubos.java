package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import main.java.zoory07.HotSpace.entity.Cubo;
import main.java.zoory07.HotSpace.scenes.evento.poolobjeto.PoolCubo;


public class GeneradorDeCubos {
    private final PoolCubo pool;
    private int timer;
    private int intervalo;
    private final int screenW;
    private final int screenH;
    private final int tamCubo;
    private static final int ALTURA_MIN = 1;
    private static final int ALTURA_MAX = 4;
    private int alturaTecho  = ALTURA_MIN;
    private int alturaSupelo = ALTURA_MIN;
    
    public GeneradorDeCubos(BufferedImage sprite, int ancho, int alto, int screenW, int screenH, int intervalo) {
        this.pool = new PoolCubo(sprite, ancho, alto, 500);
        this.screenW   = screenW;
        this.screenH   = screenH;
        this.intervalo = intervalo;
        this.tamCubo   = ancho;
        this.timer = intervalo - 1;
        preLlenarTerreno();
    }

    public void update(int velocidad) {
        timer++;
        if (timer >= intervalo) {
            timer = 0;
            spawnColumna();
        }
        pool.updateAll(velocidad);
    }

    private void spawnColumna() {
        alturaTecho  = variarAltura(alturaTecho);
        alturaSupelo = variarAltura(alturaSupelo);

        for (int i = 0; i < alturaTecho; i++) {
            pool.obtain(screenW, i * tamCubo);
        }
        for (int i = 0; i < alturaSupelo; i++) {
            pool.obtain(screenW, screenH - (i + 1) * tamCubo);
        }
    }

    private void preLlenarTerreno() {
        // Reset a mínimo para zona segura
        alturaTecho  = ALTURA_MIN;
        alturaSupelo = ALTURA_MIN;

        int zonaSegura = screenW / 2;

        for (int x = 0; x <= screenW; x += tamCubo) {
            if (x >= zonaSegura) {
                alturaTecho  = variarAltura(alturaTecho);
                alturaSupelo = variarAltura(alturaSupelo);
            }

            for (int i = 0; i < alturaTecho; i++) {
                pool.obtain(x, i * tamCubo);
            }
            for (int i = 0; i < alturaSupelo; i++) {
                pool.obtain(x, screenH - (i + 1) * tamCubo);
            }
        }
    }

    private int variarAltura(int actual) {
        int delta = (int)(Math.random() * 3) - 1;
        int nueva = actual + delta;
        return Math.max(ALTURA_MIN, Math.min(ALTURA_MAX, nueva));
    }

    public void render(Graphics g)  { pool.renderAll(g); }

    public void reiniciar() {
        pool.reiniciar();
        timer        = intervalo - 1;
        alturaTecho  = ALTURA_MIN; 
        alturaSupelo = ALTURA_MIN;
        preLlenarTerreno();        
    }
    
    
    public void setVelocidad(int velocidad) {
        this.intervalo = Math.max(1, tamCubo / velocidad);
    }
    
    public void setIntervalo(int intervalo) { 
        this.intervalo = intervalo; 
    }
    public List<Cubo> getActivos() { 
        return pool.getActivos(); 
    }
    public boolean hayActivos() { 
        return pool.hayActivos(); 
    }
    public int getYInicioHueco(){ 
        return alturaTecho * tamCubo; 
    }
    public int getYFinHueco() { 
        return screenH - (alturaSupelo * tamCubo); 
    }
    public int getCentroHueco() { 
        return (getYInicioHueco() + getYFinHueco()) / 2; 
    }
}