package main.java.zoory07.HotSpace.fisica;

import java.awt.Rectangle;
import java.util.List;
import main.java.zoory07.HotSpace.entity.ArbustoMuerto;
import main.java.zoory07.HotSpace.entity.Cubo;
import main.java.zoory07.HotSpace.entity.player;
import main.java.zoory07.HotSpace.entity.puntatierra;



public class FisicaManager {

    private static final float GRAVEDAD = 0.5f;
    private static final float SALTO_FUERZA = -17f;
    private static final float VEL_MAX_CAIDA = 12f;

    private float velocidadY = 0;
    private boolean enSuelo  = false;

    /**
     * Aplica gravedad y mueve el player verticalmente
     */
    public void update(player player) {
        // Gravedad
        velocidadY += GRAVEDAD;
        if (velocidadY > VEL_MAX_CAIDA) velocidadY = VEL_MAX_CAIDA;

        player.y += (int) velocidadY;
        enSuelo = false;
    }

    public void saltar() {
        if (enSuelo) {
            velocidadY = SALTO_FUERZA;
            enSuelo    = false;
        }
    }

    public void reiniciar() {
        velocidadY = 0;
        enSuelo    = false;
    }

    public boolean isEnSuelo() { 
        return enSuelo; 
    }
    public float getVelocidadY() { 
        return velocidadY; 
    }

    public boolean colisionConPuntas(player player, List<puntatierra> puntas) {
      Rectangle hitboxPlayer = player.getHitbox().getBounds();

      for (puntatierra p : puntas) {
         if (!p.isActivo()) continue;
         if (hitboxPlayer.intersects(p.getBounds())) {
            return true;
        }
     }
     return false;
   }

    public void resolverColisionesCubos(player player, List<Cubo> cubos) {
        Rectangle hitboxPlayer = player.getHitbox().getBounds();

        for (Cubo cubo : cubos) {
         if (!cubo.isActivo()) continue;

         Rectangle hitboxCubo = cubo.getBounds();
         if (!hitboxPlayer.intersects(hitboxCubo)) continue;

         int playerCentroX = hitboxPlayer.x + hitboxPlayer.width  / 2;
         int playerCentroY = hitboxPlayer.y + hitboxPlayer.height / 2;
         int cuboCentroX   = hitboxCubo.x   + hitboxCubo.width    / 2;
         int cuboCentroY   = hitboxCubo.y   + hitboxCubo.height   / 2;

         int overlapX = (hitboxPlayer.width  + hitboxCubo.width)  / 2 - Math.abs(playerCentroX - cuboCentroX);
         int overlapY = (hitboxPlayer.height + hitboxCubo.height) / 2 - Math.abs(playerCentroY - cuboCentroY);

         if (overlapX < overlapY) {
            if (playerCentroX < cuboCentroX) player.x -= overlapX;
            else player.x += overlapX;
         } else {
            if (playerCentroY < cuboCentroY) {
                player.y -= overlapY;
                velocidadY = 0;
                enSuelo    = true;
            } else {
                player.y += overlapY;
                velocidadY = 0;
            }
        }

        hitboxPlayer = player.getHitbox().getBounds();
    }
  }
  
  public boolean colisionConArbusto(player player, List<ArbustoMuerto> arbustos) {
     Rectangle hitboxPlayer = player.getHitbox().getBounds();

     for (ArbustoMuerto a : arbustos) {
        if (!a.isActivo()) continue;
        if (hitboxPlayer.intersects(a.getBounds())) {
            a.setActivo(false);
            return true;
        }
    }
     return false;
   } 

}