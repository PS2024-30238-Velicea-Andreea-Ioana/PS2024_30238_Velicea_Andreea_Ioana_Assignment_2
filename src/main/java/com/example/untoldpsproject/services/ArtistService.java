
package com.example.untoldpsproject.services;

import com.example.untoldpsproject.constants.ArtistConstants;
import com.example.untoldpsproject.constants.UserConstants;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.Order;
import com.example.untoldpsproject.entities.Ticket;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.factory.Artist;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.repositories.ArtistRepository;
import com.example.untoldpsproject.validators.ArtistValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@AllArgsConstructor
@Service
public class ArtistService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArtistService.class);
    public final ArtistRepository artistRepository;
    public final ArtistValidator artistValidator = new ArtistValidator();

    public String insertArtist(Artist artist) {
        try {
            artistValidator.artistValidator(artist);
            Artist artist1 = artistRepository.save(artist);
            LOGGER.info(ArtistConstants.ARTIST_INSERTED);
            return ArtistConstants.ARTIST_INSERTED;
        }catch (Exception e) {
            LOGGER.error(ArtistConstants.ARTIST_NOT_INSERTED+": "+e.getMessage());
            return ArtistConstants.ARTIST_NOT_INSERTED+": "+e.getMessage();
        }
    }
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

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
