package main.java.zoory07.HotSpace.scenes.menus;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import main.java.zoory07.HotSpace.game.InputManager;
import main.java.zoory07.HotSpace.game.Main;
import main.java.zoory07.HotSpace.scenes.Escena;
import main.java.zoory07.HotSpace.scenes.MusicaManager;
import main.java.zoory07.HotSpace.scenes.Sound;




public class menu_opciones implements Escena {
    private int seleccion = 0;
    private int lastSeleccion = -1;
    private final String[] opciones = {"Sonido", "Musica", "Pantalla Completa", "Volver"};
    private final BufferedImage fondo;
    private static final Preferences prefs = Preferences.userNodeForPackage(menu_opciones.class);
    
    private long lastMoveTime = System.currentTimeMillis();
    private static final int MOVE_DELAY = 100;

    private static final int MENU_X = 100;
    private static final int MENU_Y_START = 200;
    private static final int MENU_SPACING = 70;
    private static final int MENU_WIDTH = 400;

    private static final int BARRA_X = MENU_X + 350;
    private static final int BARRA_ANCHO = 180;
    private static final int BARRA_ALTO = 25;

    private static final int FONT_BASE = 45;

    private float zoomScale = 1.0f;
    private static final float ZOOM_MAX = 1.2f;
    private static final float ZOOM_SPEED = 0.05f;

    private static int volumenSonido = 80;
    private static int volumenMusica = 80;
    private static boolean pantallaCompleta = false;

    private Escena escenaAnterior;

    private boolean confirm = false;
    private boolean enterReady = true;
    private Sound sonidoMenu;

