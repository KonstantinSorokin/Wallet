package com.example.wallet.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Transaction entity.
 * Used for both credit and debit transactions, amount is negative for debit ones
 * Stored with relation to a player.
 */
@Entity
@Table(name = "Transaction",
       indexes = {
               @Index(name = "fn_index_id", columnList = "id"),
               @Index(name = "fn_index_player", columnList = "player")
       })
@NoArgsConstructor @AllArgsConstructor
public class Transaction {
    @Id
    @Column(name = "id", nullable = false)
    @Getter @Setter
    private Long id;

    @Column(name = "amount", nullable = false)
    @Getter @Setter
    private Long amount;

    @ManyToOne
    @JoinColumn(name = "player", nullable = false)
    private Player player;

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", amount = " + amount + ", player = FIXME]";
    }

}
