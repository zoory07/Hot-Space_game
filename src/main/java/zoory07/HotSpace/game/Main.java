package main.java.zoory07.HotSpace.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import main.java.zoory07.HotSpace.entity.player;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00.GestionDePatronesDeEventos;
import main.java.zoory07.HotSpace.scenes.evento.EscenaLimite;
import main.java.zoory07.HotSpace.scenes.evento.tiempo;
import main.java.zoory07.HotSpace.scenes.level.level_00_desierto;
import main.java.zoory07.HotSpace.scenes.menus.Inicio_menu;




public class Main extends Canvas {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    public static final int SCALE = 3;
    public static final int BASE_WIDTH = WIDTH * SCALE;   
    public static final int BASE_HEIGHT = HEIGHT * SCALE; 
    public static String NAME = "HotSpace 1.0.0";

    private JFrame Ventana;
    public boolean running = false;
    public int Loop = 0;

    public SpriteSheet spriteSheet;
    public teclado teclado;
    private player player;
    private boolean mostrarSprite = false;

    // Niveles
    private level_00_desierto lvl_desierto;
    private tiempo tiempo;
    private Inicio_menu inicio_menu;

    private EstadoJuego estadoJuego;
    private int delayEnter = 0;
    private EscenaLimite EscenaLimite;
    private GestionDePatronesDeEventos GestionDePatronesDeEventos;

    private enum EstadoJuego {
        MENU,
        JUEGO,
        PAUSA,
    }

