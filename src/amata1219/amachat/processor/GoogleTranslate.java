package amata1219.amachat.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.ws.ProtocolException;

import amata1219.amachat.Amachat;
import net.md_5.bungee.config.Configuration;

public class GoogleTranslate implements Processor {

	public static final String NAME = "GoogleTranslate";

	private String url;

	private GoogleTranslate(){

	}

	public static GoogleTranslate load(){
		GoogleTranslate api = new GoogleTranslate();

		Configuration configuration = Amachat.getConfig().getConfiguration().getSection("GoogleTranslate");
		if(!configuration.getBoolean("Enable"))
			return null;

		api.url = configuration.getString("ScriptURL") + "/exec?text=$1&source=&target=$2";

		if(!api.checkTranslate())
			return null;

		return api;
	}

	@Override
	public String getName() {
		return GoogleTranslate.NAME;
	}

	@Override
	public String process(String text) {
		LanguageCode code = LanguageCode.get(text);
		if(code == null)
			return text;

		return translate(text, code);
	}

	public static boolean canTranslate(String text){
		return LanguageCode.has(text);
	}

	public boolean checkTranslate(){
		return translate("Hello", LanguageCode.JA) != null;
	}

	public String translate(String text, LanguageCode target){
		if(text.isEmpty())
			return "";

		HttpURLConnection con = null;
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		try{
			URL url = new URL(this.url.replace("$2", target.name()).replace("$1", URLEncoder.encode(text, "UTF-8")));

			con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");
			con.setInstanceFollowRedirects(true);

			con.connect();

			reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String line = "";
			while((line = reader.readLine()) != null)
				builder.append(line);

			return builder.toString();
		}catch(MalformedURLException e){
			return text;
		}catch(ProtocolException e){
			return text;
		}catch(IOException e){
			return text;
		}finally{
			if(con != null)
				con.disconnect();

			if(reader != null){
				try{
					reader.close();
				}catch(IOException e){

				}
			}
		}
	}

}
