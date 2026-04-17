package com.example.HireMeBot_Backend.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.nio.ByteBuffer;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    
    @Id
    @ColumnDefault("(uuid_to_bin(uuid(), 1))")
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private byte[] id;

    @PrePersist
    protected void generateId() {
        if (this.id == null) {
            UUID uuid = UUID.randomUUID();
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            this.id = bb.array();
        }
    }
    
    /**
     * Gets the UUID representation of the ID
     * @return UUID
     */
    public UUID getUuid() {
        if (this.id == null) {
            return null;
        }
        ByteBuffer bb = ByteBuffer.wrap(this.id);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }
    
    /**
     * Sets the ID from a UUID
     * @param uuid The UUID to set
     */
    public void setUuid(UUID uuid) {
        if (uuid == null) {
            this.id = null;
            return;
        }
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        this.id = bb.array();
    }
}