    public void Game() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        // Configurar el tamaño inicial de la ventana
        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));

        Ventana = new JFrame(NAME);
        Ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Ventana.setLayout(new BorderLayout());
        Ventana.add(this, BorderLayout.CENTER);
        Ventana.pack();
        Ventana.setVisible(true);
        Ventana.setLocationRelativeTo(null);
        Ventana.setResizable(true); // Permitir que la ventana cambie de tamaño
        Ventana.revalidate();

        // Inicializar teclado antes de añadirlo como KeyListener
        this.teclado = new teclado();
        Ventana.addKeyListener(teclado);
        Ventana.setFocusable(true);
        Ventana.requestFocusInWindow();

        tiempo = new tiempo();
        tiempo.iniciar();

        Sprite();
        Ecenas();
        CentrarPantallaPlayer();

        estadoJuego = EstadoJuego.MENU; // Iniciar en el menú principal
        System.out.println("Estado inicial: " + estadoJuego);
    }

    public void Sprite() throws IOException {
        try {
            // Cargar imagen desde el classpath
            BufferedImage hojaSprites = ImageIO.read(getClass().getResourceAsStream("/resources/SpriteSheet.png"));
            spriteSheet = new SpriteSheet(hojaSprites);

            // Animación del player correr
            int frameWidth = 30;
            int frameHeight = 30;
            int startX = 0;
            int startY = 0;
            int correr = 5;
            int gamerover = 6;

            List<BufferedImage> correrFrames = spriteSheet.getAnimationFrames(startX, startY, frameWidth, frameHeight, correr);
            List<BufferedImage> gameOverFrame = spriteSheet.getAnimationFrames(startX, startY, frameWidth, frameHeight, gamerover);

            long frameDuracion = 100;

            this.player = new player(0, 0, correrFrames, teclado, frameDuracion, gameOverFrame, EscenaLimite);
            addKeyListener(this.teclado);
            System.out.println("Sprite inicializado correctamente");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la imagen del sprite");
        }
    }

    public void setMostrarSprite(boolean mostrar) {
        this.mostrarSprite = mostrar;
    }

    public void CentrarPantallaPlayer() {
        if (player == null) {
            System.out.println("Error: Player no está inicializado");
            return;
        }
        int ventanaAncho = getWidth();
        int ventanaAlto = getHeight();
        int playerAncho = player.getWidth();
        int playerAlto = player.getHeight();

        int x = (ventanaAncho - playerAncho) / 2;
        int y = (ventanaAlto - playerAlto) / 2;

        player.setX(x);
        player.setY(y);
    }

    public void Run() {
        long ultimoTiempo = System.nanoTime();
        double cantidadDeTicks = 60.0;
        double ns = 1000000000 / cantidadDeTicks;
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
                try {
                    Ticks();
                } catch (IOException e) {
                    e.printStackTrace();
                    running = false; // Por ejemplo, detener el juego
                }
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

    public void Ticks() throws IOException {
        Update();
    }

    public void Start() {
        running = true;
        new Thread(this::Run).start();
    }

    public void Ecenas() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        lvl_desierto = new level_00_desierto(spriteSheet, teclado, tiempo);
        inicio_menu = new Inicio_menu(0, 0);
        System.out.println("Escenas inicializadas");
    }

    public void Update() throws IOException {
        teclado.update();
        GestionEscena();
    }

    public void GestionEscena() throws IOException {

        if (delayEnter > 0) {
            delayEnter--;
            return;
        }

        switch (estadoJuego) {

            case MENU:
                inicio_menu.update(teclado);
                if (teclado.enter) {
                    if (inicio_menu.getSeleccion() == 0) { // Jugar
                        // Reiniciar el nivel y los patrones antes de cambiar el estado del juego
                        lvl_desierto.reiniciarNivel();
                        estadoJuego = EstadoJuego.JUEGO;
                        tiempo.iniciar(); // Iniciar el tiempo cuando el juego comienza
                        System.out.println("Cambio de estado: " + estadoJuego);
                    } else if (inicio_menu.getSeleccion() == 1) { // Salir
                        System.exit(0);
                    }
                    teclado.enter = false;
                }
                break;

            case JUEGO:
                if (teclado.pausa) {
                    estadoJuego = EstadoJuego.PAUSA;
                    tiempo.pausar(); // Pausar el temporizador
                    lvl_desierto.setEnPausa(true); // Indicar al nivel que está en pausa
                    teclado.pausa = false;
                    System.out.println("Cambio de estado: " + estadoJuego);
                } else {
                    if (!lvl_desierto.isGameOver()) {
                        lvl_desierto.update();
                    } else {
                        lvl_desierto.update();
                    }
                }
                break;

            case PAUSA:
                lvl_desierto.getPausa().update(teclado);
                if (teclado.enter) {
                    if (lvl_desierto.getPausa().getSeleccion() == 0) { // Reanudar
                        estadoJuego = EstadoJuego.JUEGO;
                        tiempo.reanudar(); // Reanudar el temporizador
                        lvl_desierto.setEnPausa(false); // Indicar al nivel que se ha reanudado
                        System.out.println("Cambio de estado: " + estadoJuego);
                    } else if (lvl_desierto.getPausa().getSeleccion() == 1) { // Salir al Menú Principal
                        estadoJuego = EstadoJuego.MENU;
                        tiempo.reiniciar(); // Reiniciar el temporizador si es necesario
                        lvl_desierto.setEnPausa(false);
                        delayEnter = 60;
                        System.out.println("Cambio de estado: " + estadoJuego);
                    }
                    teclado.enter = false;
                }
                break;
        }
    }

    public void Stop() {
        running = false;
    }

    public void Render() {
        if (!this.isDisplayable()) {
            return;
        }

        BufferStrategy b = getBufferStrategy();
        if (b == null) {
            createBufferStrategy(3);
            return;
        }

        // Obtenemos el Graphics2D para poder aplicar transformaciones
        Graphics2D g2d = (Graphics2D) b.getDrawGraphics();

        // Calculo de los factores de escala
        double escalaX = (double) getWidth() / BASE_WIDTH;
        double escalaY = (double) getHeight() / BASE_HEIGHT;

        // Aplicamos la transformación de escala
        g2d.scale(escalaX, escalaY);

        // Limpia la pantalla (usando las dimensiones base)... :P
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);

        // Renderiza las escenas (usando coordenadas base)
        switch (estadoJuego) {
            case MENU:
                if (inicio_menu != null) {
                    inicio_menu.render(g2d);
                }
                break;
            case JUEGO:
                if (lvl_desierto != null) {
                    lvl_desierto.render(g2d);
                }
                break;
            case PAUSA:
                if (lvl_desierto != null) {
                    lvl_desierto.render(g2d);
                    lvl_desierto.getPausa().render(g2d);
                }
                break;
        }

        // Liberamos recursos
        g2d.dispose();
        b.show();
    }

    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        Main main = new Main();
        main.Game();
        main.Start();
    }





}
