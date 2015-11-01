package final_project.dance_app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	String song_name;
	String artist_name;
	String song_uri;
	boolean fromDevice = true;
	int difficulty;
	RatingBar diff;
	private static final int ACTIVITY_CHOOSE_FILE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		diff = (RatingBar)findViewById(R.id.difficulty);
	}
	
	public void onClickOpenMenu(View view)
	{
			difficulty = (int) diff.getRating();
	
			// Create a new intent to launch the new Activity
		    Intent activityIntent = new Intent(MainActivity.this, SongSelection.class);
		    
		    activityIntent.putExtra("difficulty", difficulty);
			
			// Start the Activity using that intent
			startActivity(activityIntent);
			
		
	}
	public void onClickAddSong(View view)
	{
			
	}

	public void startDetailActivity(Song song)
	{
		Intent activityIntent = new Intent(MainActivity.this, PlayGame.class);
		activityIntent.putExtra("song", song);
		startActivity(activityIntent);
		finish();
	}
	
	
}
