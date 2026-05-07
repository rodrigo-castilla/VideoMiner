package aiss.peertubeminer.model.videominer;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class Video {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    @NotEmpty(message = "Video name cannot be empty")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("releaseTime")
    @NotEmpty(message = "Video release time cannot be empty")
    private String releaseTime;

    @JsonProperty("author")
    private User author;

    @JsonProperty("channel")
    private Channel channel;

    @JsonProperty("captions")
    private List<Caption> captions;

    @JsonProperty("comments")
    private List<Comment> comments;

    // A partir de aquí, pon todos los Getters y Setters normales
    // (getId, setId, getName, setName, etc...)

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getReleaseTime() { return releaseTime; }
    public void setReleaseTime(String releaseTime) { this.releaseTime = releaseTime; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public Channel getChannel() { return channel; }
    public void setChannel(Channel channel) { this.channel = channel; }

    public List<Caption> getCaptions() { return captions; }
    public void setCaptions(List<Caption> captions) { this.captions = captions; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
}