package main.java.zoory07.HotSpace.game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;




public class Raton implements MouseListener, MouseMotionListener {
    public int mouseX = 0;
    public int mouseY = 0;
    public boolean botonIzquierdo = false;
    public boolean botonDerecho = false;
    public boolean botonCentral = false;

    private boolean clicIzquierdoRaw = false; // Interno
    public boolean clicIzquierdo = false;     // Para usar en lógica

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) botonIzquierdo = true;
        else if (e.getButton() == MouseEvent.BUTTON2) botonCentral = true;
        else if (e.getButton() == MouseEvent.BUTTON3) botonDerecho = true;
    }
    
    public void clear() {
      clicIzquierdoRaw = false;
      clicIzquierdo = false;
      botonIzquierdo = false;
      botonDerecho = false;
      botonCentral = false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            botonIzquierdo = false;
            clicIzquierdoRaw = true; // Marcar que hubo un clic
        }
        else if (e.getButton() == MouseEvent.BUTTON2) botonCentral = false;
        else if (e.getButton() == MouseEvent.BUTTON3) botonDerecho = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {}

    public void update() {
        clicIzquierdo = clicIzquierdoRaw; // Copiar el estado
        clicIzquierdoRaw = false;         // Limpiar el raw
    }
}