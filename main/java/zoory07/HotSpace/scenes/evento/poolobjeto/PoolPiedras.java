package main.java.zoory07.HotSpace.scenes.evento.poolobjeto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import main.java.zoory07.HotSpace.entity.piedra;



public class PoolPiedras {
    
    private final List<piedra> libres;
    private final List<piedra> activas;
    private final BufferedImage sprite;
    private final int ancho;
    private final int alto;
    private final int capacidadInicial;
    
    public PoolPiedras(BufferedImage sprite, int ancho, int alto, int capacidad) {
        this.sprite = sprite;
        this.ancho = ancho;
        this.alto = alto;
        this.capacidadInicial = capacidad;
        this.libres = new ArrayList<>(capacidad);
        this.activas = new ArrayList<>(capacidad);
        
        prealocar();
    }
    
    private void prealocar() {
        for (int i = 0; i < capacidadInicial; i++) {
            libres.add(new piedra(sprite, ancho, alto));
        }
    }
    
    /**
     * Obtiene una piedra lista para usar (reciclada o nueva)
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
     * Devuelve una piedra al pool manualmente
     */
    public void free(piedra p) {
        if (p == null) return;
        
        p.setActiva(false);
        if (activas.remove(p)) {
            libres.add(p);
        }
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
     * Reinicia el pool a su estado inicial
     */
    public void reiniciar() {
        // Reciclar todas las activas
        for (piedra p : activas) {
            p.setActiva(false);
            libres.add(p);
        }
        activas.clear();
        
        // Asegurar capacidad mínima
        while (libres.size() < capacidadInicial) {
            libres.add(new piedra(sprite, ancho, alto));
        }
    }
    
    /**
     * Lista de piedras activas (solo lectura)
     */
    public List<piedra> getActivas() {
        return Collections.unmodifiableList(activas);
    }
    
    // Getters útiles
    public int getCantidadActivas() { 
        return activas.size(); 
    }
    
    public int getCantidadLibres() { 
        return libres.size(); 
    }
    
    public int getCapacidadTotal() { 
        return libres.size() + activas.size(); 
    }
    
    public int getCapacidadInicial() { 
        return capacidadInicial; 
    }
    
    public boolean hayActivas() {
        return !activas.isEmpty();
    }
}