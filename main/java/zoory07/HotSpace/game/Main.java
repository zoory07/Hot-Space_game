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
import main.java.zoory07.HotSpace.scenes.menus.menu_opciones;
import javax.sound.sampled.LineUnavailableException;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import main.java.zoory07.HotSpace.scenes.Escena;
import main.java.zoory07.HotSpace.scenes.MusicaManager;
import main.java.zoory07.HotSpace.scenes.menus.menu_SeleccionJuegos;



public class Main extends Canvas {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    public static final int SCALE = 3;
    public static final int BASE_WIDTH = WIDTH * SCALE;
    public static final int BASE_HEIGHT = HEIGHT * SCALE;
    public static Main mainInstance;
    public static String NAME = "HotSpace 1.1.1";

    private static final int SCENE_SWITCH_DELAY = 200;
    private long lastSceneChangeTime = 0;

    private JFrame Ventana;
    public boolean running = false;
    public static InputManager input;
    public static SpriteSheet spriteSheet;
    public static tiempo tiempo;

    public static Escena escenaActual;

    private Inicio_menu escenaMenu;
    private menu_SeleccionJuegos escenaSeleccion;
    private EscenaJuego escenaJuego;
    private menu_pausa escenaPausa;
    private menu_opciones escenaOpciones;
    

    public void Game() throws IOException, LineUnavailableException, UnsupportedAudioFileException, Exception {
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

        input = new InputManager();
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(input.teclado);
        this.addMouseListener(input.raton);
        this.addMouseMotionListener(input.raton);

        Image icon = ImageIO.read(getClass().getResourceAsStream("/resources/icono.png"));
        Ventana.setIconImage(icon);
        menu_opciones.cargarConfiguracion();
        MusicaManager.inicializar();
        MusicaManager.reproducir("menu");
        spriteSheet = new SpriteSheet(ImageIO.read(getClass().getResourceAsStream("/resources/SpriteSheet.png")));
        tiempo = new tiempo();
        tiempo.iniciar();
        escenaMenu = new Inicio_menu();
        escenaSeleccion = null;
        escenaJuego = null;
        escenaPausa = null;
        escenaOpciones = null;

        escenaActual = escenaMenu;
        System.out.println("Escena inicial: Menu");

        mainInstance = this;
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
                input.update();
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
        if (escenaActual != null) escenaActual.update();

        long now = System.currentTimeMillis();
        if (now - lastSceneChangeTime < SCENE_SWITCH_DELAY) {
            return;
        }

        // Manejo de cambios de escena
        if (escenaActual instanceof Inicio_menu) {
            Inicio_menu menu = (Inicio_menu) escenaActual;
            if (menu.consumeConfirm()) {
                switch (menu.getSeleccion()) {
                    case 0: // "Jugar" -> Va a seleccion de niveles
                        try {
                            escenaSeleccion = new menu_SeleccionJuegos();
                            switchScene(escenaSeleccion); 
                       } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1: // "Opciones"
                        try {
                            escenaOpciones = new menu_opciones(escenaMenu);
                            switchScene(escenaOpciones);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2: // "Salir"
                        System.exit(0);
                        break;
                }
            }
        } else if (escenaActual instanceof menu_SeleccionJuegos) {
            menu_SeleccionJuegos seleccion = (menu_SeleccionJuegos) escenaActual;
            if (seleccion.consumeConfirm()) {
                // Iniciar el nivel seleccionado
                int nivel = seleccion.getNivelActual();
                try {
                    escenaJuego = new EscenaJuego(spriteSheet, input.teclado, tiempo, nivel);
                    switchScene(escenaJuego);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (seleccion.consumeVolver()) {
                switchScene(escenaMenu);
            }
        } else if (escenaActual instanceof EscenaJuego) {
            if (input.teclado.pausa) {
                try {
                    tiempo.pausar();
                    escenaPausa = new menu_pausa(0, 0);
                    switchScene(escenaPausa);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (escenaActual instanceof menu_pausa) {
            menu_pausa pausa = (menu_pausa) escenaActual;
            if (pausa.consumeConfirm()) {
                switch (pausa.getSeleccion()) {
                    case 0: // "Reanudar"
                        tiempo.reanudar();
                        switchScene(escenaJuego);
                        break;
                    case 1: // "Opciones"
                        try {
                            escenaOpciones = new menu_opciones(escenaPausa);
                            switchScene(escenaOpciones);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2: // "Menu Principal"
                        tiempo.reiniciar();
                        switchScene(escenaMenu);
                        break;
                }
            }
        } else if (escenaActual instanceof menu_opciones) {
            menu_opciones opciones = (menu_opciones) escenaActual;
            if (opciones.consumeConfirm()) {
                Escena anterior = opciones.getEscenaAnterior();
                switchScene(anterior);
            }
        }
    }

    public void switchScene(Escena nuevaEscena) {
        escenaActual = nuevaEscena;
        input.clear();

        // Resetear la escena según su tipo
        if (nuevaEscena instanceof Inicio_menu) {
            ((Inicio_menu) nuevaEscena).reset();
        } else if (nuevaEscena instanceof menu_SeleccionJuegos) {
            ((menu_SeleccionJuegos) nuevaEscena).reset();
        } else if (nuevaEscena instanceof menu_pausa) {
            ((menu_pausa) nuevaEscena).reset();
        } else if (nuevaEscena instanceof menu_opciones) {
            ((menu_opciones) nuevaEscena).reset();
        }

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

    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException, Exception {
        Main main = new Main();
        main.Game();
        main.Start();
    }
}