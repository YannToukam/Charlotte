package ca.qc.bdeb.inf203.TP2;

import javafx.scene.image.Image;


public class BoiteSardine extends Projectiles {
    public static double forceElectrique;
    private static double accelerationY;
    private static double accelerationX;


    public BoiteSardine(Charlotte charlotte) {
        super(new Image("sardines.png"), 35, 29, 300, charlotte);
        accelerationY = 0;
        accelerationX = 0;
    }

    protected void validerLimite() {
        double facteur = 1.0;
        if ((vitesseY > 0 && posY + tailleY >= Main.HAUTEUR_CANVAS) || (vitesseY < 0 && posY <= 0)) {
            vitesseY *= -facteur;
            accelerationY = 0;
        }
    }

    public void update(double diffTemps) {

        vitesseX += accelerationX * diffTemps;

        if (vitesseX < 300) {
            vitesseX = 300;
        } else if (vitesseX > 500) {
            vitesseX = 500;
        }
        vitesseY += accelerationY * diffTemps;
        if (vitesseY < -500) {
            vitesseY = -500;
        } else if (vitesseY > 500) {
            vitesseY = 500;
        }

        validerLimite();

        super.update(diffTemps);
    }

    public void calculerForces(PoissonsEnnemi poisson) {
        double deltaX = posX - poisson.posX;
        double deltaY = (posY - poisson.posY);
        double distance = Math.pow(Math.pow(deltaX, 2) + Math.pow(deltaY, 2), 0.5);
        if (distance < 0.01) {
            distance = 0.01;
        }
        forceElectrique = (1000 * -100 * 200) / Math.pow(distance, 2);
        double proportionX = deltaX / distance;
        double proportionY = deltaY / distance;
        double forceX = forceElectrique * proportionX;
        double forceY = forceElectrique * proportionY;
        accelerationX += forceX;

        accelerationY += forceY;

    }


}
