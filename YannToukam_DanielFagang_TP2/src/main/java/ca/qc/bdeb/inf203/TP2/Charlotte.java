package ca.qc.bdeb.inf203.TP2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Charlotte extends Poisson {

    private static double accelerationX;
    private static double accelerationY;
    private static final KeyCode toucheHaut = KeyCode.UP;
    private static final KeyCode toucheBas = KeyCode.DOWN;
    private static final KeyCode toucheGauche = KeyCode.LEFT;
    private static final KeyCode toucheDroite = KeyCode.RIGHT;

    private static double tempsDeMort;

    private static double tempsDeImpact;

    private static boolean charlotteInvincible = false;

    private static boolean estMorte = false;


    public Charlotte() {
        super(new Image("charlotte.png"), 102, 90, 0);
    }

    private void updatePhysique(double deltaTemps) {
        vitesseX += deltaTemps * accelerationX;
        vitesseY += deltaTemps * accelerationY;
        posX += deltaTemps * vitesseX;
        posY += deltaTemps * vitesseY;
    }

    public void update(double diffTemps) {
        tempsTotal += diffTemps;

        boolean enMouvement = vitesseX != 0 || vitesseY != 0;

// Utiliser des constantes pour les touches et les valeurs constantes
        final int ACCELERATION = 1000;

        boolean gauche = Input.isKeyPressed(toucheGauche);
        boolean droite = Input.isKeyPressed(toucheDroite);
        boolean haut = Input.isKeyPressed(toucheHaut);
        boolean bas = Input.isKeyPressed(toucheBas);

// Utiliser des opérateurs ternaires pour simplifier le code
        accelerationX = (gauche) ? -ACCELERATION : (droite) ? ACCELERATION : 0;
        accelerationY = (haut) ? -ACCELERATION : (bas) ? ACCELERATION : 0;

// Utiliser des fonctions pour éviter la répétition du code
        vitesseX = mettreAJourVitesse(vitesseX, diffTemps, accelerationX);
        vitesseY = mettreAJourVitesse(vitesseY, diffTemps, accelerationY);


        updatePhysique(diffTemps);

        if (enMouvement) {
            image = new Image("charlotte-avant.png");
        } else {
            image = new Image("charlotte.png");
        }

        super.validerLimite();
    }

    private double mettreAJourVitesse(double vitesse, double diffTemps, double acceleration) {
        int signeVitesse = (vitesse > 0) ? 1 : -1;
        double vitesseAmortissement = -signeVitesse * 150.0;
        final double VITESSE_MAX = 300;

        // Mise à jour de la vitesse en fonction de l'accélération et de l'amortissement
        vitesse += diffTemps * (acceleration + vitesseAmortissement);

        // Vérification et ajustement de la vitesse par rapport à la limite de vitesse
        if (vitesse > VITESSE_MAX) {
            vitesse = VITESSE_MAX;
        } else if (vitesse < -VITESSE_MAX) {
            vitesse = -VITESSE_MAX;
        }

        // Vérification de changement de direction de la vitesse
        int nouveauSigneVitesse = (vitesse > 0) ? 1 : -1;
        if (nouveauSigneVitesse != signeVitesse) {
            vitesse = 0;
        }

        return vitesse;
    }


    public boolean morte(GraphicsContext context, BarreDeVie barreDeVie) {

        boolean finDePartie = false;
        if (barreDeVie.tailleX == 0) {
            context.setFill(Color.RED);

            context.fillText("Fin de la partie", Main.LARGEUR_CANVAS / 4, Main.HAUTEUR_CANVAS / 2);

            if (!estMorte) {
                estMorte = true;
                tempsDeMort = tempsTotal;
            } else if (tempsTotal - tempsDeMort >= 3) {
                finDePartie = true;
            }
        }
        return finDePartie;
    }

    public void clignotementCharlotte(ArrayList<PoissonsEnnemi> ennemi, BarreDeVie barreDeVie) {

        for (PoissonsEnnemi poissonsEnnemi : ennemi) {
            if (enCollision(poissonsEnnemi) && !charlotteInvincible) {
                tempsDeImpact = tempsTotal;
                charlotteInvincible = true;
                if (barreDeVie.tailleX > 0) {
                    barreDeVie.setTailleX(barreDeVie.tailleX - barreDeVie.getPtsDeViesPerdu());
                }
            }
        }

        if (charlotteInvincible && ((tempsTotal - tempsDeImpact <= 0.25) ||
                (tempsTotal - tempsDeImpact >= 0.50 && tempsTotal - tempsDeImpact <= 0.75)
                || (tempsTotal - tempsDeImpact >= 1 && tempsTotal - tempsDeImpact <= 1.25)
                || tempsTotal - tempsDeImpact >= 1.50 && tempsTotal - tempsDeImpact <= 1.75)) {
            image = null;
        }
        if (tempsTotal - tempsDeImpact > 2) {
            charlotteInvincible = false;
        }

        if (image != null && charlotteInvincible) {
            image = (new Image("charlotte-outch.png"));
        }

    }

}



