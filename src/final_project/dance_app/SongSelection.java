package final_project.dance_app;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SongSelection extends Activity {

	private String TAG = "Week5_CustomListView";

	private ListView listOfSongs;

	Integer difficulty;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song_selection);

		// get a handle to the ListView
		listOfSongs = (ListView)findViewById(R.id.songlist);

// OLD METHOD - shows just the Names
/*
		// give the ListView a list of cats to display
		listOfCats.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CatList.getAllCatNames()));
		
*/
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			difficulty = (Integer) extras.getSerializable("difficulty");
		}
		else
		{
			difficulty = 1;
		}
		
// END of OLD METHOD
	
// NEW METHOD - use a custom adapter		
		SongArrayAdapter songArrayAdapter = new SongArrayAdapter(this, R.layout.songlist_row, SongList.getArrayListOfSongs());
        listOfSongs.setAdapter(songArrayAdapter);
        songArrayAdapter.notifyDataSetChanged();

		listOfSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			// The onItemClick method gets called when the user taps
			//   on one of the list items
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

// for old method
//				String selected = (String)adapter.getItemAtPosition(position);
//				
//				Log.i(TAG, "Cat selected: " + selected);
//
//				showDetailActivity(CatList.getCatForName(selected));
				
				Song selected = (Song)adapter.getItemAtPosition(position);
				Log.i(TAG, "Song selected: " + selected.song_name);
				showDetailActivity(selected);
			}

		});
	
	}
		
	private void showDetailActivity(Song song) {
		Intent activityIntent = new Intent(SongSelection.this, PlayGame.class);
		activityIntent.putExtra("difficulty", difficulty);
		activityIntent.putExtra("song", song);
		startActivity(activityIntent);
	}
	
	class LoadListInBackground extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}

}
