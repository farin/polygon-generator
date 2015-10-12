package com.jcloisterzone.polygen;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import com.jcloisterzone.ui.ImmutablePoint;


public class PolygonEditor extends JLabel {
    private static final long serialVersionUID = -7132962466963671431L;

    public static final int NORMALIZED_SIZE = 1000;
    public static final int EDITOR_SIZE = 400;

    private PolygonGenerator app;

    public PolygonEditor(final PolygonGenerator app) {
        super();
        this.app = app;
        setOpaque(true);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int x = (int) (e.getX()*(NORMALIZED_SIZE/(double)EDITOR_SIZE));
                    int y = (int) (e.getY()*(NORMALIZED_SIZE/(double)EDITOR_SIZE));
                    app.getPointPanel().addPoint(new ImmutablePoint(x, y));
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    app.getPointPanel().removeLastPoint();
                }
            }
        });
    }


    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.RED);
        int idx = 0;
        for (ImmutablePoint p : app.getPointPanel().getPoints()) {
            int x = (int) (p.getX() * EDITOR_SIZE/NORMALIZED_SIZE);
            int y = (int) (p.getY() * EDITOR_SIZE/NORMALIZED_SIZE);
            g.setColor((idx % 2) == 0 ? Color.RED : Color.BLUE);
            g.fillRect(x-2,y-2,5,5);
        }
    }

}