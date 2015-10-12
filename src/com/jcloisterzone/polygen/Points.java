package com.jcloisterzone.polygen;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;



public class Points {

    private List<Point> points = new ArrayList<Point>();
    private final PolygonGenerator app;
    private String currentValue = "";

    public Points(final PolygonGenerator app) {
        this.app = app;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void clearPoints() {
        points.clear();
        app.refresh(true);
    }

    public void addPoint(Point point) {
        points.add(point);
        app.refresh(true);
    }

    public void removeLastPoint() {
        if (points.size() == 0) return;
        points.remove(points.size() - 1);
        app.refresh(true);
    }

    public String getAreaDefinition() {
        StringBuilder s = new StringBuilder();
        s.append("<svg:polygon points=\"");

        boolean first = true;
        for (Point p : points) {
            if (!first) {
                s.append(" ");
            }
            first = false;
            s.append(p.getX());
            s.append(",");
            s.append(p.getY());
        }
        s.append("\"/>");
        currentValue = s.toString();
        return currentValue;
    }

    public void parse(String text) {
        if (currentValue.equals(text) || text.equals("")) return;
        //System.out.println(currentValue + " -> " + text);
        Pattern p = Pattern.compile("\\s*<svg:polygon points=\"((\\d+,\\d+\\s?)*)\"/>\\s*");
        Matcher m = p.matcher(text);
        if (m.matches()) {
            List<Point> points = new ArrayList<Point>();
            String[] pointsStr = m.group(1).split("\\s");
            for (String tok : pointsStr) {
                String[] xy = tok.split(",");
                points.add(new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
            }
            this.points = points;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    app.refresh(false);
                }
            });
        }

    }


}

