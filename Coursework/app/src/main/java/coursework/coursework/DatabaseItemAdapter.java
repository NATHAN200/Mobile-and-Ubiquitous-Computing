package coursework.coursework;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Nicholas on 13/12/2014.
 */
public class DatabaseItemAdapter extends ArrayAdapter<DatabaseItem> {

    Context context;
    List<DatabaseItem> items;

    public DatabaseItemAdapter(Context context, int layout,List<DatabaseItem> objects)
    {
        super(context,layout,objects);
        this.context = context;
        this.items = objects;
    }

    public View getView(int position, View convertView,ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.databaserowlayout, parent, false);	//Creates the item view
        TextView title = (TextView) rowView.findViewById(R.id.stitle);	//sets the first textview
        title.setTextColor(Color.BLACK);    //sets the text to black
        title.setText(items.get(position).getTitle());	//sets the value of the first textview to the title
        TextView Description = (TextView)rowView.findViewById(R.id.sdescription);
        Description.setTextColor(Color.BLACK);
        Description.setText(items.get(position).getsDescription()); //set the description
        ImageView image = (ImageView) rowView.findViewById(R.id.simage);
        String sImagePath = "drawable/" + items.get(position).getImage().toLowerCase(); //creates image path from the Items image string
        int imgResID = context.getResources().getIdentifier(sImagePath,"drawable","coursework.coursework"); //gets the image from the path
        image.setImageResource(imgResID);//sets image
        return rowView;	//returns the finished view
    }
}
