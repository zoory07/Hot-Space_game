package main.java.zoory07.HotSpace.scenes.evento;

import main.java.zoory07.HotSpace.entity.player;








public class EscenaLimite {
   
   private int escenaX, escenaY;
   private int LimitadoX;
   private int LimitadoY;
   
   public EscenaLimite(int escenaX, int escenaY, int LimitadoX, int LimitadoY){
       this.LimitadoX = LimitadoX;
       this.LimitadoY = LimitadoY;
       this.escenaX = escenaX;
       this.escenaY = escenaY;
   }
   
   public void RestricionDeLimite(EventoColision ColisionLimite){
       int ObjetoX = ColisionLimite.GetX();
       int ObjetoY = ColisionLimite.GetY();
       int objetoAncho = ColisionLimite.getWidth();
       int objetoAlto = ColisionLimite.getHeight();
       
       if(ObjetoX < escenaX){
         ColisionLimite.setX(escenaX);
       }else if(ObjetoX + objetoAncho > LimitadoX){
         ColisionLimite.setX(LimitadoX - objetoAncho);
       }
       
       
   }
   
   public void RestricionDeLimite(player jugador){
    
     int objetoX = jugador.getX();
     int objetoY = jugador.getY();
     int objetoAncho = jugador.getWidth();
     int objetoAlto = jugador.getHeight();

    
     if (objetoX < escenaX) {
        jugador.setX(escenaX);
     } else if (objetoX + objetoAncho > LimitadoX) {
        jugador.setX(LimitadoX - objetoAncho);
     }

    
     if (objetoY < escenaY) {
        jugador.setY(escenaY);
     } else if (objetoY + objetoAlto > LimitadoY) {
        jugador.setY(LimitadoY - objetoAlto);
     }
  }



}