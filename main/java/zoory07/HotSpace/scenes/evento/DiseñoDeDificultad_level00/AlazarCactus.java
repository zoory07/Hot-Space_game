package main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.java.zoory07.HotSpace.entity.piedra;
import main.java.zoory07.HotSpace.entity.player;
import main.java.zoory07.HotSpace.scenes.CollisionManager;
import main.java.zoory07.HotSpace.scenes.evento.poolobjeto.PoolPiedras;




public class AlazarCactus {
    private final PoolPiedras pool;
    private final CollisionManager collisionManager;
    private final VelocidadDeObjecto velocidadDeCactus;
    private boolean enPausa = false;

    public AlazarCactus(BufferedImage cactusSprite,int anchoCactus,int altoCactus,int capacidadInicial,CollisionManager collisionManager) {
        this.pool = new PoolPiedras(cactusSprite, anchoCactus, altoCactus, capacidadInicial);
        this.collisionManager = collisionManager;
        this.velocidadDeCactus = new VelocidadDeObjecto(10, 5, 60);
    }

    /**
     * Genera un nuevo cactus en la posición dada, reciclando del pool
     */
    public void generarCactus(int x, int y) {
        piedra p = pool.obtain(x, y);
        collisionManager.addHitbox(p.getHitbox());
    }

    /**
     * Ajusta pausa y reanuda la velocidad de los cactus
     */
    public void setEnPausa(boolean pausa) {
        this.enPausa = pausa;
        if (pausa) velocidadDeCactus.pausar();
        else velocidadDeCactus.reanudar();
    }

    /**
     * Mueve y recicla cactus activos, y verifica colisiones
     */
    public void update(player player) {
        if (enPausa) return;
        collisionManager.checkCollisions();
        int vel = velocidadDeCactus.calcularVelocidadActual();
        pool.updateAll(vel);
    }

    /**
     * Dibuja todos los cactus activos
     */
    public void render(Graphics g) {
        pool.renderAll(g);
    }

    /**
     * Reinicia el pool y limpia colisiones (por ejemplo al morir)
     */
    public void reiniciar() {
        velocidadDeCactus.reset(10);
        pool.libres.clear();
        pool.activas.clear();
        // Pre-alocar de nuevo
        for (int i = 0; i < pool.initialCapacity; i++) {
            pool.libres.add(new piedra(pool.sprite, pool.ancho, pool.alto));
        }
        collisionManager.clear();
    }

    /**
     * Incrementa la velocidad base de los cactus en 1 unidad.
     */
    public void incrementarVelocidad() {
        int actual = velocidadDeCactus.calcularVelocidadActual();
        velocidadDeCactus.setVelocidadBase(actual + 1);
    }

    /**
     * Devuelve una lista inmodificable de los cactus activos.
     */
    public List<piedra> getCactusActivos() {
        return Collections.unmodifiableList(pool.activas);
    }
}
