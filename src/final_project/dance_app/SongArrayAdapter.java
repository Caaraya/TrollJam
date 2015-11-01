package final_project.dance_app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SongArrayAdapter extends ArrayAdapter<Song> {

    Context context; 
    int layoutResourceId;    
    ArrayList<Song> songList = null;
        
    public SongArrayAdapter(Context context, int layoutResourceId, ArrayList<Song> songList) {
        super(context, layoutResourceId, songList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.songList = songList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SongDetailsHolder holder = null;
        
        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new SongDetailsHolder();
            holder.txtName = (TextView)row.findViewById(R.id.name);
            holder.txtArtist = (TextView)row.findViewById(R.id.artist);
            
            row.setTag(holder);
        }
        else
        {
            holder = (SongDetailsHolder)row.getTag();
        }
        
        Song song = this.songList.get(position);
        holder.txtName.setText(song.song_name);
        holder.txtArtist.setText(song.artist);
                
        return row;
    }
    
    static class SongDetailsHolder
    {
        TextView txtName;
        TextView txtArtist;
    }

}