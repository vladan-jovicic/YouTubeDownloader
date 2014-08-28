import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


public class YouTubeURL {
	
	private Map<String, String> parameters;
	private Map<String, String> urlParameters;
	
	@SuppressWarnings("deprecation")
	public YouTubeURL(String url){
		parameters = new HashMap<String, String>();
		urlParameters = new HashMap<String, String>();
		String[] tmp = url.split("&");
		for(int i=0;i<tmp.length;i++){
			String[] kv = tmp[i].split("=");
			parameters.put(kv[0], kv[1]);
		}
		parameters.put("type", URLDecoder.decode((String) parameters.get("type")));
		String tmpUrl = URLDecoder.decode((String) parameters.get("url"));
		tmp = tmpUrl.substring(tmpUrl.indexOf("?")+1).split("&");
		for(int i=0;i<tmp.length;i++){
			String[] kv = tmp[i].split("=");
			urlParameters.put(kv[0], kv[1]);
		}
		parameters.put("url", URLDecoder.decode( (String) parameters.get("url") ) );
	}
	
	public String get_parameter(String key){
		return (String)parameters.get(key);
	}
	
	public String get_url_parameter(String key){
		return (String) urlParameters.get(key);
	}
	public String get_extension(){
		String ext = ((String) parameters.get("type")).split(";")[0].split("\\/")[1];
		if(ext.equals("x-flv"))return "flv";
		else return ext;
	}
	
}
