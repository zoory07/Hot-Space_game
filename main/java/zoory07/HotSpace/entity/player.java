package main.java.zoory07.HotSpace.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import main.java.zoory07.HotSpace.imagen.animacion.entidades.player.Player_animacion_corriendo;
import main.java.zoory07.HotSpace.imagen.hitbox;
import main.java.zoory07.HotSpace.imagen.sombra_entity.sombra_player;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.animacion.entidades.player.Player_animacion_Atrapado_Parado;
import main.java.zoory07.HotSpace.imagen.animacion.entidades.player.Player_animacion_GameOver;
import main.java.zoory07.HotSpace.scenes.evento.EscenaLimite;




public class player extends Entity {
    
    public teclado teclado;
    public sombra_player sombra;
    private boolean isGameOver = false;
    private int posicionInicialX, posicionInicialY;
    
    // Animaciones
    private Player_animacion_corriendo animacionCorriendo;
    private Player_animacion_GameOver animacionGameOver;
    private Player_animacion_Atrapado_Parado animacionAtrapado;
    private boolean estaAtrapado = false;

    // Escena Limitada
    private EscenaLimite escenaLimite;
    
    public player(int x, int y, List<BufferedImage> correrFrames, teclado teclado, long frameDuracion, List<BufferedImage> gameOverFrame, EscenaLimite escenaLimite) {
        super(x, y, correrFrames.get(0).getWidth(), correrFrames.get(0).getHeight());
        
        this.posicionInicialX = x;
        this.posicionInicialY = y;
        this.teclado  = teclado;
        this.velocidad   = 15;
        this.escenaLimite   = escenaLimite;

        this.animacionCorriendo = new Player_animacion_corriendo(correrFrames, frameDuracion);
        this.animacionGameOver  = new Player_animacion_GameOver(gameOverFrame, 500, 2000);

        inicializarHitbox();
    }

    public void setAnimacionAtrapado(List<BufferedImage> frames, long duracion) {
        this.animacionAtrapado = new Player_animacion_Atrapado_Parado(frames, duracion);
    }

    public void setAtrapado(boolean b) {
        if (b && !estaAtrapado && animacionAtrapado != null) {
            animacionAtrapado.reset(); // solo resetea al activarse
        }
        this.estaAtrapado = b;
    }
    
    private void inicializarHitbox() {
        int hitboxWidth = (int)(width  * 0.99);
        int hitboxHeight = (int)(height * 1.9);
        int hitboxOffsetX = (width  - hitboxWidth)  / 2;
        int hitboxOffsetY = (height - hitboxHeight) / 2;
        
        this.hitbox = new hitbox(x + hitboxOffsetX, y + hitboxOffsetY, hitboxWidth, hitboxHeight);
        this.sombra = new sombra_player(x, y + hitboxHeight, hitboxWidth * 2, hitboxHeight);
    }

    @Override
    public void update(int velocidad1) {
        if (isGameOver) {
            animacionGameOver.update();
        } else {
            teclado.update();
            procesarTeclado();

            if (estaAtrapado && animacionAtrapado != null) {
                animacionAtrapado.update();
            } else {
                animacionCorriendo.update();
            }

            actualizarHitbox();
            actualizarSombra();
        }
    }
    
    private void procesarTeclado() {
        if (isGameOver) return;
        int movimientoX = 0;
        if (teclado.derecha)   movimientoX += velocidad;
        if (teclado.izquierda) movimientoX -= velocidad;
        x += movimientoX;
    }
    
    private void actualizarHitbox() {
        int hitboxOffsetX = (int)(width  * 0.85);
        int hitboxOffsetY = (int)(height * 0.8);
        hitbox.updatePosition(x + hitboxOffsetX, y + hitboxOffsetY);
    }
    
    private void actualizarSombra() {
        sombra.x = this.x;
        sombra.y = this.y + height;
        sombra.update();
    }

    @Override
    public void render(Graphics g) {
        double scaleFactor = 3.0;
        int scaledWidth    = (int)(width  * scaleFactor);
        int scaledHeight   = (int)(height * scaleFactor);
        int adjustedY      = y + 39;

        if (isGameOver) {
            animacionGameOver.render(g, x, y, scaledWidth, scaledHeight);
        } else if (estaAtrapado && animacionAtrapado != null) {
            animacionAtrapado.render(g, x, adjustedY - (scaledHeight - height), scaledWidth, scaledHeight);
        } else {
            animacionCorriendo.render(g, x, adjustedY - (scaledHeight - height), scaledWidth, scaledHeight);
        }

        if (mostrarHitbox && hitbox != null) hitbox.render(g);
        if (sombra != null) sombra.render(g);
    }
    
    public void updatePosition(int deltaX, int deltaY) {
        if (isGameOver) return;
        x += deltaX;
        y += deltaY;
        actualizarHitbox();
    }

    public void setGameOver() {
        isGameOver = true;
        activa     = false;
        animacionGameOver.reset();
    }
    
    public void reiniciar() {
        isGameOver   = false;
        activa       = true;
        estaAtrapado = false;
        velocidad    = 15;
        x = posicionInicialX;
        y = posicionInicialY;
    
        if (animacionGameOver  != null) animacionGameOver.reset();
        if (animacionCorriendo != null) animacionCorriendo.reset();
        if (animacionAtrapado  != null) animacionAtrapado.reset();
    
        actualizarHitbox();
    }
    
    public void setVelocidad(int v) { 
        this.velocidad = v; 
    }
    public boolean isGameOver() { 
        return isGameOver; 
    }
    public boolean isAtrapado() { 
        return estaAtrapado; 
    }
}