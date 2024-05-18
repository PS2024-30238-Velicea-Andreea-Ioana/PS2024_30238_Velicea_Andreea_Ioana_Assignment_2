package com.example.untoldpsproject.factory;

public class ArtistFactory {
    public Artist createArtist(String artistType, String name, String description, int nrOfPersons, String genre, String photoUrl) {
        if(artistType == null){
            return null;
        }
        switch (artistType) {
            case "Singer": return new Singer(name, description,genre, nrOfPersons, photoUrl);
            case "Band":  return new Band( name,description,nrOfPersons,genre, photoUrl);
            case "DJ": return new Dj( name, description, genre, nrOfPersons, photoUrl);
            default: return null;
        }
    }
}
