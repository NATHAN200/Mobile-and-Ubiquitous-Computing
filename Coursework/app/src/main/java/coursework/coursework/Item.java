package coursework.coursework;

/**
 * Created by Nicholas on 29/11/2014.
 */
public class Item {

    private String title;   //The title of the Item
    private String description; //Description of the story
    private String link;    //URL to the BBC website news article
    private String ImageURL;    //URL to story thumbnail

    public Item()
    {
        setTitle("");
        setDescription("");
        setLink("");
        setImageURL("");
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
