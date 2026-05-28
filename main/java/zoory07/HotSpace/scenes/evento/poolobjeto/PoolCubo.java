package main.java.zoory07.HotSpace.scenes.evento.poolobjeto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import main.java.zoory07.HotSpace.entity.Cubo;


public class PoolCubo {
    
    private final List<Cubo> libres;
    private final List<Cubo> activos;
    private final BufferedImage sprite;
    private final int ancho;
    private final int alto;
    private final int capacidadInicial;
    
    public PoolCubo(BufferedImage sprite, int ancho, int alto, int capacidad) {
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
            libres.add(new Cubo(sprite, ancho, alto));
        }
    }
    
    public Cubo obtain(int x, int y) {
        Cubo c;
        
        if (libres.isEmpty()) {
            c = new Cubo(sprite, ancho, alto);
        } else {
            c = libres.remove(libres.size() - 1);
        }
        
        c.init(x, y);
        activos.add(c);
        return c;
    }
    
    public void free(Cubo c) {
        if (c == null) return;
        
        c.setActivo(false);
        if (activos.remove(c)) {
            libres.add(c);
        }
    }
    
    public void updateAll(int velocidad) {
        Iterator<Cubo> it = activos.iterator();
        
        while (it.hasNext()) {
            Cubo c = it.next();
            c.update(velocidad);
            
            if (!c.isActivo()) {
                it.remove();
                libres.add(c);
            }
        }
    }
    
    public void renderAll(Graphics g) {
        for (Cubo c : activos) {
            c.render(g);
        }
    }
    
    public void reiniciar() {
        for (Cubo c : activos) {
            c.setActivo(false);
            libres.add(c);
        }
        activos.clear();
        
        while (libres.size() < capacidadInicial) {
            libres.add(new Cubo(sprite, ancho, alto));
        }
    }
    
    public List<Cubo> getActivos() {
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
    public int getCapacidadInicial(){ 
        return capacidadInicial; 
    }
    public boolean hayActivos()     { 
        return !activos.isEmpty(); 
    }
}
