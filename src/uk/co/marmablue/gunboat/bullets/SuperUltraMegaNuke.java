package uk.co.marmablue.gunboat.bullets;

import java.util.ArrayList;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.ships.Ship;

public class SuperUltraMegaNuke extends Bullet {

    private ArrayList<Ship> targets;

    public SuperUltraMegaNuke(double xPos, double yPos, double zPos, double heading, double speed, double range, GameRenderer renderer, boolean firedByPlayer) {
        super(renderer.getModelLoader().getModel("AntiShipMissile"), xPos, yPos, zPos, heading, speed, 100000, range, renderer, firedByPlayer);
    }

    @Override
    public void iterate() {
        try {
            if (isAllyControlled()) {
                targets = renderer.getEnemyShips();
            } else {
                targets = renderer.getAllyShips();
            }
        } catch (NullPointerException ex) {
            this.setRemovable();
        }

        try {
            int closest = 999999;
            double closestDistance = 999999;
            for (int i = 0; i < targets.size(); i++) {
                // If target is closer than previous ones, and the target isn't destroyed already...
                if ((getDistanceTo(targets.get(i)) < closestDistance) && (!targets.get(i).isDestroyed())) {
                    closestDistance = getDistanceTo(targets.get(i));
                    closest = i;
                }
            }

            if (closestDistance != 999999) {
                double angle = -getRelativeAngleTo(targets.get(closest)) / Math.PI * 180;
                if (angle < -3) {
                    turn(-3);
                } else if (angle > 3) {
                    turn(3);
                }
            }
        } catch (Exception e) {
        }

        super.iterate();
    }
}
