package ca.qc.bdeb.inf203.TP2;

import javafx.scene.image.Image;

public class PoissonsEnnemi extends Poisson {

    public PoissonsEnnemi(double numeroNiveau) {
        super(gen.nextDouble(50, 121),
                new Image("poisson" + gen.nextInt(1, 6) + ".png"),
                gen.nextDouble(-100, 100),
                -(100 * Math.pow(numeroNiveau, 0.33) + 200),
                gen.nextDouble((1.0 / 5) * Main.HAUTEUR_CANVAS, (4.0 / 5) * Main.HAUTEUR_CANVAS));

        posX = (Main.LARGEUR_CANVAS + Camera.getCamera().getPosX()+tailleX);

    }

    @Override
    public void update(double diffTemps) {
        ACCELERATION = -500;
        vitesseX += diffTemps * ACCELERATION;
        super.update(diffTemps);
    }


}

