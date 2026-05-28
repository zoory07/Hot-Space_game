package main.java.zoory07.HotSpace.game;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class teclado implements KeyListener {
    private boolean[] teclas = new boolean[256]; 
    public boolean izquierda, derecha, arriba, abajo, enter, pausa, espacio; 
    
    public void update() {
        izquierda = teclas[KeyEvent.VK_LEFT] || teclas[KeyEvent.VK_A]; 
        derecha = teclas[KeyEvent.VK_RIGHT] || teclas[KeyEvent.VK_D];
        arriba = teclas[KeyEvent.VK_UP] || teclas[KeyEvent.VK_W];
        abajo = teclas[KeyEvent.VK_DOWN] || teclas[KeyEvent.VK_S];  
        enter = teclas[KeyEvent.VK_ENTER];
        pausa = teclas[KeyEvent.VK_ESCAPE];
        espacio = teclas[KeyEvent.VK_SPACE];
    }
    
    public void clear() {
      Arrays.fill(teclas, false);
      izquierda = derecha = arriba = abajo = enter = pausa = espacio = false; 
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < teclas.length) {
             teclas[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < teclas.length) {
             teclas[e.getKeyCode()] = false;  
        }
    }
}