package coursework.coursework;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nicholas on 29/11/2014.
 */
public class ItemAdapter extends ArrayAdapter<Item>{

    Context context;
    List<Item> items;

    public ItemAdapter(Context context, int layout,List<Item> objects)
    {
        super(context, layout, objects);
        this.context = context;
        this.items = objects;
    }

    public View getView(int position, View convertView,ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);	//Creates the item view

        TextView title = (TextView) rowView.findViewById(R.id.ItemTitle);	//sets the first textview
        title.setTextColor(Color.BLACK);
        title.setText(items.get(position).getTitle());	//sets the value of the first textview to the title
        TextView Description = (TextView)rowView.findViewById(R.id.ItemDescription);
        Description.setTextColor(Color.BLACK);
        Description.setText(items.get(position).getDescription());
        ImageView image = (ImageView) rowView.findViewById(R.id.ItemImage);	//finds the image view
        ImageLoader loader = new ImageLoader(items.get(position).getImageURL(),image);  //Sets up the image loader to download the image from the URL
        loader.execute();  //Downloads the image and sets the image view
        return rowView;	//returns the finished view
    }
}
