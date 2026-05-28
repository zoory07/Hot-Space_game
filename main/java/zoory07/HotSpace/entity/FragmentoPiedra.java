package main.java.zoory07.HotSpace.entity;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;



public class FragmentoPiedra extends Entity {

    private BufferedImage sprite;
    private boolean activo;
    private float velX, velY;
    private int vida;

    public FragmentoPiedra(BufferedImage sprite, int ancho, int alto) {
        super(0, 0, ancho, alto);
        this.sprite = sprite;
        this.width  = ancho;
        this.height = alto;
        this.activo = false;
    }

    public void init(int x, int y) {
        this.x    = x;
        this.y    = y;
        // Dirección aleatoria al explotar
        this.velX = (float)(Math.random() * 6) - 3;
        this.velY = (float)(Math.random() * -6) - 2;
        this.vida  = 40; // frames que dura
        this.activo = true;
    }

    @Override
    public void update(int velocidad) {
        if (!activo) return;

        x    += (int) velX;
        y    += (int) velY;
        velY += 0.4f; // gravedad
        vida--;

        if (vida <= 0) activo = false;
    }

    @Override
    public void render(Graphics g) {
        if (!activo || sprite == null) return;
        float alpha = (float) vida / 40f;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(sprite, x, y, width, height, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public boolean isActivo(){ 
        return activo; 
    }
    public void setActivo(boolean b) { 
        this.activo = b; 
    }
}
