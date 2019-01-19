package amata1219.amachat.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import amata1219.amachat.Amachat;
import amata1219.amachat.Util;

public final class GoogleIME implements Processor {

	public static final String NAME = "GoogleIME";

	private static final String URL = "http://www.google.com/transliterate?langpair=ja-Hira|ja&text=";
	private static final Map<String, String[]> TABLE;
	static{
		Map<String, String[]> map = new HashMap<>();
		map.put("", Util.toArguments("あ","い","う","え","お"));
		map.put("k", Util.toArguments("か","き","く","け","こ"));
		map.put("s", Util.toArguments("さ","し","す","せ","そ"));
		map.put("t", Util.toArguments("た","ち","つ","て","と"));
		map.put("n", Util.toArguments("な","に","ぬ","ね","の"));
		map.put("h", Util.toArguments("は","ひ","ふ","へ","ほ"));
		map.put("m", Util.toArguments("ま","み","む","め","も"));
		map.put("y", Util.toArguments("や","い","ゆ","いぇ","よ"));
		map.put("r", Util.toArguments("ら","り","る","れ","ろ"));
		map.put("w", Util.toArguments("わ","うぃ","う","うぇ","を"));
		map.put("g", Util.toArguments("が","ぎ","ぐ","げ","ご"));
		map.put("z", Util.toArguments("ざ","じ","ず","ぜ","ぞ"));
		map.put("j", Util.toArguments("じゃ","じ","じゅ","じぇ","じょ"));
		map.put("d", Util.toArguments("だ","ぢ","づ","で","ど"));
		map.put("b", Util.toArguments("ば","び","ぶ","べ","ぼ"));
		map.put("p", Util.toArguments("ぱ","ぴ","ぷ","ぺ","ぽ"));
		map.put("gy", Util.toArguments("ぎゃ","ぎぃ","ぎゅ","ぎぇ","ぎょ"));
		map.put("gw", Util.toArguments("ぐぁ","ぐぃ","ぐぅ","ぐぇ","ぐぉ"));
		map.put("zy", Util.toArguments("じゃ","じぃ","じゅ","じぇ","じょ"));
		map.put("jy", Util.toArguments("じゃ","じぃ","じゅ","じぇ","じょ"));
		map.put("dy", Util.toArguments("ぢゃ","ぢぃ","ぢゅ","ぢぇ","ぢょ"));
		map.put("dh", Util.toArguments("でゃ","でぃ","でゅ","でぇ","でょ"));
		map.put("dw", Util.toArguments("どぁ","どぃ","どぅ","どぇ","どぉ"));
		map.put("by", Util.toArguments("びゃ","びぃ","びゅ","びぇ","びょ"));
		map.put("py", Util.toArguments("ぴゃ","ぴぃ","ぴゅ","ぴぇ","ぴょ"));
		map.put("v", Util.toArguments("ヴぁ","ヴぃ","ヴ","ヴぇ","ヴぉ"));
		map.put("vy", Util.toArguments("ヴゃ","ヴぃ","ヴゅ","ヴぇ","ヴょ"));
		map.put("sh", Util.toArguments("しゃ","し","しゅ","しぇ","しょ"));
		map.put("sy", Util.toArguments("しゃ","し","しゅ","しぇ","しょ"));
		map.put("c", Util.toArguments("か","し","く","せ","こ"));
		map.put("ch", Util.toArguments("ちゃ","ち","ちゅ","ちぇ","ちょ"));
		map.put("cy", Util.toArguments("ちゃ","ち","ちゅ","ちぇ","ちょ"));
		map.put("f", Util.toArguments("ふぁ","ふぃ","ふ","ふぇ","ふぉ"));
		map.put("fy", Util.toArguments("ふゃ","ふぃ","ふゅ","ふぇ","ふょ"));
		map.put("fw", Util.toArguments("ふぁ","ふぃ","ふ","ふぇ","ふぉ"));
		map.put("q", Util.toArguments("くぁ","くぃ","く","くぇ","くぉ"));
		map.put("ky", Util.toArguments("きゃ","きぃ","きゅ","きぇ","きょ"));
		map.put("kw", Util.toArguments("くぁ","くぃ","く","くぇ","くぉ"));
		map.put("ty", Util.toArguments("ちゃ","ちぃ","ちゅ","ちぇ","ちょ"));
		map.put("ts", Util.toArguments("つぁ","つぃ","つ","つぇ","つぉ"));
		map.put("th", Util.toArguments("てゃ","てぃ","てゅ","てぇ","てょ"));
		map.put("tw", Util.toArguments("とぁ","とぃ","とぅ","とぇ","とぉ"));
		map.put("ny", Util.toArguments("にゃ","にぃ","にゅ","にぇ","にょ"));
		map.put("hy", Util.toArguments("ひゃ","ひぃ","ひゅ","ひぇ","ひょ"));
		map.put("my", Util.toArguments("みゃ","みぃ","みゅ","みぇ","みょ"));
		map.put("ry", Util.toArguments("りゃ","りぃ","りゅ","りぇ","りょ"));
		map.put("l", Util.toArguments("ぁ","ぃ","ぅ","ぇ","ぉ"));
		map.put("x", Util.toArguments("ぁ","ぃ","ぅ","ぇ","ぉ"));
		map.put("ly", Util.toArguments("ゃ","ぃ","ゅ","ぇ","ょ"));
		map.put("lt", Util.toArguments("た","ち","っ","て","と"));
		map.put("lk", Util.toArguments("ヵ","き","く","ヶ","こ"));
		map.put("xy", Util.toArguments("ゃ","ぃ","ゅ","ぇ","ょ"));
		map.put("xt", Util.toArguments("た","ち","っ","て","と"));
		map.put("xk", Util.toArguments("ヵ","き","く","ヶ","こ"));
		map.put("wy", Util.toArguments("わ","ゐ","う","ゑ","を"));
		map.put("wh", Util.toArguments("うぁ","うぃ","う","うぇ","うぉ"));
		TABLE = Collections.unmodifiableMap(map);
	}

