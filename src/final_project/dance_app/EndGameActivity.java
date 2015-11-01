package final_project.dance_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EndGameActivity extends Activity {
	private int score;
	private int difficulty;
	private boolean released;
	private TextView endGameScore;
	private TextView clickScore;
	private ImageView endGameImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_game);
		Bundle extras = getIntent().getExtras();
		endGameScore = (TextView)findViewById(R.id.gameResult);
		clickScore = (TextView)findViewById(R.id.clicks);
		
		if (extras != null) {			
			score = extras.getInt("score");
			difficulty = extras.getInt("difficulty");
			released = extras.getBoolean("early_release");
		}
		else
		{
			difficulty = 0;
			endGameScore.setText("No score available");
		}
		int maxErr = diffSet(difficulty);
		endGameImage =(ImageView)findViewById(R.id.showMoves);
		if(score <= maxErr && released != true)
		{
			endGameImage.setImageResource(R.drawable.smurf_impersonator);
			endGameScore.setText("You defeated the troll");
			clickScore.setText("You missed " + score + " clicks");
		}
		else
		{
			endGameImage.setImageResource(R.drawable.cartoon_troll);
			endGameScore.setText("You couldn't out jam the troll");
			if(released != true)
				clickScore.setText("You missed " + score + " clicks" + "\r\n" + "You need " + (score - maxErr) + " less misses to win!");
			else
				clickScore.setText("Better click more next time");
		}
	}
	public void toMenu(View view)
	{
	
			// Create a new intent to launch the new Activity
		    Intent activityIntent = new Intent(EndGameActivity.this, MainActivity.class);
			
			// Start the Activity using that intent
			startActivity(activityIntent);
		
	}

	public void toSongList(View view)
	{
	
			// Create a new intent to launch the new Activity
		    Intent activityIntent = new Intent(EndGameActivity.this, SongSelection.class);
			
		    activityIntent.putExtra("difficulty", difficulty);
			// Start the Activity using that intent
			startActivity(activityIntent);
		
	}
	public int diffSet(int num)
	{
		if(num == 0)
		{
			return 120;
		}
		if(num == 1)
		{
			return 100;
		}
		if(num == 2)
		{
			return 80;
		}
		if(num == 3)
		{
			return 60;
		}
		return 120;
		
	}
}
