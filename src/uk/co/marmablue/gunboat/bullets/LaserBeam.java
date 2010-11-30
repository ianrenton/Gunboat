package uk.co.marmablue.gunboat.bullets;

import uk.co.marmablue.gunboat.gui.GameRenderer;

public class LaserBeam extends Bullet {

    public LaserBeam(double xPos, double yPos, double zPos, double heading, double speed, double range, GameRenderer renderer, boolean firedByPlayer) {
        super(renderer.getModelLoader().getModel("LaserBeam"), xPos, yPos, zPos, heading, speed, 50, range, renderer, firedByPlayer);
    }
}
