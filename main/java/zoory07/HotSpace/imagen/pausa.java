package main.java.zoory07.HotSpace.imagen;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;



public class pausa {
    private int x, y;
    private BufferedImage pausa;
    private boolean EnPausa = false;
    
    public pausa(String path, int x, int y) {
        this.x = x;
        this.y = y;
        cargarImagen(path);
        
    }
    
    // Método para cargar la imagen desde el classpath
    private void cargarImagen(String path) {
        try {
            pausa = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Error: Imagen no encontrada en el classpath: " + path);
        }
    }

    
    public void render(Graphics g) {
        if (pausa != null) {
            g.drawImage(pausa, x, y, null);
        } else {
            System.err.println("Error: Imagen de pausa no está cargada.");
        }
    }
}
