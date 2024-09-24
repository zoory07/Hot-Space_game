package main.java.zoory07.HotSpace.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;






public class teclado implements KeyListener {

    private boolean[] teclas = new boolean[256]; 
    public boolean izquierda, derecha, arriba, abajo, enter, pausa;
    

    
    public void update() {
        izquierda = teclas[KeyEvent.VK_LEFT] || teclas[KeyEvent.VK_A]; 
        derecha = teclas[KeyEvent.VK_RIGHT] || teclas[KeyEvent.VK_D];
        arriba = teclas[KeyEvent.VK_UP] || teclas[KeyEvent.VK_W];
        abajo = teclas[KeyEvent.VK_DOWN] || teclas[KeyEvent.VK_S];  
        enter = teclas[KeyEvent.VK_ENTER];
        pausa = teclas[KeyEvent.VK_ESCAPE];
        
    }

    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyPressed(KeyEvent e) {
        teclas[e.getKeyCode()] = true;
        //System.out.println("Tecla presionada: " + e.getKeyCode()); 
    }

    @Override
    public void keyReleased(KeyEvent e) {
        teclas[e.getKeyCode()] = false;  
    }



    
}