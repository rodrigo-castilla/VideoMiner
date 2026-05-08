package aiss.dailymotionminer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DMVideoDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_time")
    private String createdTime; // Cambiado a String para mapeo directo con VideoMiner

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("subtitles")
    private List<DMSubtitleDTO> subtitles;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public List<DMSubtitleDTO> getSubtitles() { return subtitles; }
    public void setSubtitles(List<DMSubtitleDTO> subtitles) { this.subtitles = subtitles; }
}