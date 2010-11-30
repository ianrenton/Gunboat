package uk.co.marmablue.gunboat.weapons;

import uk.co.marmablue.gunboat.bullets.Bullet;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import uk.co.marmablue.gunboat.miscobjects.RenderableGameObject;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.model.Model;
import uk.co.marmablue.gunboat.ships.Ship;

public abstract class Weapon extends RenderableGameObject {

    Timer reloadTimer = new Timer();
    int reloadTime = 0;
    boolean loaded = false;
    int time = 0;
    private boolean allyControlled;
    private double range = 0;
    private double currentTargetRange = 9999;
    private GameRenderer renderer;
    ArrayList<Ship> targets = null;
    private boolean autonomous = false;

    public Weapon(Model model, RenderableGameObject host, GameRenderer renderer, double[] mountPoint, double heading, int reloadTime, double range, boolean allyControlled, boolean autonomous) {
        super(model, host, mountPoint[0], mountPoint[1], mountPoint[2], heading, 0);
        this.reloadTime = reloadTime;
        this.allyControlled = allyControlled;
        this.range = range;
        this.renderer = renderer;
        this.autonomous = autonomous;
        startTimer();
    }

    @Override
    public void iterate() {
        // Don't iterate a weapon automatically, the host will do that.  This
        // makes sure the two positions update together, so there's no jitter.
    }

    public void iterateFromHost() {
        super.iterate();
        if (autonomous) {
            pickTarget();
        }
    }

    private void pickTarget() {
        // Try to target closest possible target, if it fails because there are no enemies left, don't bother doing anything.

        if (allyControlled) {
            targets = renderer.getEnemyShips();
        } else {
            targets = renderer.getAllyShips();
        }

        try {
            int closest = 999999;
            double closestDistance = 999999;
            for (int i = 0; i < targets.size(); i++) {
                if ((getDistanceTo(targets.get(i)) < closestDistance) && (!targets.get(i).isDestroyed())) {
                    closestDistance = getDistanceTo(targets.get(i));
                    closest = i;
                }
            }
            if (closestDistance != 999999) {
                aimAt(targets.get(closest));
            }
            currentTargetRange = closestDistance;
            fire(renderer);
        } catch (Exception e) {
        }
    }

    private void startTimer() {
        reloadTimer = new Timer();
        reloadTimer.scheduleAtFixedRate(new reloadTimerTask(), 0, 100);
    }

    /**
     * @return the allyControlled
     */
    public boolean isAllyControlled() {
        return allyControlled;
    }

    /**
     * @return the range
     */
    public double getRange() {
        return range;
    }

    /**
     * Reload reloadTimer thread
     */
    private class reloadTimerTask extends TimerTask {

        int count = 0;

        @Override
        public void run() {
            time = count++;
            if (count > reloadTime) {
                loaded = true;
            }
        }
    }

    public abstract String getName();

    public void aimAt(RenderableGameObject target) {
        turn(-getRelativeAngleTo(target)/Math.PI*180);
    }

    public void fire(GameRenderer renderer) {
        // If loaded and host is alive and (within range or not in auto mode)...
        if ((loaded) && (!((Ship)getHost()).isDestroyed()) && ((currentTargetRange <= getRange()) || (!autonomous))) {
            if (allyControlled) {
                makeBullets(renderer.getAllyBullets(), renderer);
            } else {
                makeBullets(renderer.getEnemyBullets(), renderer);
            }
            loaded = false;
            reloadTimer.cancel();
            startTimer();
        }
    }

    abstract void makeBullets(ArrayList<Bullet> bulletList, GameRenderer renderer);

    @Override
    public boolean hasExpired() {
        return getHost().hasExpired();
    }
}
