package main.java.zoory07.HotSpace.scenes.menus;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.pausa;



public class menu_pausa {
    private int x, y;
    private String[] opciones = {"Reanudar", "Salir al Menú Principal"};
    private int selecion = 0;
    private pausa pausa;
    
    public menu_pausa(int x, int y) {
        this.x = x;
        this.y = y;
        pausa = new pausa("/resources/pausa.png", x + 30, y + 30);
    
    }
   
    public void render(Graphics g) {
        
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(x, y, 900, 600); // Ajusta el tamaño 
        
        pausa.render(g);
        
        
        // Dibujar opciones del menú
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        int opcionesX = x + 85; // Ajusta la posición X de las opciones del menú
        int opcionesY = y + 260; // Ajusta la posición Y de las opciones del menú
        int espacioEntreOpciones = 50; // Ajusta el espacio entre opciones
       
        for (int i = 0; i < opciones.length; i++) {
            if (i == selecion) {
                g.setColor(Color.RED);
                g.drawString("> " + opciones[i], opcionesX, opcionesY + i * espacioEntreOpciones);
            } else {
                g.setColor(Color.WHITE);
                g.drawString(opciones[i], opcionesX, opcionesY + i * espacioEntreOpciones);
            }
        }
    }

    public void update(teclado input) {
        if (input.arriba) {
            selecion--;
            if (selecion < 0) {
                selecion = opciones.length - 1;
            }
            input.arriba = false;
        }
        if (input.abajo) {
            selecion++;
            if (selecion >= opciones.length) {
                selecion = 0;
            }
            input.abajo = false; // Reset the key state to avoid multiple triggers
        }
    }

    public int getSeleccion() {
        return selecion;
    }





}
