package aiss.dailymotionminer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DMChannelDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("username") // equivalente a "name"
    private String username;

    @JsonProperty("description")
    private String description;

    // Getters and setters (MISMO ESTILO QUE TU COMPAÑERO)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}