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
import main.java.zoory07.HotSpace.entity.piedra;
import main.java.zoory07.HotSpace.entity.player;
import main.java.zoory07.HotSpace.fisica.FisicaManager;
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.Efecto.EffectAtrapado;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.imagen.animacion.Escena.Level_01_Decierto_Animacion;
import main.java.zoory07.HotSpace.scenes.CollisionManager;
import main.java.zoory07.HotSpace.scenes.Escena;
import main.java.zoory07.HotSpace.scenes.MusicaManager;
import main.java.zoory07.HotSpace.scenes.Sound;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level.GeneradorDeArbustoMuerto;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level.GeneradorDeCubos;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level.GeneradorDePuntas;
import main.java.zoory07.HotSpace.scenes.evento.EventoColision;
import main.java.zoory07.HotSpace.scenes.evento.reinicio_level;
import main.java.zoory07.HotSpace.scenes.evento.tiempo;


public class EscenaSurvival implements Escena {
    
    private SpriteSheet spritesheet;
    private player player;
    private teclado teclado;
    private CollisionManager collisionManager;
    private GeneradorDeCubos generadorDeCubos;
    private tiempo tiempo;
    private EventoColision eventoColision;
    private boolean gameOver;
    private reinicio_level reinicioLevel;
    private Level_01_Decierto_Animacion deciertoAnimacion;
    private Sound sonido;
    private Sound sonidoArbusto;
    private BufferedImage gameOverImage;
    private int velocidad = 2;
    private int intervalo = 30 / velocidad;
    private FisicaManager fisica;
    private int timerProteccion = 120;
    private GeneradorDePuntas generadorDePuntas;
    private GeneradorDeArbustoMuerto generadorArbustos;
    private EffectAtrapado effectAtrapado;
    private int timerDificultad = 0;
    
    public EscenaSurvival(SpriteSheet ss, teclado t, tiempo tiempoGlobal) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        this.spritesheet = ss;
        this.teclado = t;
        this.tiempo = tiempoGlobal;
        this.collisionManager = new CollisionManager();
        deciertoAnimacion = new Level_01_Decierto_Animacion(50);
        reinicioLevel = new reinicio_level(ss, teclado, tiempo);
        MusicaManager.reproducir("survival");
        sonido = new Sound("game_over.wav");
        sonidoArbusto = new Sound("arbustoM.wav");
        gameOverImage = ImageIO.read(getClass().getResourceAsStream("/resources/GameOver.png"));
        gameOver = false;
        
        inicializarNivel();
    }
    
    private void inicializarNivel() throws IOException {
      List<BufferedImage> correr = spritesheet.getAnimationFrames(357, 0, 30, 30, 4);
      List<BufferedImage> gameOverFrames = spritesheet.getAnimationFrames(150, 0, 30, 30, 1);
      List<BufferedImage> atrapadoFrames = spritesheet.getAnimationFrames(330, 30, 30, 30, 5);
      
      BufferedImage spriteCubo = spritesheet.getSprite(60, 60, 30, 30);
      generadorDeCubos = new GeneradorDeCubos(spriteCubo, 30, 30, 900, 600, intervalo);

      int playerY = generadorDeCubos.getCentroHueco() - 45;
      player = new player(300, playerY, correr, teclado, 100, gameOverFrames, null);
    
     
      
      player.setAnimacionAtrapado(atrapadoFrames, 150);

      collisionManager.addHitbox(player.getHitbox());
      eventoColision = new EventoColision(player, new ArrayList<piedra>(), tiempo);

      fisica = new FisicaManager();
      reinicioLevel.setNivelSurvival(this);

      BufferedImage spritePunta   = spritesheet.getSprite(180, 30, 30, 30);
      BufferedImage spriteArbusto = spritesheet.getSprite(35, 30, 30, 30);
      generadorArbustos = new GeneradorDeArbustoMuerto(spriteArbusto, 60, 60, 900, 600, 180, generadorDeCubos);
      generadorDePuntas = new GeneradorDePuntas(spritePunta, 30, 30, 900, 600, 90);

      effectAtrapado = new EffectAtrapado(180, 0.3f);
   }
    
    @Override
    public void update() {
      deciertoAnimacion.update();
    
      if (!gameOver) {
        
        timerDificultad++;
        if (timerDificultad >= 1800) {
            timerDificultad = 0;
            velocidad++;
            generadorDeCubos.setIntervalo(Math.max(5, 30 / velocidad));
            generadorDePuntas.setIntervalo(Math.max(30, 90 - velocidad * 5));
            generadorArbustos.setIntervalo(Math.max(60, 180 - velocidad * 10));
        }

        fisica.update(player);
        fisica.resolverColisionesCubos(player, generadorDeCubos.getActivos());

        if (teclado.espacio) {
            fisica.saltar();
        }

        if (fisica.colisionConArbusto(player, generadorArbustos.getActivos())) {
            sonidoArbusto.play();
            effectAtrapado.activar();
        }
        effectAtrapado.update();

        int velocidadActual = effectAtrapado.aplicar(velocidad);
        player.setAtrapado(effectAtrapado.isActivo());
        player.setVelocidad(effectAtrapado.isActivo() ? (int)(15 * 0.3f) : 15);

        player.update(velocidadActual);
        generadorDeCubos.update(velocidadActual);
        generadorDePuntas.update(velocidadActual);
        generadorArbustos.update(velocidadActual);

        if (player.y < -100 || player.y > 600) {
            gameOver = true;
            player.setGameOver();
            sonido.play();
            
            deciertoAnimacion.detenerAnimacion();
            return;
        }

        if (fisica.colisionConPuntas(player, generadorDePuntas.getActivos())) {
            gameOver = true;
            player.setGameOver();
            sonido.play();
            deciertoAnimacion.detenerAnimacion();
            return;
        }
        
        eventoColision.checkColision();

        if (eventoColision.isGameOver()) {
            gameOver = true;
            player.setGameOver();
            sonido.play();
            deciertoAnimacion.detenerAnimacion();
        }
    } else {
        reinicioLevel.reiniciar();
    }
  }
    
    @Override
    public void render(Graphics g) {
        deciertoAnimacion.render(g);
        generadorDeCubos.render(g);
        generadorArbustos.render(g);
        generadorDePuntas.render(g);
        player.render(g);
        tiempo.render(g, 10, 20);
        
        if (gameOver) {
            renderGameOver(g);
        }
    }
    
    private void renderGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Jersey 10", Font.PLAIN, 60));
        if (gameOverImage != null) {
            g.drawImage(gameOverImage, 255, 90, 400, 190, null);
        }
        g.drawString("Presiona ENTER", 300, 320);
    }
    
    public boolean isGameOver() { 
        return gameOver; 
    }
    public player getPlayer(){ 
        return player; 
    }
    
    public void reiniciarNivel() {
      velocidad  = 2;          
      timerDificultad = 0;         
      intervalo  = 30 / velocidad; 
      player.setVelocidad(15);
      generadorDeCubos.reiniciar();
      generadorDePuntas.reiniciar();
      generadorArbustos.reiniciar();
      effectAtrapado.reiniciar();
      tiempo.reiniciar();
      gameOver = false;
      eventoColision.reiniciar();
      fisica.reiniciar();
      if (player != null) player.reiniciar();
      if (deciertoAnimacion != null) deciertoAnimacion.reiniciarAnimacion();
      tiempo.reanudar();
    }
}