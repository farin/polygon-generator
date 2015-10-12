package com.jcloisterzone.polygen;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.swing.JLabel;

import com.jcloisterzone.ui.ImmutablePoint;

/*
 * Created on 7.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author Roman Krejcik
 */
public class Preview extends JLabel {

    private PolygonGenerator app;

    private static final AlphaComposite AREA_ALPHA_COMPOSITE =
        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .6f);

    public Preview(PolygonGenerator app) {
        super();
        this.app = app;
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (app.getPointPanel().getPoints().size() > 2) {
            Graphics2D g2 = (Graphics2D) g;
            Polygon poly = new Polygon();
            for (ImmutablePoint p : app.getPointPanel().getPoints()) {
                int x = (int) (p.getX() * PolygonGenerator.PREVIEW_SIZE/(double)PolygonEditor.NORMALIZED_SIZE);
                int y = (int) (p.getY() * PolygonGenerator.PREVIEW_SIZE/(double)PolygonEditor.NORMALIZED_SIZE);
                poly.addPoint(x, y);
            }
            g2.setColor(Color.RED);
            g2.setComposite(AREA_ALPHA_COMPOSITE);
            g2.fill(poly);
        }
    }
}