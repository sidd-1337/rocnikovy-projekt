package com.example.backend1.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "Subjects")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectsModel {
    @Id
    private String id;
    private String oborId;
    @JsonProperty("zkratka")
    private String zkratka;
    @JsonProperty("katedra")
    private String katedra;
    @JsonProperty("nazev")
    private String nazev;
    @JsonProperty("statut")
    private String statut;
    @JsonProperty("doporucenyRocnik")
    private String doporucenyRocnik;
    @JsonProperty("doporucenySemestr")
    private String doporucenySemestr;


    @JsonCreator
    public SubjectsModel(
            @JsonProperty("zkratka") String zkratka,
            @JsonProperty("katedra") String katedra,
            @JsonProperty("nazev") String nazev,
            @JsonProperty("statut") String statut,
            @JsonProperty("doporucenyRocnik") String doporucenyRocnik,
            @JsonProperty("doporucenySemestr") String doporucenySemestr) {
        this.zkratka = zkratka;
        this.katedra = katedra;
        this.nazev = nazev;
        this.statut = statut;
        this.doporucenyRocnik = doporucenyRocnik;
        this.doporucenySemestr = doporucenySemestr;
    }
    public SubjectsModel() {
        // Default constructor required by Spring Data
    }

    // Getters for all fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getZkratka() {
        return zkratka;
    }

    public void setOborId(String oborId) {this.oborId = oborId;}


    public void setZkratka(String zkratka) {
        this.zkratka = zkratka;
    }
    public String getKatedra() {
        return katedra;
    }
    public void setKatedra(String katedra) {
        this.katedra = katedra;
    }
    public String getNazev() {
        return nazev;
    }
    public void setNazev(String nazev) {
        this.nazev = nazev;
    }
    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public String getDoporucenyRocnik() {
        return doporucenyRocnik;
    }
    public void setDoporucenyRocnik(String doporucenyRocnik) {
        this.doporucenyRocnik = doporucenyRocnik;
    }
    public String getDoporucenySemestr() {
        return doporucenySemestr;
    }
    public void setDoporucenySemestr(String doporucenySemestr) {
        this.doporucenySemestr = doporucenySemestr;
    }

}


