package com.example.HireMeBot_Backend.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "otp_verifications", schema = "hiremebot_db")
public class OtpVerification {
    @Id
    @ColumnDefault("(uuid_to_bin(uuid(), 1))")
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private byte[] id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    private User user;

    @Column(name = "identifier", nullable = false)
    private String identifier;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @Column(name = "purpose", nullable = false, length = 50)
    private String purpose;

    @Column(name = "otp_hash", nullable = false)
    private String otpHash;

    @ColumnDefault("0")
    @Column(name = "attempts")
    private Byte attempts;

    @ColumnDefault("0")
    @Column(name = "is_used")
    private Boolean isUsed;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at")
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
        if (this.attempts == null) {
            this.attempts = 0;
        }
        if (this.isUsed == null) {
            this.isUsed = false;
        }
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

}