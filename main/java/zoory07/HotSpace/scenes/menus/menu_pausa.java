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
import main.java.zoory07.HotSpace.imagen.pausa;
import main.java.zoory07.HotSpace.scenes.Escena;
import main.java.zoory07.HotSpace.scenes.Sound;





public class menu_pausa implements Escena {
    private int seleccion = 0;
    private final String[] opciones = {"Reanudar", "Menu Principal"};
    private final pausa pausaImagen;
    private final BufferedImage fondo;

    private long lastMoveTime = System.currentTimeMillis();
    private static final int MOVE_DELAY = 100;       // ms entre movimientos en pausa

    // Flag para confirmar ENTER sólo al presionar y soltar
    private boolean confirm = false;
    private boolean enterReady = true;
    private Sound sonidoMenu; 
    
    public menu_pausa(int x, int y) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        fondo = ImageIO.read(getClass().getResourceAsStream("/resources/fondo_pausa.png"));
        pausaImagen = new pausa("/resources/pausa.png", x + 30, y + 30);
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

        // Confirmación sólo al presionar y soltar ENTER
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
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, 900, 600, null);
        }
        pausaImagen.render(g);
        g.setFont(new Font("Arial", Font.BOLD, 35));
        for (int i = 0; i < opciones.length; i++) {
            if (i == seleccion) {
                g.setColor(Color.RED);
                g.drawString("> " + opciones[i], 45, 300 + i * 50);
            } else {
                g.setColor(Color.WHITE);
                g.drawString(opciones[i], 45, 300 + i * 50);
            }
        }
    }

    /**
     * Devuelve true solo una vez tras una pulsación válida de ENTER.
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

