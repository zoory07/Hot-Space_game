package main.java.zoory07.HotSpace.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import main.java.zoory07.HotSpace.imagen.animacion.entity_a.player.Player_animacion_corriendo;
import main.java.zoory07.HotSpace.imagen.hitbox;
import main.java.zoory07.HotSpace.imagen.sombra_entity.sombra_player;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.animacion.entity_a.player.Player_animacion_GameOver;
import main.java.zoory07.HotSpace.scenes.evento.EscenaLimite;





public class player {
    private int width, height;
    private int x, y;
    public teclado teclado;
    public int velocidad = 15;
    private hitbox hitbox;
    private boolean mostrarHitbox = false;
    public sombra_player sombra;
    private boolean isGameOver = false;
    private int posicionInicialX;
    private int posicionInicialY;
    
    // Animaciones
    private Player_animacion_corriendo animacionCorriendo;
    private Player_animacion_GameOver animacionGameOver;
    
    // Escena Limitada
    private EscenaLimite EscenaLimite;
    
     
    public player(int x, int y, List<BufferedImage> correrFrames, teclado teclado, long frameDuracion, List<BufferedImage> gameOverFrame, EscenaLimite EscenaLimite) {
        this.x = x;
        this.y = y;
        this.posicionInicialX = x;
        this.posicionInicialY = y;
        
        this.teclado = teclado;

        // Inicializar las animaciones
        this.animacionCorriendo = new Player_animacion_corriendo(correrFrames, frameDuracion);
        this.animacionGameOver = new Player_animacion_GameOver(gameOverFrame, 500, 2000);  // Game Over

        // Asumimos que todos los frames tienen el mismo tamaño
        BufferedImage primerFrame = correrFrames.get(0);
        this.width = primerFrame.getWidth();
        this.height = primerFrame.getHeight();
        
        // Configurar el hitbox
        double scaleFactor = 1.0;
        int scaledWidth = (int) (width * scaleFactor);
        int scaledHeight = (int) (height * scaleFactor);
        int hitboxWidth = (int) (scaledWidth * 0.99);
        int hitboxHeight = (int) (scaledHeight * 1.9);
        int hitboxOffsetX = (scaledWidth - hitboxWidth) / 2;
        int hitboxOffsetY = (scaledHeight - hitboxHeight) / 2;
        
        this.hitbox = new hitbox(x + hitboxOffsetX, y + hitboxOffsetY, hitboxWidth, hitboxHeight);
        this.sombra = new sombra_player(x, y + hitboxHeight, hitboxWidth * 2, hitboxHeight);
        this.EscenaLimite = EscenaLimite;
        
        
    }

    // Alternar la visibilidad del hitbox
    public void toggleHitboxVisibility() {
        this.mostrarHitbox = !mostrarHitbox;
    }

   
    public void update() {
        if (isGameOver) {
            animacionGameOver.update();  
        } else {
            teclado.update();  // Actualizar el estado del teclado
            teclado();
            animacionCorriendo.update();  // Actualizar la animación de correr
          
            int hitboxOffsetX = (int) (width * 0.85);
            int hitboxOffsetY = (int) (height * 0.8);
            hitbox.updatePosition(x + hitboxOffsetX, y + hitboxOffsetY);
            sombra.x = this.x;
            sombra.y = this.y + height;
            sombra.update();
        }
        
    }

    
    public void teclado() {
        if (isGameOver) return;  // No permitir movimiento en Game Over

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
        double scaleFactor = 3.0;
        int scaledWidth = (int) (width * scaleFactor);
        int scaledHeight = (int) (height * scaleFactor);
        int adjustedY = y + 39; // Ajuste de la posición vertical

        if (isGameOver) {
            animacionGameOver.render(g, x, y, scaledWidth, scaledHeight);  // Renderizar Game Over
        } else {
            animacionCorriendo.render(g, x, adjustedY - (scaledHeight - height), scaledWidth, scaledHeight);  // Renderizar la animación de correr
        }

        // Renderizar hitbox y sombra para depuración
        if (mostrarHitbox && hitbox != null) {
            hitbox.render(g);
        }
        if (sombra != null) {
            sombra.render(g);
        }
    }

    
    public void updatePosition(int deltaX, int deltaY) {
        if (isGameOver) return;  // No permitir movimiento en "Game Over"
        
        x += deltaX;
        y += deltaY;

        int hitboxOffsetX = (int) (width * 0.25);
        int hitboxOffsetY = (int) (height * 0.8);
        hitbox.updatePosition(x + hitboxOffsetX, y + hitboxOffsetY);
    }

    // Getter y Setter del hitbox
    public hitbox getHitbox() {
        return hitbox;
    }

    // Método para activar el estado "Game Over"
    public void setGameOver() {
        isGameOver = true;
        animacionGameOver.reset();  // Reiniciar la animación de Game Over
        System.out.println("Estado de Game Over activado.");
    }
    
    public void reiniciar(){
       this.posicionInicialX = x;
       this.posicionInicialY = y;
    }
    
    // Métodos para obtener el ancho y alto del jugador
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

    public int getX() {
       return x;
    }

    public int getY() {
       return y; 
    }
    
    

}