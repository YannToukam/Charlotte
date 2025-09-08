package ca.qc.bdeb.inf203.TP2;

import javafx.scene.image.Image;

import java.util.Random;

public class Decor extends ElementsGraphiques {

    protected static final Random gen = new Random();

    public Decor(double ancienPosX) {

        super(new Image("decor" + gen.nextInt(1, 7) + ".png"));

        tailleX = image.getWidth();
        tailleY = image.getHeight();

        int valeurRandom = gen.nextInt(2);

        double espacements;
        if (valeurRandom == 0) {
            espacements = 200;
        } else {
            espacements = 150;
        }

        posX = ancienPosX + espacements;

        posY = Main.HAUTEUR_CANVAS + 10 - image.getHeight();

    }

}
