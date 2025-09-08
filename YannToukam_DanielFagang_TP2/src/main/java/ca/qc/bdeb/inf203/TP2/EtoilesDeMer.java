package ca.qc.bdeb.inf203.TP2;

import javafx.scene.image.Image;

public class EtoilesDeMer extends Projectiles{
    public EtoilesDeMer(Charlotte charlotte) {
        super(new Image("etoile.png"),36, 35, 800,charlotte);
    }
}
