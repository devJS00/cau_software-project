package googleMapTest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Sns extends JPanel {
	
	public Sns() {
		
		SNSAPI snsapi = new SNSAPI();
		
		setBackground(new Color(255, 0,0,0));
		setLayout(new BorderLayout());
		
		JPanel inputPanel = new JPanel();
		JButton button = new JButton("입력");
		JTextField kw = new JTextField(20);
		inputPanel.add(new JLabel("키워드를 입력해주세요. ex) 맛집, 전시회"));
		inputPanel.add(kw);
		inputPanel.add(button);
		add(inputPanel, BorderLayout.NORTH);

		JPanel twitterPanel = new JPanel();
		twitterPanel.setSize(800, 300);
		twitterPanel.add(new JLabel("           트위터 검색 결과"));
		JTextArea twt_textArea = new JTextArea(21, 110);
		twt_textArea.setEditable(false);
		JScrollPane twt_scroll = new JScrollPane(twt_textArea);
		twt_scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		twitterPanel.add(twt_scroll);
		add(twitterPanel, BorderLayout.CENTER);

		JPanel nvbPanel = new JPanel();
		nvbPanel.setSize(800, 300);
		nvbPanel.add(new JLabel("네이버블로그 검색 결과"));
		JTextArea nvb_textArea = new JTextArea(21, 110);
		nvb_textArea.setEditable(false);
		JScrollPane nvb_scroll = new JScrollPane(nvb_textArea);
		nvb_scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		nvbPanel.add(nvb_scroll);
		add(nvbPanel, BorderLayout.SOUTH);

		setVisible(true);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String keyword = kw.getText();
				snsapi.twitterSearch(keyword, twt_textArea);
				snsapi.naverSearch(keyword, nvb_textArea);

			}
		});
	}

	
}
