package main.java.zoory07.HotSpace.imagen.animacion.entity_a.player;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;




public class Player_animacion_GameOver {

   private List<BufferedImage> frames;
   private int currentFrame;
   private long UltimoMarcoTiempoDeCambio;
   private long Duracion; 
   private long parar; 
   private boolean paraEstaQ = false; 

   // Constructor
   public Player_animacion_GameOver(List<BufferedImage> gameOverFrame, long Duracion, long parar) {
      this.frames = gameOverFrame; // Asignación correcta de frames
      this.Duracion = Duracion; // Tiempo entre cambios de frame
      this.parar = parar; // Tiempo total antes de detener
      this.currentFrame = 0;
      this.UltimoMarcoTiempoDeCambio = System.currentTimeMillis(); 
   }

   // Actualizar el frame de la animación
   public void update() {
      if (paraEstaQ) {
         return; // Si la animación está detenida, no hacer nada
      }
      
      long currentTime = System.currentTimeMillis();
      
      // Verificar si es momento de detener la animación
      if (currentTime - UltimoMarcoTiempoDeCambio >= parar) {
         paraEstaQ = true; // Detenemos la animación
         return;
      }
      
      // Cambiar al siguiente frame si ha pasado suficiente tiempo
      if (currentTime - UltimoMarcoTiempoDeCambio >= Duracion) {
         currentFrame = (currentFrame + 1) % frames.size(); // Cambiar al siguiente frame
         UltimoMarcoTiempoDeCambio = currentTime; // Actualizamos el tiempo del último cambio de frame
      }
   }

   // Renderizar el frame actual
   public void render(Graphics g, int x, int y, int width, int height) {
      if (frames != null && !frames.isEmpty()) {
         BufferedImage frame = frames.get(currentFrame);
         g.drawImage(frame, x, y, width, height, null); // Dibujar el frame actual
      }
   }

   // Resetear la animación a su estado inicial
   public void reset() {
      this.currentFrame = 0;
      this.UltimoMarcoTiempoDeCambio = System.currentTimeMillis();
      this.paraEstaQ = false;
   }

   // Detener la animación manualmente
   public void StopAnimacion() {
      this.paraEstaQ = true;
   }

   // Reanudar la animación
   public void resumenAnimation() {
      this.paraEstaQ = false;
      this.UltimoMarcoTiempoDeCambio = System.currentTimeMillis(); 
   }

   // Verificar si la animación está detenida
   public boolean isStopped() {
      return paraEstaQ;
   } 
}
