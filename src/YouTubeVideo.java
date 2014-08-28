import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;


public class YouTubeVideo {
	private String url;
	private String id = null;
	private String title = null;
	private String error;
	private YouTubeURL[] all_videos;
	//private String[] urls;
	public YouTubeVideo(String url) {
		this.url = url;
		getVideoIdFromUrl();
		if(this.id != null){
			getVideoInfoFile();
			if(this.error == null) {
				decodeVideoInfoFile();
				if(this.error == null) {
					this.error = "OK";
				}
			}
		}
		//downloadVideo();
	}
	private void getVideoIdFromUrl() {
		// TODO Auto-generated method stub
		String[] parameters;
		try {
			parameters = this.url.split("\\?")[1].split("\\&");
		}catch(Exception e) {
			this.error = "Error: Invalid url";
			return;
		}
		//String[] param2 = parameters[1].split("&");
		//boolean success = false;
		for(int i=0; i<parameters.length;i++){
			System.out.println(parameters[i]);
			if(parameters[i].startsWith("v=")) {
				String[] tmp = parameters[i].split("=");
				if(tmp.length>1){
					this.id = tmp[1];
				}
			}
		}
		System.out.println(this.id);
	}
	private void getVideoInfoFile() {
		// TODO Auto-generated method stub
		String fileUrl = "http://youtube.com/get_video_info?video_id=" + this.id;
		int cnt = 0;
		while(true){
			cnt++;
			if(Downloader.download(fileUrl, this.id + ".ytd") > 0) {
				break;
			}
			if(cnt > 110) {
				break;
			}
		}
		if(cnt > 110){
			this.error = "Error: Connection problem";
			//return false;
		}
		//return true;
	}
	@SuppressWarnings("deprecation")
	private void decodeVideoInfoFile(){
		String content = null;
		File infoFile = null;
		try {
			infoFile = new File(this.id + ".ytd");
		}catch (Exception e){
			e.printStackTrace();
			this.error = "Error: Can not load downloaded info file";
		}
		
		//File infoFile = new File("nesto.ytd");
		try {
			FileReader rd = new FileReader(infoFile);
			char[] chars = new char[(int) infoFile.length()];
			rd.read(chars);
			content = new String(chars);
			rd.close();
		}catch (IOException e){
			e.printStackTrace();
			this.error = "Error: Can not read content of downloaded info file";
		}
		String[] newContent = content.split("&");
		String fmt_stream_map = null;
		for(int i=0;i<newContent.length;i++){
			if(newContent[i].startsWith("url_encoded_fmt_stream_map")){
				fmt_stream_map = newContent[i].split("=")[1];
			}
			if(newContent[i].startsWith("title")){
				this.title = URLDecoder.decode(newContent[i].split("=")[1]);
			}
			if(newContent[i].startsWith("reason")){
				this.error = URLDecoder.decode(newContent[i].split("=")[1]);
			}
		}
		String[] allUrls;
		if(fmt_stream_map != null){
			allUrls = URLDecoder.decode(fmt_stream_map).split(",");
			all_videos = new YouTubeURL[allUrls.length];
			for(int i=0;i<allUrls.length;i++){
				all_videos[i] = new YouTubeURL(allUrls[i]);
			}
			//System.out.println(allUrls[0]);
		}
	}
	public int get_number_of_videos() {
		return all_videos.length;
	}
	public YouTubeURL get_video(int index) {
		return all_videos[index];
	}
	public String get_title(){
		return this.title;
	}
	public String get_error(){
		return this.error;
	}
	
}
