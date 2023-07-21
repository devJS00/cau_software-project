package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@SuppressWarnings("serial")
class DrawingPiePanel extends JPanel {
	int k_num; // ������ ������ ���õ� ǥ���� ���Ե� Ʈ���� ��
	int u_num; // ������ ������ ���õ� ǥ���� ���Ե� Ʈ���� ��


public void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());						
		int total = k_num + u_num;
		if (total == 0) return;
		
		int arc1 = (int) 360.0 * k_num / total;				
		g.setColor(Color.YELLOW);
		g.fillArc(50, 20, 200, 200, 0, arc1);
		g.setColor(Color.BLUE);
		g.fillArc(50, 20, 200, 200, arc1, 360-arc1);		
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("����ü", Font.PLAIN, 15));
		g.drawString("YELLOW: POSITIVE TWEETS", 300, 120);
		g.drawString("BLUE: NEGATIVE TWEETS", 300, 140);	
		if(k_num > u_num) g.drawString("���� ��ſ� ���� ��������!", 40, 250);	
		else if (k_num < u_num) g.drawString("���� ���� ���� ��������Ф�", 40, 250);
		else if (k_num == u_num) g.drawString("��ſ� �ϵ�, ���� �ϵ� �־�������", 40, 250);
		g.drawString("(������ Ʈ��: " + k_num + "��, ������ Ʈ��: " + u_num + "��)", 40, 270);	
	}
	
	void setNumbers(int num1, int num2) {
		this.k_num = num1;
		this.u_num = num2;
	}
}

class DrawingPieActionListener implements ActionListener {	
	JTextField text;
	DrawingPiePanel drawingPanel;
	
	public DrawingPieActionListener(JTextField text,DrawingPiePanel drawingPanel) {
		this.text = text;
		this.drawingPanel = drawingPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {				
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		    .setOAuthConsumerKey("")
		    .setOAuthConsumerSecret("")
		    .setOAuthAccessToken("")
		    .setOAuthAccessTokenSecret("");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
		Paging p = new Paging();
		p.setCount(150); // �ֱ� Ʈ�� 150���� �о���� ��		
		String userAccount = text.getText();					       		
		List<Status> statusList = null;
		try {
			statusList = twitter.getUserTimeline(userAccount, p);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    int  k_count = 0, u_count = 0;
		for (Status status : statusList) 
		{
			if(status.getText().contains("����") || status.getText().contains("����") || status.getText().contains(":)"))
			{
		    System.out.println(status.getUser().getName() + " : " + status.getText());
			k_count++;
			}
			
			if(status.getText().contains("�Ф�") || status.getText().contains("��..") || status.getText().contains(":("))
			{
		    System.out.println(status.getUser().getName() + " : " + status.getText());
		    System.out.println(status.getRetweetedStatus().getRetweetCount());
			u_count++;
			}
			
		}										
			drawingPanel.setNumbers(k_count, u_count);
			drawingPanel.repaint();	
	}
}

public class TestTwitter {
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
	JFrame frame = new JFrame("���� ����� ����� �����?");
	frame.setLocation(400, 200);
	frame.setPreferredSize(new Dimension(600, 400));
	Container contentPane = frame.getContentPane();
	
	DrawingPiePanel drawingPanel = new DrawingPiePanel();
	contentPane.add(drawingPanel, BorderLayout.CENTER);
	
	JPanel controlPanel = new JPanel();
	JTextField text = new JTextField(20);		
	JButton button = new JButton("�Է�");
	controlPanel.add(new JLabel("Ʈ���� ���̵� �Է����ּ���. ex) @abc123"));
	controlPanel.add(text);		
	controlPanel.add(button);					
	contentPane.add(controlPanel,BorderLayout.NORTH);
	frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
    
	DrawingPieActionListener listener = new DrawingPieActionListener(text, drawingPanel);			
	button.addActionListener(listener);
	frame.pack();
	frame.setVisible(true);
	}
}