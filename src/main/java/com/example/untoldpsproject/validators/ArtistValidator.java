package com.example.untoldpsproject.validators;


import com.example.untoldpsproject.constants.ArtistConstants;
import com.example.untoldpsproject.factory.Artist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArtistValidator {
    public boolean nameValidator(String name) throws Exception {
        if(name == null) throw new Exception(ArtistConstants.ARTIST_NULL);
        Pattern pattern = Pattern.compile("[a-zA-Z]+-?[a-zA-Z]*$");
        Matcher matcher = pattern.matcher(name);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            matcher.reset();
            throw new Exception(ArtistConstants.NAME_NOT_MATCH);
        }
    }
    public boolean descriptionValidator(String description) throws Exception {
        if(description == null) throw new Exception(ArtistConstants.ARTIST_NULL);
        if(description.length() > 255){
            throw new Exception(ArtistConstants.DESCRIPTION_TOO_LONG);
        }else{
            return true;
        }
    }
    public boolean numberOfPersonValidator(int numberOfPerson) throws Exception {
        if(numberOfPerson < 0) throw new Exception(ArtistConstants.NUMBER_NEGATIVE);
        else{
            return true;
        }
    }
    public boolean genreValidator(String genre) throws Exception {
        if(genre == null) throw new Exception(ArtistConstants.ARTIST_NULL);
        Pattern pattern = Pattern.compile("[a-zA-Z]+-?[a-zA-Z]*$");
        Matcher matcher = pattern.matcher(genre);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            matcher.reset();
            throw new Exception(ArtistConstants.GENRE_NOT_MATCH);
        }
    }

    public boolean artistValidator(Artist artist) throws Exception {
        return nameValidator(artist.getName()) && descriptionValidator(artist.getDescription())&& numberOfPersonValidator(artist.getNumberOfPerson())&& genreValidator(artist.getGenre());
    }
}
