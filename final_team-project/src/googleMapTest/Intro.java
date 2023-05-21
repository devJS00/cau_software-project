package googleMapTest;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Intro extends JFrame{

	private Image background=new ImageIcon("./backgroundimage/background.png").getImage();

	public Intro() {
		setTitle("SNS검색 & 위치 찾기");
		setSize(1495,830);
		setResizable(false);

		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		System.out.println(background.getWidth(null));
//		System.out.println(background.getHeight(null));
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Frame f = new Frame();
				dispose();
			}
		});
	}
	
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);
	}

}
