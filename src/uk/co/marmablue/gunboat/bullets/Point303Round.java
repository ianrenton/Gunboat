package uk.co.marmablue.gunboat.bullets;

import uk.co.marmablue.gunboat.gui.GameRenderer;

public class Point303Round extends Bullet {

    public Point303Round(double xPos, double yPos, double zPos, double heading, double speed, double range, GameRenderer renderer, boolean firedByPlayer) {
        super(renderer.getModelLoader().getModel("Point303Round"), xPos, yPos, zPos, heading, speed, 3, range, renderer, firedByPlayer);
    }

}
