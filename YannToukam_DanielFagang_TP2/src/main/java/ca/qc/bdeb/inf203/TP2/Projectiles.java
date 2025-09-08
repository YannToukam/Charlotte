package ca.qc.bdeb.inf203.TP2;

import javafx.scene.image.Image;

public class Projectiles extends ElementsGraphiques {

    public Projectiles(Image image, double posX) {
        this.image = image;
        this.posX = posX;
        this.tailleX = image.getWidth();
        this.tailleY = image.getHeight();
        this.posY = (Main.HAUTEUR_CANVAS - tailleY) / 2;
    }

    public Projectiles(Image image, double tailleX, double tailleY, double vitesseX, Charlotte charlotte) {
        this.image = image;
        this.tailleX = tailleX;
        this.tailleY = tailleY;
        this.vitesseX = vitesseX;
        this.posX = charlotte.posX + charlotte.tailleX;
        this.posY = charlotte.posY + charlotte.tailleY / 2 - image.getHeight() / 2;
    }


}
