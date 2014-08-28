import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;


public class Frame extends JFrame{
	
	/**
	 * 
	 */
	private JTextField urlField;
	//private JButton audioButt, videoButt;
	private JButton okButt, downBtn;
	private static final long serialVersionUID = 1L;
	private YouTubeVideo videos;
	private ButtonGroup btnGroup;
	private JRadioButton[] qualityBtn;
	private JPanel radioPanel;
	private JLabel statusBar;
	private int index;

	public Frame() {
		setTitle("YouTube Downloader");
		setSize(500,120);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		
		//create textfield
		createGUI();
	}
	
	private void createGUI() {
		// TODO Auto-generated method stub
		btnGroup = new ButtonGroup();
		radioPanel = new JPanel();
		radioPanel.setBounds(10, 50, 464, 200);
		downBtn = new JButton("Download");
		downBtn.setBounds(0, 0, 0, 0);
		downBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				for(int i=0;i<videos.get_number_of_videos();i++){
					if(qualityBtn[i].isSelected()){
						index = i;
						SwingWorker<Void, Void> worker = new SwingWorker<Void,Void>(){

							@Override
							protected Void doInBackground() throws Exception {
								// TODO Auto-generated method stub
								int cnt = 0;
								while(true){
									cnt++;
									if(Downloader.downloadWithUpdate(videos.get_video(index).get_parameter("url"), videos.get_title() + "." + videos.get_video(index).get_extension(), statusBar)>0){
										statusBar.setText("Downloaded " + videos.get_title() + "." + videos.get_video(index).get_extension());
										break;
									}
									if(cnt > 110){
										statusBar.setText("Error: Network problem");
										break;
									}
								}
								return null;
							}
							
						};
						worker.run();
						break;
					}
				}
			}
		});
		statusBar = new JLabel("Ready");
		statusBar.setBounds(0, this.getSize().height-60,485,20);
		statusBar.setBorder(BorderFactory.createEtchedBorder());
		urlField = new JTextField(20);
		urlField.setBounds(10, 10, 340, 30);
		okButt = new JButton("OK");
		okButt.setBounds(360, 10, 112, 27);
		okButt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String text = urlField.getText();
				videos = new YouTubeVideo(text);
				if(videos.get_error().equals("OK")){
					statusBar.setText("Getting video indo");
					int numberOf = videos.get_number_of_videos();
					System.out.println(videos.get_title());
					qualityBtn = new JRadioButton[numberOf];
					radioPanel.setLayout(new GridLayout(numberOf,1));
					radioPanel.setBorder(BorderFactory.createTitledBorder(
                                         BorderFactory.createEtchedBorder(), videos.get_title()));
					for(int i=0;i<numberOf;i++){
						System.out.println("type: "+videos.get_video(i).get_parameter("type") + " " + " quality: " + 
								videos.get_video(i).get_parameter("quality") + " url: " +
								videos.get_video(i).get_parameter("url"));
						qualityBtn[i] = new JRadioButton(videos.get_video(i).get_parameter("type")+"; quality: " +
														videos.get_video(i).get_parameter("quality"),true);
						btnGroup.add(qualityBtn[i]);
						radioPanel.add(qualityBtn[i]);
					}
				Dimension size = radioPanel.getSize();
				setWindowSize(500, 130 + size.height + 30);
				statusBar.setBounds(0, getWindowSize()[1]-60,485,20);
				downBtn.setBounds(150, size.height + 50, 200, 30);
				statusBar.setText("Ready");
				}else {
					System.out.println(videos.get_error());
					statusBar.setText(videos.get_error());
				}
			}
			
		});
		/*audioButt = new JButton("Audio");
		audioButt.setBounds(10, 50, 100, 30);
		videoButt = new JButton("Video");
		videoButt.setBounds(120, 50, 100, 30);*/
		this.add(urlField);
		this.add(okButt);
		this.add(statusBar);
		this.add(radioPanel);
		this.add(downBtn);
		//this.add(audioButt);
		//this.add(videoButt);
	}
	
	private void setWindowSize(int x, int y){
		this.setSize(x, y);
	}
	private int[] getWindowSize() {
		int[] arr = new int[2];
		arr[0] = this.getSize().width;
		arr[1] = this.getSize().height;
		return arr;
	}

}
