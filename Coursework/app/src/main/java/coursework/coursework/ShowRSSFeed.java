package coursework.coursework;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


/**
 * Created by Nicholas on 29/11/2014.
 */
public class ShowRSSFeed extends Activity implements View.OnClickListener {

    Button Back;    //Button to take us back to the main menu
    Button Refresh; //Refresh the rss feed Button
    ListView theList;   //List view to display items
    String URL; //THe RSS feed URL
    FragmentManager dialogmanager;
    public void onCreate(Bundle SavedInstanceState)
    {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.display_feed);
        dialogmanager = this.getFragmentManager();
        URL = "http://feeds.bbci.co.uk/news/rss.xml";   //set the URL
        Back = (Button)findViewById(R.id.RSSBack);
        Back.setOnClickListener(this);
        Refresh = (Button)findViewById(R.id.RssRefresh);
        Refresh.setOnClickListener(this);
        theList = (ListView)findViewById(R.id.RSSList);
        //User clicked on an item in the list
        theList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Item i  = (Item)theList.getAdapter().getItem(position); //Get the Item from the adapter
            Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(i.getLink()));    //Open the browser with the URL from the Item selected
            startActivity(openBrowser);
            }
        });
        loadRSS();  //Loads the feed

    }

    public void loadRSS() {
        //Creates a progress bas to display while we load the rss feed.
        ProgressDialog progressbar = new ProgressDialog(this);
        //Creates a new loadRSSfeed object with the url, list view and progress bar to use
        LoadRSSFeed loader = new LoadRSSFeed(getApplicationContext(),URL,progressbar, theList);
        loader.execute(""); //Load the rss feed
    }

    public void onClick(View v)
    {
        if(v instanceof Button)
        {
            //Clicked on a button to play button press sound
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.buttonpress);
            mp.start();
            //Usr wants to go back to the main menu
            if(v == Back)
            {
                finish();
            }
            //User wants to refresh the rss feed
            if(v == Refresh)
            {
                //Reload the rss feed
                loadRSS();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.back) {
            finish();
        }
        if(id == R.id.mainAbout)
        {
            String message  = "Shows a list of the BBC's Top news Stories \n" +
                    "\nClick Refresh to refresh with the latest stories\n" +
                    "\nClick on an Item to Open the webpage for that story"+
                    "\nClick Back to return to the Main Menu";
            DialogFragment dialog = AboutDialog.newInstance(message);
            dialog.show(dialogmanager, "tag");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
