package aiss.dailymotionminer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class DMVideoDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title") // equivalente a name
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_time") // equivalente a publishedAt
    private Long createdTime;

    @JsonProperty("owner") // equivalente a account (pero solo ID)
    private String owner;

    @JsonProperty("tags") // IMPORTANTE → comments
    private List<String> tags;

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}