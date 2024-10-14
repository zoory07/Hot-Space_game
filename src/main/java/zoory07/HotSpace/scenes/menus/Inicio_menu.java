package main.java.zoory07.HotSpace.scenes.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.titulo;
import main.java.zoory07.HotSpace.scenes.Sound;


/**
 * Clase que representa el menú de inicio del juego.
 */
public class Inicio_menu {
    // Variables de posición y estado
    private int x, y;
    private BufferedImage fondo;
    private String[] opciones = {"Jugar", "Salir"};
    private int seleccion = 0;
    private titulo titulo;
    private Sound sonidoMenu;
    
    
    // Constantes y configuración de input
    private static final int INPUT_DELAY = 120; 
    private long lastInputTime; 

    /**
     * Constructor del menú de inicio.
     * @param x Posición X del menú.
     * @param y Posición Y del menú.
     * @throws IOException Si ocurre un error al cargar las imágenes.
     */
    public Inicio_menu(int x, int y) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        this.x = x;
        this.y = y;
        cargarImagen("/resources/menu_inicio.png");
        titulo = new titulo("/resources/titulo.png", x + 10, y + 20);
        lastInputTime = System.currentTimeMillis();
        sonidoMenu = new Sound("menu.wav");
    }

    /**
     * Cargar la imagen de fondo del menú desde el classpath.
     * @param path Ruta de la imagen.
     * @throws IOException Si no se puede cargar la imagen.
     */
    private void cargarImagen(String path) throws IOException {
        fondo = ImageIO.read(getClass().getResourceAsStream(path));
    }

    /**
     * Renderizar el menú de inicio.
     * @param g Contexto gráfico para dibujar el menú.
     */
    public void render(Graphics g) {
        // Dibujar fondo
        if (fondo != null) {
            int newWidth = 900;  // Ajusta el ancho deseado
            int newHeight = 600; // Ajusta el alto deseado
            g.drawImage(fondo, x, y, newWidth, newHeight, null);
        }

        // Renderizar el título
        titulo.render(g);

        // Dibujar opciones del menú
        dibujarOpcionesMenu(g);
    }

    /**
     * Dibujar las opciones del menú de inicio.
     * @param g Contexto gráfico para dibujar las opciones.
     */
    private void dibujarOpcionesMenu(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        int opcionesX = x + 100; // Posición X de las opciones
        int opcionesY = y + 300; // Posición Y de las opciones
        int espacioEntreOpciones = 40; // Espacio entre opciones

        for (int i = 0; i < opciones.length; i++) {
            if (i == seleccion) {
                g.setColor(Color.RED); // Opción seleccionada en rojo
                g.drawString("> " + opciones[i], opcionesX, opcionesY + i * espacioEntreOpciones);
            } else {
                g.setColor(Color.WHITE); // Opciones no seleccionadas en blanco
                g.drawString(opciones[i], opcionesX, opcionesY + i * espacioEntreOpciones);
            }
        }
    }

    /**
     * Actualizar el estado del menú de inicio basado en la entrada del teclado.
     * @param input Objeto de entrada del teclado.
     */
    public void update(teclado input) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastInputTime >= INPUT_DELAY) {
            // Navegar hacia arriba
            if (input.arriba) {
                seleccionarOpcionAnterior();
                input.arriba = false;
                sonidoMenu.play();
            }
            // Navegar hacia abajo
            if (input.abajo) {
                seleccionarOpcionSiguiente();
                input.abajo = false;
                sonidoMenu.play();
            }
            lastInputTime = currentTime; 
        }
    }

    /**
     * Selecciona la opción anterior en el menú.
     */
    private void seleccionarOpcionAnterior() {
        seleccion--;
        if (seleccion < 0) {
            seleccion = opciones.length - 1;
        }
    }

    /**
     * Selecciona la siguiente opción en el menú.
     */
    private void seleccionarOpcionSiguiente() {
        seleccion++;
        if (seleccion >= opciones.length) {
            seleccion = 0;
        }
    }

    /**
     * Obtener la opción seleccionada actualmente.
     * @return El índice de la opción seleccionada.
     */
    public int getSeleccion() {
        return seleccion;
    }

    /**
     * Restablecer la opción seleccionada a la primera opción.
     */
    public void resetSeleccion() {
        seleccion = 0;
    }
}
