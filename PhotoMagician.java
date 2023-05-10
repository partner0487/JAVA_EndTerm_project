package ClassPackage;
import javax.swing.*;
import ClassPackage.imageFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PhotoMagician {
    public static void main(String[] args){
        JFrame application = new JFrame("Photo Magician");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(dimension.width / 2, dimension.height / 2);
        int x = (int) ((dimension.getWidth() - application.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - application.getHeight()) / 2);
        application.setLocation(x, y);
        application.getContentPane().setLayout(new BoxLayout(application.getContentPane(), BoxLayout.Y_AXIS));


        JButton Filter = new JButton("Filter"), Mix = new JButton("Mix"), Cut = new JButton("Cut");


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        panel.add(Filter);
        panel.add(Mix);
        panel.add(Cut);

        application.add(panel, BorderLayout.CENTER);
        application.setVisible(true);

        Filter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageFilter.solve();
                application.setVisible(false);
                application.dispose();
            }
        });
        Mix.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                PictureMix.solve();
                application.setVisible(false);
                application.dispose();
            }
        });
    }
}
