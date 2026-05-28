package main.java.zoory07.HotSpace.scenes.evento.poolobjeto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import main.java.zoory07.HotSpace.entity.Zanahoria;


public class PoolZanahoria {

    private final List<Zanahoria> libres;
    private final List<Zanahoria> activos;
    private final BufferedImage sprite;
    private final int ancho;
    private final int alto;
    private final int capacidadInicial;

    public PoolZanahoria(BufferedImage sprite, int ancho, int alto, int capacidad) {
        this.sprite = sprite;
        this.ancho = ancho;
        this.alto = alto;
        this.capacidadInicial = capacidad;
        this.libres = new ArrayList<>(capacidad);
        this.activos = new ArrayList<>(capacidad);
        prealocar();
    }

    private void prealocar() {
        for (int i = 0; i < capacidadInicial; i++) {
            libres.add(new Zanahoria(sprite, ancho, alto));
        }
    }

    public Zanahoria obtain(int x, int y) {
        Zanahoria z;

        if (libres.isEmpty()) {
            z = new Zanahoria(sprite, ancho, alto);
        } else {
            z = libres.remove(libres.size() - 1);
        }

        z.init(x, y);
        z.inicializarHitbox();
        activos.add(z);
        return z;
    }

    public void free(Zanahoria z) {
        if (z == null) return;
        z.setActivo(false);
        if (activos.remove(z)) {
            libres.add(z);
        }
    }

    public void updateAll(int velocidad) {
        Iterator<Zanahoria> it = activos.iterator();
        while (it.hasNext()) {
            Zanahoria z = it.next();
            z.update(velocidad);
            if (!z.isActivo()) {
                it.remove();
                libres.add(z);
            }
        }
    }

    public void renderAll(Graphics g) {
        for (Zanahoria z : activos) {
            z.render(g);
        }
    }

    public void reiniciar() {
        for (Zanahoria z : activos) {
            z.setActivo(false);
            libres.add(z);
        }
        activos.clear();

        while (libres.size() < capacidadInicial) {
            libres.add(new Zanahoria(sprite, ancho, alto));
        }
    }

    public List<Zanahoria> getActivos()  { 
        return Collections.unmodifiableList(activos); 
    }
    public int getCantidadActivos() { 
        return activos.size();
    }
    public int getCantidadLibres() { 
        return libres.size(); 
    }
    public int getCapacidadTotal(){ 
        return libres.size() + activos.size(); 
    }
    public boolean hayActivos() { 
        return !activos.isEmpty(); 
    }
}
