package aiss.videominer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Video")
public class Video {
    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    @NotEmpty(message = "Video name cannot be empty")
    private String name;

    @JsonProperty("description")
    @Column(columnDefinition="TEXT")
    private String description;

    @JsonProperty("releaseTime")
    @NotEmpty(message = "Video release time cannot be empty")
    private String releaseTime;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    @JsonBackReference
    private Channel channel;

    @JsonProperty("author")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private User author;

    @JsonProperty("comments")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "video")
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    @JsonProperty("captions")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "video")
    @JsonManagedReference
    private List<Caption> captions = new ArrayList<>();

    public Video() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReleaseTime() { return releaseTime; }
    public void setReleaseTime(String releaseTime) { this.releaseTime = releaseTime; }
    public Channel getChannel() { return channel; }
    public void setChannel(Channel channel) { this.channel = channel; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public List<Comment> getComments() { return comments; }
    public List<Caption> getCaptions() { return captions; }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        if (comments != null) comments.forEach(c -> c.setVideo(this));
    }

    public void setCaptions(List<Caption> captions) {
        this.captions = captions;
        if (captions != null) captions.forEach(c -> c.setVideo(this));
    }
}