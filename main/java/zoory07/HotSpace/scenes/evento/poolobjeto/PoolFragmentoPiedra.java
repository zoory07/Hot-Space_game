package main.java.zoory07.HotSpace.scenes.evento.poolobjeto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import main.java.zoory07.HotSpace.entity.FragmentoPiedra;


public class PoolFragmentoPiedra {

    private final List<FragmentoPiedra> libres;
    private final List<FragmentoPiedra> activos;
    private final BufferedImage sprite;
    private final int ancho, alto;

    public PoolFragmentoPiedra(BufferedImage sprite, int ancho, int alto, int capacidad) {
        this.sprite = sprite;
        this.ancho  = ancho;
        this.alto   = alto;
        this.libres  = new ArrayList<>(capacidad);
        this.activos = new ArrayList<>(capacidad);
        for (int i = 0; i < capacidad; i++) libres.add(new FragmentoPiedra(sprite, ancho, alto));
    }

    public void spawnExplosion(int x, int y, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            FragmentoPiedra f = libres.isEmpty()
                ? new FragmentoPiedra(sprite, ancho, alto)
                : libres.remove(libres.size() - 1);
            f.init(x, y);
            activos.add(f);
        }
    }

    public void updateAll() {
        Iterator<FragmentoPiedra> it = activos.iterator();
        while (it.hasNext()) {
            FragmentoPiedra f = it.next();
            f.update(0);
            if (!f.isActivo()) {
                it.remove();
                libres.add(f);
            }
        }
    }

    public void renderAll(Graphics g) {
        for (FragmentoPiedra f : activos) f.render(g);
    }

    public void reiniciar() {
        for (FragmentoPiedra f : activos) { f.setActivo(false); libres.add(f); }
        activos.clear();
    }
}
