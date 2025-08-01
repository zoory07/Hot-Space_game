package main.java.zoory07.HotSpace.scenes.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import main.java.zoory07.HotSpace.game.Main;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.scenes.Escena;
import main.java.zoory07.HotSpace.scenes.Sound;




public class Inicio_menu implements Escena {
    private int seleccion = 0;
    private final String[] opciones = {"Jugar", "Salir"};
    private final BufferedImage fondo;
    private final BufferedImage titulo;

    private long lastMoveTime = System.currentTimeMillis();
    private static final int MOVE_DELAY = 120;       
    
    // Flags para confirmar ENTER solo al presionar y soltar
    private boolean confirm = false;
    private boolean enterReady = true;
    private Sound sonidoMenu; 
    
    public Inicio_menu() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        fondo  = ImageIO.read(getClass().getResourceAsStream("/resources/menu_inicio.png"));
        titulo = ImageIO.read(getClass().getResourceAsStream("/resources/titulo.png"));
        sonidoMenu = new Sound("menu.wav");
    }
    
   
    
    @Override
    public void update() {
        teclado input = Main.teclado;
        long now = System.currentTimeMillis();

        // Movimiento de selección con debounce
        if (now - lastMoveTime >= MOVE_DELAY) {
            if (input.arriba) {
                seleccion = (seleccion - 1 + opciones.length) % opciones.length;
                input.arriba = false;
                lastMoveTime = now;
                sonidoMenu.play();
            } else if (input.abajo) {
                seleccion = (seleccion + 1) % opciones.length;
                input.abajo = false;
                lastMoveTime = now;
                sonidoMenu.play();
            }
        }

        // Confirmacion al presionar y soltar ENTER
        if (input.enter) {
            if (enterReady) {
                confirm = true;
                enterReady = false;
            }
        } else {
            enterReady = true;
        }
    }

    @Override
    public void render(Graphics g) {
        if (fondo  != null) g.drawImage(fondo,  0, 0, 900, 600, null);
        if (titulo != null) g.drawImage(titulo, 0, 20, 390, 190, null);

        g.setFont(new Font("Arial", Font.BOLD, 36));
        for (int i = 0; i < opciones.length; i++) {
            if (i == seleccion) {
                g.setColor(Color.RED);
                g.drawString("> " + opciones[i], 100, 300 + i * 50);
            } else {
                g.setColor(Color.WHITE);
                g.drawString(opciones[i], 120, 300 + i * 50);
            }
        }
    }

    /**
     * Devuelve true solo una vez tras una pulsacion valida de ENTER.
     */
    public boolean consumeConfirm() {
        if (confirm) {
            confirm = false;
            return true;
        }
        return false;
    }

    public int getSeleccion() {
        return seleccion;
    }
}
