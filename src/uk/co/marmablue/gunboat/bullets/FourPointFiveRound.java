package uk.co.marmablue.gunboat.bullets;

import uk.co.marmablue.gunboat.gui.GameRenderer;

public class FourPointFiveRound extends Bullet {

    public FourPointFiveRound(double xPos, double yPos, double zPos, double heading, double speed, double range, GameRenderer renderer, boolean firedByPlayer) {
        super(renderer.getModelLoader().getModel("FourPointFiveRound"), xPos, yPos, zPos, heading, speed, 10, range, renderer, firedByPlayer);
    }

}
