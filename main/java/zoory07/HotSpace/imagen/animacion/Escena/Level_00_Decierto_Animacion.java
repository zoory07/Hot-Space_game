package main.java.zoory07.HotSpace.imagen.animacion.Escena;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;



public class Level_00_Decierto_Animacion {
    private BufferedImage[] imagenes;
    private int indiceActual;
    private long tiempoUltimaActualizacion;
    private long intervaloAnimacion; // Tiempo entre frames en milisegundos
    private String numeroImagen;
    private boolean animacionActiva;
    
    
    public Level_00_Decierto_Animacion(long intervaloAnimacion) {
        this.intervaloAnimacion = intervaloAnimacion;
        this.indiceActual = 0;
        this.tiempoUltimaActualizacion = System.currentTimeMillis();
        this.animacionActiva = true;
        cargarImagenes();
    }

   private void cargarImagenes() {
     imagenes = new BufferedImage[14]; // imagenes desde 00 hasta 13, necesitas un arreglo de tamaño 14
     for (int i = 0; i <= 13; i++) {
        try {
            if (i < 10) {
                numeroImagen = "0" + i; // Agregar un cero inicial para números menores a 10
            } else {
                numeroImagen = String.valueOf(i);
            }
            String ruta = "/resources/animaciones/decierto_" + numeroImagen + ".png";
            System.out.println("Intentando cargar la imagen: " + ruta);
            InputStream is = getClass().getResourceAsStream(ruta);
            if (is == null) {
                System.err.println("No se pudo encontrar el recurso en la ruta: " + ruta);
            } else {
                imagenes[i] = ImageIO.read(is);
                System.out.println("Imagen cargada exitosamente: " + ruta);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("No se pudo cargar la imagen: " + numeroImagen);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println("No se encontró la imagen: " + numeroImagen);
        }
    }
 }


    public void update() {
        if(animacionActiva){
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoUltimaActualizacion >= intervaloAnimacion) {
            indiceActual = (indiceActual + 1) % imagenes.length;
            tiempoUltimaActualizacion = tiempoActual;
        }
      }
    }

    public void render(Graphics g) {
        int ancho = 900;
        int alto = 600;
        if (imagenes[indiceActual] != null) {
            
            g.drawImage(imagenes[indiceActual], 0, 0, ancho, alto, null);
        }
    }

    public void detenerAnimacion() {
        animacionActiva = false;
    }

    public void iniciarAnimacion() {
        animacionActiva = true;
        tiempoUltimaActualizacion = System.currentTimeMillis(); 
    }
    public void reiniciarAnimacion() {
        indiceActual = 0;
        tiempoUltimaActualizacion = System.currentTimeMillis();
        animacionActiva = true;
    }
    

    

}