    public menu_opciones(Escena escenaAnterior) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        this.escenaAnterior = escenaAnterior;
        fondo = ImageIO.read(getClass().getResourceAsStream("/resources/fondo_opciones.png"));
        sonidoMenu = new Sound("menu.wav", false);
    }

    private int getOpcionBajoMouse(int logicalX, int logicalY) {
        for (int i = 0; i < opciones.length; i++) {
            int y = MENU_Y_START + i * MENU_SPACING;
            if (logicalX >= MENU_X && logicalX <= MENU_X + MENU_WIDTH
                    && logicalY >= y - 35 && logicalY <= y + 10) {
                return i;
            }
        }
        return -1;
    }

    private int getVolumenBajoMouse(int logicalX, int logicalY, int opcion) {
        int y = MENU_Y_START + opcion * MENU_SPACING - 20;
        if (logicalX >= BARRA_X && logicalX <= BARRA_X + BARRA_ANCHO
                && logicalY >= y && logicalY <= y + BARRA_ALTO) {
            int valor = (int)(((float)(logicalX - BARRA_X) / BARRA_ANCHO) * 100);
            return Math.max(0, Math.min(100, valor));
        }
        return -1;
    }

    private int[] getMouseLogico(InputManager input) {
        int mouseX = input.raton.mouseX;
        int mouseY = input.raton.mouseY;
        double scaleX = (double) Main.mainInstance.getWidth()  / Main.BASE_WIDTH;
        double scaleY = (double) Main.mainInstance.getHeight() / Main.BASE_HEIGHT;
        return new int[]{ (int)(mouseX / scaleX), (int)(mouseY / scaleY) };
    }

    @Override
    public void update() {
        InputManager input = Main.input;
        long now = System.currentTimeMillis();

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

            if (input.teclado.izquierda) {
                ajustarValor(-5);
                input.teclado.izquierda = false;
                lastMoveTime = now;
            } else if (input.teclado.derecha) {
                ajustarValor(5);
                input.teclado.derecha = false;
                lastMoveTime = now;
            }
        }

        int[] mouseLogico = getMouseLogico(input);
        int logicalX = mouseLogico[0];
        int logicalY = mouseLogico[1];

        int opcionHover = getOpcionBajoMouse(logicalX, logicalY);
        if (opcionHover >= 0) seleccion = opcionHover;

        if (seleccion != lastSeleccion) {
            zoomScale = ZOOM_MAX;
            lastSeleccion = seleccion;
        }
        if (zoomScale > 1.0f) {
            zoomScale -= ZOOM_SPEED;
            if (zoomScale < 1.0f) zoomScale = 1.0f;
        }

        if (input.raton.clicIzquierdo) {
            if (enterReady) {
                boolean clicEnBarra = false;

                // Barra Sonido
                int valorSonido = getVolumenBajoMouse(logicalX, logicalY, 0);
                if (valorSonido >= 0) {
                    volumenSonido = valorSonido;
                    Sound.setVolumenSonido(volumenSonido / 100.0f);
                    sonidoMenu.play();
                    clicEnBarra = true;
                }

                // Barra Música — AGREGADO MusicaManager.actualizarVolumen()
                int valorMusica = getVolumenBajoMouse(logicalX, logicalY, 1);
                if (valorMusica >= 0) {
                    volumenMusica = valorMusica;
                    Sound.setVolumenMusica(volumenMusica / 100.0f);
                    MusicaManager.actualizarVolumen();  
                    clicEnBarra = true;
                }

                if (!clicEnBarra && opcionHover >= 0) procesarSeleccion();
                enterReady = false;
            }
        } else {
            enterReady = true;
        }

        if (input.teclado.enter) {
            if (enterReady) {
                procesarSeleccion();
                enterReady = false;
            }
        }

        if (input.teclado.pausa) {
            confirm = true;
            seleccion = 3;
            input.teclado.pausa = false;
        }
    }

    private void ajustarValor(int delta) {
        switch (seleccion) {
            case 0:
                volumenSonido = Math.max(0, Math.min(100, volumenSonido + delta));
                Sound.setVolumenSonido(volumenSonido / 100.0f);
                sonidoMenu.play();
                guardarConfiguracion();
                break;
            case 1:
                volumenMusica = Math.max(0, Math.min(100, volumenMusica + delta));
                Sound.setVolumenMusica(volumenMusica / 100.0f);
                MusicaManager.actualizarVolumen();  
                guardarConfiguracion();
                break;
            case 2:
                pantallaCompleta = !pantallaCompleta;
                aplicarPantallaCompleta();
                sonidoMenu.play();
                guardarConfiguracion();
                break;
        }
    }

    private void procesarSeleccion() {
        switch (seleccion) {
            case 0:
                volumenSonido = (volumenSonido > 0) ? 0 : 80;
                Sound.setVolumenSonido(volumenSonido / 100.0f);
                sonidoMenu.play();
                guardarConfiguracion();
                break;
            case 1:
                volumenMusica = (volumenMusica > 0) ? 0 : 80;
                Sound.setVolumenMusica(volumenMusica / 100.0f);
                MusicaManager.actualizarVolumen();  
                guardarConfiguracion();
                break;
            case 2:
                pantallaCompleta = !pantallaCompleta;
                aplicarPantallaCompleta();
                sonidoMenu.play();
                guardarConfiguracion();
                break;
            case 3:
                confirm = true;
                sonidoMenu.play();
                guardarConfiguracion();
                break;
        }
    }

    private void aplicarPantallaCompleta() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Container container = Main.mainInstance.getParent();
        while (container != null && !(container instanceof javax.swing.JFrame)) {
            container = container.getParent();
        }
        if (container == null) return;
        javax.swing.JFrame ventana = (javax.swing.JFrame) container;
        if (pantallaCompleta) {
            ventana.dispose();
            ventana.setUndecorated(true);
            gd.setFullScreenWindow(ventana);
            ventana.setVisible(true);
        } else {
            gd.setFullScreenWindow(null);
            ventana.dispose();
            ventana.setUndecorated(false);
            ventana.setSize(Main.BASE_WIDTH, Main.BASE_HEIGHT);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        }
        Main.mainInstance.requestFocus();
    }

    @Override
    public void render(Graphics g) {
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, 900, 600, null);
        } else {
            g.setColor(new Color(20, 20, 40));
            g.fillRect(0, 0, 900, 600);
        }

        g.setFont(new Font("Jersey 10", Font.PLAIN, 60));
        g.setColor(Color.WHITE);
        g.drawString("OPCIONES", 320, 100);

        for (int i = 0; i < opciones.length; i++) {
            int x = MENU_X;
            int y = MENU_Y_START + i * MENU_SPACING;
            boolean isSelected = (i == seleccion);
            int fontSize = isSelected ? (int)(FONT_BASE * zoomScale) : FONT_BASE;
            g.setFont(new Font("Jersey 10", Font.PLAIN, fontSize));

            if (isSelected) {
                g.setColor(Color.RED);
                int offsetX = (int)((fontSize - FONT_BASE) * 0.3f);
                g.drawString(">" + opciones[i], x - offsetX, y);
            } else {
                g.setColor(Color.WHITE);
                g.drawString(opciones[i], x + 20, y);
            }

            if (i == 0) dibujarBarraVolumen(g, BARRA_X, y - 20, volumenSonido, isSelected);
            else if (i == 1) dibujarBarraVolumen(g, BARRA_X, y - 20, volumenMusica, isSelected);
            else if (i == 2) dibujarCheckbox(g, BARRA_X, y - 25, pantallaCompleta, isSelected);
        }

        g.setFont(new Font("Jersey 10", Font.PLAIN, 18));
        g.setColor(Color.BLACK);
        g.drawString("Clic en barra para ajustar  |  <- -> teclado  |  ESC volver", 220, 520);
    }

    private void dibujarBarraVolumen(Graphics g, int x, int y, int valor, boolean selected) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, BARRA_ANCHO, BARRA_ALTO);
        g.setColor(selected ? Color.RED : Color.GREEN);
        g.fillRect(x, y, (int)(BARRA_ANCHO * valor / 100.0), BARRA_ALTO);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, BARRA_ANCHO, BARRA_ALTO);
        g.setFont(new Font("Jersey 10", Font.BOLD, 18));
        g.setColor(Color.WHITE);
        g.drawString(valor + "%", x + BARRA_ANCHO + 15, y + 19);
    }

    private void dibujarCheckbox(Graphics g, int x, int y, boolean checked, boolean selected) {
        int size = 30;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, size, size);
        if (checked) {
            g.setColor(selected ? Color.RED : Color.GREEN);
            g.fillRect(x + 5, y + 5, size - 10, size - 10);
        }
        g.setColor(selected ? Color.RED : Color.WHITE);
        g.drawRect(x, y, size, size);
        g.setFont(new Font("Jersey 10", Font.BOLD, 18));
        g.setColor(Color.WHITE);
        g.drawString(checked ? "ON" : "OFF", x + size + 15, y + 22);
    }
    
    public static void guardarConfiguracion() {
      prefs.putInt("volumenSonido", volumenSonido);
      prefs.putInt("volumenMusica", volumenMusica);
      prefs.putBoolean("pantallaCompleta", pantallaCompleta);
   }

    public static void cargarConfiguracion() {
      volumenSonido    = prefs.getInt("volumenSonido", 80);
      volumenMusica    = prefs.getInt("volumenMusica", 80);
      pantallaCompleta = prefs.getBoolean("pantallaCompleta", false);
      Sound.setVolumenSonido(volumenSonido / 100.0f);
      Sound.setVolumenMusica(volumenMusica / 100.0f);
      MusicaManager.actualizarVolumen();
    }
    
    
    public boolean consumeConfirm()  { if (confirm) { confirm = false; return true; } return false; }
    public void reset()              { confirm = false; enterReady = true; zoomScale = 1.0f; }
    public Escena getEscenaAnterior(){ return escenaAnterior; }
    public int getSeleccion()        { return seleccion; }
    public static int getVolumenSonido()    { return volumenSonido; }
    public static int getVolumenMusica()    { return volumenMusica; }
    public static boolean isPantallaCompleta() { return pantallaCompleta; }
}