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
import java.util.Map;

@RestController
@CrossOrigin
@AllArgsConstructor
@Setter
@Getter
@RequestMapping(value ="/artist")
public class ArtistController {
    private ArtistFactory artistFactory;
    private ArtistService artistService;

    @GetMapping("/list/{userId}")
    public ModelAndView artistList(@PathVariable("userId") String userId) {
        ModelAndView mav = new ModelAndView("artist-list");
        List<Artist> artists = artistService.getAllArtists();
        mav.addObject("artists", artists);
        mav.addObject("userId", userId);
        return mav;
    }
    @GetMapping("/add/{userId}")
    public ModelAndView showAddArtistForm(@PathVariable("userId") String userId) {
        ModelAndView modelAndView = new ModelAndView("artist-add");
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

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