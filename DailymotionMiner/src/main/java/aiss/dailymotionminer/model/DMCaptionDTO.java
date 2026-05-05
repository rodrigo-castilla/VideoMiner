package aiss.dailymotionminer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DMSubtitleDTO {

    @JsonProperty("url") // equivalente a fileUrl
    private String url;

    @JsonProperty("language")
    private String language;

    // Getters and setters

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}