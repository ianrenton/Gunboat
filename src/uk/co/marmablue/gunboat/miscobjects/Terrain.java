package uk.co.marmablue.gunboat.miscobjects;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import uk.co.marmablue.gunboat.model.Model;
import uk.co.marmablue.gunboat.texture.TextureReader;

public class Terrain extends RenderableGameObject {
    private Image scaledImage;
    private double imgScaleX;
    private double imgScaleY;

    public Terrain(Model model) {
        super(model, null, 0, 0, 0, 0, 0);
        try {
            BufferedImage bitmap = ImageIO.read(TextureReader.getResourceAsStream("maps/" + model.getName() + ".png"));
            scaledImage = bitmap.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imgScaleX = bitmap.getWidth() / 100.0;
            imgScaleY = bitmap.getHeight() / 100.0;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Image getImage() {
        return scaledImage;
    }

    public double getImgScaleX() {
        return imgScaleX;
    }

    public double getImgScaleY() {
        return imgScaleY;
    }

    @Override
    public boolean hasExpired() {
        return false;
    }
}
