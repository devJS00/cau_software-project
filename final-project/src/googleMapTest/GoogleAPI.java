package googleMapTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.ImageIcon;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GoogleAPI {
	public double[] getLocation(String location) {
		try {
			String tempURL = "https://maps.googleapis.com/maps/api/geocode/json?address="
					+URLEncoder.encode(location, "UTF-8")
					+"&key=";
			
			URL urlLocation = new URL(tempURL);
			HttpURLConnection con = (HttpURLConnection)urlLocation.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = br.readLine()) != null) {
				response.append(inputLine + "\n");
				//System.out.println(inputLine);
			}
			br.close();
			
			JSONParser jsonPares = new JSONParser();
			JSONObject obj = (JSONObject)jsonPares.parse(response.toString());
			JSONArray jArray = (JSONArray)obj.get("results");
			JSONObject tmp = (JSONObject)(jArray.get(0));
			JSONObject geometry = (JSONObject)tmp.get("geometry");
			JSONObject locationJSON = (JSONObject)geometry.get("location");
			//System.out.println(String.valueOf(locationJSON.get("lat")));
			//System.out.println(String.valueOf(locationJSON.get("lng")));
			
			double[] result = new double[2];
			result[0] = Double.parseDouble(String.valueOf(locationJSON.get("lat")));
			result[1] = Double.parseDouble(String.valueOf(locationJSON.get("lng")));
			
			return result;
		} catch ( Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	
	public void downloadMap(double lat, double lng, double targetLat, double targetLng, String location, int zoomScale) {
		try {
			String imageURL = "https://maps.googleapis.com/maps/api/staticmap?center="
					+lat + "," + lng
					+"&markers=icon:https://ifh.cc/g/81AKXo.png%7C" +targetLat + "," + targetLng
					+"&zoom="
					+zoomScale
					+"&size=640x512&scale=2"
					+"&key=";
			URL url = new URL(imageURL);
			writeImage(url, location);
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeImage(URL url, String location) {
		try {
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(location);
			byte[] b = new byte[2048];
			int length;
			while((length = is.read(b)) != -1) {
				os.write(b,0,length);
			}
			is.close();
			os.close();
			
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}
	

	public ImageIcon getMap(String location) {
		return new ImageIcon((new ImageIcon(location)).getImage().getScaledInstance(1000, 800, java.awt.Image.SCALE_SMOOTH));
	}
	public void fileDelete(String fileName) {
		File f = new File(fileName);
		f.delete();
	}
}
