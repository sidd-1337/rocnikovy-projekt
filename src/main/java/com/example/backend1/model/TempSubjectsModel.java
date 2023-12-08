package com.example.backend1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "TempSubjects")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TempSubjectsModel {
    @Id
    private String id;

    @CreatedDate
    private Date createdAt;

    @JsonProperty("roakIdno")
    private String roakIdno;

    @JsonProperty("nazev")
    private String nazev;

    @JsonProperty("predmet")
    private String zkratka;

    private Ucitel ucitel;

    @JsonProperty("budova")
    private String budova;

    @JsonProperty("mistnost")
    private String mistnost;

    @JsonProperty("typAkceZkr")
    private String typAkceZkr;

    @JsonProperty("denZkr")
    private String denZkr;

    @JsonProperty("hodinaSkutOd")
    private Object hodinaSkutOd;

    @JsonProperty("hodinaSkutDo")
    private Object hodinaSkutDo;

    @JsonProperty("tydenZkr")
    private String tydenZkr;

    @JsonProperty("katedra")
    private String katedra;

    public TempSubjectsModel() {
        // Default constructor required by Spring Data
        this.createdAt = new Date();
    }

    // Getters and setters for your fields

    public String getId() {return id; }

    public void setId(String id) {this.id = id;}

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public String getRoakIdno() {
        return roakIdno;
    }

    public void setRoakIdno(String roakIdno) {
        this.roakIdno = roakIdno;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getZkratka() {
        return zkratka;
    }

    public void setZkratka(String zkratka) {
        this.zkratka = zkratka;
    }

    public Ucitel getUcitel() {
        return ucitel;
    }

    public void setUcitel(Ucitel ucitel) {
        this.ucitel = ucitel;
    }

    public String getBudova() {
        return budova;
    }

    public void setBudova(String budova) {
        this.budova = budova;
    }

    public String getMistnost() {
        return mistnost;
    }

    public void setMistnost(String mistnost) {
        this.mistnost = mistnost;
    }

    public String getTypAkceZkr() {
        return typAkceZkr;
    }

    public void setTypAkceZkr(String typAkceZkr) {
        this.typAkceZkr = typAkceZkr;
    }

    public String getDenZkr() {
        return denZkr;
    }

    public void setDenZkr(String denZkr) {
        this.denZkr = denZkr;
    }

    public Object getHodinaSkutOd() {
        return hodinaSkutOd;
    }

    public void setHodinaSkutOd(Object hodinaSkutOd) {
        this.hodinaSkutOd = hodinaSkutOd;
    }

    public Object getHodinaSkutDo() {
        return hodinaSkutDo;
    }

    public void setHodinaSkutDo(Object hodinaSkutDo) {
        this.hodinaSkutDo = hodinaSkutDo;
    }

    public String getTydenZkr() {
        return tydenZkr;
    }

    public void setTydenZkr(String tydenZkr) {
        this.tydenZkr = tydenZkr;
    }

    public String getKatedra() {
        return katedra;
    }

    public void setKatedra(String katedra) {
        this.katedra = katedra;
    }

    // Nested class representing 'ucitel'
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ucitel {
        private String ucitIdno;
        private String jmeno;
        private String prijmeni;
        private String titulPred;

        // Getters and setters for ucitel fields

        public String getUcitIdno() {
            return ucitIdno;
        }

        public void setUcitIdno(String ucitIdno) {
            this.ucitIdno = ucitIdno;
        }

        public String getJmeno() {
            return jmeno;
        }

        public void setJmeno(String jmeno) {
            this.jmeno = jmeno;
        }

        public String getPrijmeni() {
            return prijmeni;
        }

        public void setPrijmeni(String prijmeni) {
            this.prijmeni = prijmeni;
        }

        public String getTitulPred() {
            return titulPred;
        }

        public void setTitulPred(String titulPred) {
            this.titulPred = titulPred;
        }
    }
}
