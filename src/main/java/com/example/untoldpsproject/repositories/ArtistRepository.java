package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.factory.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, String> {
}
