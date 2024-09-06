package main.java.zoory07.HotSpace.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import main.java.zoory07.HotSpace.imagen.animacion.entity_a.player.Player_animacion_corriendo;
import main.java.zoory07.HotSpace.imagen.hitbox;
import main.java.zoory07.HotSpace.imagen.sombra_entity.sombra_player;
import main.java.zoory07.HotSpace.game.teclado;





public class player {
    private int width, height;
    private int x, y;
    public teclado teclado;
    public int velocidad = 13;
    private hitbox hitbox;
    private boolean mostrarHitbox = false; // Algo temporal para el debug
    public sombra_player sombra;

    // Animaciones
    private Player_animacion_corriendo animacionCorriendo;

    
    public player(int x, int y, List<BufferedImage> correrFrames, teclado teclado, long frameDuracion) {
        this.x = x;
        this.y = y;
        this.teclado = teclado;
        this.animacionCorriendo = new Player_animacion_corriendo(correrFrames, frameDuracion);

        // Asumimos que todos los frames tienen el mismo tamaño
        BufferedImage primerFrame = correrFrames.get(0);
        this.width = primerFrame.getWidth();
        this.height = primerFrame.getHeight();

        double scaleFactor = 1.0; // tamaño actual del hitbox en el juego
        int scaledWidth = (int) (width * scaleFactor);
        int scaledHeight = (int) (height * scaleFactor);

        int hitboxWidth = (int) (scaledWidth * 0.99); // La hitbox es más pequeña que el sprite escalado
        int hitboxHeight = (int) (scaledHeight * 1.9); // La hitbox es más pequeña que el sprite escalado
        int hitboxOffsetX = (scaledWidth - hitboxWidth) / 2;
        int hitboxOffsetY = (scaledHeight - hitboxHeight)/ 2;

        this.hitbox = new hitbox(x + hitboxOffsetX, y + hitboxOffsetY, hitboxWidth, hitboxHeight);
        this.sombra = new sombra_player(x, y + hitboxHeight, hitboxWidth * 2, hitboxHeight);
    }

    public void toggleHitboxVisibility() {
        this.mostrarHitbox = !mostrarHitbox;
    }

    public void update() {
        if (teclado != null) {
            teclado.update();
        }
        teclado();
        int hitboxOffsetX = (int) (width * 0.85); // Ajusta estos valores según sea necesario
        int hitboxOffsetY = (int) (height * 0.8); // Ajusta estos valores según sea necesario
        hitbox.updatePosition(x + hitboxOffsetX, y + hitboxOffsetY);
        if (sombra != null) {
            sombra.x = this.x;
            sombra.y = this.y + height;
            sombra.update();
        }

        animacionCorriendo.update();
    }

    public void teclado() {
        int movimientoX = 0;

        if (teclado.derecha) {
            movimientoX += velocidad;
        }
        if (teclado.izquierda) {
            movimientoX -= velocidad;
        }

        x += movimientoX;
    }

    public void render(Graphics g) {
        
        double scaleFactor = 3.0; // Ajusta este valor para escalar el sprite
        int scaledWidth = (int) (width * scaleFactor);
        int scaledHeight = (int) (height * scaleFactor);
        int adjustedY = y + 39; // Ajusta este valor para mover el sprite hacia abajo
        animacionCorriendo.render(g, x, adjustedY - (scaledHeight - height), scaledWidth, scaledHeight);

        if (mostrarHitbox && hitbox != null) {
            hitbox.render(g);
        }
        if (sombra != null) {
            sombra.render(g);
        }
    }

    public void updatePosition(int deltaX, int deltaY) {
        x += deltaX;
        y += deltaY;
        int hitboxOffsetX = (int) (width * 0.25); // Ajusta estos valores según sea necesario
        int hitboxOffsetY = (int) (height * 0.8); // Ajusta estos valores según sea necesario
        hitbox.updatePosition(x + hitboxOffsetX, y + hitboxOffsetY);
    }

    // Centrar Player
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public hitbox getHitbox() {
        return hitbox;
    }





}
