
package main.java.zoory07.HotSpace.scenes.evento;

import main.java.zoory07.HotSpace.entity.Entity;






public class EscenaLimite {
   
    private int escenaX, escenaY;
    private int limiteX, limiteY;
   
    public EscenaLimite(int escenaX, int escenaY, int limiteX, int limiteY) {
        this.escenaX = escenaX;
        this.escenaY = escenaY;
        this.limiteX = limiteX;
        this.limiteY = limiteY;
    }
   
    /**
     * Restringe cualquier Entity dentro de los límites de la escena
     */
    public void RestricionDeLimite(Entity entidad) {
        int x = entidad.getX();
        int y = entidad.getY();
        int ancho = entidad.getWidth();
        int alto = entidad.getHeight();
        
        // Límite horizontal
        if (x < escenaX) {
            entidad.setX(escenaX);
        } else if (x + ancho > limiteX) {
            entidad.setX(limiteX - ancho);
        }
        
        // Límite vertical
        if (y < escenaY) {
            entidad.setY(escenaY);
        } else if (y + alto > limiteY) {
            entidad.setY(limiteY - alto);
        }
    }
    
    /**
     * Verifica si una entidad está dentro de los límites (sin modificar)
     */
    public boolean estaDentro(Entity entidad) {
        int x = entidad.getX();
        int y = entidad.getY();
        int ancho = entidad.getWidth();
        int alto = entidad.getHeight();
        
        return x >= escenaX && 
               x + ancho <= limiteX && 
               y >= escenaY && 
               y + alto <= limiteY;
    }
    
    /**
     * Verifica solo límites horizontales
     */
    public boolean estaDentroHorizontal(Entity entidad) {
        int x = entidad.getX();
        int ancho = entidad.getWidth();
        return x >= escenaX && x + ancho <= limiteX;
    }
    
    /**
     * Verifica solo límites verticales
     */
    public boolean estaDentroVertical(Entity entidad) {
        int y = entidad.getY();
        int alto = entidad.getHeight();
        return y >= escenaY && y + alto <= limiteY;
    }
    
    // Getters
    public int getEscenaX() { return escenaX; }
    public int getEscenaY() { return escenaY; }
    public int getLimiteX() { return limiteX; }
    public int getLimiteY() { return limiteY; }
    
    // Ancho y alto del área
    public int getAncho() { return limiteX - escenaX; }
    public int getAlto() { return limiteY - escenaY; }
}