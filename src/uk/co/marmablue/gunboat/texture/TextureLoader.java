/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.marmablue.gunboat.texture;

import uk.co.marmablue.gunboat.texture.TextureReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author Ian
 */
public class TextureLoader {

    private HashMap textures = new HashMap();
    private GL gl;
    private GLU glu;

    public TextureLoader(GL gl, GLU glu) {
        this.gl = gl;
        this.glu = glu;
        loadTextures();
    }

    public void loadTextures() {
        File dir = new File("textures/");
        String[] textureFiles = dir.list(new FilenameFilter() {

            @Override
            public boolean accept(File file, String name) {
                return (name.endsWith(".png"));
            }
        });
        for (int i = 0; i < textureFiles.length; i++) {
            int tmpTextureRef = genTexture(gl);
            textures.put(textureFiles[i], Integer.valueOf(tmpTextureRef));
            gl.glBindTexture(GL.GL_TEXTURE_2D, tmpTextureRef);
            Texture loadedTexture = null;
            try {
                loadedTexture = uk.co.marmablue.gunboat.texture.TextureReader.readTexture("textures/" + textureFiles[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            makeRGBTexture(gl, glu, loadedTexture, GL.GL_TEXTURE_2D, false);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        }

    }

    public int getTextureID(String textureName) {
        return (Integer) textures.get(textureName+".png");
    }

    private static void makeRGBTexture(GL gl, GLU glu, Texture img, int target, boolean mipmapped) {
        if (mipmapped) {
            glu.gluBuild2DMipmaps(target, GL.GL_RGB8, img.getWidth(), img.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
        } else {
            gl.glTexImage2D(target, 0, GL.GL_RGB, img.getWidth(), img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
        }
    }

    private static int genTexture(GL gl) {
        final int[] tmp = new int[1];
        gl.glGenTextures(1, tmp, 0);
        return tmp[0];
    }
}
