package coursework.coursework;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nicholas on 13/12/2014.
 */
public class ShowDatabase extends Activity implements View.OnClickListener
{
    Button backMain;    //The button to return us to the main menu
    Button backList;    //The button to take us back to the list
    ListView theList;   //The listview that displays the items
    Button viewOnline;      //Opens the browser at the page URL
    ImageView image;    //Displays the image for the currently displayed Item
    TextView Title;     //Displays the title for the currently displayed item
    TextView Description;   //Displays the description for the currently displayed Item
    List<DatabaseItem> Items;   //THe list of items to display
    ViewFlipper flipper;    //Flipper toswitch between displaying the list and displaying more details when an item is selected
    String link;    // The link to the webpage for the currently displayed item
    FragmentManager dialogmanager;

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.database_view);
        dialogmanager = this.getFragmentManager();
        flipper = (ViewFlipper)findViewById(R.id.theFlipper);   //Find the flipper
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.listFront)));
        backMain = (Button)findViewById(R.id.listViewBack);
        backMain.setOnClickListener(this);
        backList = (Button)findViewById(R.id.SelectedViewBack);
        backList.setOnClickListener(this);
        theList = (ListView)findViewById(R.id.listView);
        //What happens whe we click on an item in the list
        theList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String sImagePath = "drawable/" + Items.get(position).getImage().toLowerCase(); //get the image path for the selected item
            Context appContext = getApplicationContext();   //get the context
            int imgResID = appContext.getResources().getIdentifier(sImagePath,"drawable","coursework.coursework");  //load the image from the device using the path name
            image.setImageResource(imgResID);   //set the image
            Title.setText(Items.get(position).getTitle());  //set the title for the slected item
            Description.setText(Items.get(position).getDescription());  //set the description for the selected Item
             //Displays the selected Items data
            flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.listBack)));
            link = Items.get(position).getLink();   //sets the link to be the link of the currently selected item

            }
        });
        viewOnline = (Button)findViewById(R.id.viewOnline);
        viewOnline.setOnClickListener(this);
        image = (ImageView)findViewById(R.id.SelectedImage);
        Title = (TextView)findViewById(R.id.SelectedTitle);
        Description = (TextView)findViewById(R.id.SelectedDescription);
        Items = new LinkedList<DatabaseItem>();
        DatabaseManager dbManager = new DatabaseManager(this,"itemDatabase.s3db",null,1);
        try
        {
            dbManager.dbCreate();   //load the database

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Items = dbManager.allData();    //get all the database forom the database
        //Display the data form the database in the listview
        DatabaseItemAdapter adapter = new DatabaseItemAdapter(getApplicationContext(),R.layout.databaserowlayout,Items);
        theList.setAdapter(adapter);

    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v)
    {
        if(v instanceof Button)
        {
            //we clicked a button so wplay the button press sound
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.buttonpress);
            mp.start();
            if(v == backMain)
            {
                //User wants to go back to the main menu
                finish();
            }
            if(v == backList)
            {
                //Flip back to the list view
                flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.listFront)));
            }
            if(v == viewOnline)
            {
                //Open the browser using the link from the currently selected item
                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(openBrowser);
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
            String message  = "Shows a list of the BBC news Stories read form a Database \n" +
                    "\nClick On an Item to see more details" +
                    "\nClick Back to return to the Main Menu" +
                    "\nClick VIew Online to see the webpage for the Story";
            DialogFragment dialog = AboutDialog.newInstance(message);
            dialog.show(dialogmanager, "tag");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
