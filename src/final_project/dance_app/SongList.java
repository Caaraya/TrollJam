package final_project.dance_app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SongList {
	private static String TAG = "Final_Project";
/*
	public static final String FLD_ID = "_id";
	public static final String FLD_SONG_NAME = "song_name";
	public static final String FLD_FILE_NAME = "file_name";
	public static final String FLD_ARTIST = "artist";
	public static final String FLD_FROM_DEVICE = "from_device";
	// Cat(int id, String name, String claimToFame, String pictureURL) {

*/
static Song[] allSongs = new Song[] { 
	new Song(1, "Cyber Rush", "Dan Johansen", "605201_Cyber-Rush.mp3"),
	new Song(2, "Enchanted", "Bafana", "604482_Bafana---Enchanted-.mp3"),
	new Song(3, "Crop Duster", "Waterflame", "603944_-CropDuster-.mp3"),
	new Song(4, "Cosmic Jun", "Bafana", "604483_Bafana---Cosmic-Jun.mp3"),
	new Song(5, "Just Press", "Bafana", "604486_Bafana---Just-Press.mp3"),
	new Song(6, "Soul Techno", "Deshiel", "606456_Soul-Techno.mp3"),
	};
	
public static String[] getAllSongNames() {
	
	List<String> allNames = new ArrayList<String>();

	for (Song song : allSongs) {
		allNames.add(song.song_name);
	}
	
	// Convert to an array of Strings
	String[] allNamesArray = new String[allNames.size()];
	return allNames.toArray(allNamesArray);
}

public static Song[] getAllSongs() {
	return allSongs;
}

public static ArrayList<Song> getArrayListOfSongs() {
	return new ArrayList<Song>(Arrays.asList(allSongs));
}

public static Song getSongsForName(String songName) {
	Song result = null;
	
	if (songName == null) {
		return null;
	}
	
	for (Song song : allSongs) {
		
		if (songName.equalsIgnoreCase(song.song_name)) {
			result = song;
			break;
		}
	}
	
	return result;
}

}