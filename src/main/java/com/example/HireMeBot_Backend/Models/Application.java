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
@Table(name = "applications", schema = "hiremebot_db")
public class Application {
    @Id
    @ColumnDefault("(uuid_to_bin(uuid(), 1))")
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private byte[] id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "student_id", nullable = false, columnDefinition = "BINARY(16)")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "recruiter_student_assignments_id", nullable = false, columnDefinition = "BINARY(16)")
    private RecruiterStudentAssignment recruiterStudentAssignments;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "job_location", nullable = false)
    private String jobLocation;

    @Column(name = "job_link", nullable = false, columnDefinition = "TEXT")
    private String jobLink;

    @Column(name = "job_source", nullable = false, length = 100)
    private String jobSource;

    @Column(name = "applied_at")
    private Instant appliedAt;

    @ColumnDefault("0")
    @Column(name = "is_viewed", nullable = false)
    private Boolean isViewed = false;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            UUID uuid = UUID.randomUUID();
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            this.id = bb.array();
        }
        if (this.isViewed == null) {
            this.isViewed = false;
        }
        if (this.appliedAt == null) {
            this.appliedAt = Instant.now();
        }
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = Instant.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

}