package com.example.wallet.repositories;

import com.example.wallet.entities.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Repository for Player entity
 */
@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    Optional<Player> getPlayerByName(String name);

    Collection<Player> findAll();

}
