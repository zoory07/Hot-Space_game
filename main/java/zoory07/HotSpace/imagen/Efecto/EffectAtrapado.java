package main.java.zoory07.HotSpace.imagen.Efecto;








public class EffectAtrapado {

    private boolean activo;
    private int duracion;  
    private int timer;
    private float multiplicador; 

    public EffectAtrapado(int duracion, float multiplicador) {
        this.duracion = duracion;
        this.multiplicador = multiplicador;
        this.activo = false;
        this.timer = 0;
    }

    public void activar() {
        activo = true;
        timer  = 0;
    }

    public void update() {
        if (!activo) return;

        timer++;
        if (timer >= duracion) {
            activo = false;
            timer  = 0;
        }
    }

    /**
     * Aplica la ralentización a la velocidad original
     */
    public int aplicar(int velocidadOriginal) {
      if (!activo) return velocidadOriginal;
      return Math.max(1, (int)(velocidadOriginal * multiplicador));
    }

    public void reiniciar() {
        activo = false;
        timer  = 0;
    }

    public boolean isActivo(){ 
        return activo; 
    }
    public int getTimer(){ 
        return timer; 
    }
    public int getDuracion(){ 
        return duracion; 
    }
}
