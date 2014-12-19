package coursework.coursework;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Nicholas on 01/12/2014.
 */
public class ImageLoader extends AsyncTask<Void,Void,Bitmap>

{
    String aURL;
    ImageView Image;

    public ImageLoader(String URL, ImageView Image)
    {
        this.aURL = URL;    //Url to download from
        this.Image = Image; //Image view to display the image on
    }

    @Override
    protected Bitmap doInBackground(Void... Params )
    {
        Bitmap myBitmap = null;
        try
        {
            URL urlConnection = new URL(aURL);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();   //Connect to the url
            InputStream input = connection.getInputStream();    //download the data from the url
            myBitmap = BitmapFactory.decodeStream(input);   //convert the data into a bitmap
        }
        catch(MalformedURLException e)
        {
            Log.e("MyTag","URL Error");
        }
        catch (IOException e)
        {
            Log.e("MyTag", "IOException");
        }
        return myBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap image)
    {
        Image.setImageBitmap(image);    //set the imageview to display the image
    }

}
