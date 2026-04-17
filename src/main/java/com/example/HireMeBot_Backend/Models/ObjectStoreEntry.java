package com.example.HireMeBot_Backend.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "object_store_entries", schema = "hiremebot_db")
public class ObjectStoreEntry {
    @Id
    @ColumnDefault("(uuid_to_bin(uuid(), 1))")
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private byte[] id;

    @Column(name = "object_key", nullable = false, length = 512)
    private String objectKey;

    @Column(name = "checksum", nullable = false, length = 64)
    private String checksum;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (this.id == null) {
            UUID uuid = UUID.randomUUID();
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            this.id = bb.array();
        }
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

}