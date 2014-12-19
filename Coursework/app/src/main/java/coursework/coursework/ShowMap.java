package coursework.coursework;


import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicholas on 12/12/2014.
 */
public class ShowMap extends FragmentActivity implements View.OnClickListener {

    Button backButton;  //Button to take the user back to main menu
    List<MapDataItem> mapDataList;  //list of items used to create markers
    private GoogleMap theMap;   //HTe map we want to use to display
    private float colours[] = {210.0f,120.0f,300.0f,330.0f,270.0f}; //colours of the markers
    FragmentManager dialogmanager;
    private LatLng BBCScotland = new LatLng(55.857897,-4.291129);   //Starting view point for the map
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate((bundle));
        setContentView(R.layout.map_view);
        backButton = (Button)findViewById(R.id.mapBackButton);
        backButton.setOnClickListener(this);
        mapDataList = new ArrayList<MapDataItem>();
        dialogmanager = this.getFragmentManager();
        MapDatabaseManager mapDB = new MapDatabaseManager(this.getApplicationContext(),"mapDatabase.s3db",null,1);  //create the database manager
        try
        {
            mapDB.dbCreate();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        mapDataList = mapDB.allMapData();   //Get all data from he database
        SetUpMap();
        AddMarkers();
    }

    void SetUpMap()
    {
        //Get the m,ap from the layout
        theMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if(theMap != null)
        {
            //Move the camera to the starting posiiton
            theMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BBCScotland,15));
            theMap.setMyLocationEnabled(true);
            theMap.getUiSettings().setCompassEnabled(true); //SHow the compass
            theMap.getUiSettings().setMyLocationButtonEnabled(true);
            theMap.getUiSettings().setRotateGesturesEnabled(true);
        }
    }

    void AddMarkers()
    {
        MarkerOptions marker;   //Create a marker object
        MapDataItem mapData;    //Holds the marker data from the database
        String mrkTitle;    //Marker title
        String mrkText; //marker text
        //Loop through all the items from the database
        for(int i = 0;i < mapDataList.size();i++)
        {
            //get the item object
            mapData = mapDataList.get(i);
            mrkTitle = mapData.getName();   //set the name
            mrkText = mapData.getDescription(); //set the description
            marker = SetMarker(mrkTitle,mrkText,new LatLng(mapData.getLatitude(),mapData.getLongitude()),colours[i],true);
            theMap.addMarker(marker);
        }
    }

    public MarkerOptions SetMarker(String title, String snippet, LatLng position, float markerColour, boolean centreAnchor)
    {
        float anchorX;
        float anchorY;
        if(centreAnchor)
        {
            anchorX = 0.5f;
            anchorY = 0.5f;
        }
        else
        {
            anchorX = 0.5f;
            anchorY = 1f;
        }
        MarkerOptions marker = new MarkerOptions().title(title).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(markerColour)).anchor(anchorX,anchorY).position(position);
        return marker;
    }

    public void onClick(View v)
    {
        if(v instanceof Button)
        {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.buttonpress);
            mp.start();
            if(v == backButton)
            {
                finish();
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

        //the user wants to go back to the main menu
        if (id == R.id.back) {
            finish();
        }
        if(id == R.id.mainAbout)
        {
            //About message
            String message  = "Shows the Loacation of BBC Scotland studio and nearby Buildings"+
                    "\nClick Back to return to the Main Menu";
            DialogFragment dialog = AboutDialog.newInstance(message);
            dialog.show(dialogmanager, "tag");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
