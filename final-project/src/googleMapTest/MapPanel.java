package googleMapTest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MapPanel extends JPanel implements ActionListener{
	
	private JTextField textField = new JTextField(30);
	private JButton searchButton = new JButton("검색");
	private JPanel panel = new JPanel();

	private ImageIcon plus = new ImageIcon("./buttonImage/plus.png");
	private ImageIcon minus = new ImageIcon("./buttonImage/minus.png");
	private Image plusimg = plus.getImage();
	private Image minusimg = minus.getImage();
	Image newplusimg = plusimg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	private JButton plusButton = new JButton(new ImageIcon(newplusimg));
	Image newminusimg = minusimg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	private JButton minusButton = new JButton(new ImageIcon(newminusimg));
	private JPanel panelSouth = new JPanel();
	
	private ImageIcon left = new ImageIcon("./buttonImage/left.png");
	private Image leftimg = left.getImage();
	Image newleftimg = leftimg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	private JButton leftButton = new JButton(new ImageIcon(newleftimg));
	
	private ImageIcon right = new ImageIcon("./buttonImage/right.png");
	private Image rightimg = right.getImage();
	Image newrightimg = rightimg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	private JButton rightButton = new JButton(new ImageIcon(newrightimg));
	
	private ImageIcon up = new ImageIcon("./buttonImage/up.png");
	private Image upimg = up.getImage();
	Image newupimg = upimg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	private JButton upButton = new JButton(new ImageIcon(newupimg));
	
	private ImageIcon down = new ImageIcon("./buttonImage/down.png");
	private Image downimg = down.getImage();
	Image newdownimg = downimg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	private JButton downButton = new JButton(new ImageIcon(newdownimg));
	
	private JPanel mapAndText = new JPanel();
	
	
//	private JButton rightButton = new JButton("우");
//	private JButton upButton = new JButton("상");
//	private JButton downButton = new JButton("하");
	
	
	private GoogleAPI googleAPI = new GoogleAPI();
	private JLabel googleMap = new JLabel();
	
	private double targetLat;
	private double targetLng;
	private double nowLat;
	private double nowLng;
	private int Zoom = 15;
	
	private JTextArea locationTextArea = new JTextArea();

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(searchButton.hasFocus()) {
			Zoom = 15;
			double geoLocation[] = googleAPI.getLocation(textField.getText());
			targetLat = geoLocation[0];
			targetLng = geoLocation[1];
			nowLat = targetLat;
			nowLng = targetLng;
			System.out.println(targetLat);
			System.out.println(targetLng);
			setLocationText(textField.getText());
			revalidate();
			repaint();
		}
		
		else if(plusButton.hasFocus()) {
			if (Zoom < 20)
				Zoom++; // Zoom의 최댓값 20
		}
		else if(minusButton.hasFocus()) {
			if (Zoom > 0)
				Zoom--; // Zoom의 최솟값은 0
		}
		else if(leftButton.hasFocus())
			nowLng -= 0.0003 * (21 - Zoom);
		else if(rightButton.hasFocus())
			nowLng += 0.0003 * (21 - Zoom);
		else if(upButton.hasFocus())
			nowLat += 0.0003 * (21 - Zoom);
		else if(downButton.hasFocus())
			nowLat -= 0.0003 * (21 - Zoom);
		
		setMap(textField.getText());
		//System.out.println(Zoom);	
	}
	
	public void setMap(String location) {
		googleAPI.downloadMap(nowLat, nowLng, targetLat, targetLng, location, Zoom);
		googleMap.setIcon(googleAPI.getMap(location));
		googleAPI.fileDelete(location);
		
		add(BorderLayout.WEST, googleMap);
//		pack();		
	}
	
	public void setLocationText(String location) {
		SNSAPI snsapi = new SNSAPI();
		
		String clientId = ""; // 애플리케이션 클라이언트 아이디값"
		String clientSecret = ""; // 애플리케이션 클라이언트 시크릿값"
//		String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + text + "&display=1&start="+start+"&sort=random";
		String LocationTextURL = null;
		try {
			LocationTextURL = "https://openapi.naver.com/v1/search/local.json?query=" 
					+URLEncoder.encode(location, "UTF-8") + "&display=1&start=1&sort=random";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", clientId);
		requestHeaders.put("X-Naver-Client-Secret", clientSecret);
		String responseBody = snsapi.get(LocationTextURL, requestHeaders);

		System.out.println(responseBody);
		String[] strArr = responseBody.split("\"");

		
		locationTextArea.setEditable(false);
		
		locationTextArea.append("\t\t\t\t\n");
		locationTextArea.append(location + "\n");
		locationTextArea.append(strArr[19] + "\n");
		locationTextArea.append(strArr[23] + "\n");
		locationTextArea.append(strArr[35] + "\n\n");
//		revalidate();
//		repaint();
		
		System.out.println(strArr[30]);// title
		System.out.println(strArr[19]);// link
		System.out.println(strArr[23]);// text
		System.out.println(strArr[35]);// date
		
		add(BorderLayout.EAST, locationTextArea);
	}
	
	
	
	
	public MapPanel() {
		setLayout(new BorderLayout());
		setBackground(new Color(255, 0,0,0));
		
		panel.add(textField);
		panel.add(searchButton);
		searchButton.addActionListener(this);
		
		panelSouth.add(leftButton);
		leftButton.addActionListener(this);
		leftButton.setBorderPainted(false);
		leftButton.setContentAreaFilled(false);
		panelSouth.add(rightButton);
		rightButton.addActionListener(this);
		rightButton.setBorderPainted(false);
		rightButton.setContentAreaFilled(false);
		panelSouth.add(upButton);
		upButton.addActionListener(this);
		upButton.setBorderPainted(false);
		upButton.setContentAreaFilled(false);
		panelSouth.add(downButton);
		downButton.addActionListener(this);
		downButton.setBorderPainted(false);
		downButton.setContentAreaFilled(false);
		
		panelSouth.add(plusButton);
		plusButton.addActionListener(this);
		plusButton.setBorderPainted(false);
		plusButton.setContentAreaFilled(false);
		
		panelSouth.add(minusButton);
		minusButton.addActionListener(this);
		minusButton.setBorderPainted(false);
		minusButton.setContentAreaFilled(false);
		
		add(BorderLayout.NORTH, panel);
		
		add(BorderLayout.SOUTH, panelSouth);
//		pack();
	}



}
