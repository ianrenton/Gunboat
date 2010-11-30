package uk.co.marmablue.gunboat.miscobjects;

import javax.media.opengl.GL;
import uk.co.marmablue.gunboat.ships.Ship;

/**
 *
 * @author Ian
 */
public class ShipHealthBar {
    
    Ship ship;

    public ShipHealthBar(Ship ship) {
        this.ship = ship;
    }

    public void render(GL gl) {
        double healthFraction = ship.getHealth() / ship.getMaxHealth();
        double red=0; double green=0; double blue=0;
        if (healthFraction > 0.5) {
            green = 1;
            red = (1-healthFraction)*2;
        } else {
            green = healthFraction*2;
            red = 1f;
        }

        double shipX = ship.getXPos();
        double shipY = ship.getYPos();
        double shipZ = ship.getZPos();
        double shipYaw = ship.getYawRadians();
        double x, y;
        double radiansToSkip = (1-healthFraction)*java.lang.Math.PI;

        // Right half
        gl.glBegin(GL.GL_POLYGON);
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        gl.glColor3d(red, green, blue);
        for (double a = radiansToSkip-shipYaw; a < java.lang.Math.PI-shipYaw; a = a + 0.01) {
            y = 25 * java.lang.Math.cos(a);
            x = 25 * java.lang.Math.sin(a);
            gl.glVertex3d(shipX+x, shipY+y, shipZ+0.05);
        }
        gl.glVertex3d(shipX, shipY, shipZ+0.05);
        gl.glEnd();

        // Left half
        gl.glBegin(GL.GL_POLYGON);
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        gl.glColor3d(red, green, blue);
        for (double a = java.lang.Math.PI-shipYaw; a < (2*java.lang.Math.PI)-shipYaw-radiansToSkip; a = a + 0.01) {
            y = 25 * java.lang.Math.cos(a);
            x = 25 * java.lang.Math.sin(a);
            gl.glVertex3d(shipX+x, shipY+y, shipZ+0.05);
        }
        gl.glVertex3d(shipX, shipY, shipZ+0.05);
        gl.glEnd();

        // Central blank circle
        gl.glBegin(GL.GL_POLYGON);
        gl.glColor3f(0.4F, 0.46F, 0.63F);
        for (double a = 0; a < 2*java.lang.Math.PI; a = a + 0.01) {
            x = 23.5 * java.lang.Math.cos(a);
            y = 23.5 * java.lang.Math.sin(a);
            gl.glVertex3d(shipX+x, shipY+y, shipZ+0.1);
        }
        gl.glEnd();

        }
}
