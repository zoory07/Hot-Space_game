package main.java.zoory07.HotSpace.scenes.evento.poolobjeto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import main.java.zoory07.HotSpace.entity.piedra;





public class PoolPiedras {
    public final List<piedra> libres = new ArrayList<>();
    public final List<piedra> activas = new ArrayList<>();
    public final BufferedImage sprite;
    public final int ancho;
    public final int alto;
    public final int initialCapacity;

    public PoolPiedras(BufferedImage sprite, int ancho, int alto, int capacity) {
        this.sprite = sprite;
        this.ancho = ancho;
        this.alto = alto;
        this.initialCapacity = capacity;
        for (int i = 0; i < capacity; i++) {
            libres.add(new piedra(sprite, ancho, alto));
        }
    }

    /**
     * Obtiene una piedra lista para usar o reciclada
     */
    public piedra obtain(int x, int y) {
        piedra p;
        if (libres.isEmpty()) {
            p = new piedra(sprite, ancho, alto);
        } else {
            p = libres.remove(libres.size() - 1);
        }
        p.init(x, y);
        activas.add(p);
        return p;
    }

    /**
     * Actualiza todas las piedras activas y recicla las inactivas
     */
    public void updateAll(int velocidad) {
        Iterator<piedra> it = activas.iterator();
        while (it.hasNext()) {
            piedra p = it.next();
            p.update(velocidad);
            if (!p.isActiva()) {
                it.remove();
                libres.add(p);
            }
        }
    }

    /**
     * Dibuja todas las piedras activas
     */
    public void renderAll(Graphics g) {
        for (piedra p : activas) {
            p.render(g);
        }
    }

    /**
     * Devuelve una lista inmodificable de las piedras activas
     */
    public Iterable<piedra> getActivas() {
        return Collections.unmodifiableList(activas);
    }
}


