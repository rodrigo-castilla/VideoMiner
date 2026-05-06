package aiss.dailymotionminer.model.videominer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "Caption")
public class Caption {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("link")
    private String link;

    @JsonProperty("language")
    private String language;

    @ManyToOne
    @JoinColumn(name = "video_id")
    @JsonBackReference
    private Video video;

    public Caption() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public Video getVideo() { return video; }
    public void setVideo(Video video) { this.video = video; }
}