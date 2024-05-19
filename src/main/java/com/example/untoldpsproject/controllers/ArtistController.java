package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.constants.ArtistConstants;
import com.example.untoldpsproject.factory.Artist;
import com.example.untoldpsproject.factory.ArtistFactory;
import com.example.untoldpsproject.services.ArtistService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller class for artists.
 */

@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value ="/artist")
public class ArtistController {
    private ArtistFactory artistFactory;
    private ArtistService artistService;

    /**
     * Retrieves a list of artists.
     * @param userId The ID of the user.
     * @return ModelAndView representing the artist list view.
     */

    @GetMapping("/list/{userId}")
    public ModelAndView artistList(@PathVariable("userId") String userId) {
        ModelAndView mav = new ModelAndView("artist-list");
        List<Artist> artists = artistService.getAllArtists();
        mav.addObject("artists", artists);
        mav.addObject("userId", userId);
        return mav;
    }

    /**
     * Displays the form for adding a new artist.
     * @param userId The ID of the user.
     * @return ModelAndView representing the add artist form view.
     */
    @GetMapping("/add/{userId}")
    public ModelAndView showAddArtistForm(@PathVariable("userId") String userId) {
        ModelAndView modelAndView = new ModelAndView("artist-add");
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

    /**
     * Handles the submission of the add artist form.
     * @param userId The ID of the user.
     * @param type The type of the artist.
     * @param name The name of the artist.
     * @param description The description of the artist.
     * @param nrOfPersons The number of persons in the artist group.
     * @param genre The genre of the artist.
     * @param photoUrl The URL of the artist's photo.
     * @param redirectAttributes RedirectAttributes for handling flash attributes.
     * @return ModelAndView representing the redirection after adding the artist.
     */
    @PostMapping("/add/{userId}")
    public ModelAndView addArtist(@PathVariable("userId") String userId,
                                  @RequestParam String type,
                                  @RequestParam String name,
                                  @RequestParam String description,
                                  @RequestParam int nrOfPersons,
                                  @RequestParam String genre,
                                  @RequestParam String photoUrl,
                                  RedirectAttributes redirectAttributes) {
        Artist newArtist = artistFactory.createArtist(type, name, description, nrOfPersons, genre, photoUrl);
        String result = artistService.insertArtist(newArtist);
        if (ArtistConstants.ARTIST_INSERTED.equals(result)) {
            return new ModelAndView("redirect:/artist/list/" + userId);
        } else {
            redirectAttributes.addFlashAttribute("error", result);
            return new ModelAndView("redirect:/artist/add/" + userId);
        }
    }
}