package coursework.coursework;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Nicholas on 18/12/2014.
 */
public class ShowPreferences extends Activity implements View.OnClickListener {


    EditText editFirstName; //Edit text to show and edit first name
    EditText editSurname;   //Edit text to show and edit surname
    EditText editLocation;  //Edit text to show and edit location
    Button back;    //button to take user back to main menu
    Button save;    //button to save current data of the edit text
    SharedPreferences sharedPrefs;  //The saved preferences
    FragmentManager dialogmanager;

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        super.setContentView(R.layout.preferences);
        //Find the edit texts from the layout
        editFirstName = (EditText)findViewById(R.id.editFirstName);
        editSurname = (EditText)findViewById(R.id.editSurname);
        editLocation = (EditText)findViewById(R.id.editLocation);
        back = (Button)findViewById(R.id.prefBack);
        back.setOnClickListener(this);
        save = (Button)findViewById(R.id.prefSave);
        save.setOnClickListener(this);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        dialogmanager = this.getFragmentManager();
        load();

    }

    private void load()
    {
        //Load the saved strings form the share prefs and display them in the edit text
        editFirstName.setText(sharedPrefs.getString("FirstName","John"));
        editSurname.setText(sharedPrefs.getString("Surname","Smith"));
        editLocation.setText(sharedPrefs.getString("Location","Glasgow"));
    }

    public void onClick(View v)
    {
        if(v instanceof Button)
        {
            //Button was click so play sound
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.buttonpress);
            mp.start();
            if(v==back)
            {
                //Go back to the main menu
                finish();
            }
            if(v==save)
            {
                //save the data
                save();
            }
        }
    }

    public void save()
    {
        //Saves the current values of the edit texts to the shared preferences
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("FirstName",editFirstName.getText().toString());
        editor.putString("Surname",editSurname.getText().toString());
        editor.putString("Location",editLocation.getText().toString());
        editor.commit();
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
            String message  = "Edit each of the fields and then click save to save the data"+
                    "\nClick Back to return to the Main Menu";
            DialogFragment dialog = AboutDialog.newInstance(message);
            dialog.show(dialogmanager, "tag");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
