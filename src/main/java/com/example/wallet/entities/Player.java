package com.example.wallet.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Player entity.  Represents a player as a named balance.
 */
@Entity
@Table(name="Player")
@NoArgsConstructor
public class Player {

    // The balance which is set to a new Player when it is created
    public static final long INITIAL_BALANCE = 0;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter @Setter
    private Long id;

    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @Column(name = "balance", nullable = false)
    @Getter @Setter
    private Long balance;

    public Player(String name) {
        this.name = name;
        this.balance = INITIAL_BALANCE;
    }

}
