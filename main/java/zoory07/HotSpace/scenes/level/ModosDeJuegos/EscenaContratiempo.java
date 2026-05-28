package main.java.zoory07.HotSpace.scenes.level.ModosDeJuegos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import main.java.zoory07.HotSpace.entity.Zanahoria;
import main.java.zoory07.HotSpace.entity.piedra;
import main.java.zoory07.HotSpace.entity.player;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.imagen.animacion.Escena.Level_00_Decierto_Animacion;
import main.java.zoory07.HotSpace.scenes.CollisionManager;
import main.java.zoory07.HotSpace.scenes.Escena;
import main.java.zoory07.HotSpace.scenes.MusicaManager;
import main.java.zoory07.HotSpace.scenes.Sound;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level.AlazarCactus;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level.GeneradorDePatronesContraTiempo;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level.GeneradorDeZanahorias;
import main.java.zoory07.HotSpace.scenes.evento.EscenaLimite;
import main.java.zoory07.HotSpace.scenes.evento.EventoColision;
import main.java.zoory07.HotSpace.scenes.evento.poolobjeto.PoolFragmentoPiedra;
import main.java.zoory07.HotSpace.scenes.evento.reinicio_level;
import main.java.zoory07.HotSpace.scenes.evento.tiempo;





public class EscenaContratiempo implements Escena {
    
    private SpriteSheet spritesheet;
    private player player;
    private teclado teclado;
    private CollisionManager collisionManager;
    private AlazarCactus alazarCactus;
    private GeneradorDePatronesContraTiempo generadorPatrones;
    private tiempo tiempo;
    private EventoColision eventoColision;
    private boolean gameOver;
    private reinicio_level reinicioLevel;
    private EscenaLimite escenaLimite;
    private Level_00_Decierto_Animacion deciertoAnimacion;
    private Sound sonido;
    private BufferedImage gameOverImage;
    private int velocidad;
    private GeneradorDeZanahorias generadorZanahorias;
    private int tiempoRestante = 60 * 60;
    private boolean tiempoAgotado = false;
    private PoolFragmentoPiedra poolFragmentos;
    private Sound sonidoFragmento;
    private Sound pop;
    
    public EscenaContratiempo(SpriteSheet ss, teclado t, tiempo tiempoGlobal) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        this.spritesheet = ss;
        this.teclado = t;
        this.tiempo = tiempoGlobal;
        this.collisionManager = new CollisionManager();

        
        BufferedImage cactusSprite = ss.getSprite(0, 30, 30, 30);
        alazarCactus = new AlazarCactus(cactusSprite, 120, 120, 50, collisionManager);

        deciertoAnimacion = new Level_00_Decierto_Animacion(50);
        reinicioLevel = new reinicio_level(ss, teclado, tiempo);
        reinicioLevel.setNivelContraTiempo(this);
        
        sonido = new Sound("game_over.wav");
        sonidoFragmento = new Sound("fragmento.wav");
        pop = new Sound("pop.wav");
        gameOverImage = ImageIO.read(getClass().getResourceAsStream("/resources/GameOver.png"));
        gameOver = false;
        
