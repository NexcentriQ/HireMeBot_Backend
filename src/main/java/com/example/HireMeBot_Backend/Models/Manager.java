package com.example.HireMeBot_Backend.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "managers", schema = "hiremebot_db")
public class Manager {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private byte[] id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private User users;

}