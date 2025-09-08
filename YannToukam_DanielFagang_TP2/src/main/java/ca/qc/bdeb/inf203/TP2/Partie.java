package ca.qc.bdeb.inf203.TP2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;

public class Partie {

    private static final Random gen = new Random();

    private final ArrayList<ElementsGraphiques> elements;

    private static final int NOMBRE_DECORS = 200;

    private Color couleurFond;

    private static Charlotte charlotte;

    private final Baril baril;

    private final BarreDeVie barreDeVie;

    private static double tempsApparitionEnnemis;

    private static double tempsEcouleDepuisDebut;

    private static double momentChangementNiveau;

    private static double tempsAuTir;

    private static String projectile = "etoile";

    private static final ArrayList<Projectiles> tabProjectiles = new ArrayList<>();

    private static final ArrayList<String> choixProjectiles = new ArrayList<>();

    public static ArrayList<BoiteSardine> boitesSardines = new ArrayList<>();

    private static ArrayList<PoissonsEnnemi> tabEnnemis;

    private static double momentApparition;

    private static boolean vaguePeutApparaitre = true;

    private static boolean aToucheBaril = false;

    public BarreDeVie getBarreDeVie() {
        return barreDeVie;
    }

    public Color getCouleurFond() {
        return couleurFond;
    }

    public void setCouleurFond(Color couleurFond) {
        this.couleurFond = couleurFond;
    }

    public Partie(int numeroNiveau, double anciennePosCharlotte) {

        elements = new ArrayList<>();

        couleurFond = nouvelleCouleurFond();

        choixProjectiles.add("etoile");
        choixProjectiles.add("hippocampe");
        choixProjectiles.add("sardines");

        elements.add(charlotte = new Charlotte());

        elements.add(baril = new Baril());

        elements.add(barreDeVie = new BarreDeVie());

        ArrayList<Decor> tabDecors = new ArrayList<>();

        tabDecors.add(new Decor(0));

        for (int i = 1; i <= NOMBRE_DECORS; i++) {
            tabDecors.add(new Decor(tabDecors.get(i - 1).posX));
        }
        elements.addAll(tabDecors);

        tempsApparitionEnnemis = 0.75 + Math.sqrt(numeroNiveau);

        if (numeroNiveau != 1) {
            charlotte.setPosY(anciennePosCharlotte);
        }
        tempsEcouleDepuisDebut = 0;
        tempsAuTir = 0;

    }

    public boolean finie(GraphicsContext context, BarreDeVie barreDeVie) {

        return charlotte.morte(context, barreDeVie);
    }

    private void logiqueSardines() {

        if (!boitesSardines.isEmpty()) {
            for (var ennemi : tabEnnemis) {
                for (var boite : boitesSardines) {
                    if (ennemi.posX > boite.posX) {

                        boite.calculerForces(ennemi);
                    }
                }
            }

        }

    }

    public void update(double diff, int numeroNiveau) {
        tempsEcouleDepuisDebut += diff;

        vagueEnnemis(numeroNiveau);

        for (var element : elements) {
            element.update(diff);
        }
        tuerEnnemis();
        logiqueRecuperationBaril();
        charlotte.clignotementCharlotte(tabEnnemis, barreDeVie);
        logiqueSardines();
        Camera.getCamera().update(charlotte);


    }

    private void logiqueRecuperationBaril() {
        if (charlotte.enCollision(baril) && !aToucheBaril) {
            barilRecupere();
            aToucheBaril = true;
        }
    }

    private void barilRecupere() {

        String precedent = projectile;
        choixProjectiles.remove(precedent);
        baril.setImage(new Image("baril-ouvert.png"));
        projectile = choixProjectiles.get(gen.nextInt(choixProjectiles.size()));
        choixProjectiles.add(precedent);

    }

    public void tirerProjectile() {


        final double TEMPS_ENTRE_CHAQUE_TIR = 0.5;
        boolean peutTirer = tempsEcouleDepuisDebut - tempsAuTir >= TEMPS_ENTRE_CHAQUE_TIR;
        if (peutTirer) {
            switch (projectile) {
                case "etoile" -> {
                    var etoile = new EtoilesDeMer(charlotte);
                    tabProjectiles.add(etoile);
                    elements.add(etoile);
                }
                case "hippocampe" -> {
                    int NOMBRE_PROJECTILE = 3;
                    for (int i = 0; i < NOMBRE_PROJECTILE; i++) {
                        var hippo = new Hippocampe(charlotte);
                        tabProjectiles.add(hippo);
                        elements.add(hippo);
                    }
                }
                case "sardines" -> {
                    BoiteSardine sardine = new BoiteSardine(charlotte);
                    tabProjectiles.add(sardine);
                    boitesSardines.add(sardine);
                    elements.add(sardine);
                }
            }
            tempsAuTir = tempsEcouleDepuisDebut;
        }
    }

    public void dessiner(GraphicsContext context, int numeroNiveau, boolean checkerDebugger) {

        for (var element : elements) {
            element.dessiner(context);
        }
        debbuger(context, checkerDebugger);
        charlotte.morte(context, barreDeVie);
        ImageProjectileActuel(context);
        affichageTexteDesNiveau(context, numeroNiveau);

    }

