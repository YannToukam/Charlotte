package ca.qc.bdeb.inf203.TP2;

import javafx.scene.image.Image;

public class Hippocampe extends Projectiles {
    private double tempsEcoule = 0;

    private final double positionInitialeY;

    private final double amplitude;
    private final double periode;

    public Hippocampe(Charlotte charlotte) {
        super(new Image("hippocampe.png"), 20, 36, 500, charlotte);
        positionInitialeY = charlotte.posY + charlotte.tailleY / 2 - image.getHeight() / 2;
        int random = gen.nextInt(30, 61);
        int signe = (gen.nextBoolean()) ? 1 : -1;
        amplitude = random * signe;
        periode = gen.nextDouble(1, 4);

    }

    @Override
    public void update(double diffTemps) {
        tempsEcoule += diffTemps;

        posY = amplitude * Math.sin(2 * Math.PI * tempsEcoule / periode) + positionInitialeY;

        super.update(diffTemps);
    }


}
