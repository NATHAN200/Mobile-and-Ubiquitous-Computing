package coursework.coursework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Button ReadFeed;    //Button to open feed
    Button OpenPrefs;   //Button open preferences
    Button OpenMap;     //Button to Open Map
    Button OpenDatabase;    //Button to Open Database
    LinearLayout surface;   //Layout that will hold the surface view
    FragmentManager dialogmanager;  //The dialog manager
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //Set the layout
        ReadFeed = (Button)findViewById(R.id.RSSButton);    //get the button from the layout
        ReadFeed.setOnClickListener(this);  //set the onClick listener
        OpenPrefs = (Button)findViewById(R.id.OpenPrefs);
        OpenPrefs.setOnClickListener(this);
        OpenMap = (Button)findViewById(R.id.OpenMap);
        OpenMap.setOnClickListener(this);
        OpenDatabase = (Button)findViewById(R.id.OpenDatabase);
        OpenDatabase.setOnClickListener(this);
        surface = (LinearLayout)findViewById(R.id.theCanvas);   //Find where we want to put the canvas
        surface.addView(new WaveSurfaceView(this)); // Add a new Surafce view to the layout
        dialogmanager = this.getFragmentManager();  //get the fragment manager
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);  //get the menu XMl
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();  //What button did the user press

        //Did the user press quit
        if (id == R.id.quit) {
            finish();
        }

        //did the user press about
        if(id == R.id.mainAbout)
        {
            //Message to display ina bout dialog
            String message  = "Click RSS Feed to read the BBC RSS Feed \n" +
                    "\nClick Read Map to open the map of Locations\n" +
                    "\nClick Read Database to see the stories the database holds\n" +
                    "\nClick preferences to change your preferences";
            DialogFragment dialog = AboutDialog.newInstance(message);   //make a new dialog with the message
            dialog.show(dialogmanager, "tag");  //Display the dialog
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v)
    {
        //Did the user click on a button
        if(v instanceof Button)
        {
            //Play the button press sound
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.buttonpress);
            mp.start();
            //The user wants to see the RSS feed
            if(v == ReadFeed)
            {
                //Create a new intent using the correct class
                Intent showFeed = new Intent(getApplicationContext(),ShowRSSFeed.class);
                //Start the intent
                startActivity(showFeed);
            }
            //The user wants to see the map
            if(v == OpenMap)
            {
                Intent showMap = new Intent(getApplicationContext(),ShowMap.class);
                startActivity(showMap);
            }
            //The user wants to see the database
            if(v == OpenDatabase)
            {
                Intent showDatabase = new Intent(getApplicationContext(),ShowDatabase.class);
                startActivity(showDatabase);
            }
            //The user wants to see the preferences
            if(v == OpenPrefs)
            {
                Intent showPrefs = new Intent(getApplicationContext(),ShowPreferences.class);
                startActivity(showPrefs);
            }
        }
    }
}
