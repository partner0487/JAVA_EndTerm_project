package ClassPackage;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.ChangedCharSetException;
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
        this.setSize(1000, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JButton selectPicture = new JButton("select a picture");
        JButton filter = new JButton("filter");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(selectPicture);
        panel.add(filter);

        add(panel, BorderLayout.LINE_START);
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
                imageData = getImageData(fileName[0]);
                BlackWhite(getGraphics());
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
            }catch(Exception e) {
                img[0]=null;
            }
            scrollPane1 = new JScrollPane(new JLabel(new ImageIcon(img[0])));//把Image放進label裡 
            ImgPanel.add(scrollPane1);       
        }
        
        if(fileName[1]!=""){
            try {
                    img[1]=ImageIO.read(new File(fileName[1]));//讀取檔案
            }catch(Exception e) {
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
    public static void solve(){
        imageFilter imageFilter = new imageFilter();
        imageFilter.init();
    }





    public int[][] getImageData(String path){
        File now = new File(path);
        BufferedImage img = null;
        
        try{
            img = ImageIO.read(now);
        }catch(IOException ex){
            ex.printStackTrace();
        }
        
        w = img.getWidth(); h = img.getHeight();
        int[][] ret = new int[w][h];

        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++)
                ret[i][j] = img.getRGB(i, j);
        }
        return ret;
    }
    
    public void BlackWhite(Graphics g){
        super.paint(g);

        int rgb, R, G, B;
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = color.getRed(); G = color.getGreen(); B = color.getBlue();
                int gray = (R + G + B) / 3;
                g.setColor(new Color(gray, gray, gray));
                g.fillRect(100 + i, 100 + j, 1, 1);
            }
        }
    }
}
