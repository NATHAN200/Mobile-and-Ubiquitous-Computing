package coursework.coursework;

/**
 * Created by Nicholas on 13/12/2014.
 */
public class DatabaseItem extends Item {

    private String image;   //Path to the image stored on the device
    private String sDescription;    //The short description to be displayed on the List View

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getsDescription() {
        return sDescription;
    }

    public void setsDescription(String sDescription) {
        this.sDescription = sDescription;
    }
}
