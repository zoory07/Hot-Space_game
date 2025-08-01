package main.java.zoory07.HotSpace.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.scenes.evento.tiempo;
import main.java.zoory07.HotSpace.scenes.menus.Inicio_menu;
import main.java.zoory07.HotSpace.scenes.menus.EscenaJuego;
import main.java.zoory07.HotSpace.scenes.menus.menu_pausa;
import javax.sound.sampled.LineUnavailableException;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import main.java.zoory07.HotSpace.scenes.Escena;




public class Main extends Canvas {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    public static final int SCALE = 3;
    public static final int BASE_WIDTH = WIDTH * SCALE;
    public static final int BASE_HEIGHT = HEIGHT * SCALE;
    public static String NAME = "HotSpace 1.0.1";

    private static final int SCENE_SWITCH_DELAY = 200; 
    private long lastSceneChangeTime = 0;

    private JFrame Ventana;
    public boolean running = false;
    public static teclado teclado;
    public static SpriteSheet spriteSheet;
    public static tiempo tiempo;

    public static Escena escenaActual;

    private Inicio_menu escenaMenu;
    private EscenaJuego escenaJuego;
    private menu_pausa escenaPausa;

    public void Game() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));
        Ventana = new JFrame(NAME);
        Ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Ventana.setLayout(new BorderLayout());
        Ventana.add(this, BorderLayout.CENTER);
        Ventana.pack();
        Ventana.setVisible(true);
        Ventana.setLocationRelativeTo(null);
        Ventana.setResizable(true);
        Ventana.revalidate();

        teclado = new teclado();
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(teclado);

        Image icon = ImageIO.read(getClass().getResourceAsStream("/resources/icono.png"));
        Ventana.setIconImage(icon);

        spriteSheet = new SpriteSheet(ImageIO.read(getClass().getResourceAsStream("/resources/SpriteSheet.png")));
        tiempo = new tiempo();
        tiempo.iniciar();
        escenaMenu = new Inicio_menu();
        escenaJuego = null; // se creará al entrar a jugar
        escenaPausa = null; // se creará al pausar

        escenaActual = escenaMenu;
        System.out.println("Escena inicial: Menu");
    }

    public void Run() {
        long ultimoTiempo = System.nanoTime();
        double cantidadDeTicks = 60.0;
        double ns = 1_000_000_000 / cantidadDeTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frame = 0;

        running = true;
        while (running) {
            long ahora = System.nanoTime();
            delta += (ahora - ultimoTiempo) / ns;
            ultimoTiempo = ahora;

            while (delta >= 1) {
                Ticks();
                updates++;
                delta--;
            }
            Render();
            frame++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frame + ", Ticks: " + updates);
                frame = 0;
                updates = 0;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void Ticks() {
        teclado.update();
        if (escenaActual != null) escenaActual.update();

        long now = System.currentTimeMillis();
        // Si no paso el tiempo mínimo desde el ultimo cambio, no procesamos inputs
        if (now - lastSceneChangeTime < SCENE_SWITCH_DELAY) {
            return;
        }

        // Manejo de cambios de escena
        if (escenaActual instanceof Inicio_menu) {
            Inicio_menu menu = (Inicio_menu) escenaActual;
            if (menu.consumeConfirm()) {
                if (menu.getSeleccion() == 0) { // "Jugar"
                    try {
                        escenaJuego = new EscenaJuego(spriteSheet, teclado, tiempo);
                        switchScene(escenaJuego);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (menu.getSeleccion() == 1) { // "Salir"
                    System.exit(0);
                }
            }
        } else if (escenaActual instanceof EscenaJuego) {
            if (teclado.pausa) {
                try {
                    escenaPausa = new menu_pausa(0, 0);
                    switchScene(escenaPausa);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (escenaActual instanceof menu_pausa) {
            menu_pausa pausa = (menu_pausa) escenaActual;
            if (pausa.consumeConfirm()) {
                if (pausa.getSeleccion() == 0) { // "Reanudar"
                    switchScene(escenaJuego);
                } else if (pausa.getSeleccion() == 1) { // "Menu Principal"
                    switchScene(escenaMenu);
                }
            }
        }
    }

    public void switchScene(Escena nuevaEscena) {
        escenaActual = nuevaEscena;
        // Limpiar TODOS los flags del teclado
        teclado.arriba = false;
        teclado.abajo = false;
        teclado.enter = false;
        teclado.pausa = false;

        // Nose esta cosa funcion pero me chupa un huevo :p
        lastSceneChangeTime = System.currentTimeMillis();
    }

    public void Render() {
        if (!this.isDisplayable()) return;

        BufferStrategy b = getBufferStrategy();
        if (b == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics2D g2d = (Graphics2D) b.getDrawGraphics();
        double escalaX = (double) getWidth() / BASE_WIDTH;
        double escalaY = (double) getHeight() / BASE_HEIGHT;
        g2d.scale(escalaX, escalaY);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);

        if (escenaActual != null) escenaActual.render(g2d);

        g2d.dispose();
        b.show();
    }

    public void Start() {
        running = true;
        new Thread(this::Run).start();
    }

    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        Main main = new Main();
        main.Game();
        main.Start();
    }
}
