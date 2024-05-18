package com.example.untoldpsproject.factory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public abstract class Artist {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    public String name;
    public String description;
    public int nrOfPersons;
    public String genre;
    public String photoUrl;
    abstract public String getName();
    abstract public String getDescription();
    abstract public int getNumberOfPerson();
    abstract public String getGenre();
    abstract public String getPhotoUrl();
    abstract public void setName(String name);
    abstract public void setDescription(String description);
    abstract public void setGenre(String genre);
    abstract public void setPhotoUrl(String photoUrl);
    abstract public void setNrOfPerson(int nrOfPerson);
    abstract public String getId();
    abstract public void setId(String id);
}
