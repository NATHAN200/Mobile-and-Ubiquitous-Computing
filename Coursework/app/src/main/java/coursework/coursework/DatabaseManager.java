package coursework.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nicholas on 13/12/2014.
 */
public class DatabaseManager extends SQLiteOpenHelper
{

    private static final int DB_VER = 1;
    private static final String DB_PATH = "/data/data/coursework.coursework/databases/";
    private static final String DB_NAME = "itemDatabase.s3db";
    private static final String TBL_MAPEKFAME = "itemTable";
    //SQLiteDatabase db;
    public static final String COL_ID = "ID";
    public static final String COL_TITLE = "title";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_SDESCRIPTION = "sdescription";
    public static final String COL_IMAGE = "image";
    public static final String COL_LINK = "link";
    private final Context appContext;

    public DatabaseManager(Context context, String name,SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_MAPEKFAME_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TBL_MAPEKFAME + "("
                + COL_ID + " INTEGER PRIMARY KEY," + COL_TITLE
                + " TEXT," + COL_DESCRIPTION + " TEXT," + COL_SDESCRIPTION + " TEXT" + COL_IMAGE + " TEXT" + COL_LINK + " TEXT"+ ")";
        db.execSQL(CREATE_MAPEKFAME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(newVersion > oldVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_MAPEKFAME);
            onCreate(db);
        }
    }

    public void dbCreate() throws IOException
    {
        boolean dbExist = dbCheck();
        SQLiteDatabase db_Read = null;
        if(!dbExist)
        {
            //By calling this method an empty database will be created into the default system path
            //of your application so we can overwrite that database with our database.
            db_Read = this.getWritableDatabase();
            db_Read.close();
            try
            {
                copyDBFromAssets();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean dbCheck()
    {
        SQLiteDatabase db = null;
        try
        {
            String dbPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);
        }
        catch(SQLiteException e)
        {
            Log.e("SQLHelper", "Database not Found!");
        }
        if(db != null)
        {
            db.close();
        }
        return db != null ? true : false;
    }

    private void copyDBFromAssets() throws IOException
    {
        InputStream dbInput = null;
        OutputStream dbOutput = null;
        String dbFileName = DB_PATH + DB_NAME;
        try
        {
            dbInput = appContext.getAssets().open(DB_NAME);
            dbOutput = new FileOutputStream(dbFileName);
            //transfer bytes from the dbInput to the dbOutput
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dbInput.read(buffer)) > 0) {
                dbOutput.write(buffer, 0, length);
            }
            //Close the streams
            dbOutput.flush();
            dbOutput.close();
            dbInput.close();
        }
        catch (IOException e)
        {
            throw new Error("Problems copying DB!");
        }
    }

    //Gets all Items in the database
    public List<DatabaseItem> allData()
    {
        String query = "Select * FROM " + TBL_MAPEKFAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        List<DatabaseItem> mcMapDataList = new ArrayList<DatabaseItem>();
        cursor.moveToFirst();
        if(cursor.moveToFirst())
        {
            while (cursor.isAfterLast() == false)
            {
                //sets the individual properties of a new Item object
                DatabaseItem DatabaseEntry = new DatabaseItem();
                DatabaseEntry.setTitle(cursor.getString(1));
                DatabaseEntry.setDescription(cursor.getString(2));
                DatabaseEntry.setsDescription(cursor.getString(3));
                DatabaseEntry.setImage(cursor.getString(4));
                DatabaseEntry.setLink(cursor.getString(5));
                mcMapDataList.add(DatabaseEntry);   //adds the Item to the list
                cursor.moveToNext();
            }
        }
        else
        {
            mcMapDataList.add(null);
        }
        cursor.close();
        db.close();
        return mcMapDataList;
    }
}



