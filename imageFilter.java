package ClassPackage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.ChangedCharSetException;

import ClassPackage.PictureMix;

import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.sql.rowset.FilteredRowSet;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class imageFilter extends JFrame {
    JButton JumpToFilter, JumpToMix, JumpToCut;
    JButton gray, old, mosaic, blackWhite, desaturate, singleColor, 
            opposite, comics, freeze, casting, relief, selectPicture;
    JPanel Filter, select_picture, JumpTo;

    String fileName;
    JPanel ImgPanel = new JPanel(new GridLayout(1, 1));
    BufferedImage[] img = new BufferedImage[1];

    int[][] imageData;
    int w, h;
    int rgb, R, G, B;

    public void init() {
        this.setTitle("image filter");
        this.setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JumpToFilter = new JButton("Filter"); 
        JumpToMix = new JButton("Mix"); 
        JumpToCut = new JButton("Cut");
        JumpTo = new JPanel(new GridLayout(1, 3));
        JumpTo.add(JumpToFilter); JumpTo.add(JumpToMix); JumpTo.add(JumpToCut);
        add(JumpTo, BorderLayout.PAGE_START);

        JumpToFilter.setEnabled(false);
        JumpToMix.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                PictureMix.solve();
                setVisible(false);
                dispose();
            }
        });
        JumpToCut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

            }
        });


        {
            gray = new JButton("灰色"); old = new JButton("懷舊");
            mosaic = new JButton("馬賽克"); blackWhite = new JButton("黑白");
            desaturate = new JButton("去色"); singleColor = new JButton("單色");
            opposite = new JButton("反色"); comics = new JButton("連環畫"); 
            freeze = new JButton("冰凍"); casting = new JButton("鎔鑄"); 
            relief = new JButton("浮雕");
            JPanel Filter = new JPanel(new GridLayout(11, 1));
            Filter.add(gray); Filter.add(old); Filter.add(mosaic);
            Filter.add(blackWhite); Filter.add(desaturate);
            Filter.add(singleColor); Filter.add(opposite); Filter.add(comics);
            Filter.add(freeze); Filter.add(casting); Filter.add(relief);
            add(Filter, BorderLayout.LINE_START);

            gray.setEnabled(false); old.setEnabled(false);
            mosaic.setEnabled(false); blackWhite.setEnabled(false);
            desaturate.setEnabled(false); singleColor.setEnabled(false);
            opposite.setEnabled(false); comics.setEnabled(false);
            freeze.setEnabled(false); casting.setEnabled(false);
            relief.setEnabled(false);

            selectPicture = new JButton("select a picture");
            select_picture = new JPanel();
            select_picture.add(selectPicture);

            add(select_picture, BorderLayout.EAST);
        }

        selectPicture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gray.setEnabled(true); old.setEnabled(true);
                mosaic.setEnabled(true); blackWhite.setEnabled(true);
                desaturate.setEnabled(true); singleColor.setEnabled(true);
                opposite.setEnabled(true); comics.setEnabled(true);
                freeze.setEnabled(true); casting.setEnabled(true);
                relief.setEnabled(true);

                JFileChooser fileChooser = new JFileChooser();
                File CurrentDirectory;
				try {
					CurrentDirectory = new File((new File(".").getCanonicalPath()));
					fileChooser.setCurrentDirectory(CurrentDirectory);
				} 
				catch (Exception ex) {
					ex.printStackTrace();
				}
                int ret = fileChooser.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileName = selectedFile.getName();
                }
                addPicture();
            }
        });
        gray.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                gray(getGraphics());
            }
        });
        old.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                old(getGraphics());
            }
        });
        mosaic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                mosaic(getGraphics());
            }
        });
        blackWhite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                blackWhite(getGraphics());
            }
        });
        desaturate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                desaturate(getGraphics());
            }
        });
        singleColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                singleColor(getGraphics());
            }
        });
        opposite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                opposite(getGraphics());
            }
        });
        comics.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                comics(getGraphics());
            }
        });
        freeze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                freeze(getGraphics());
            }
        });
        casting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                casting(getGraphics());
            }
        });
        relief.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                relief(getGraphics());
            }
        });
    }

    public void addPicture() {
        remove(ImgPanel);
        ImgPanel.removeAll();
        JScrollPane scrollPane1;

        try{
            img[0] = ImageIO.read(new File(fileName));
        }
        catch(Exception e){
            img[0] = null;
        }
        
        scrollPane1 = new JScrollPane(new JLabel(new ImageIcon(img[0])));// 把Image放進label裡
        ImgPanel.add(scrollPane1);
        add(ImgPanel, BorderLayout.LINE_START);
        setSize(1000, 1000);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    public static void solve() {
        imageFilter imageFilter = new imageFilter();
        imageFilter.init();
    }

    public int[][] getImageData(String path) {
        File now = new File(path);
        BufferedImage img = null;

        try {
            img = ImageIO.read(now);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        w = img.getWidth();
        h = img.getHeight();
        int[][] ret = new int[w][h];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++)
                ret[i][j] = img.getRGB(i, j);
        }
        return ret;
    }

    public void gray(Graphics g) {
        super.paint(g);

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = color.getRed();
                G = color.getGreen();
                B = color.getBlue();
                int gray = (R + G + B) / 3;
                g.setColor(new Color(gray, gray, gray));
                g.fillRect(1000 + i, 325 + j, 1, 1);
            }
        }
    }
    public void old(Graphics g) {
        super.paint(g);

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                rgb = imageData[i][j];
                Color color = new Color((int)rgb);
                R = (int)(color.getRed() * 0.393 + color.getGreen() * 0.769 + color.getBlue() * 0.189);
                G = (int)(color.getRed() * 0.349 + color.getGreen() * 0.686 + color.getBlue() * 0.168);
                B = (int)(color.getRed() * 0.272 + color.getGreen() * 0.534 + color.getBlue() * 0.131);
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                g.setColor(new Color(R, G, B));
                g.fillRect(100 + i, 100 + j, 1, 1);
            }
        }
    }
    public void mosaic(Graphics g){
        super.paint(g);
        
        for(int i = 0; i < w; i += 10){
            for(int j = 0; j < h; j += 10){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                g.setColor(color);
                g.fillRect(100 + i, 100 + j, 10, 10);
            }
        }
    }
    public void blackWhite(Graphics g){
        super.paint(g);

        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = color.getRed();
                G = color.getGreen();
                B = color.getBlue();
                int newColor = (R + G + B) / 3;
                newColor = (newColor >= 100 ? 255 : 0);
                g.setColor(new Color(newColor, newColor, newColor));
                g.fillRect(100 + i, 100 + j, 1, 1);
            }
        }
    }
    public void desaturate(Graphics g){
        super.paint(g);

        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = color.getRed();
                G = color.getGreen();
                B = color.getBlue();
                
                int Max = Math.max(R, Math.max(G, B));
                int Min = Math.min(R, Math.min(G, B));
                int newColor = (Max + Min) / 2;
                g.setColor(new Color(newColor, newColor, newColor));
                g.fillRect(100 + i, 100 + j, 1, 1);
            }
        }
    }
    public void singleColor(Graphics g){
        super.paint(g);

        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = color.getRed();
                G = color.getGreen();
                B = color.getBlue();
                
            }
        }
    }
    public void opposite(Graphics g){
        super.paint(g);

        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = 255 - color.getRed();
                G = 255 - color.getGreen();
                B = 255 - color.getBlue();
                g.setColor(new Color(R, G, B));
                g.fillRect(100 + i, 100 + j, 1, 1);
            }
        }
    }
    public void comics(Graphics g){
        super.paint(g);

        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = Math.abs(2 * color.getGreen() - color.getBlue() + color.getRed()) * color.getRed() / 256;
                G = Math.abs(2 * color.getBlue() - color.getGreen() + color.getRed()) * color.getRed() / 256;
                B = Math.abs(2 * color.getBlue() - color.getGreen() + color.getRed()) * color.getGreen() / 256;
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                g.setColor(new Color(R, G, B));
                g.fillRect(100 + i, 100 + j, 1, 1);
            }
        }
    }
    public void freeze(Graphics g){
        super.paint(g);

        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = (color.getRed() - color.getGreen() - color.getBlue()) * 3 / 2;
                G = (color.getGreen() - color.getRed() - color.getBlue()) * 3 / 2;
                B = (color.getBlue() - color.getRed() - color.getGreen()) * 3 / 2;
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                R = R < 0 ? 0 : R; G = G < 0 ? 0 : G; B = B < 0 ? 0 : B;
                g.setColor(new Color(R, G, B));
                g.fillRect(100 + i, 100 + j, 1, 1);
            }
        }
    }
    public void casting(Graphics g){
        super.paint(g);

        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = 128 * color.getRed() / (color.getGreen() + color.getBlue() + 1);
                G = 128 * color.getGreen() / (color.getRed() + color.getBlue() + 1);
                B = 128 * color.getBlue() / (color.getRed() + color.getGreen() + 1);
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                R = R < 0 ? 0 : R; G = G < 0 ? 0 : G; B = B < 0 ? 0 : B;
                g.setColor(new Color(R, G, B));
                g.fillRect(100 + i, 100 + j, 1, 1);
            }
        }
    }
    public void relief(Graphics g){
        super.paint(g);

        for(int i = 0; i < w - 1; i++){
            for(int j = 0; j < h - 1; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                int r0 = color.getRed(), g0 = color.getGreen(), b0 = color.getBlue();

                rgb = imageData[i + 1][j + 1];
                color = new Color(rgb);
                int r1 = color.getRed(), g1 = color.getGreen(), b1 = color.getBlue();

                R = r0 - r1 + 128;
                G = g0 - g1 + 128;
                B = b0 - b1 + 128;
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                R = R < 0 ? 0 : R; G = G < 0 ? 0 : G; B = B < 0 ? 0 : B;
                g.setColor(new Color(R, G, B));
                g.fillRect(100 + i, 100 + j, 1, 1);
            }
        }
    }
}
