package com.example.untoldpsproject.services;

import com.example.untoldpsproject.constants.ArtistConstants;
import com.example.untoldpsproject.entities.Artist;
import com.example.untoldpsproject.entities.Band;
import com.example.untoldpsproject.entities.Dj;
import com.example.untoldpsproject.entities.Singer;
import com.example.untoldpsproject.repositories.ArtistRepository;
import com.example.untoldpsproject.validators.ArtistValidator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArtistFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArtistFactory.class);
    public final ArtistRepository artistRepository;
    public final ArtistValidator artistValidator = new ArtistValidator();

    /**
     * Inserts and creates a new artist.
     *
     * @param artistType The type of the artist : Dj, Singer, Band.
     * @param name Artist name.
     * @param description Artist description.
     * @param nrOfPersons Number of persons.
     * @param genre The popular music genre.
     * @param photoUrl The photoUrl of the artist.
     * @return A string indicating the result of the insertion.
     */
    public String createArtist(String artistType, String name, String description, int nrOfPersons, String genre, String photoUrl) {
        if(artistType == null){
            return null;
        }
        Artist artist;
        switch (artistType) {
            case "Singer":
                try {
                    artist =  new Singer(name, description,genre, nrOfPersons, photoUrl);
                    artistValidator.artistValidator(artist);
                    Artist artist1 = artistRepository.save(artist);
                    LOGGER.info(ArtistConstants.ARTIST_INSERTED);
                    return ArtistConstants.ARTIST_INSERTED;
                }catch (Exception e) {
                    LOGGER.error(ArtistConstants.ARTIST_NOT_INSERTED + ": " + e.getMessage());
                    return ArtistConstants.ARTIST_NOT_INSERTED + ": " + e.getMessage();
                }
            case "Band":
                try {
                    artist =  new Band( name,description,nrOfPersons,genre, photoUrl);
                    artistValidator.artistValidator(artist);
                    Artist artist1 = artistRepository.save(artist);
                    LOGGER.info(ArtistConstants.ARTIST_INSERTED);
                    return ArtistConstants.ARTIST_INSERTED;
                }catch (Exception e) {
                    LOGGER.error(ArtistConstants.ARTIST_NOT_INSERTED+": "+e.getMessage());
                    return ArtistConstants.ARTIST_NOT_INSERTED+": "+e.getMessage();
                }
            case "DJ":
                try {
                    artist = new Dj( name, description, genre, nrOfPersons, photoUrl);
                    artistValidator.artistValidator(artist);
                    Artist artist1 = artistRepository.save(artist);
                    LOGGER.info(ArtistConstants.ARTIST_INSERTED);
                    return ArtistConstants.ARTIST_INSERTED;
                }catch (Exception e) {
                    LOGGER.error(ArtistConstants.ARTIST_NOT_INSERTED+": "+e.getMessage());
                    return ArtistConstants.ARTIST_NOT_INSERTED+": "+e.getMessage();
                }
            default: return null;
        }
    }
    /**
     * Retrieves all artists.
     *
     * @return A list of all artists.
     */
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }


    /**
     * Finds an artist by ID.
     *
     * @param id The ID of the artist to find.
     * @return The artist object if found, otherwise null.
     */
    public Artist findById(String id){
        Optional<Artist> artistOptional = artistRepository.findById(id);
        if(artistOptional.isEmpty()){
            LOGGER.error(ArtistConstants.ARTIST_NOT_FOUND);
            return null;
        }else {
            return artistOptional.get();
        }
    }

}
