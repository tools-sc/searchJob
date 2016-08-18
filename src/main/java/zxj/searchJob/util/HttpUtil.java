package zxj.searchJob.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	private String path;
	private String charSet;
	
	public HttpUtil(String path,String charSet){
		this.path = path;
		this.charSet = charSet;
	}
	
	public String request(){
		InputStream in = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
			in = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in, charSet));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line= reader.readLine()) != null){
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
				}
				reader = null;
			}
		}
	}
	
}
