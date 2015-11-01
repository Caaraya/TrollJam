package final_project.dance_app;

import java.io.Serializable;


public class Song implements Serializable {
 
    private static final long serialVersionUID = 1L;
	
	public int id;
	public String song_name;
	public String artist;
	public String file_name;
	//public Boolean from_device;
	
	Song(int id, String song_name, String artist, String file_name) {
		this.song_name = song_name;
		this.artist = artist;
		this.file_name = file_name;
		//this.from_device = from_device;
	}
	
	/*public Boolean isFromDevice(Song song)
	{
		return song.from_device;
	}*/
}
