package ca.qc.bdeb.inf203.TP2;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BarreDeVie extends ElementsGraphiques {

    private final Color couleur;

    private final double vieDeDepart = tailleX;

    public double getVieDeDepart() {
        return vieDeDepart;
    }

    public double getPtsDeViesPerdu() {
        final int nombreDeVies = 4; //Nombre de vie de charlotte
        return vieDeDepart / nombreDeVies;
    }

    //cette methode retourne le nombre de hp perdu de charlotte

    public BarreDeVie() {
        super(Camera.getCamera().transformer(25), 25, 150, 45);
        couleur = Color.WHITE;
    }


    @Override
    public void dessiner(GraphicsContext context) {
        context.setFill(couleur);
        context.strokeRect(posX, posY, vieDeDepart, tailleY);
        context.setStroke(couleur);
        context.fillRect(posX, posY, tailleX, tailleY);

    }
}
