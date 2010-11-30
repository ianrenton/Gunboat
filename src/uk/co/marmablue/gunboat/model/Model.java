package uk.co.marmablue.gunboat.model;

import uk.co.marmablue.gunboat.gui.GameRenderer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.media.opengl.GL;

/**
 *
 * @author Ian
 */
public class Model {

    private String name;
    private int polycount;
    private ArrayList<Quad> polys = new ArrayList<Quad>();
    private double length;
    private double width;

    public Model(File f, GameRenderer renderer) {
        try {
            name = f.getName().replaceFirst("\\.mod", "");
            BufferedReader r = new BufferedReader(new FileReader(f));
            String firstline = r.readLine();
            polycount = Integer.parseInt(firstline.substring(10));
            for (int i=0; i<polycount; i++) {
                int textureID = renderer.getTextureLoader().getTextureID(readTextureName(r));
                polys.add(new Quad(new Vertex[]{readVertex(r), readVertex(r), readVertex(r), readVertex(r)}, textureID));
            }
            storeSizes();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Model(String name, int polycount, ArrayList<Quad> polys) {
        this.name = name;
        this.polycount = polycount;
        this.polys = polys;
        storeSizes();
    }

    private Vertex readVertex(BufferedReader r) throws IOException {
        String line;
        do {
            line = r.readLine();
        } while ((line.length()==0) || (line.startsWith("#")));
        String[] bits = line.split(" ");
        return new Vertex(Float.valueOf(bits[0]), Float.valueOf(bits[1]), Float.valueOf(bits[2]), Float.valueOf(bits[3]), Float.valueOf(bits[4]));
    }

    private String readTextureName(BufferedReader r) throws IOException {
        String line;
        do {
            line = r.readLine();
        } while ((line.length()==0) || (line.startsWith("#")));
        return line;
    }


    public void render(GL gl) {
        for (Quad poly : polys) {
            poly.render(gl);
        }
    }

    @Override
    public Model clone() {
        return new Model(name, polycount, polys);
    }

    public void storeSizes() {
        double maxX = -99999;
        double minX = 99999;
        double maxY = -99999;
        double minY = 99999;
        for (Quad poly : polys) {
            for (Vertex vertex : poly.getVertices()) {
                maxX = Math.max(maxX, vertex.x);
                minX = Math.min(minX, vertex.x);
                maxY = Math.max(maxY, vertex.y);
                minY = Math.min(minY, vertex.y);
            }
        }
        length = (maxY - minY);
        width = (maxX - minX);
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public String getName() {
        return name;
    }
}
