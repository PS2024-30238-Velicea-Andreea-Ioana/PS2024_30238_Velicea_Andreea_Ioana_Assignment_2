package com.example.untoldpsproject.controllers;

import com.example.untoldpsproject.constants.ArtistConstants;
import com.example.untoldpsproject.constants.UserConstants;
import com.example.untoldpsproject.dtos.UserDto;
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
        return mav;
    }
    @GetMapping("/add/{userId}")
    public ModelAndView showAddArtistForm(@PathVariable("userId") String userId) {
        ModelAndView modelAndView =new ModelAndView("artist-add");
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

    @PostMapping("/add/{userId}")
    public ModelAndView addArtist(@PathVariable("userId") String userId,@RequestParam String type, @RequestParam String name,
                                  @RequestParam String description,
                                  @RequestParam int nrOfPersons,
                                  @RequestParam String genre,
                                  @RequestParam String photoUrl, RedirectAttributes redirectAttributes) {
        Artist newArtist = artistFactory.createArtist(type, name, description, nrOfPersons, genre, photoUrl);
        String result = artistService.insertArtist(newArtist);
        if(result.equals(ArtistConstants.ARTIST_INSERTED)){
            return new ModelAndView("redirect:/artist/list/" + userId);
        }else{
            redirectAttributes.addFlashAttribute("error", result);
            return new ModelAndView("redirect:/artist/add/"+userId);
        }
    }

    @GetMapping("/delete/{id}/{userId}")
    public ModelAndView deleteArtist(@PathVariable("userId") String adminId, @PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        String result = artistService.deleteArtist(id);
        redirectAttributes.addFlashAttribute("error", result);
        return new ModelAndView("redirect:/artist/list/"+adminId);
    }

    @GetMapping("/edit/{id}/{userId}")
    public ModelAndView editArtistForm(@PathVariable("userId") String adminId, @PathVariable("id") String artistId) {
        ModelAndView mav = new ModelAndView("artist-edit");
        Artist artist = artistService.findById(artistId);
        mav.addObject("artist", artist);
        mav.addObject("userId", adminId);
        return mav;
    }

    @PostMapping ("/edit/{id}/{userId}")
    public ModelAndView updateArtist(@PathVariable("userId") String adminId, @ModelAttribute("artist") Artist artist, RedirectAttributes redirectAttributes, @RequestParam String type, @RequestParam String name,
                                     @RequestParam String description,
                                     @RequestParam int nrOfPersons,
                                     @RequestParam String genre,
                                     @RequestParam String photoUrl) {
        Artist newArtist = artistFactory.createArtist(type, name, description, nrOfPersons, genre, photoUrl);
        String result = artistService.updateArtistById(artist.getId(), newArtist);
        if(result.equals(ArtistConstants.ARTIST_UPDATED)){
            return new ModelAndView("redirect:/artist/list/"+adminId);
        }else{
            redirectAttributes.addFlashAttribute("error", result);
            return new ModelAndView("redirect:/artist/edit/"+artist.getId()+"/"+adminId);
        }
    }


}
