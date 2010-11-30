/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.marmablue.gunboat.miscobjects;

import com.sun.opengl.util.j2d.Overlay;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import uk.co.marmablue.gunboat.crates.Crate;
import uk.co.marmablue.gunboat.gui.GameRenderer;
import uk.co.marmablue.gunboat.ships.Ship;
import uk.co.marmablue.gunboat.texture.TextureReader;

/**
 *
 * @author Ian
 */
public class HUD {

    private boolean fullMapAsRadar = true;
    private boolean show = true;
    private Overlay hudOverlay;
    private Graphics2D hudGraphics;
    private GameRenderer renderer;
    private HashMap hudImages = new HashMap();

    public HUD(GameRenderer renderer, Overlay hudOverlay) {
        this.renderer = renderer;
        this.hudOverlay = hudOverlay;
        hudGraphics = hudOverlay.createGraphics();
        loadImages();
    }

    private void loadImages() {
        File dir = new File("hud/");
        String[] imageFiles = dir.list(new FilenameFilter() {

            @Override
            public boolean accept(File file, String name) {
                return (name.endsWith(".png"));
            }
        });
        for (int i = 0; i < imageFiles.length; i++) {
            try {
                hudImages.put(imageFiles[i], ImageIO.read(TextureReader.getResourceAsStream("hud/" + imageFiles[i])));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public BufferedImage getImage(String imageName) {
        return (BufferedImage) hudImages.get(imageName);
    }

    public void toggle() {
        show = !show;
    }

    public void changeRadarMode() {
        fullMapAsRadar = !fullMapAsRadar;
    }

    public void render(int windowWidth) {
        if (show) {
            hudGraphics.drawImage((BufferedImage)hudImages.get("HUDTopLeft.png"), null, 0, 0);
            hudGraphics.drawImage((BufferedImage)hudImages.get("HUDTopRight.png"), null, windowWidth - 270, 0);

            renderPrimaryWeapons(hudGraphics);

            if (fullMapAsRadar) {
                renderFullMapRadar(hudGraphics, windowWidth);
            } else {
                renderCloseUpRadar(hudGraphics, windowWidth);
            }
            renderHeading(hudGraphics, windowWidth);
            renderSpeedMeter(hudGraphics, windowWidth);

            hudOverlay.drawAll();
            hudOverlay.markDirty(0, 0, 248, 89);
            hudOverlay.markDirty(windowWidth - 270, 0, 270, 128);
        }
    }

    private void renderCloseUpRadar(Graphics2D hudGraphics, int windowWidth) {
        hudGraphics.setColor(Color.BLACK);
        hudGraphics.fillOval(windowWidth - 110, 10, 100, 100);
        hudGraphics.setColor(Color.WHITE);
        hudGraphics.drawLine(windowWidth - 60, 10, windowWidth - 60, 110);
        hudGraphics.drawLine(windowWidth - 10, 60, windowWidth - 110, 60);
        hudGraphics.setColor(Color.CYAN);
        for (Crate crate : renderer.getCrates()) {
            if ((renderer.getPlayerShip().getDistanceTo(crate) < 900) && (!crate.hasExpired())) {
                double[] relPos = renderer.getPlayerShip().getRelativePositionOf(crate);
                int targetX = windowWidth - 60 + (int) (relPos[1] / 20);
                int targetY = 60 - (int) (relPos[0] / 20);
                hudGraphics.fillRect(targetX - 1, targetY - 1, 2, 2);
            }
        }
        hudGraphics.setColor(Color.YELLOW);
        for (Ship ship : renderer.getAllyShips()) {
            if ((renderer.getPlayerShip().getDistanceTo(ship) < 900) && (!ship.isDestroyed())) {
                double[] relPos = renderer.getPlayerShip().getRelativePositionOf(ship);
                int targetX = windowWidth - 60 + (int) (relPos[1] / 20);
                int targetY = 60 - (int) (relPos[0] / 20);
                hudGraphics.fillRect(targetX - 2, targetY - 2, 4, 4);
            }
        }
        hudGraphics.setColor(Color.RED);
        for (Ship ship : renderer.getEnemyShips()) {
            if ((renderer.getPlayerShip().getDistanceTo(ship) < 900) && (!ship.isDestroyed())) {
                double[] relPos = renderer.getPlayerShip().getRelativePositionOf(ship);
                int targetX = windowWidth - 60 + (int) (relPos[1] / 20);
                int targetY = 60 - (int) (relPos[0] / 20);
                hudGraphics.fillRect(targetX - 2, targetY - 2, 4, 4);
            }
        }
        hudGraphics.setColor(Color.GREEN);
        hudGraphics.fillRect(windowWidth - 62, 58, 4, 4);
    }

    private void renderFullMapRadar(Graphics2D hudGraphics, int windowWidth) {
        double imgScaleX = renderer.getTerrain().getImgScaleX();
        double imgScaleY = renderer.getTerrain().getImgScaleY();
        hudGraphics.setColor(Color.BLACK);
        hudGraphics.drawRect(windowWidth - 111, 9, 102, 102);
        hudGraphics.drawImage(renderer.getTerrain().getImage(), windowWidth - 110, 10, null);

        for (Ship ship : renderer.getAllyShips()) {
            drawShipOnRadar(ship, hudGraphics, windowWidth, imgScaleX, imgScaleY, Color.YELLOW);
        }
        for (Ship ship : renderer.getEnemyShips()) {
            drawShipOnRadar(ship, hudGraphics, windowWidth, imgScaleX, imgScaleY, Color.RED);
        }
        for (Crate crate : renderer.getCrates()) {
            drawCrateOnRadar(crate, hudGraphics, windowWidth, imgScaleX, imgScaleY);
        }
        drawShipOnRadar(renderer.getPlayerShip(), hudGraphics, windowWidth, imgScaleX, imgScaleY, Color.GREEN);
    }

    private void drawShipOnRadar(Ship ship, Graphics2D hudGraphics, int windowWidth, double imgScaleX, double imgScaleY, Color color) {
        hudGraphics.setColor(color);
        int targetX = windowWidth - 60 + (int) (ship.getXPos() / 20 / imgScaleX);
        int targetY = 60 - (int) (ship.getYPos() / 20 / imgScaleY);
        int dotSize = 2;
        if (ship.getLength() < 40) {
            dotSize = 1;
        } else if (ship.getLength() > 100) {
            dotSize = 4;
        }
        hudGraphics.fillRect(targetX - dotSize / 2, targetY - dotSize / 2, dotSize, dotSize);
    }

    private void drawCrateOnRadar(Crate crate, Graphics2D hudGraphics, int windowWidth, double imgScaleX, double imgScaleY) {
        hudGraphics.setColor(Color.BLUE);
        int targetX = windowWidth - 60 + (int) (crate.getXPos() / 20 / imgScaleX);
        int targetY = 60 - (int) (crate.getYPos() / 20 / imgScaleY);
        hudGraphics.fillRect(targetX, targetY, 1, 1);
    }

    private void renderPrimaryWeapons(Graphics2D hudGraphics) {
        for (int i=0; i<renderer.getPlayerShip().getPrimaryWeapons().size(); i++) {
            hudGraphics.drawImage((BufferedImage)hudImages.get(renderer.getPlayerShip().getPrimaryWeapons().get(i)+".png"), null, 73+(33*i), 6);
        }
        int selWeapon = renderer.getPlayerShip().getPrimaryWeaponSelected();
        hudGraphics.setColor(Color.YELLOW);
        hudGraphics.drawRect(73+(33*selWeapon), 6, 31, 31);
    }

    private void renderSpeedMeter(Graphics2D hudGraphics, int windowWidth) {
        hudGraphics.setColor(Color.BLACK);
        hudGraphics.drawRect(windowWidth - 241, 13, 11, 93);
        hudGraphics.setColor(Color.LIGHT_GRAY);
        hudGraphics.fillRect(windowWidth - 240, 14, 10, 92);

        hudGraphics.setColor(Color.RED);
        double speed = renderer.getPlayerShip().getSpeed();
        if (speed > 0) {
            hudGraphics.fillRect(windowWidth - 240, 94 - (int) (speed * 4.0), 10, (int) (speed * 4.0));
        } else if (speed < 0) {
            hudGraphics.fillRect(windowWidth - 240, 94, 10, (int) (-speed * 4.0));
        }

        hudGraphics.setColor(Color.BLACK);
        hudGraphics.drawLine(windowWidth - 240, 94, windowWidth - 230, 94);
    }

    private void renderHeading(Graphics2D hudGraphics, int windowWidth) {
        hudGraphics.setColor(Color.BLACK);
        hudGraphics.drawOval(windowWidth - 200, 30, 60, 60);
        double heading = renderer.getPlayerShip().getYawRadians();
        for (double i = 0; i < Math.PI * 2; i += Math.PI / 2) {
            double lineAngle = heading + i;
            int endX = (int) ((windowWidth - 170.0) + (30.0 * Math.sin(lineAngle)));
            int endY = (int) (60.0 - (30.0 * Math.cos(lineAngle)));
            hudGraphics.drawLine(windowWidth - 170, 60, endX, endY);

            if (i == 0) {
                int arrowheadLeftX = (int) ((windowWidth - 170.0) + (25.0 * Math.sin(Math.PI - heading - 0.2)));
                int arrowheadLeftY = (int) (60.0 + (25.0 * Math.cos(Math.PI - heading - 0.2)));
                int arrowheadRightX = (int) ((windowWidth - 170.0) + (25.0 * Math.sin(Math.PI - heading + 0.2)));
                int arrowheadRightY = (int) (60.0 + (25.0 * Math.cos(Math.PI - heading + 0.2)));
                hudGraphics.drawLine(arrowheadLeftX, arrowheadLeftY, endX, endY);
                hudGraphics.drawLine(arrowheadRightX, arrowheadRightY, endX, endY);
                int nPosX = (int) ((windowWidth - 170.0) + (40.0 * Math.sin(Math.PI - heading)));
                int nPosY = (int) (60.0 + (40.0 * Math.cos(Math.PI - heading)));
                hudGraphics.drawString("N", nPosX - 5, nPosY + 6);
            }
        }

        hudGraphics.setColor(Color.RED);
        hudGraphics.drawLine(windowWidth - 170, 30, windowWidth - 170, 60);
        hudGraphics.fillPolygon(new int[]{windowWidth - 170, windowWidth - 175, windowWidth - 165}, new int[]{30, 40, 40}, 3);
    }
}
