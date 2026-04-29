package aiss.peertubeminer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PTCaptionDTO {

    @JsonProperty("fileUrl")
    private String fileUrl;

    @JsonProperty("language")
    private String language;

    // Getters and setters
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}