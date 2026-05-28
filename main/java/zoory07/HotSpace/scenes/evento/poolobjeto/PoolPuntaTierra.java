package main.java.zoory07.HotSpace.scenes.evento.poolobjeto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import main.java.zoory07.HotSpace.entity.puntatierra;


public class PoolPuntaTierra {

    private final List<puntatierra> libres;
    private final List<puntatierra> activos;
    private final BufferedImage sprite;
    private final int ancho;
    private final int alto;
    private final int capacidadInicial;

    public PoolPuntaTierra(BufferedImage sprite, int ancho, int alto, int capacidad) {
        this.sprite           = sprite;
        this.ancho            = ancho;
        this.alto             = alto;
        this.capacidadInicial = capacidad;
        this.libres           = new ArrayList<>(capacidad);
        this.activos          = new ArrayList<>(capacidad);
        prealocar();
    }

    private void prealocar() {
        for (int i = 0; i < capacidadInicial; i++) {
            libres.add(new puntatierra(sprite, ancho, alto));
        }
    }

    public puntatierra obtain(int x, int y, int direccion) {
        puntatierra p;

        if (libres.isEmpty()) {
            p = new puntatierra(sprite, ancho, alto);
        } else {
            p = libres.remove(libres.size() - 1);
        }

        p.init(x, y, direccion);
        p.inicializarHitbox();
        activos.add(p);
        return p;
    }

    public void free(puntatierra p) {
        if (p == null) return;
        p.setActivo(false);
        if (activos.remove(p)) {
            libres.add(p);
        }
    }

    public void updateAll(int velocidad) {
        Iterator<puntatierra> it = activos.iterator();
        while (it.hasNext()) {
            puntatierra p = it.next();
            p.update(velocidad);
            if (!p.isActivo()) {
                it.remove();
                libres.add(p);
            }
        }
    }

    public void renderAll(Graphics g) {
        for (puntatierra p : activos) {
            p.render(g);
        }
    }

    public void reiniciar() {
        for (puntatierra p : activos) {
            p.setActivo(false);
            libres.add(p);
        }
        activos.clear();

        while (libres.size() < capacidadInicial) {
            libres.add(new puntatierra(sprite, ancho, alto));
        }
    }

    public List<puntatierra> getActivos()  { 
        return Collections.unmodifiableList(activos); 
    }
    public int getCantidadActivos(){ 
        return activos.size(); 
    }
    public int getCantidadLibres(){ 
        return libres.size(); 
    }
    public int getCapacidadTotal(){ 
        return libres.size() + activos.size(); 
    }
    public boolean hayActivos(){ 
        return !activos.isEmpty(); 
    }
}
