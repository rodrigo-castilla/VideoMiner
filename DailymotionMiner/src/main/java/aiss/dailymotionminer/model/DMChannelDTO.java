package aiss.dailymotionminer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DMChannelDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("screenname") // Dailymotion lo llama "screenname"
    private String username;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_time")
    private String createdTime; // Añadido para que no de error la validación

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
}