package coursework.coursework;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import javax.xml.transform.Result;

/**
 * Created by Nicholas on 29/11/2014.
 */
public class LoadRSSFeed extends AsyncTask<String, Integer, LinkedList<Item>> {

    ListView listView;  //The listview which willdisplay all the items
    String URL = new String();  //THe URL where we download the RSS feed
    ProgressDialog progressbar; //the progress bar to display while we load the RSS feed
    LinkedList<Item> ItemList = new LinkedList<Item>(); //THe list of items parsed from the RSS feed
    Context context;    //The current context

    public LoadRSSFeed()
    {

    }

    public LoadRSSFeed(Context context,String URL,ProgressDialog progressbar, ListView listView)
    {
        this.URL = URL; //sets the url
        this.listView = listView;   //sets the list view
        this.progressbar = progressbar; //sets the progress bar
        this.context = context; //sets the context
    }

    @Override
    protected void onPreExecute()
    {
       progressbar.show();  //display the progressbar before we start to load the RSS feed
    }

    //Loads the rss feed in the background
    protected LinkedList<Item> doInBackground(String... Params)
    {   //Downloads the RSS feed
        String XML = getRSSFeed(URL);
        //Parses the RSS feed
        ParseData(XML);
        return ItemList;
    }

    @Override
    protected void onPostExecute(LinkedList<Item> Items)
    {
        //Display te items in the list view after we have parsed them
        ItemAdapter adapter = new ItemAdapter(context,R.layout.rowlayout,ItemList);
        listView.setAdapter(adapter);
        //Close the progress bar
        progressbar.dismiss();
    }

    String getRSSFeed(String URL)
    {
        String RSSXML = new String();   //Holds the XML downloaded from the RSS feed

        try
        {
            URL rssURL = new URL(URL);  //creates a url from the link provided
            RSSXML = getStringFromInputStream(getInputStream(rssURL), "UTF-8"); //downloads the RSS feed
        }
        catch(MalformedURLException ae1)
        {
            Log.e("MyTag","URL Error");
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IOException");
        }
        return RSSXML;
    }

    public InputStream getInputStream(java.net.URL url) throws IOException
    {
        return url.openConnection().getInputStream();
    }

    public static String getStringFromInputStream(InputStream stream, String charsetName) throws IOException
    {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, charsetName);
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }

    void ParseData(String data)
    {
        try {
            XmlPullParserFactory parseRSSfactory = XmlPullParserFactory.newInstance();
            parseRSSfactory.setNamespaceAware(true);
            XmlPullParser RSSxmlPP = parseRSSfactory.newPullParser();
            RSSxmlPP.setInput(new StringReader(data));
            int theEventType = RSSxmlPP.getEventType();
            Boolean first = true;   //have we made the first empty Item
            Boolean setThumbnail = false;   //Have we set the thumbnail
            while (theEventType != XmlPullParser.END_DOCUMENT) {
                    if (theEventType == XmlPullParser.START_TAG)
                    {
                        if(RSSxmlPP.getName().equalsIgnoreCase("item")) //found an item tag
                        {
                            first = false;  //we have made an item item
                            Item aItem = new Item();    //create a new Item
                            ItemList.add(aItem);    //add the item to the list
                            setThumbnail = false;   //we have not set the thumbnail for the item
                        }
                        if(!first)
                        {// Check which Tag we have
                            if (RSSxmlPP.getName().equalsIgnoreCase("title"))
                            {
                                // Now just get the associated text
                                String temp = RSSxmlPP.nextText();

                                // store data in class
                                ItemList.getLast().setTitle(temp);
                            }
                                // Check which Tag we have
                            else if (RSSxmlPP.getName().equalsIgnoreCase("description"))
                            {
                                // Now just get the associated text
                                String temp = RSSxmlPP.nextText();
                                // store data in class
                                ItemList.getLast().setDescription(temp);
                            }

                                // Check which Tag we have
                            else if (RSSxmlPP.getName().equalsIgnoreCase("link"))
                            {
                                //Now just get the associated text
                                String temp = RSSxmlPP.nextText();
                                //store data in class
                                ItemList.getLast().setLink(temp);
                            }
                            else if(RSSxmlPP.getName().equalsIgnoreCase("thumbnail"))
                            {
                                //We want to set the imageURL to the first thumbnail we find
                                if (!setThumbnail)
                                {
                                    //we have set the thumbnail so dont set it again for the second thumbnail tag
                                    setThumbnail = true;
                                    String temp = RSSxmlPP.getAttributeValue(2);    //gets the link for the thumbnail tag
                                    ItemList.getLast().setImageURL(temp);
                                }
                            }

                        }

                    }
                // Get the next event
                theEventType = RSSxmlPP.next();

            } // End of while
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag", "Parsing error" + ae1.toString());
        }
        catch (IOException parserExp1)
        {
            Log.e("MyTag","IO error during parsing");
        }
    }

}
