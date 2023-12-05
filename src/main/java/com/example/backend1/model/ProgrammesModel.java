package com.example.backend1.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "Programmes")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgrammesModel {
    @Id
    private String id;
    @JsonProperty("oborIdno")
    private String oborIdno;

    @JsonProperty("nazevCZ")
    private String nazevCZ;

    @JsonProperty("nazevEN")
    private String nazevEN;

    @JsonProperty("fakultaOboru")
    private String fakultaOboru;

    @JsonProperty("typ")
    private String typ;

    @JsonProperty("forma")
    private String forma;


}