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
@Table(name = "recruiter_student_assignments", schema = "hiremebot_db")
public class RecruiterStudentAssignment {
    @Id
    @ColumnDefault("(uuid_to_bin(uuid(), 1))")
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private byte[] id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "recruiter_id", nullable = false, columnDefinition = "BINARY(16)")
    private Recruiter recruiter;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "student_id", nullable = false, columnDefinition = "BINARY(16)")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assigned_by", nullable = false, columnDefinition = "BINARY(16)")
    private Manager assignedBy;

    @Column(name = "assigned_at")
    private Instant assignedAt;

    @PrePersist
    void prePersist() {
        if (this.id == null) {
            UUID uuid = UUID.randomUUID();
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            this.id = bb.array();
        }
        if (this.assignedAt == null) {
            this.assignedAt = Instant.now();
        }
    }

}