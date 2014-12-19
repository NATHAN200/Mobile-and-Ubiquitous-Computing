package coursework.coursework;

import java.io.Serializable;

/**
 * Created by Nicholas on 12/12/2014.
 */
public class MapDataItem implements Serializable {

    private int ID; //Id in the database
    private String name;    //Value of name
    private float Latitude; //Latitude value
    private float Longitude;    //Longitude value
    private String Description; //description of marker

    public MapDataItem()
    {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
