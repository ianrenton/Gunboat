package uk.co.marmablue.gunboat.bullets;

import uk.co.marmablue.gunboat.gui.GameRenderer;

public class FifteenInchRound extends Bullet {

    public FifteenInchRound(double xPos, double yPos, double zPos, double heading, double speed, double range, GameRenderer renderer, boolean firedByPlayer) {
        super(renderer.getModelLoader().getModel("FifteenInchRound"), xPos, yPos, zPos, heading, speed, 100, range, renderer, firedByPlayer);
    }
}
