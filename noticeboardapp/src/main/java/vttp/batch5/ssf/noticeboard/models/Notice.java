package vttp.batch5.ssf.noticeboard.models;

import java.io.StringReader;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Notice {
    @NotBlank (message="please add a title to your post")
    @Size (min=3, max=128, message="title must be between 3 and 128 characters")
    private String title;
    @NotBlank (message="please input your email")
    @Email (message="must be a well-formed email address")
    private String poster;
    @NotNull (message="please select a date")
    @Future (message="please select a future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date postDate;
    @NotBlank (message="please input at least 1 category")
    private String categories;
    @NotBlank (message="please input your notice content")
    private String text;

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getPoster() {return poster;}
    public void setPoster(String poster) {this.poster = poster;}
    public Date getPostDate() {return postDate;}
    public void setPostDate(Date postDate) {this.postDate = postDate;}
    public String getCategories() {return categories;}
    public void setCategories(String categories) {this.categories = categories;}
    public String getText() {return text;}
    public void setText(String text) {this.text = text;}

    @Override
    public String toString() {
        return "Notice [title=" + title + ", poster=" + poster + ", postDate=" + postDate + ", categories=" + categories
                + ", text=" + text + "]";
    }


    public static JsonObject objToJson(Notice notice){
        //create jsonArray to hold categories
        String[] catArray = notice.getCategories().split(",");
        JsonArrayBuilder catJArrayB = Json.createArrayBuilder();
        for(int i = 0; i< catArray.length; i++){
            catJArrayB = catJArrayB
                .add(catArray[i]);
        }
        JsonArray categoryArray = catJArrayB.build();

        //build Json object
        JsonObject jObj = Json.createObjectBuilder()
            .add("title", notice.getTitle())
            .add("poster", notice.getPoster())
            .add("postDate", dateToEpoch(notice.getPostDate()))
            .add("categories", categoryArray)
            .add("text", notice.getText())
            .build();
        return jObj;
    }
        
    public static long dateToEpoch(Date date){
        long epochMilli = date.getTime();
        return epochMilli;
    }


    //convert json string back into json object
    public static JsonObject toJson(String jsonStr) {
        JsonReader reader = Json.createReader(new StringReader(jsonStr));
        JsonObject jsonObj = reader.readObject();
        return jsonObj;   
    }


}
