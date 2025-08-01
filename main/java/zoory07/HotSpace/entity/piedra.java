package main.java.zoory07.HotSpace.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.java.zoory07.HotSpace.game.Main;
import main.java.zoory07.HotSpace.imagen.hitbox;
import main.java.zoory07.HotSpace.imagen.sombra_entity.sombra_entity;




public class piedra {
    private int x, y;
    private final int width, height;
    private final BufferedImage sprite;
    public final hitbox hitbox;
    private final sombra_entity sombra;
    private boolean activa;
    private boolean mostrarHitbox;

    public piedra(BufferedImage sprite, int width, int height) {
      this.sprite = sprite;
      this.width = width;
      this.height = height;
    
      // Crear hitbox una sola vez y reutilizarla
      int hbW = (int) (width * 0.50);
      int hbH = (int) (height * 0.50);
    
      // PARA BAJAR LA HITBOX 
      this.hitbox = new hitbox(50, 20, hbW, hbH);  // Era (50, 0), ahora (50, 20)
    
      // Crear sombra una sola vez
      this.sombra = new sombra_entity(0, 0, hbW, hbH / 2);
      this.activa = false;
   }

    /**
     * Inicializa o reinicia la piedra para reciclarla desde el pool
     */
    public void init(int startX, int startY) {
      this.x = startX;
      this.y = startY;
      this.activa = true;
      this.mostrarHitbox = false; 
      // Posicionar hitbox (mantener la posición Y bajada)
      int hbW = hitbox.getWidth();
      int hbH = hitbox.getHeight();
      int offsetX = (width - hbW) / 2;
      int offsetY = (height - hbH) / 2;
    
      // AÑADI UN VALOR EXTRA PARA BAJAR LA HITBOX:
      hitbox.updatePosition(x + offsetX + 50, y + offsetY + 55);
   }

    /**
     * Actualiza posicion y hitbox sin crear nuevos objetos
     */
    public void update(int velocidad) {
       if (!activa) return;
       y += velocidad;
    
       // Reposicionar hitbox
       int hbW = hitbox.getWidth();
       int hbH = hitbox.getHeight();
       int offsetX = (width - hbW) / 2;
       int offsetY = (height - hbH) / 2;
    
       // BAJAR LA HITBOX AQUI TAMBIEN:
       hitbox.updatePosition(x + offsetX + 50, y + offsetY + 55);
    
       // Actualizar sombra
       sombra.x = x;
       sombra.y = y + height;
       sombra.update();
    
       // Desactivar si sale de pantalla
       if (y > Main.BASE_HEIGHT) {
           activa = false;
       }
   }

    /**
     * Renderiza piedra y opcionalmente la hitbox
     */
    public void render(Graphics g) {
        if (!activa) return;
        if (mostrarHitbox) {
            hitbox.render(g);
        }
        // Dibujar sprite centrado en width x height
        int adjX = x + (width - sprite.getWidth()) / 2;
        int adjY = y + (height - sprite.getHeight()) / 2;
        g.drawImage(sprite, adjX, adjY, width, height, null);
    }

    public boolean isActiva() {
        return activa;
    }

    public void setMostrarHitbox(boolean mostrar) {
        this.mostrarHitbox = mostrar;
    }

    public hitbox getHitbox() {
       return this.hitbox;
    }

}