    private void vagueEnnemis(int numeroNiveau) {

        if (vaguePeutApparaitre) {

            tabEnnemis = new ArrayList<>();

            final int NOMBRE_ENNEMIS = 5;
            int nbEnnemisTemp = gen.nextInt(1, NOMBRE_ENNEMIS + 1);

            for (int i = 0; i < nbEnnemisTemp; i++) {
                tabEnnemis.add(new PoissonsEnnemi(numeroNiveau));
            }
            elements.addAll(tabEnnemis);
            vaguePeutApparaitre = false;
            momentApparition = tempsEcouleDepuisDebut;
        }

        if (tempsEcouleDepuisDebut - momentApparition >= tempsApparitionEnnemis) {
            vaguePeutApparaitre = true;
        }

        for (int i = NOMBRE_DECORS + 4; i < elements.size(); i++) {
            if (elements.get(i).posX + elements.get(i).tailleX <= Camera.getCamera().getPosX()) {
                elements.remove(elements.get(i));
            }
        }
    }

    private void tuerEnnemis() {

        for (int i = 0; i < tabProjectiles.size() && !tabProjectiles.isEmpty(); i++) {

            for (int j = 0; j < tabEnnemis.size() && !tabEnnemis.isEmpty(); j++) {

                if (tabProjectiles.get(i).enCollision(tabEnnemis.get(j))) {

                    tabEnnemis.get(j).setImage(null);
                    tabEnnemis.remove(tabEnnemis.get(j));
                }
            }

            if (tabProjectiles.get(i).posX >= Camera.getCamera().getPosX() + Main.LARGEUR_CANVAS) {
                tabProjectiles.remove(tabProjectiles.get(i));
            }
        }
    }

    private void affichageTexteDesNiveau(GraphicsContext context, int numeroNiveau) {

        final int TEMPS_APPARITION_TEXTE_NIVEAU = 4;
        if (tempsEcouleDepuisDebut - momentChangementNiveau <= TEMPS_APPARITION_TEXTE_NIVEAU || tempsEcouleDepuisDebut < TEMPS_APPARITION_TEXTE_NIVEAU) {
            var textNiveau = new Text("Niveau " + numeroNiveau);
            textNiveau.setFont(Font.font("Arial", 80));

            context.setFill(Color.WHITE);
            context.fillText(textNiveau.getText(), Main.LARGEUR_CANVAS / 3.2, Main.HAUTEUR_CANVAS / 2);
        }

    }

    public Color nouvelleCouleurFond() {
        couleurFond = Color.hsb(gen.nextInt(190, 271), 0.84, 1);
        return couleurFond;
    }

    public boolean estCompletee() {
        boolean finiNiveau = charlotte.posX + charlotte.tailleX >= Main.LARGEUR_MONDE;
        if (finiNiveau) {
            changerNiveau();
        }
        return finiNiveau;
    }

    private void changerNiveau() {

        final int POSITION_CHARLOTTE_DEBUTNIVEAU = 10;
        aToucheBaril = false;
        momentChangementNiveau = tempsEcouleDepuisDebut;
        baril.setImage(new Image("baril.png"));
        charlotte.setPosX(POSITION_CHARLOTTE_DEBUTNIVEAU);
        Camera.getCamera().setPosX(0);

    }

    private void ImageProjectileActuel(GraphicsContext context) {
        final int TAILLEX_IconeProjectile = 50;
        final int TAILLEY_IconeProjectile = 50;
        final int POSITION_ICONE = 190;
        context.drawImage(new Image(projectile + ".png"), POSITION_ICONE,
                barreDeVie.posY - 5, TAILLEX_IconeProjectile, TAILLEY_IconeProjectile);
    }

    public void debbuger(GraphicsContext context, boolean checkerDebugger) {

        if (checkerDebugger) {
            for (var element : elements) {
                context.strokeRect(Camera.getCamera().transformer(element.posX), element.posY, element.tailleX, element.tailleY);
                context.setStroke(Color.YELLOW);
                var informationsJeu = new Text("NB Poissons: " + tabEnnemis.size() + "\n"
                        + "NB Projectiles: " + tabProjectiles.size() + "\n"
                        + "Position Charlotte: " + 100 * charlotte.posX / Main.LARGEUR_MONDE + "%");
                informationsJeu.setFont(Font.font(1));

                context.setFont(Font.font(20));
                context.fillText(informationsJeu.getText(), barreDeVie.posX, barreDeVie.posY + barreDeVie.tailleY + 30);
            }
        }
    }

    public void actionDebbuger(KeyCode touche) {

        switch (touche) {
            case Q -> projectile = "etoile";
            case W -> projectile = "hippocampe";
            case E -> projectile = "sardines";
            case R -> barreDeVie.setTailleX(barreDeVie.getVieDeDepart());
            case T -> {
                charlotte.setVitesseX(0);
                charlotte.posX = Main.LARGEUR_MONDE - charlotte.tailleX;
            }
        }
    }
}


