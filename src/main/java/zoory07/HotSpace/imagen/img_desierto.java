package main.java.zoory07.HotSpace.imagen;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.InputStream;


public class img_desierto {

    private int x, y;   
    private BufferedImage fondo;  // Imagen original
    private BufferedImage imagenEscalada; // Imagen escalada para el renderizado con transparencia

    // Constructor
    public img_desierto(int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        cargarImagen("/resources/fondo_decierto.png");
    }

    // Cargar imagen desde el classpath
    public final void cargarImagen(String path) throws IOException {
        InputStream is = getClass().getResourceAsStream(path); // Cargar desde el classpath
        if (is == null) {
            throw new IOException("El archivo de imagen no se pudo encontrar en el classpath: " + path);
        }
        fondo = ImageIO.read(is);

        // Ajustar la imagen al tamaño deseado solo una vez
        Ajusteimagen();
    }

    // Ajustar la imagen (escalado)
    public void Ajusteimagen() {
        int Ancho = 900;
        int Alto = 600;

        // Escalar la imagen solo una vez
        Image imagenEscaladaTemp = fondo.getScaledInstance(Ancho, Alto, Image.SCALE_SMOOTH);

        // Crear un BufferedImage con el tamaño ajustado
        imagenEscalada = new BufferedImage(Ancho, Alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagenEscalada.createGraphics();
        g2d.drawImage(imagenEscaladaTemp, 0, 0, null);
        g2d.dispose();
    }

    // Renderizar la imagen en pantalla con transparencia
    public void render(Graphics g) {
        if (imagenEscalada != null) {
            // Aplicar transparencia antes de dibujar
            Graphics2D g2d = (Graphics2D) g;
            float transparencia = 0.80f;
            AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparencia);
            g2d.setComposite(alpha);

            // Dibujar la imagen escalada con la transparencia aplicada
            g2d.drawImage(imagenEscalada, x, y, null);

            // Restablecer el AlphaComposite por defecto (opcional, pero recomendado)
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
}
