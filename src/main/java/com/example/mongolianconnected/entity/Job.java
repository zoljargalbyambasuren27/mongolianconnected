package com.example.mongolianconnected.entity;
import com.example.mongolianconnected.enums.JobStatus;
import com.example.mongolianconnected.enums.JobType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jobs",
        indexes = {
                @Index(name = "idx_job_region", columnList = "region_id")
        })
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private User employer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private Integer salaryMin;
    private Integer salaryMax;

    private boolean sponsorAvailable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    private boolean featured;
}