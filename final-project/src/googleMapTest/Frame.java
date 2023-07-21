package googleMapTest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Frame extends JFrame{
	
	public Frame() {
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("SNS검색 & 위치 찾기");
		setVisible(true);
		
		
		JTabbedPane tabpane = new JTabbedPane();
		tabpane.addTab("Map", new MapPanel());
		tabpane.addTab("SNS", new Sns());
		
		tabpane.setBackground(new Color(255,0,0,0));
		
		add(tabpane, BorderLayout.CENTER);
	
		
		pack();
		
	}
	
	
	
}
