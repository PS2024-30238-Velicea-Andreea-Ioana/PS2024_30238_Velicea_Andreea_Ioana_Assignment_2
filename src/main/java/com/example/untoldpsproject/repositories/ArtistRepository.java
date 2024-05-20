package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.entities.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, String> {
}
