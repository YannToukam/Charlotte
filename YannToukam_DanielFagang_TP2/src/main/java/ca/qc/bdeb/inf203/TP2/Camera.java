package ca.qc.bdeb.inf203.TP2;

public class Camera {
    private static Camera camera = null;

    private double posX;

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosX() {
        return posX;
    }

    private Camera() {

    }
    public static Camera getCamera() {

        if (camera == null) {
            camera = new Camera();
        }
        return camera;
    }


    public void update(Charlotte charlotte) {

        if (charlotte.posX >= (((Main.LARGEUR_CANVAS+posX)-posX) / 5)+posX
                && posX + Main.LARGEUR_CANVAS <= Main.LARGEUR_MONDE && charlotte.vitesseX >= 0) {
            posX = charlotte.posX - Main.LARGEUR_CANVAS / 5;
        }
    }

    public double transformer(double x) {
        return x - posX;
    }
}
