package com.example.untoldpsproject.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Dj extends Artist {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    public String name;
    public String description;
    public int nrOfPersons;
    public String genre;
    public String photoUrl;

    public Dj(String name, String description, String genre, int nrOfPersons, String photoUrl) {
        this.name = name;
        this.description = description;
        this.nrOfPersons = nrOfPersons;
        this.genre = genre;
        this.photoUrl = photoUrl;
    }

    public Dj() {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getNumberOfPerson() {
        return nrOfPersons;
    }

    @Override
    public String getGenre() {
        return genre;
    }
    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public void setNrOfPerson(int nrOfPerson) {
        this.nrOfPersons = nrOfPerson;
    }
    @Override
    public String getId() {
        return id;
    }
    @Override
    public void setId(String id) {
        this.id = id;
    }
}