        inicializarNivel();
    }
    
    private void inicializarNivel() throws IOException {
        escenaLimite = new EscenaLimite(0, 0, 850, 900);
        BufferedImage spriteFragmento = spritesheet.getSprite(60, 30, 30, 30);
        poolFragmentos = new PoolFragmentoPiedra(spriteFragmento, 35, 35, 50);
        List<BufferedImage> correr = spritesheet.getAnimationFrames(0, 0, 30, 30, 5);
        List<BufferedImage> gameOverFrames = spritesheet.getAnimationFrames(150, 0, 30, 30, 1);
        
        player = new player(440, 400, correr, teclado, 100, gameOverFrames, escenaLimite);
        collisionManager.addHitbox(player.getHitbox());
        eventoColision = new EventoColision(player, new ArrayList<piedra>(), tiempo);
        MusicaManager.reproducir("contratiempo");
        // ZANAHORIAS
        BufferedImage spriteZanahoria = spritesheet.getSprite(210, 30, 30, 30);
        generadorZanahorias = new GeneradorDeZanahorias(
            spriteZanahoria, 30, 30, 10, 850, 900, 300, null, collisionManager
        );
        generadorPatrones = new GeneradorDePatronesContraTiempo(alazarCactus, generadorZanahorias);
    }
    
    @Override
    public void update() {
       deciertoAnimacion.update();
       poolFragmentos.updateAll();
       if (!gameOver && !tiempoAgotado) {
        player.update(velocidad);
        generadorPatrones.update();
        alazarCactus.update(player);
        generadorZanahorias.update(velocidad);
        
        // Cuenta regresiva
        tiempoRestante--;
        if (tiempoRestante <= 0) {
            tiempoAgotado = true;
            gameOver = true;
            player.setGameOver();
            sonido.play();
            deciertoAnimacion.detenerAnimacion();
            return;
        }

        
        for (piedra p : new ArrayList<>(alazarCactus.getCactusActivos())) {
            if (p.isActiva() && player.getHitbox().collidesWith(p.getHitbox())) {
                p.setActiva(false);
                sonidoFragmento.play(); 
                poolFragmentos.spawnExplosion(p.getX(), p.getY(), 6);
                tiempoRestante -= 5 * 60; // -5 segundos
                if (tiempoRestante <= 0) {
                    tiempoRestante = 0;
                    tiempoAgotado  = true;
                    gameOver = true;
                    player.setGameOver();
                    sonido.play();
                    deciertoAnimacion.detenerAnimacion();
                    return;
                }
            }
        }
        
        // Colisión con zanahoria — suma 5 segundos
        for (Zanahoria z : new ArrayList<>(generadorZanahorias.getActivos())) {
            if (z.isActivo() && player.getHitbox().collidesWith(z.getHitbox())) {
                pop.play();
                z.setActivo(false);
                tiempoRestante += 10 * 60; // +5 sugundo 
            }
        }

        } else {
         reinicioLevel.reiniciar();
       }
    
      escenaLimite.RestricionDeLimite(player);
    }
    
    @Override
    public void render(Graphics g) {
      deciertoAnimacion.render(g);
      alazarCactus.render(g);
      generadorZanahorias.render(g);
      poolFragmentos.renderAll(g);  
      player.render(g);
      renderTiempoRestante(g);
    
      if (gameOver) {
         renderGameOver(g);
      }
   }

    private void renderTiempoRestante(Graphics g) {
        int segundos = tiempoRestante / 60;
        g.setColor(segundos <= 10 ? Color.RED : Color.WHITE);
        g.setFont(new Font("Jersey 10", Font.PLAIN, 30));
        g.drawString("Tiempo: " + segundos + "s", 10, 30);
    }
    
    private void renderGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Jersey 10", Font.PLAIN, 60));
        if (gameOverImage != null) {
            g.drawImage(gameOverImage, 255, 90, 400, 190, null);
        }
        g.drawString("Presiona ENTER", 300, 320);
    }

    public boolean isGameOver() { return gameOver; }
    public player getPlayer()   { return player; }
    
    public void reiniciarNivel() {
        poolFragmentos.reiniciar();
        generadorPatrones.reiniciar();
        generadorZanahorias.reiniciar();
        alazarCactus.reiniciar();
        tiempoRestante = 60 * 60;
        tiempoAgotado  = false;
        tiempo.reiniciar();
        gameOver = false;
        eventoColision.reiniciar();
        
        if (player != null) player.reiniciar();
        if (deciertoAnimacion != null) deciertoAnimacion.reiniciarAnimacion();
        
        generadorPatrones.setEnPausa(false);
        alazarCactus.setEnPausa(false);
        tiempo.reanudar();
    }

}