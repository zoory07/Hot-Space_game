package main.java.zoory07.HotSpace.scenes.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.pausa;
import main.java.zoory07.HotSpace.scenes.Sound;



public class menu_pausa {
    private int x, y;
    private String[] opciones = {"Reanudar", "Menu Principal"};
    private int selecion = 0;
    private pausa pausa;
    private boolean enPausa = false; 
    private long ultimoTiempoEntrada = 0;
    private final long retardoEntrada = 200; 
    private Sound sonidoMenu;
    
    public menu_pausa(int x, int y) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        this.x = x;
        this.y = y;
        pausa = new pausa("/resources/pausa.png", x + 30, y + 30);  // Imagen de pausa
        sonidoMenu = new Sound("menu.wav");
    }

    public void render(Graphics g) {
        // Dibujar el fondo del menú con transparencia
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(x, y, 900, 600); // Ajusta el tamaño del fondo

        // Renderizar la imagen de pausa
        pausa.render(g);

        // Dibujar las opciones del menu
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        int opcionesX = x + 45; // Ajusta la posición X de las opciones
        int opcionesY = y + 300; // Ajusta la posición Y de las opciones
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

    public void update(teclado teclado) {
        long tiempoActual = System.currentTimeMillis();

        if (tiempoActual - ultimoTiempoEntrada > retardoEntrada) {
        if (teclado.arriba) {
            selecion = (selecion - 1 + opciones.length) % opciones.length;
            teclado.arriba = false;
            sonidoMenu.play();
            ultimoTiempoEntrada = tiempoActual;
        } else if (teclado.abajo) {
            selecion = (selecion + 1) % opciones.length;
            sonidoMenu.play();
            teclado.abajo = false;
            ultimoTiempoEntrada = tiempoActual;
        }
    }
 }


    // Obtener la opción seleccionada actualmente
    public int getSeleccion() {
        return selecion;
    }

    // Método que devuelve si el juego está en pausa o no
    public boolean isEnPausa() {
        return enPausa;
    }

    // Método para alternar el estado de pausa
    public void alternarPausa() {
        enPausa = !enPausa;  
    }


}

