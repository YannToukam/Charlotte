package ca.qc.bdeb.inf203.TP2;

import javafx.scene.image.Image;

import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public abstract class ElementsGraphiques {

    protected Image image;

    protected static final Random gen = new Random();

    protected double posX;

    protected double posY;

    protected double tailleX;

    protected double tailleY;

    protected double vitesseX;

    protected double vitesseY;

    protected double tempsTotal;

    public void setTailleX(double tailleX) {
        this.tailleX = tailleX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setVitesseX(double vitesseX) {
        this.vitesseX = vitesseX;
    }

    public void setImage(Image image) {
        this.image = image;
    }


    public ElementsGraphiques() {

    }

    public ElementsGraphiques(double posX, double posY, double tailleX, double tailleY) {
        this.posX = posX;
        this.posY = posY;
        this.tailleX = tailleX;
        this.tailleY = tailleY;
    }

    public ElementsGraphiques(Image image) {
        this.image = image;

    }

    public void update(double diffTemps) {
        tempsTotal += diffTemps;
        posX += diffTemps * vitesseX;
        posY += diffTemps * vitesseY;
    }

    public boolean enCollision(ElementsGraphiques element) {

        boolean enCollision = false;

        if (element.posX <= posX + tailleX && element.posX >= posX) {

            if ((element.posY <= posY + tailleY && element.posY >= posY)
                    || (element.posY + element.tailleY >= posY && element.posY + tailleY <= posY + tailleY)) {
                enCollision = true;
            }

        }

        return enCollision;
    }

    protected void validerLimite() {

        double limiteDevantEnX = (Camera.getCamera().getPosX() + Main.LARGEUR_CANVAS);
        double limiteDerriereEnX = Camera.getCamera().getPosX();

        if (posY + tailleY >= Main.HAUTEUR_CANVAS && vitesseY > 0) {
            posY = Main.HAUTEUR_CANVAS - tailleY;
            vitesseY = 0;
        } else if (posY <= 0 && vitesseY < 0) {
            posY = 0;
            vitesseY = 0;
        }

        if (posX + tailleX >= limiteDevantEnX && vitesseX > 0) {
            posX = limiteDevantEnX - tailleX;
            vitesseX = 0;

        } else if (posX <= limiteDerriereEnX && vitesseX < 0) {
            posX = limiteDerriereEnX;
            vitesseX = 0;

        }

    }

    public void dessiner(GraphicsContext context) {

        context.drawImage(image, Camera.getCamera().transformer(posX), posY, tailleX, tailleY);
    }
}
