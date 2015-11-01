package final_project.dance_app;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayGame extends Activity {

private String TAG = "Game";
	boolean earlyRelease = false;
	BitmapDrawable frame1;
	BitmapDrawable frame2;
	BitmapDrawable frame3;
	BitmapDrawable frame4;
	boolean ended = false;
	TranslateAnimation move;
	private TextView title;
	private TextView artist;
	private TextView directions;
	private ImageView down_arrow;
	private ImageView left_arrow;
	private ImageView right_arrow;
	private ImageView up_arrow;
	private ImageView down_move;
	private ImageView left_move;
	private ImageView right_move;
	private ImageView up_move;
	private ImageView stillAnimation;
	private ImageView playerAnimation;
	private ImageView beatAnimation;
	private Song song;
	long secondFraction;
	BeatDetector beat;
	MediaPlayer player = null;
	AnimationDrawable Animation;
	int currentBeat = 0;
	int arrowHelperVariable = 0;
	int clickCounter = 0;
	boolean from_device;
	boolean downVisible = false;
	boolean upVisible = false;
	boolean leftVisible = false;
	boolean rightVisible = false;
	boolean missed = true;
	int missCount = 0;
    int songLength = 240;
    HandlerThread hThread;
    int difficulty;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_game);
		
		directions = (TextView)findViewById(R.id.gameResult);
		down_move = (ImageView)findViewById(R.id.downMove);
		left_move = (ImageView)findViewById(R.id.leftMove);
		right_move = (ImageView)findViewById(R.id.rightMove);
		up_move = (ImageView)findViewById(R.id.upMove);
		down_arrow = (ImageView)findViewById(R.id.downArrow);
		left_arrow = (ImageView)findViewById(R.id.leftArrow);
		right_arrow = (ImageView)findViewById(R.id.rightArrow);
		up_arrow = (ImageView)findViewById(R.id.upArrow);
		title = (TextView)findViewById(R.id.song);
		artist = (TextView)findViewById(R.id.song_by);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			song = (Song) extras.getSerializable("song");
			difficulty = (Integer) extras.getSerializable("difficulty");
			if(song.id == 7)
			{
				from_device = true;
			}
			player = startSong(song.file_name);
			artist.setText("By " + song.artist);
			title.setText(song.song_name);	
		}
		else
		{
			difficulty = 0;
			title.setText("No song available");
			artist.setText("No artist available");
		}
		AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE); 
		if (audioManager.isMicrophoneMute())
			audioManager.setMicrophoneMute(false);
		secondFraction = speed(difficulty);
		
		hThread = new HandlerThread("HandlerThread");
		hThread.start();
		final Handler handler = new Handler(hThread.getLooper());
		Runnable eachFractonOfSecond = new Runnable() {	
			@Override
			public void run() {
				Log.d(TAG, "task executing");
				if(beat.setDanceMove() != 0)
				{
					//showAlert("Beat", beat.setDanceMove() + "yea");
					currentBeat = beat.setDanceMove();
					songLength -= 1;
					if(songLength == 0 && !earlyRelease && !ended)
					{ 
						quitGameActivity();
					}
					//directions.setText(currentBeat);
				}
				handler.postDelayed(this, secondFraction);
				//directions.setText("Current Beat: "+currentBeat);
			}
		};
		
		final Handler h = new Handler();
		final int delay = (int) secondFraction; //milliseconds

		h.postDelayed(new Runnable(){
		    @SuppressWarnings("deprecation")
			public void run(){
		        if(currentBeat != 0)
		        { 
		        if(missed = true)
		        	missCount += 1;
		        missed = true;
		        ArrayList<BitmapDrawable> BeatFrames = animateBeat(currentBeat);
	  			Animation = new AnimationDrawable();
	  			animate(BeatFrames.get(0),BeatFrames.get(1),BeatFrames.get(2),BeatFrames.get(3));
	  			beatAnimation.setBackgroundDrawable(Animation);
	  			Animation.start();
	  			
	  			ArrayList<BitmapDrawable> PartnerFrames = animatePartner(arrowHelperVariable);
	  			Animation = new AnimationDrawable();
	  			animate(PartnerFrames.get(0),PartnerFrames.get(1),PartnerFrames.get(2),PartnerFrames.get(3));
	  			stillAnimation.setBackgroundDrawable(Animation);
	  			Animation.start();
			    clickCounter += 1;
			    chooseAnimation(currentBeat);
			    if(currentBeat == 1)
			    {
		        	 down_move.setVisibility(View.VISIBLE);
		        	 down_move.startAnimation(move);
			    }
		         else
		        	 down_move.setVisibility(View.INVISIBLE);
		         if(currentBeat == 2)
		         {
		        	 left_move.setVisibility(View.VISIBLE);
		        	 left_move.startAnimation(move);
		         }
		         else
		        	 left_move.setVisibility(View.INVISIBLE);
		         if(currentBeat == 3)
		         {
		        	 right_move.setVisibility(View.VISIBLE);
		        	 right_move.startAnimation(move);
		         }
		         else
		        	 right_move.setVisibility(View.INVISIBLE);
		         if(currentBeat == 4)
		         {
		        	 up_move.setVisibility(View.VISIBLE);
		        	 up_move.startAnimation(move);
		         }
		         else
		        	 up_move.setVisibility(View.INVISIBLE);
		         if(arrowHelperVariable == 1)
		         {
		        	 down_arrow.setVisibility(View.VISIBLE);
		         	 downVisible = true;
		         }
		         else
		         {
		        	 down_arrow.setVisibility(View.INVISIBLE);
		        	 downVisible = false;
		         }	 
		         if(arrowHelperVariable == 2)
		         {
		        	 left_arrow.setVisibility(View.VISIBLE);
		        	 leftVisible = true;
		         }
		         else
		         {
		        	 left_arrow.setVisibility(View.INVISIBLE);
		        	 leftVisible = false;
		        	 
		         }
		         if(arrowHelperVariable == 3)
		         {
		        	 right_arrow.setVisibility(View.VISIBLE);
		        	 rightVisible = true;
		         }
		         else
		         {
		        	 right_arrow.setVisibility(View.INVISIBLE);
		        	 rightVisible = false;
		         }
		         if(arrowHelperVariable == 4)
		         {
		        	 up_arrow.setVisibility(View.VISIBLE);
		        	 upVisible = true;
		         }
		         else
		         {
		        	 up_arrow.setVisibility(View.INVISIBLE);
		        	 upVisible = false;
		         }
		        }
		       // directions.setText("Jammin to " + song.song_name);
		        h.postDelayed(this, delay);
		        if(missCount >= (allowedMistakes(difficulty) - 3))
		        	directions.setText("CLICK LIKE NEVER BEFORE");
		        else
		        	directions.setText("Jammin to " + song.song_name);
		        
		        if(missCount == allowedMistakes(difficulty) && songLength != 0)
		        {
		        	if(!ended)
		        	{
		        		earlyRelease = true;
		        		quitGameActivity();
		        	}
					
		        }
		        arrowHelperVariable = currentBeat;
		    }
		}, delay);
		

			handler.postDelayed(eachFractonOfSecond, secondFraction);
			
	       // directions.setText("Current Beat: "+currentBeat);
			up_arrow.setOnClickListener(new View.OnClickListener() {        
		        @SuppressWarnings("deprecation")
				@Override
		           public void onClick(View view) {
		            if (view == findViewById(R.id.upArrow)) {     
		                   if(upVisible == true)
		                   {
		                	   	missed = false;
		                	   	missCount = 0;
		                	   	clickCounter -= 1;
		                	   	frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.up1);
		                		frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.up2);
		                		frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.up3);
		                		frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.up2);
			  				    Animation = new AnimationDrawable();
			  				    animate(frame1,frame2,frame3,frame4);
				  				playerAnimation.setBackgroundDrawable(Animation);
			  		         	Animation.start();
			  			        directions.setText("getting up with " + song.song_name);
		                	    upVisible = false;
		                   }
		            }
		            }
		        });
			down_arrow.setOnClickListener(new View.OnClickListener() {        
		        @SuppressWarnings("deprecation")
				@Override
		           public void onClick(View view) {
		            if (view == findViewById(R.id.downArrow)) {     
		            	if(downVisible == true)
		                   {
		            		   missed = false;
		            		   missCount = 0;
		                	   clickCounter -= 1;
		                	   frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.down1);
			  				   frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.down2);
			  				   frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.down3);
			  				   frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.down4);
			  				   Animation = new AnimationDrawable();
			  				   animate(frame1,frame2,frame3,frame4);
			  				   playerAnimation.setBackgroundDrawable(Animation);
			  				   Animation.start();
			  			       directions.setText("getting down with " + song.song_name);
		                	   downVisible = false;
		                   }
		            }
		            }
		        });
			left_arrow.setOnClickListener(new View.OnClickListener() {        
		        @SuppressWarnings("deprecation")
				@Override
		           public void onClick(View view) {
		            if (view == findViewById(R.id.leftArrow)) {     
		            	if(leftVisible == true)
		                   {
		            		   missed = false;
		            		   missCount = 0;
		                	   clickCounter -= 1;
		                	   frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.left1);
			  				   frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.left2);
			  				   frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.left3);
			  				   frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.left4);
			  				   Animation = new AnimationDrawable();
			  				   animate(frame1,frame2,frame3,frame4);
			  				   playerAnimation.setBackgroundDrawable(Animation);
			  				   Animation.start();
			  				   directions.setText("Jamm to the left with " + song.song_name);
		                	   leftVisible = false;
		                   }
		            }
		            }
		        });
			right_arrow.setOnClickListener(new View.OnClickListener() {        
		        @SuppressWarnings("deprecation")
				@Override
		           public void onClick(View view) {
		            if (view == findViewById(R.id.rightArrow)) {     
		            	if(rightVisible == true)
		                   {
		            		   missed = false;
		            		   missCount = 0;
		                	   clickCounter -= 1; 
		                	   frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.right1);
			  				   frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.right2);
			  				   frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.right3);
			  				   frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.right4);
			  				   Animation = new AnimationDrawable();
			  				   animate(frame1,frame2,frame3,frame4);
			  				   playerAnimation.setBackgroundDrawable(Animation);
			  				   Animation.start();
			  				   directions.setText("Jamm to the right with " + song.song_name);
		                	   rightVisible = false;
		                   }

		            }
		            }
		        });/*
			player.setOnCompletionListener(new
				    OnCompletionListener() {        
				        @Override
				        public void onCompletion(MediaPlayer arg0) {
				        	Intent activityIntent = new Intent(PlayGame.this, EndGameActivity.class);

				    		activityIntent.putExtra("score", (clickCounter - 1));
				    		
				    		// Start the Activity using that intent
				    		startActivity(activityIntent);               
				    }
				});
			
	        	*/
				stillAnimation = (ImageView) findViewById(R.id.showMoves);
				playerAnimation = (ImageView) findViewById(R.id.showYourMoves);
				beatAnimation = (ImageView) findViewById(R.id.beat);
			    
	}
	public void quitGameActivity()
	{
		ended = true;
    	Intent activityIntent = new Intent(PlayGame.this, EndGameActivity.class);
    	activityIntent.putExtra("early_release", earlyRelease);
		activityIntent.putExtra("score", (clickCounter - 1));
		activityIntent.putExtra("difficulty", difficulty);
		// Start the Activity using that intent
		startActivity(activityIntent);       
		finish();
	}

	
	public MediaPlayer startSong(String song)
	{
		
		try {

		    player = new MediaPlayer();
		    AssetFileDescriptor afd;
		    if(from_device != true)
		    {
		    	afd = getAssets().openFd(song);
			    player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			    player.prepare();
		    }
		    else
		    {
		    	Uri myUri1 = Uri.parse(song);
		        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		        try {
		            player.setDataSource(getApplicationContext(), myUri1);
		        } catch (IllegalArgumentException e) {
		            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
		        } catch (SecurityException e) {
		            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
		        } catch (IllegalStateException e) {
		            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        try {
		            player.prepare();
		        } catch (IllegalStateException e) {
		            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
		        } catch (IOException e) {
		            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
		        }
		    }
		    player.start();
		    beat = new BeatDetector();
		    beat.link(player);
		    } 
		catch (IllegalArgumentException e) {    } 
		catch (IllegalStateException e) { } 
		catch (IOException e) { } 
		
		return player;

		
	}
private void animate(BitmapDrawable frame1, BitmapDrawable frame2, BitmapDrawable frame3, BitmapDrawable frame4)
{
	Animation.addFrame(frame1, (int)secondFraction/8);
    Animation.addFrame(frame2, (int)secondFraction/8);
    Animation.addFrame(frame3, (int)secondFraction/8);
    Animation.addFrame(frame4, (int)secondFraction/4);
    Animation.addFrame(frame3, (int)secondFraction/8);
    Animation.addFrame(frame2, (int)secondFraction/8);
    Animation.addFrame(frame1, (int)secondFraction/8);
	
}
private void chooseAnimation(int beat)
{

	if(beat == 1)
		move = new TranslateAnimation(0, 0, -200, 0);
	if(beat == 2)
		move = new TranslateAnimation(100, 0, 0, 0);
	if(beat == 3)
		move = new TranslateAnimation(-100, 0, 0, 0);
	if(beat == 4)
		move = new TranslateAnimation(0, 0, 200, 0);

		move.setDuration(500);
		move.setFillAfter(false);
	
}
private ArrayList<BitmapDrawable> animateBeat(int beat)
{
	if (beat == 1)
	{
		frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.beat1);
	    frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.beat2);
	    frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.beat3);
	    frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.beat2);
	}
	else
	{
		if (beat == 2)
		{
			frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.beat1);
		    frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.alternate_beat2);
		    frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.alternate_left3);
		    frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.alternate_left4);
		}
		else
		{
			if (beat == 3)
			{
				frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.beat1);
			    frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.alternate_beat2);
			    frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.alternate_right3);
			    frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.alternate_right4);
			}
			else
			{
				frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.beat1);
			    frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.alternate_beat2);
			    frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.alternate_down3);
			    frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.alternate_down4);
			}
		}
	}
	ArrayList<BitmapDrawable> arr = new ArrayList<BitmapDrawable>(); 
	arr.add(frame1);
	arr.add(frame2);
	arr.add(frame3);
	arr.add(frame4);
	return arr;
}
private ArrayList<BitmapDrawable> animatePartner(int beat)
{
	if (beat == 1)
	{
		frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.down1);
	    frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.down2);
	    frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.down3);
	    frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.down4);
	}
	else
	{
		if (beat == 2)
		{
			frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.left1);
		    frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.left2);
		    frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.left3);
		    frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.left4);
		}
		else
		{
			if (beat == 3)
			{
				frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.right1);
			    frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.right2);
			    frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.right3);
			    frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.right4);
			}
			else
			{
				frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.up1);
			    frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.up2);
			    frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.up3);
			    frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.up2);
			}
		}
	}
	ArrayList<BitmapDrawable> arr = new ArrayList<BitmapDrawable>(); 
	arr.add(frame1);
	arr.add(frame2);
	arr.add(frame3);
	arr.add(frame4);
	return arr;
}
	@SuppressLint("NewApi")
	@Override
	public void onStop()
	{
		super.onStop();
		player.stop();
		player.reset();
		player.release();
		hThread.quitSafely();
	}

	public long speed(int diff)
	{
		if(diff == 0)
		{
			return 600;
		}
		if(diff == 1)
		{
			return 500;
		}
		if(diff == 2)
		{
			return 400;
		}
		if(diff == 3)
		{
			return 300;
		}
		return 600;
	}
	
	public long allowedMistakes(int diff)
	{
		if(diff == 0)
		{
			return 12;
		}
		if(diff == 1)
		{
			return 10;
		}
		if(diff == 2)
		{
			return 8;
		}
		if(diff == 3)
		{
			return 6;
		}
		return 12;
	}
	
	public void toSongList(View view)
	{
		ended = true;      
		finish();
	}
	public void toMainMenu(View view)
	{
		ended = true;
    	Intent activityIntent = new Intent(PlayGame.this, MainActivity.class);
		startActivity(activityIntent);       
		finish();
	}

}
