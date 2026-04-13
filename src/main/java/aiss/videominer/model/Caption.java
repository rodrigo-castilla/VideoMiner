package aiss.videominer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "Caption")
public class Caption {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("name") // La PDF pedía 'link' pero mantengo tu variable
    private String name;

    @JsonProperty("language")
    private String language;

    @ManyToOne
    @JoinColumn(name = "video_id")
    @JsonBackReference
    private Video video;

    public Caption() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public Video getVideo() { return video; }
    public void setVideo(Video video) { this.video = video; }
}