package main.java.zoory07.HotSpace.scenes.level;

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
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.imagen.animacion.Escena.Level_00_Decierto_Animacion;
import main.java.zoory07.HotSpace.scenes.CollisionManager;
import main.java.zoory07.HotSpace.scenes.Escena;
import main.java.zoory07.HotSpace.scenes.Sound;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00.AlazarCactus;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level00.GestionDePatronesDeEventos;
import main.java.zoory07.HotSpace.scenes.evento.EscenaLimite;
import main.java.zoory07.HotSpace.scenes.evento.EventoColision;
import main.java.zoory07.HotSpace.scenes.evento.reinicio_level;
import main.java.zoory07.HotSpace.scenes.evento.tiempo;





public class level_00_desierto implements Escena {
    private SpriteSheet spritesheet;
    private player player;
    private teclado teclado;
    private CollisionManager collisionManager;
    private AlazarCactus alazarCactus;
    private GestionDePatronesDeEventos gestionDePatrones;
    private tiempo tiempo;
    private EventoColision eventoColision;
    private boolean gameOver;
    private reinicio_level reinicioLevel;
    private EscenaLimite escenaLimite;
    private Level_00_Decierto_Animacion deciertoAnimacion;
    private Sound sonido;
    private BufferedImage GameOver;
    
    public level_00_desierto(SpriteSheet ss, teclado t, tiempo tiempoGlobal)
        throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        this.spritesheet = ss;
        this.teclado = t;
        this.tiempo = tiempoGlobal;
        this.collisionManager = new CollisionManager();
        
        // Pool de cactus
        BufferedImage cactusSprite = ss.getSprite(0, 30, 30, 30);
        alazarCactus = new AlazarCactus(cactusSprite, 120, 120, 50, collisionManager);
        gestionDePatrones = new GestionDePatronesDeEventos(alazarCactus);

        // Animación y reinicio
        deciertoAnimacion = new Level_00_Decierto_Animacion(50);
        reinicioLevel = new reinicio_level(ss, teclado, tiempo);
        reinicioLevel.setNivel(this);

        sonido = new Sound("game_over.wav");
        GameOver = ImageIO.read(getClass().getResourceAsStream("/resources/GameOver.png"));
        gameOver = false;
        inicializarNivel();
    }

    private void inicializarNivel() throws IOException {
        escenaLimite = new EscenaLimite(0, 0, 850, 900);
        List<BufferedImage> correr = spritesheet.getAnimationFrames(0,0,30,30,5);
        List<BufferedImage> gameOverFrames = spritesheet.getAnimationFrames(150,0,30,30,1);
        player = new player(440, 400, correr, teclado, 100, gameOverFrames, escenaLimite);
        collisionManager.addHitbox(player.getHitbox());
        eventoColision = new EventoColision(player, new ArrayList<piedra>(), tiempo);
    }

    @Override
    public void update() {
        if (!gameOver) {
           player.update();
           gestionDePatrones.update();
           alazarCactus.update(player);

          // Actualizar colisiones
          eventoColision.setListaCactus(new ArrayList<piedra>(alazarCactus.getCactusActivos()));
          eventoColision.checkColision();
        
        if (eventoColision.isGameOver()) {
            gameOver = true;
            player.setGameOver();
            sonido.play();
            deciertoAnimacion.detenerAnimacion();
        }
        } else {
        // Game Over - manejar reinicio
           deciertoAnimacion.detenerAnimacion();
           reinicioLevel.reiniciar();
           deciertoAnimacion.update();
        }
        deciertoAnimacion.update();
        escenaLimite.RestricionDeLimite(player);
        player.update();
    }

    @Override
    public void render(Graphics g) {
        deciertoAnimacion.render(g);
        alazarCactus.render(g);
        player.render(g);
        tiempo.render(g,10,20);
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial",Font.BOLD,50));
            if (GameOver != null) g.drawImage(GameOver, 255, 90, 400, 190, null);
            //g.drawString("GAME OVER",300,300);
            g.drawString("Presiona ENTER",250,350);
        }
    }

    
    public boolean isGameOver() {
        return gameOver;
    }

    
    public player getPlayer() {
        return player;
    }

    /**
     * Resetea el estado completo del nivel
     */
    public void reiniciarNivel() {
     gestionDePatrones.ReinicioDePatrones();
     alazarCactus.reiniciar();
     tiempo.reiniciar();
     gameOver = false;
    
     
     eventoColision.reiniciar();
    
     if (player != null) {
        player.reiniciar();
        
     }
     
     if (deciertoAnimacion != null) {
        deciertoAnimacion.reiniciarAnimacion();
     }
     gestionDePatrones.setEnPausa(false);
     alazarCactus.setEnPausa(false);
     tiempo.reanudar();
   }
}
