package aiss.dailymotionminer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DMAccountDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("screenname") // equivalente a name
    private String screenname;

    @JsonProperty("url")
    private String url;

    @JsonProperty("avatar")
    private String avatar;

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScreenname() {
        return screenname;
    }

    public void setScreenname(String screenname) {
        this.screenname = screenname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAvatar() {
        this.avatar = avatar;
        return "";
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}