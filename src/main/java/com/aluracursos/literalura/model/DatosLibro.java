package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosLibro {

    private Long id;

    private String title;

    private List<DatosAutor> authors;

    private List<String> languages;

    @JsonProperty("download_count")
    private Integer downloadCount;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<DatosAutor> getAuthors() {
        return authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }
}