	private GoogleIME(){

	}

	public static void load(){
		if(Amachat.getConfig().getConfiguration().getBoolean("GoogleIME.Enable"))
			ProcessorManager.registerProcessor(new GoogleIME());
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String process(String text) {
		return GoogleIME.japanize(text);
	}

	public static boolean canJapanize(String text){
		return text.length() == text.getBytes().length || text.matches("[ \\uFF61-\\uFF9F]+");
	}

	public boolean checkJapanize(){
		return process("konnnitiha") != null;
	}

	public static String japanize(String text){
		StringBuilder builder = new StringBuilder();
		String line = "";
		for(int i = 0; i < text.length(); i++){
			String tmp = text.substring(i, i + 1);
			switch(tmp){
			case "a":
				builder.append(getHiragana(line, 0));
				line = "";
				break;
			case "i":
				builder.append(getHiragana(line, 1));
				line = "";
				break;
			case "u":
				builder.append(getHiragana(line, 2));
				line = "";
				break;
			case "e":
				builder.append(getHiragana(line, 3));
				line = "";
				break;
			case "o":
				builder.append(getHiragana(line, 4));
				line = "";
				break;
			default:
				if(line.equals("n") && !line.equals("y")){
					builder.append("ん");
					line = "";
					if(tmp.equals("n"))
						continue;
				}
				char firstChar = tmp.charAt(0);
				if(Character.isLetter(firstChar)){
					if(Character.isUpperCase(firstChar)){
						builder.append(line + tmp);
						line = "";
					}else if(line.equals(tmp)){
						builder.append("っ");
						line = tmp;
					}else{
						line = line + tmp;
					}
				}else{
					switch(tmp){
					case ",":
						builder.append(line + "、");
						break;
					case ".":
						builder.append(line + "。");
						break;
					case "-":
						builder.append(line + "ー");
						break;
					case "!":
						builder.append(line + "！");
						break;
					case "?":
						builder.append(line + "？");
						break;
					case "[":
						builder.append(line + "「");
						break;
					case "]":
						builder.append(line + "」");
						break;
					default:
						break;
					}
					line = "";
				}
				break;
			}
		}

		text = builder.append(line).toString();

		if(text.isEmpty())
			return "";

		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try{
			URL url = new URL(URL + URLEncoder.encode(text, "UTF-8"));
			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			connection.setInstanceFollowRedirects(false);

			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			line = "";
			builder.setLength(0);
			while((line = reader.readLine()) != null){
				StringBuilder mBuilder = new StringBuilder();
				int mLine = 0, mIndex = 0;
				while(mIndex < line.length()){
					int start = 0, end = 0;
					if(mLine < 3){
						start = line.indexOf("[", mIndex);
						end = line.indexOf("]", mIndex);
						if(start == -1)
							break;

						if(start < end){
							mLine++;
							mIndex = start + 1;
						}else{
							mLine--;
							mIndex = end + 1;
						}
					}else{
						start = line.indexOf("\"", mIndex);
						end = line.indexOf("\"", start + 1);
						if(start == -1 || end == -1)
							break;

						mBuilder.append(line.substring(start, end + 1));
						int n = line.indexOf("[", end);
						if(n == -1)
							break;

						mLine--;
						mIndex = n + 1;
					}
				}
				builder.append(mBuilder.toString());
			}

			return builder.toString();
		}catch(MalformedURLException | ProtocolException e){
			e.printStackTrace();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	private static String getHiragana(String s, int index){
		return TABLE.containsKey(s) ? TABLE.get(s)[index] : s + TABLE.get("")[index];
	}

}
