package uk.co.marmablue.gunboat.bullets;

import java.util.ArrayList;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.miscobjects.RenderableGameObject;
import uk.co.marmablue.gunboat.model.Model;
import uk.co.marmablue.gunboat.ships.Ship;

public abstract class Bullet extends RenderableGameObject {

    double damage = 0;
    private boolean removable = false;
    GameRenderer renderer;
    private boolean allyControlled;
    private boolean readyForCollisionCheck = false;
    private double expiresAfter = 0;

    public Bullet(Model model, double xPos, double yPos, double zPos, double heading, double speed, double damage, double range, GameRenderer renderer, boolean allyControlled) {
        super(model, null, xPos, yPos, zPos, heading, speed);
        this.damage = damage;
        this.renderer = renderer;
        this.allyControlled = allyControlled;
        readyForCollisionCheck = true;
        expiresAfter = range/speed*iterationRateHz;
    }

    @Override
    public void iterate() {
        super.iterate();
        if (readyForCollisionCheck) {
            if (allyControlled) {
                removable = checkForCollisions(renderer.getEnemyShips());
            } else {
                removable = checkForCollisions(renderer.getAllyShips());
            }
        }
    }

    private boolean checkForCollisions(ArrayList<Ship> ships) {
        for (Ship ship : ships) {
            if (ship.checkForCollision(this)) {
                ship.damage(damage);
                return true;
            }
        }
        return false;
    }

    public void setRemovable() {
        removable = true;
    }

    public boolean hasExpired() {
        if (removable || isOffScreen() || (iteration>expiresAfter)) {
            this.stop();
            return true;
        } else return false;
    }

    public boolean isAllyControlled() {
        return allyControlled;
    }
}
