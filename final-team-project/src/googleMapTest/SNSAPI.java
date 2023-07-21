package googleMapTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class SNSAPI {
	public Twitter getT() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("")
				.setOAuthConsumerSecret("")
				.setOAuthAccessToken("")
				.setOAuthAccessTokenSecret("");
		TwitterFactory fac = new TwitterFactory(cb.build());
		Twitter twitter = fac.getInstance();

		return twitter;
	}

	
	public HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

	public String readBody(InputStream body) throws UnsupportedEncodingException {
		InputStreamReader streamReader = new InputStreamReader(body, "UTF-8");

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}

			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}

	public String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				return readBody(con.getInputStream());
			} else { // 에러 발생
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	public void twitterSearch(String keyword, JTextArea twt_textArea) {
		/* 트위터 search */
		Twitter tw = getT();
		int count = 0;
		try {

			if (keyword.equals("맛집")) {
				Query query = new Query("#몬베베가_몬베베에게_추천하는_맛집");
				query.setCount(200);
				QueryResult result = null;
				result = tw.search(query);

				for (Status status : result.getTweets()) {
					if (status.getRetweetCount() > 10) {
//						System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
//						System.out.println("리트윗 수: " + status.getRetweetCount());
						count++;

						twt_textArea.append("@" + status.getUser().getScreenName() + " - " + status.getText() + "\n\n");
					}
				}
			}

			else {
				Query query = new Query(keyword);
				query.setCount(200);
				QueryResult result = null;
				result = tw.search(query);

				for (Status status : result.getTweets()) {
					if (status.getRetweetCount() > 10) {
//						System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
//						System.out.println("리트윗 수: " + status.getRetweetCount());
						count++;

						twt_textArea.append("@" + status.getUser().getScreenName() + " - " + status.getText() + "\n\n");
					}
				}
			}
		} catch (TwitterException b) {
			b.printStackTrace();
		}
		System.out.println("전체 트윗 수: " + count + "\n");
	}

	public void naverSearch(String keyword, JTextArea nvb_textArea) {
		/* 네이버 블로그 search */
		String clientId = ""; // 애플리케이션 클라이언트 아이디값"
		String clientSecret = ""; // 애플리케이션 클라이언트 시크릿값"

		String text = null;
		try {
			text = URLEncoder.encode(keyword, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			throw new RuntimeException("검색어 인코딩 실패", e1);
		}
		int start = 0;
		for (int i = 0; i < 30; i++) {
			start++;

			String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text + "&display=1&start=" + start
					+ "&sort=sim"; // json 결과
			//String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + text + "&display=1&start="+start+"&sort=random";
			// String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text;
			// // xml 결과

			Map<String, String> requestHeaders = new HashMap<>();
			requestHeaders.put("X-Naver-Client-Id", clientId);
			requestHeaders.put("X-Naver-Client-Secret", clientSecret);
			String responseBody = get(apiURL, requestHeaders);

//			System.out.println(responseBody);
			String[] strArr = responseBody.split("\"");
//			System.out.println(strArr[30]);// title
//			System.out.println(strArr[19]);// link
//			System.out.println(strArr[23]);// text
//			System.out.println(strArr[35]);// date

			nvb_textArea.append(strArr[30] + "  ");
			nvb_textArea.append(strArr[19] + "  ");
			nvb_textArea.append(strArr[23] + "  ");
			nvb_textArea.append(strArr[35] + "\n\n");
		}
	}
}
