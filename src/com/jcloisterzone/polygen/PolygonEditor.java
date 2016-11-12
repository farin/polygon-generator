package com.jcloisterzone.polygen;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;


public class PolygonEditor extends JLabel {
    private static final long serialVersionUID = -7132962466963671431L;

    public static final int NORMALIZED_SIZE = 1000;
    public static final int EDITOR_SIZE = 400;

    private PolygonGenerator app;
    private int offsetTop;
    private int maxY;

    public PolygonEditor(final PolygonGenerator app) {
        super();
        this.app = app;
        setOpaque(true);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {          
                	System.out.println(e.getX() + ", "+e.getY());
                    int x = (int) (e.getX()*(NORMALIZED_SIZE/(double)EDITOR_SIZE));
                    int y = (int) (e.getY()*(NORMALIZED_SIZE/(double)EDITOR_SIZE)) - offsetTop;
                    y -= (offsetTop != 0 ? 5 : 0); //HACK some error is in scaling, fix it here using magic constant
                    if (e.isControlDown()) {
                        if (Math.abs(x) < 16) x = 0;
                        if (x > NORMALIZED_SIZE - 16) x = NORMALIZED_SIZE-1;
                        if (Math.abs(y) < 16) y = 0;
                        if (y < -offsetTop + 16) y = -offsetTop;
                        if (y > maxY-offsetTop-16) y = maxY-offsetTop-1;
                    }
                    app.getPointPanel().addPoint(new Point(x, y));
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    app.getPointPanel().removeLastPoint();
                }
            }
        });
    }

    public int getOffsetTop() {
        return offsetTop;
    }

    public void setOffsetTop(int offsetTop) {
        this.offsetTop = offsetTop;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.RED);
        //int idx = 0;
        for (Point p : app.getPointPanel().getPoints()) {
            int x = (int) (p.getX() * EDITOR_SIZE/NORMALIZED_SIZE);
            int y = (int) ((p.getY() + offsetTop) * EDITOR_SIZE/NORMALIZED_SIZE);
            //g.setColor((idx % 2) == 0 ? Color.RED : Color.BLUE);
            g.fillRect(x-2,y-2,5,5);
            //idx++;
        }
    }

}