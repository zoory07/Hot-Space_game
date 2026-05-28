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
import main.java.zoory07.HotSpace.game.teclado;
import main.java.zoory07.HotSpace.imagen.SpriteSheet;
import main.java.zoory07.HotSpace.imagen.animacion.Escena.Level_00_Decierto_Animacion;
import main.java.zoory07.HotSpace.scenes.CollisionManager;
import main.java.zoory07.HotSpace.scenes.Escena;
import main.java.zoory07.HotSpace.scenes.MusicaManager;
import main.java.zoory07.HotSpace.scenes.Sound;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level.AlazarCactus;
import main.java.zoory07.HotSpace.scenes.evento.DiseñoDeDificultad_level.GestionDePatronesDeEventos;
import main.java.zoory07.HotSpace.scenes.evento.EscenaLimite;
import main.java.zoory07.HotSpace.scenes.evento.EventoColision;
import main.java.zoory07.HotSpace.scenes.evento.reinicio_level;
import main.java.zoory07.HotSpace.scenes.evento.tiempo;





public class EscenaArcade implements Escena {
    
    private SpriteSheet spritesheet;
    private player player;  // Original
    private teclado teclado;  // Original
    private CollisionManager collisionManager;
    private AlazarCactus alazarCactus;
    private GestionDePatronesDeEventos gestionDePatrones;
    private tiempo tiempo;  // Original
    private EventoColision eventoColision;
    private boolean gameOver;
    private reinicio_level reinicioLevel;  // Original
    private EscenaLimite escenaLimite;
    private Level_00_Decierto_Animacion deciertoAnimacion;
    private Sound sonido;
    private BufferedImage gameOverImage;
    private int velocidad;
    private MusicaManager MusicaManager;
    
    public EscenaArcade(SpriteSheet ss, teclado t, tiempo tiempoGlobal)throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        this.spritesheet = ss;
        this.teclado = t;
        this.tiempo = tiempoGlobal;
        this.collisionManager = new CollisionManager();
        
        BufferedImage cactusSprite = ss.getSprite(0, 30, 30, 30);
        alazarCactus = new AlazarCactus(cactusSprite, 120, 120, 50, collisionManager);
        gestionDePatrones = new GestionDePatronesDeEventos(alazarCactus);
        
        deciertoAnimacion = new Level_00_Decierto_Animacion(50);
        reinicioLevel = new reinicio_level(ss, teclado, tiempo);
        reinicioLevel.setNivel(this);
        
        sonido = new Sound("game_over.wav");
        MusicaManager.reproducir("arcade");
        gameOverImage = ImageIO.read(getClass().getResourceAsStream("/resources/GameOver.png"));
        gameOver = false;
        
        inicializarNivel();
    }
    
    private void inicializarNivel() throws IOException {
        escenaLimite = new EscenaLimite(0, 0, 850, 900);
        
        List<BufferedImage> correr = spritesheet.getAnimationFrames(0, 0, 30, 30, 5);
        List<BufferedImage> gameOverFrames = spritesheet.getAnimationFrames(150, 0, 30, 30, 1);
        
        player = new player(440, 400, correr, teclado, 100, gameOverFrames, escenaLimite);
        collisionManager.addHitbox(player.getHitbox());
        eventoColision = new EventoColision(player, new ArrayList<piedra>(), tiempo);
    }
    
    @Override
    public void update() {
        deciertoAnimacion.update();
        
        if (!gameOver) {
            player.update(velocidad);
            gestionDePatrones.update();
            alazarCactus.update(player);
            
            eventoColision.setListaCactus(new ArrayList<piedra>(alazarCactus.getCactusActivos()));
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
        
        escenaLimite.RestricionDeLimite(player);
    }
    
    @Override
    public void render(Graphics g) {
        deciertoAnimacion.render(g);
        alazarCactus.render(g);
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
    
    public player getPlayer() {
        return player;
    }
    
    public void reiniciarNivel() {
        gestionDePatrones.ReinicioDePatrones();
        alazarCactus.reiniciar();
        tiempo.reiniciar();
        gameOver = false;
        eventoColision.reiniciar();
        
        if (player != null) player.reiniciar();
        if (deciertoAnimacion != null) deciertoAnimacion.reiniciarAnimacion();
        
        gestionDePatrones.setEnPausa(false);
        alazarCactus.setEnPausa(false);
        tiempo.reanudar();
    }
}