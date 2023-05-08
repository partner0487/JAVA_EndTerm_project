import javax.swing.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class imageFilter extends JFrame{
    String[] fileName = {"", ""};
    JPanel ImgPanel = new JPanel();
    BufferedImage[] img = new BufferedImage[1];

    int[][] imageData;
    int w, h;

    public void init(){
        this.setTitle("image filter");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JButton selectPicture = new JButton("select a picture");
        JButton filter = new JButton("filter");
        JPanel ButtonPanel = new JPanel();
        ButtonPanel.setLayout(new GridLayout(2, 1));
        ButtonPanel.add(selectPicture);
        ButtonPanel.add(filter);
        add(ButtonPanel, BorderLayout.LINE_START);
        setLocationRelativeTo(null);
        
        selectPicture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JFileChooser fileChooser = new JFileChooser();
                int ret = fileChooser.showOpenDialog(null);
                if(ret == JFileChooser.APPROVE_OPTION){
                    File selectedFile = fileChooser.getSelectedFile();
                    fileName[0] = selectedFile.getName();
                }
                addPicture();
            }
        });
        filter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData("A.jpg");
                w = imageData.length; h = imageData[0].length;
                gray(getGraphics());
            }
        });
    }
    public void addPicture(){
        remove(ImgPanel);
        ImgPanel.removeAll();
        JScrollPane scrollPane1,scrollPane2;

        ImgPanel.setLayout(new GridLayout(1, 2));

        if(fileName[0]!=""){
                try {
                        img[0]=ImageIO.read(new File(fileName[0]));//讀取檔案
                }
                catch(Exception e) {
                        img[0]=null;
                }
                scrollPane1 = new JScrollPane(new JLabel(new ImageIcon(img[0])));//把Image放進label裡 
                ImgPanel.add(scrollPane1);       
        }
        
        if(fileName[1]!=""){
                try {
                        img[1]=ImageIO.read(new File(fileName[1]));//讀取檔案
                }
                catch(Exception e) {
                        img[1]=null;
                }
                scrollPane2 = new JScrollPane(new JLabel(new ImageIcon(img[1])));//把Image放進label裡
                ImgPanel.add(scrollPane2);
        }

        if(fileName[0]!="" || fileName[1]!=""){
            add(ImgPanel, BorderLayout.CENTER);
            pack();
        }
    }
    public static void main(String[] args){
        imageFilter imageFilter = new imageFilter();
        imageFilter.init();
    }
    public int[][] getImageData(String path){
        File now = new File(path);
        BufferedImage img = null;
        try{
            img = ImageIO.read(now);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        int w = img.getWidth(), h = img.getHeight();
        int[][] ret = new int[w][h];

        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++)
                ret[i][j] = img.getRGB(i, j);
        }
        return ret;
    }
    

    public void gray(Graphics g){
        super.paint(g);

        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                int rgb = imageData[i][j];
                Color color = new Color(rgb);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int gray = (red + green + blue) / 3;
                Color newColor = new Color(gray, gray, gray);
                g.setColor(newColor);
                g.fillRect(100 + i, 100 + j, 1, 1);
            }
        }
    }
}
