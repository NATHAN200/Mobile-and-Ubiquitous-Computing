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
 * Created by Nicholas on 12/12/2014.
 */
public class MapDatabaseManager extends SQLiteOpenHelper
{
    private static final int DB_VER = 1;
    private static final String DB_PATH = "/data/data/coursework.coursework/databases/";
    private static final String DB_NAME = "mapDatabase.s3db";
    private static final String TBL_MAPEKFAME = "mapTable";
    //SQLiteDatabase db;
    public static final String COL_ID = "ID";
    public static final String COL_NAME = "Surname";
    public static final String COL_DESCRIPTION = "FirstName";
    public static final String COL_LATITUDE = "Latitude";
    public static final String COL_LONGITUDE = "Longitude";
    private final Context appContext;

    public MapDatabaseManager(Context context, String name,SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_MAPEKFAME_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TBL_MAPEKFAME + "("
                + COL_ID + " INTEGER PRIMARY KEY," + COL_NAME
                + " TEXT," + COL_DESCRIPTION + " TEXT," + COL_LATITUDE + " TEXT" + COL_LONGITUDE + " TEXT" +")";
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
            db = null;
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

    public void addaMapEKFameEntry(MapDataItem aMapEKFame) {

        ContentValues values = new ContentValues();
        values.put(COL_NAME, aMapEKFame.getName());
        values.put(COL_DESCRIPTION, aMapEKFame.getDescription());
        values.put(COL_LATITUDE, aMapEKFame.getLatitude());
        values.put(COL_LONGITUDE, aMapEKFame.getLongitude());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TBL_MAPEKFAME, null, values);
        db.close();
    }

    public boolean removeaMapEKFameEntry(String aMapEKFameEntry)
    {
        boolean result = false;
        String query = "Select * FROM " + TBL_MAPEKFAME + " WHERE " + COL_NAME + " =  \"" + aMapEKFameEntry + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MapDataItem StarSignsInfo = new MapDataItem();
        if (cursor.moveToFirst())
        {
            StarSignsInfo.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TBL_MAPEKFAME, COL_ID + " = ?",
                    new String[] { String.valueOf(StarSignsInfo.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public List<MapDataItem> allMapData()
    {
        String query = "Select * FROM " + TBL_MAPEKFAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        List<MapDataItem> mcMapDataList = new ArrayList<MapDataItem>();
        cursor.moveToFirst();
        if(cursor.moveToFirst())
        {
            while (cursor.isAfterLast() == false)
            {
                MapDataItem MapDataEntry = new MapDataItem();
                MapDataEntry.setID(Integer.parseInt(cursor.getString(0)));
                MapDataEntry.setName(cursor.getString(1));
                MapDataEntry.setDescription(cursor.getString(2));
                MapDataEntry.setLatitude(Float.parseFloat(cursor.getString(3)));
                MapDataEntry.setLongitude(Float.parseFloat(cursor.getString(4)));
                mcMapDataList.add(MapDataEntry);
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
