package main.java.zoory07.HotSpace.scenes.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import main.java.zoory07.HotSpace.game.InputManager;
import main.java.zoory07.HotSpace.game.Main;
import main.java.zoory07.HotSpace.scenes.Escena;
import main.java.zoory07.HotSpace.scenes.Sound;



public class Inicio_menu implements Escena {
    private int seleccion = 0;
    private int lastSeleccion = -1;  // Para detectar cambios
    private final String[] opciones = {"Jugar", "Opciones", "Salir"};
    private final BufferedImage fondo;
    private final BufferedImage titulo;

    private long lastMoveTime = System.currentTimeMillis();
    private static final int MOVE_DELAY = 120;
    
    // Constantes para posicion del menú
    private static final int MENU_X = 100;
    private static final int MENU_Y_START = 250;
    private static final int MENU_SPACING = 60;
    private static final int MENU_WIDTH = 200;
    
    // Tamaño de fuente base
    private static final int FONT_BASE = 50;
    
    // Animacion zoom interfaz
    private float zoomScale = 0.2f;
    private static final float ZOOM_MAX = 1.3f;      
    private static final float ZOOM_SPEED = 0.05f;   
    
    // Flags para confirmación
    private boolean confirm = false;
    private boolean enterReady = true;
    private Sound sonidoMenu;
    
    public Inicio_menu() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        fondo  = ImageIO.read(getClass().getResourceAsStream("/resources/menu_inicio.png"));
        titulo = ImageIO.read(getClass().getResourceAsStream("/resources/titulo.png"));
        sonidoMenu = new Sound("menu.wav");
    }
    
    /**
     * Verifica si el mouse esta sobre una opción del menu
     */
    private int getOpcionBajoMouse(int logicalX, int logicalY) {
        for (int i = 0; i < opciones.length; i++) {
            int x = MENU_X;
            int y = MENU_Y_START + i * MENU_SPACING;
            
            int yMin = y - 40;
            int yMax = y + 5;

            if (logicalX >= x && logicalX <= x + MENU_WIDTH && logicalY >= yMin && logicalY <= yMax) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Convierte coordenadas de mouse a coordenadas lógicas
     */
    private int[] getMouseLogico(InputManager input) {
        int mouseX = input.raton.mouseX;
        int mouseY = input.raton.mouseY;

        int actualWidth = Main.mainInstance.getWidth();
        int actualHeight = Main.mainInstance.getHeight();

        double scaleX = (double) actualWidth / Main.BASE_WIDTH;
        double scaleY = (double) actualHeight / Main.BASE_HEIGHT;

        return new int[] {
            (int) (mouseX / scaleX),
            (int) (mouseY / scaleY)
        };
    }
    
    @Override
    public void update() {
        InputManager input = Main.input;
        long now = System.currentTimeMillis();

        // Movimiento de selección con teclado y debounce
        if (now - lastMoveTime >= MOVE_DELAY) {
            if (input.teclado.arriba) {
                seleccion = (seleccion - 1 + opciones.length) % opciones.length;
                input.teclado.arriba = false;
                lastMoveTime = now;
                sonidoMenu.play();
            } else if (input.teclado.abajo) {
                seleccion = (seleccion + 1) % opciones.length;
                input.teclado.abajo = false;
                lastMoveTime = now;
                sonidoMenu.play();
            }
        }

        // Obtener posición lógica del mouse
        int[] mouseLogico = getMouseLogico(input);
        int logicalX = mouseLogico[0];
        int logicalY = mouseLogico[1];

        // Hover del mouse (actualizar selección)
        int opcionHover = getOpcionBajoMouse(logicalX, logicalY);
        if (opcionHover >= 0) {
            seleccion = opcionHover;
        }

        // Detectar cambio de selección → disparar zoom
        if (seleccion != lastSeleccion) {
            zoomScale = ZOOM_MAX;  // Iniciar animación
            lastSeleccion = seleccion;
        }
        
        // Animación: volver gradualmente al tamaño normal
        if (zoomScale > 1.0f) {
            zoomScale -= ZOOM_SPEED;
            if (zoomScale < 1.0f) {
                zoomScale = 1.0f;
            }
        }

        boolean quiereConfirmar = false;
        
        if (input.teclado.enter) {
            quiereConfirmar = true;
        }
        else if (input.raton.clicIzquierdo && opcionHover >= 0) {
            quiereConfirmar = true;
        }
        
        if (quiereConfirmar) {
            if (enterReady) {
                confirm = true;
                enterReady = false;
                sonidoMenu.play();
            }
        } else {
            enterReady = true;
        }
    }

    @Override
    public void render(Graphics g) {
        if (fondo  != null) g.drawImage(fondo,  0, 0, 900, 600, null);
        if (titulo != null) g.drawImage(titulo, 0, 20, 390, 190, null);

        for (int i = 0; i < opciones.length; i++) {
            int x = MENU_X;
            int y = MENU_Y_START + i * MENU_SPACING;

            boolean isSelected = (i == seleccion);

            if (isSelected) {
                // Opción seleccionada: rojo + zoom animado
                int fontSize = (int) (FONT_BASE * zoomScale);
                g.setFont(new Font("Jersey 10", Font.PLAIN, fontSize));
                g.setColor(Color.RED);
                
                // Compensar posición por el zoom
                int offsetX = (int) ((fontSize - FONT_BASE) * 0.3f);
                int offsetY = (int) ((fontSize - FONT_BASE) * 0.3f);
                
                g.drawString(">" + opciones[i], x - offsetX, y + offsetY);
            } else {
                // Opción normal
                g.setFont(new Font("Jersey 10", Font.PLAIN, FONT_BASE));
                g.setColor(Color.WHITE);
                g.drawString(opciones[i], x + 20, y);
            }
        }
    }

    public boolean consumeConfirm() {
        if (confirm) {
            confirm = false;
            return true;
        }
        return false;
    }
    
    public void reset() {
        confirm = false;
        enterReady = true;
        zoomScale = 1.0f;
    }
   
    public int getSeleccion() {
        return seleccion;
    }
}