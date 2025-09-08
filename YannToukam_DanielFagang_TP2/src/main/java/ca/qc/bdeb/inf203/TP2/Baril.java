package ca.qc.bdeb.inf203.TP2;

import javafx.scene.image.Image;

public class Baril extends Projectiles {
    private static double tempsEcoule = 0;

    public Baril() {
        super(new Image("baril.png"), gen.nextDouble(Main.LARGEUR_MONDE * 0.2, Main.LARGEUR_MONDE * 0.8));
    }

    public void update(double diffTemps) {
        tempsEcoule += diffTemps;
        posY = (Main.HAUTEUR_CANVAS - tailleY) / 2 * Math.sin(2 * Math.PI * tempsEcoule / 3) + (Main.HAUTEUR_CANVAS - tailleY) / 2;
        //Oscillation du baril

    }

}
