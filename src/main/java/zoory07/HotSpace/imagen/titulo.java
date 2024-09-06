package main.java.zoory07.HotSpace.imagen;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class titulo {
    private int x, y;
    private BufferedImage titulo;

    /**
     * Constructor de la clase título.
     * @param path Ruta del recurso de la imagen del título en el classpath.
     * @param x Posición X donde se dibujará el título.
     * @param y Posición Y donde se dibujará el título.
     */
    public titulo(String path, int x, int y) {
        this.x = x;
        this.y = y;
        cargarImagen(path);
    }

    /**
     * Cargar la imagen desde el classpath.
     * @param path Ruta del recurso de la imagen en el classpath.
     */
    private void cargarImagen(String path) {
        try {
            // Cargar imagen desde el classpath
            titulo = ImageIO.read(getClass().getResourceAsStream(path));
            if (titulo == null) {
                throw new IOException("La imagen no se pudo encontrar: " + path);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen del título desde: " + path);
            e.printStackTrace();
        }
    }

    /**
     * Renderizar el título en la pantalla.
     * @param g Contexto gráfico donde se dibujará el título.
     */
    public void render(Graphics g) {
        if (titulo != null) {
            g.drawImage(titulo, x, y, null);
        } else {
            System.err.println("No se pudo renderizar el título porque la imagen no está cargada.");
        }
    }
}
