package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import main.java.zoory07.HotSpace.entity.Zanahoria;
import main.java.zoory07.HotSpace.scenes.CollisionManager;
import main.java.zoory07.HotSpace.scenes.evento.poolobjeto.PoolZanahoria;


public class GeneradorDeZanahorias {

    private final PoolZanahoria pool;
    private final CollisionManager collisionManager;
    private final VelocidadDeObjecto velocidadDeZanahoria;
    private boolean enPausa = false;
    private int timer;
    private int intervalo;
    private final int screenW;
    private final int screenH;
    private final int tamZanahoria;
    private GeneradorDeCubos generadorCubos;

    public GeneradorDeZanahorias(BufferedImage sprite, int ancho, int alto,int capacidadInicial, int screenW, int screenH,int intervalo, GeneradorDeCubos generadorCubos,CollisionManager collisionManager) {
        this.pool = new PoolZanahoria(sprite, ancho, alto, capacidadInicial);
        this.collisionManager  = collisionManager;
        this.velocidadDeZanahoria = new VelocidadDeObjecto(10, 5, 60);
        this.screenW = screenW;
        this.screenH = screenH;
        this.intervalo = intervalo;
        this.tamZanahoria  = ancho;
        this.generadorCubos = generadorCubos;
        this.timer  = 0;
    }

    public void generarZanahoria(int x, int y) {
        Zanahoria z = pool.obtain(x, y);
        if (z != null) {
            collisionManager.addHitbox(z.getHitbox());
        }
    }

    public void setEnPausa(boolean pausa) {
        this.enPausa = pausa;
        if (pausa) {
            velocidadDeZanahoria.pausar();
        } else {
            velocidadDeZanahoria.reanudar();
        }
    }

    public boolean isEnPausa() {
        return enPausa;
    }

    public void update(int velocidad) {
        if (enPausa) return;

        timer++;
        if (timer >= intervalo) {
            timer = 0;
            spawnZanahoria();
        }

        collisionManager.checkCollisions();
        int vel = velocidadDeZanahoria.calcularVelocidadActual();
        pool.updateAll(vel);
    }

    private void spawnZanahoria() {
        if (generadorCubos == null) {
        // Sin terreno — spawna arriba como los cactus
        int x = (int)(Math.random() * screenW);
        int y = -(int)(Math.random() * 4) * tamZanahoria;
        generarZanahoria(x, y);
        return;
    }

     int yInicio = generadorCubos.getYInicioHueco() + tamZanahoria;
     int yFin    = generadorCubos.getYFinHueco()    - tamZanahoria;
     if (yFin <= yInicio) return;
     int y = yInicio + (int)(Math.random() * (yFin - yInicio));
     generarZanahoria(screenW, y);
   }

    public void render(Graphics g) {
        pool.renderAll(g);
    }

    public void reiniciar() {
        velocidadDeZanahoria.reset(10);
        pool.reiniciar();
        collisionManager.clear();
        timer    = 0;
        enPausa  = false;
    }

    public void incrementarVelocidad() {
        int actual = velocidadDeZanahoria.calcularVelocidadActual();
        velocidadDeZanahoria.setVelocidadBase(actual + 1);
    }

    public void setIntervalo(int intervalo)  { 
        this.intervalo = intervalo; 
    }
    public List<Zanahoria> getActivos() { 
        return pool.getActivos(); 
    }
    public boolean hayActivos() { 
        return pool.hayActivos(); 
    }
    public int getCantidadActivos() { 
        return pool.getCantidadActivos(); 
    }
    public int getVelocidadActual() { 
        return velocidadDeZanahoria.calcularVelocidadActual(); 
    }
}