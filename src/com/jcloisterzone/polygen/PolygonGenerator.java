package com.jcloisterzone.polygen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jcloisterzone.board.Rotation;

public class PolygonGenerator extends JFrame {
    private static final long serialVersionUID = -2934047702924373458L;

    //TODO read path from env
    //public static final String DEFAULT_PATH = "C:/Development/java/JCloisterZone/src/main/resources/plugins/jcz/tiles/BA";
    public static final String DEFAULT_PATH = "/Users/farin/w/JCloisterZone/src/main/resources/plugins/jcz/tiles/BA";
    public static final String TITLE = "Polygon Generator";

    public static final int PREVIEW_SIZE = 150;
    public static final int OFFSET_TOP_3D = 49;  // 0 for classic tiles, 49 for rect tiles

    private PolygonEditor editor;
    private Preview preview;
    private File file;

    private JFileChooser fc = new JFileChooser();
    private Points points;

    private JTextArea xmlDef = new JTextArea();


    public PolygonGenerator() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new FlowLayout());

        editor = new PolygonEditor(this);
        editor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        editor.setPreferredSize(new Dimension(PolygonEditor.EDITOR_SIZE, PolygonEditor.EDITOR_SIZE));
        getContentPane().add(editor);

        preview = new Preview(this);
        preview.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));
        preview.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        getContentPane().add(preview);


        JButton open = new JButton("Open image");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fc.setCurrentDirectory(new File(DEFAULT_PATH));
                int returnVal = fc.showOpenDialog(PolygonGenerator.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                    ResizedImage ri = getImage(file, PolygonEditor.EDITOR_SIZE, Rotation.R0);
                    ImageIcon editorIcon = new ImageIcon(ri.img);
                    int w = editorIcon.getIconWidth();
                    int h = editorIcon.getIconHeight();
                    double ratio = (double)ri.sourceHeight / ri.sourceWidth;
                    //double scale = (double)PolygonEditor.NORMALIZED_SIZE / PolygonEditor.NORMALIZED_SIZE; 
                    int baseOffsetTop;
                    if (w == h) { //classic	
                    	baseOffsetTop = 0;
                    } else { //jcz
                    	baseOffsetTop = OFFSET_TOP_3D;
                    }                   
                    double scale = (double)PolygonEditor.NORMALIZED_SIZE / ri.sourceWidth;
                    int offsetTop = (int)(baseOffsetTop * scale);
                    
                    editor.setPreferredSize(new Dimension(PolygonEditor.EDITOR_SIZE, (int)(PolygonEditor.EDITOR_SIZE*ratio)));
                    editor.setIcon(editorIcon);
                    editor.setOffsetTop(offsetTop);        
                    editor.setMaxY((int) (PolygonEditor.NORMALIZED_SIZE * ratio));


                    ri = getImage(file, PREVIEW_SIZE, Rotation.R0);
                    ImageIcon previewIcon = new ImageIcon(ri.img);
                    w = previewIcon.getIconWidth();
                    h = previewIcon.getIconHeight();
                    preview.setIcon(previewIcon);
                    preview.setOffsetTop(offsetTop);
                    preview.setPreferredSize(new Dimension(PREVIEW_SIZE, (int)(PREVIEW_SIZE*ratio)));
                    setTitle(TITLE + " - " + file.getName());
                }

            }
        });
        getContentPane().add(open);

        JButton clear = new JButton("Clear points");
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                points.clearPoints();
            }
        });
        getContentPane().add(clear);

        JButton up = new JButton("^");
        up.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Point p : points.getPoints()) {
                    p.y -= 1;
                }
                refresh(true);
            }
        });
        getContentPane().add(up);
        JButton down = new JButton("v");
        down.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Point p : points.getPoints()) {
                    p.y += 1;
                }
                refresh(true);
            }
        });
        getContentPane().add(down);
        JButton left = new JButton("<");
        left.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Point p : points.getPoints()) {
                    p.x -= 1;
                }
                refresh(true);
            }
        });
        getContentPane().add(left);
        JButton right = new JButton(">");
        right.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Point p : points.getPoints()) {
                    p.x += 1;
                }
                refresh(true);
            }
        });
        getContentPane().add(right);

        points = new Points(this);

        getContentPane().add(xmlDef);
        xmlDef.setPreferredSize(new Dimension(900,100));
        xmlDef.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        xmlDef.setText(points.getAreaDefinition());
        xmlDef.setLineWrap(true);
        xmlDef.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent arg0) {
                points.parse(xmlDef.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent arg0) {
                points.parse(xmlDef.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                points.parse(xmlDef.getText());
            }
        });


        setTitle(TITLE);
        setSize(1000, PolygonEditor.EDITOR_SIZE + 300);
        setVisible(true);
    }


    public void refresh(boolean updateXml) {
        editor.repaint();
        preview.repaint();
        if (updateXml) {
            getXmlDef().setText(points.getAreaDefinition());
        }
    }

    public Points getPointPanel() {
        return points;
    }

    public JTextArea getXmlDef() {
        return xmlDef;
    }

    public static class ResizedImage {
        public Image img;
        public int sourceWidth;
        public int sourceHeight;
    }


    private ResizedImage getImageWithoutTracking(File f, int width) {
        Image img = null;
        try {
            img = Toolkit.getDefaultToolkit().getImage(f.toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ResizedImage ri = new ResizedImage();

        ri.img = (new ImageIcon(img)).getImage();
        ri.sourceWidth = img.getWidth(null);
        ri.sourceHeight = img.getHeight(null);

        if (ri.sourceWidth != width) {
            ri.img = img.getScaledInstance(width, width * ri.sourceHeight / ri.sourceWidth, Image.SCALE_AREA_AVERAGING);
        }
        return ri;
    }


    public ResizedImage getImage(File f, int width, Rotation rot) {
        ResizedImage img = getImageWithoutTracking(f, width);
        try {
            MediaTracker tracker = new MediaTracker(editor);
            tracker.addImage(img.img, 0);
            tracker.waitForID(0);
        } catch (Exception ex) {
            //do nothing
        }
        return img;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PolygonGenerator();
            }
        });
    }
}