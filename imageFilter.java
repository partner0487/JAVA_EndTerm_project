package ClassPackage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.ChangedCharSetException;

import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.sql.rowset.FilteredRowSet;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class imageFilter extends JFrame {
    JButton JumpToFilter, JumpToMix, JumpToCut;
    JButton gray, old, mosaic, blackWhite, desaturate,
            opposite, comics, casting, relief, selectPicture, savePicture, SinCity;
    JPanel Filter, File, JumpTo;

    String fileName;
    JPanel ImgPanel = new JPanel(new GridLayout(1, 1));
    BufferedImage[] img = new BufferedImage[1];

    int[][] imageData;
    int[][] newImageData;
    int w, h;
    int rgb, R, G, B;

    public void init() {
        this.setTitle("image filter");
        this.setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JumpToFilter = new JButton("Filter"); 
        JumpToMix = new JButton("Mix"); 
        JumpToCut = new JButton("Gif");
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
                GifProcessor.solve();
                setVisible(false);
                dispose();
            }
        });

        {
            gray = new JButton("灰色"); old = new JButton("懷舊");
            mosaic = new JButton("馬賽克"); blackWhite = new JButton("黑白");
            desaturate = new JButton("去色"); opposite = new JButton("反色"); 
            comics = new JButton("連環畫");
            casting = new JButton("鎔鑄"); relief = new JButton("浮雕");
            SinCity = new JButton("SinCity");
            JPanel Filter = new JPanel(new GridLayout(10, 1));
            Filter.add(gray); Filter.add(old); Filter.add(mosaic);
            Filter.add(blackWhite); Filter.add(desaturate);
            Filter.add(opposite); Filter.add(comics);
            Filter.add(casting); Filter.add(relief); Filter.add(SinCity);
            add(Filter, BorderLayout.LINE_START);

            gray.setEnabled(false); old.setEnabled(false);
            mosaic.setEnabled(false); blackWhite.setEnabled(false);
            desaturate.setEnabled(false);
            opposite.setEnabled(false); comics.setEnabled(false);
            casting.setEnabled(false);
            relief.setEnabled(false); SinCity.setEnabled(false);

            File = new JPanel();

            selectPicture = new JButton("select a picture");
            File.add(selectPicture);
            
            savePicture = new JButton("save");
            File.add(savePicture);

            add(File, BorderLayout.EAST);
        }

        selectPicture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gray.setEnabled(true); old.setEnabled(true);
                mosaic.setEnabled(true); blackWhite.setEnabled(true);
                desaturate.setEnabled(true);
                opposite.setEnabled(true); comics.setEnabled(true);
                casting.setEnabled(true);
                relief.setEnabled(true); SinCity.setEnabled(true);

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
                imageData = getImageData(fileName);
            }
        });
        savePicture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                for(int i = 0; i < w; i++){
                    for(int j = 0; j < h; j++){
                        newImage.setRGB(i, j, newImageData[i][j]);
                    }
                }
                try{
                    File output = new File("FilerStore.jpg");
                    ImageIO.write(newImage, "jpg", output);
                }
                catch(IOException ex){
                    System.out.println("error");
                }
            }
        });
        gray.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                grayFilter gray = new grayFilter();
                newImageData = gray.getNewImageData(imageData, w, h);
                displayFilterImage();
            }
        });
        old.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                oldFilter old = new oldFilter();
                newImageData = old.getNewImageData(imageData, w, h);
                displayFilterImage();
            }
        });
        mosaic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                mosaicFilter mosaic = new mosaicFilter();
                newImageData = mosaic.getNewImageData(imageData, w, h);
                displayFilterImage();
            }
        });
        blackWhite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                blackWhiteFilter blackWhite = new blackWhiteFilter();
                newImageData = blackWhite.getNewImageData(imageData, w, h);
                displayFilterImage();
            }
        });
        desaturate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                desaturateFilter desaturate = new desaturateFilter();
                newImageData = desaturate.getNewImageData(imageData, w, h);
                displayFilterImage();
            }
        });
        opposite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                oppositeFilter opposite = new oppositeFilter();
                newImageData = opposite.getNewImageData(imageData, w, h);
                displayFilterImage();
            }
        });
        comics.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                comicsFilter comics = new comicsFilter();
                newImageData = comics.getNewImageData(imageData, w, h);
                displayFilterImage();
            }
        });
        casting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                castingFilter casting = new castingFilter();
                newImageData = casting.getNewImageData(imageData, w, h);
                displayFilterImage();
            }
        });
        relief.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                reliefFilter relief = new reliefFilter();
                newImageData = relief.getNewImageData(imageData, w, h);
                displayFilterImage();
            }
        });
        SinCity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                SinCityFilter SinCity = new SinCityFilter();
                newImageData = SinCity.getNewImageData(imageData, w, h);
                displayFilterImage();
            }
        });
    }
    public void displayFilterImage(){
        try{
            ImgPanel.remove(1);
        } catch(ArrayIndexOutOfBoundsException ex){
            System.out.printf("還沒有圖片\n");
        }
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < w; i++)
            for(int j = 0; j < h; j++)
                newImage.setRGB(i, j, newImageData[i][j]);

        ImageIcon displayImage = new ImageIcon(newImage);
        JScrollPane displayPane = new JScrollPane(new JLabel(displayImage));
        ImgPanel.add(displayPane, 1);
        add(ImgPanel);
        setSize(1000, 1000);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    public void addPicture() {
        remove(ImgPanel);
        ImgPanel.removeAll();
        JScrollPane scrollPane1;

        try{
            img[0] = ImageIO.read(new File(fileName));
        } catch(Exception e){
            img[0] = null;
        }
        ImageIcon displayedImage = new ImageIcon(img[0]);
        Image image = displayedImage.getImage();
        Image resizeImage;
        if(displayedImage.getIconWidth() >= displayedImage.getIconHeight())
            resizeImage = image.getScaledInstance(displayedImage.getIconWidth() / (displayedImage.getIconHeight() / 500)
                            , 500, java.awt.Image.SCALE_SMOOTH);
        else
            resizeImage = image.getScaledInstance(500
                            , displayedImage.getIconHeight() / (displayedImage.getIconWidth() / 500), java.awt.Image.SCALE_SMOOTH);
        displayedImage = new ImageIcon(resizeImage);

        scrollPane1 = new JScrollPane(new JLabel(displayedImage));// 把Image放進label裡
        ImgPanel.add(scrollPane1);
        add(ImgPanel);
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

        ImageIcon displayedImage = new ImageIcon(img);
        Image image = displayedImage.getImage();
        Image resizeImage;
        if(displayedImage.getIconWidth() >= displayedImage.getIconHeight()){
            resizeImage = image.getScaledInstance(displayedImage.getIconWidth() / (displayedImage.getIconHeight() / 500)
                                                    , 500, java.awt.Image.SCALE_SMOOTH);
        }
        else{
            resizeImage = image.getScaledInstance(500
                                                , displayedImage.getIconHeight() / (displayedImage.getIconWidth() / 500), java.awt.Image.SCALE_SMOOTH);
        }
        displayedImage = new ImageIcon(resizeImage);

        Graphics2D draw = img.createGraphics();
        draw.drawImage(resizeImage, 0, 0, null);

        image = displayedImage.getImage();

        w = displayedImage.getIconWidth();
        h = displayedImage.getIconHeight();
        int[][] ret = new int[w][h];

        for (int i = 0; i < w; i++) 
            for (int j = 0; j < h; j++)
                ret[i][j] = img.getRGB(i, j);
        return ret;
    }
}
