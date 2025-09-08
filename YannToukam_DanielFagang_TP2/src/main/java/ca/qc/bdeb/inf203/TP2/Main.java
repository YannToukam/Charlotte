package ca.qc.bdeb.inf203.TP2;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Partie partie;

    private static int numeroNiveau = 1;

    private static boolean activerDebbugger = false;

    private Stage stage;
    public static final double LARGEUR_CANVAS = 900;

    public static final double HAUTEUR_CANVAS = 520;
    public static final double LARGEUR_MONDE = 8 * LARGEUR_CANVAS;

    private Scene sceneAccueil() {

        var root = new StackPane();

        var scene = new Scene(root, LARGEUR_CANVAS, 672);

        var hbox = new HBox();

        var btnJouer = new Button("Jouer!");

        var btnInfo = new Button("Infos");

        var logo = new ImageView("logo.png");


        BackgroundFill background_fill = new BackgroundFill(Color.web("#2A7FFF"),
                CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(background_fill);
        root.setBackground(background);


        btnJouer.setOnAction((e) -> stage.setScene(sceneJeu()));
        btnInfo.setOnAction((e) -> stage.setScene(sceneInfo()));

        hbox.getChildren().addAll(btnJouer, btnInfo);
        hbox.setSpacing(25);
        hbox.setPadding(new Insets(15));
        hbox.setAlignment(Pos.BOTTOM_CENTER);

        root.getChildren().add(logo);
        root.getChildren().add(hbox);
        root.setAlignment(Pos.BOTTOM_CENTER);

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });

        stage.setScene(scene);
        stage.show();
        return scene;
    }

    private Scene sceneInfo() {

        var root = new VBox();
        var scene = new Scene(root, LARGEUR_CANVAS, HAUTEUR_CANVAS);

        BackgroundFill background_fill = new BackgroundFill(Color.PINK,
                CornerRadii.EMPTY, Insets.EMPTY);

        Background background = new Background(background_fill);
        root.setBackground(background);

        var titre = new Text("Charlotte la Barbotte");
        titre.setFont(Font.font(60));
        root.getChildren().add(titre);
        root.setAlignment(Pos.TOP_CENTER);

        Random gen = new Random();
        int quelPoisson= gen.nextInt(1,6);
        var imgPoisson = new ImageView("poisson"+quelPoisson+".png");
        root.getChildren().add(imgPoisson);
        root.setSpacing(30);

        var nomYann = new Text("Par Yann Toukam");
        nomYann.setFont(Font.font(35));
        root.getChildren().add(nomYann);

        var nomDaniel = new Text("et Daniel Fagang");
        nomDaniel.setFont(Font.font(35));
        root.getChildren().add(nomDaniel);

        var remise = new Text("Travail remis à Nicolas Hurtubise et Georges Côté. Graphisme adaptés de https://game-icons.net/ et de" +
                " https://openclipart.org/. Développé dans le cadre du cours 420-203-RE - Développement de programmes" +
                " dans un environnement graphique, au Collège de Bois-de-Boulogne.");
        remise.setWrappingWidth(680);
        remise.setFont(Font.font(12));
        root.getChildren().add(remise);

        var btnRetour = new Button("Retour");
        btnRetour.setOnAction((e) -> stage.setScene(sceneAccueil()));
        root.getChildren().add(btnRetour);

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.setScene(sceneAccueil());
            }
        });

        stage.setScene(scene);
        stage.show();

        return scene;
    }

    private Scene sceneJeu() {

        var root = new StackPane();

        partie = new Partie(numeroNiveau, Main.HAUTEUR_CANVAS / 2);

        var scene = new Scene(root, LARGEUR_CANVAS, HAUTEUR_CANVAS);

        var canvas = new Canvas(LARGEUR_CANVAS, HAUTEUR_CANVAS);

        var context = canvas.getGraphicsContext2D();

        Font font = Font.font("Arial", 80);

        context.setFont(font);

        root.getChildren().add(canvas);

        stage.setScene(scene);

        stage.show();

        AnimationTimer timer = new AnimationTimer() {

            private long dernierTemps = 0;

            @Override
            public void handle(long now) {
                if (dernierTemps == 0) {
                    dernierTemps = now;
                }
                double diffTemps = (now - dernierTemps) * 1e-9;

                dernierTemps = now;

                partie.update(diffTemps, numeroNiveau);

                logiqueDuJeu(context);

                partie.dessiner(context, numeroNiveau, activerDebbugger);

            }
        };

        timer.start();

        controleDeScene(scene, timer, context);

        scene.setOnKeyReleased((e) -> Input.setKeyPressed(e.getCode(), false));

        return scene;
    }

    private void logiqueDuJeu(GraphicsContext context) {

        if (partie.estCompletee()) {
            numeroNiveau += 1;
            partie.setCouleurFond(partie.nouvelleCouleurFond());
        }

        context.setFill(partie.getCouleurFond());
        context.fillRect(Camera.getCamera().transformer(0), 0, Main.LARGEUR_MONDE, Main.HAUTEUR_CANVAS);

    }

    private void controleDeScene(Scene scene, AnimationTimer timer, GraphicsContext context) {
        scene.setOnKeyPressed((e) -> {


            if (e.getCode() == KeyCode.ESCAPE) {
                timer.stop();
                stage.setScene(sceneAccueil());

                //Passer de la scene de jeu a l'accueil

            } else if (e.getCode() == KeyCode.SPACE) {

                partie.tirerProjectile();
                //permet de tirer les projectiles

            } else if (e.getCode() == KeyCode.D) {
                activerDebbugger = !activerDebbugger; // boolean qui determine si le debugger peut etre activé ou non

                partie.debbuger(context, activerDebbugger); // recoit la valeur true ou false de la variable

            } else if (activerDebbugger && (e.getCode() == KeyCode.Q || e.getCode() == KeyCode.W ||
                    e.getCode() == KeyCode.E || e.getCode() == KeyCode.R || e.getCode() == KeyCode.T)) {

                partie.actionDebbuger(e.getCode()); //méthode qui gere toutes les actions liées au debugger

            } else if (partie.finie(context, partie.getBarreDeVie())) {
                stage.setScene(sceneAccueil());
                numeroNiveau = 1;
                partie = new Partie(numeroNiveau, Main.HAUTEUR_CANVAS / 2);
                timer.stop();
                Camera.getCamera().setPosX(0);

            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });
    }

    public void start(Stage stage){
        this.stage = stage;
        sceneAccueil();
        stage.setResizable(false);
    }
}