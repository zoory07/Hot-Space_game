package main.java.zoory07.HotSpace.scenes.evento.poolobjeto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import main.java.zoory07.HotSpace.entity.ArbustoMuerto;


public class PoolArbustoMuerto {

    private final List<ArbustoMuerto> libres;
    private final List<ArbustoMuerto> activos;
    private final BufferedImage sprite;
    private final int ancho;
    private final int alto;
    private final int capacidadInicial;

    public PoolArbustoMuerto(BufferedImage sprite, int ancho, int alto, int capacidad) {
        this.sprite  = sprite;
        this.ancho = ancho;
        this.alto = alto;
        this.capacidadInicial = capacidad;
        this.libres = new ArrayList<>(capacidad);
        this.activos = new ArrayList<>(capacidad);
        prealocar();
    }

    private void prealocar() {
        for (int i = 0; i < capacidadInicial; i++) {
            libres.add(new ArbustoMuerto(sprite, ancho, alto));
        }
    }

    public ArbustoMuerto obtain(int x, int y) {
        ArbustoMuerto a;

        if (libres.isEmpty()) {
            a = new ArbustoMuerto(sprite, ancho, alto);
        } else {
            a = libres.remove(libres.size() - 1);
        }

        a.init(x, y);
        a.inicializarHitbox();
        activos.add(a);
        return a;
    }

    public void free(ArbustoMuerto a) {
        if (a == null) return;
        a.setActivo(false);
        if (activos.remove(a)) {
            libres.add(a);
        }
    }

    public void updateAll(int velocidad) {
        Iterator<ArbustoMuerto> it = activos.iterator();
        while (it.hasNext()) {
            ArbustoMuerto a = it.next();
            a.update(velocidad);
            if (!a.isActivo()) {
                it.remove();
                libres.add(a);
            }
        }
    }

    public void renderAll(Graphics g) {
        for (ArbustoMuerto a : activos) {
            a.render(g);
        }
    }

    public void reiniciar() {
        for (ArbustoMuerto a : activos) {
            a.setActivo(false);
            libres.add(a);
        }
        activos.clear();

        while (libres.size() < capacidadInicial) {
            libres.add(new ArbustoMuerto(sprite, ancho, alto));
        }
    }

    public List<ArbustoMuerto> getActivos()  { 
        return Collections.unmodifiableList(activos); 
    }
    public int getCantidadActivos() { 
        return activos.size(); 
    }
    public int getCantidadLibres() { 
        return libres.size(); 
    }
    public int getCapacidadTotal() { 
        return libres.size() + activos.size(); 
    }
    public boolean hayActivos() { 
        return !activos.isEmpty(); 
    }
}
