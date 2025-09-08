package ca.qc.bdeb.inf203.TP2;

import javafx.scene.image.Image;

public class Poisson extends ElementsGraphiques {

    protected double ACCELERATION;

    public Poisson(Image image, double tailleX, double tailleY, double posX) {
        this.image = image;
        this.tailleX = tailleX;
        this.tailleY = tailleY;
        this.posX = posX;
        this.posY = (Main.HAUTEUR_CANVAS / 2) - tailleY / 2;

    }

    public Poisson(double tailleY, Image image, double vitesseY, double vitesseX, double posY) {
        this.image = image;
        this.tailleY = tailleY;
        this.vitesseY = vitesseY;
        this.vitesseX= vitesseX;
        this.posY = posY;
        this.tailleX = tailleY * 120 / 104;

    }

}
