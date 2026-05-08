package aiss.dailymotionminer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DMAccountDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("screenname")
    private String screenname;

    @JsonProperty("url")
    private String url;

    @JsonProperty("avatar")
    private String avatar;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getScreenname() { return screenname; }
    public void setScreenname(String screenname) { this.screenname = screenname; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    // ¡Corregido el bug que devolvía un string vacío!
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}