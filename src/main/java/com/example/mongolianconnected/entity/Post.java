package com.example.mongolianconnected.entity;

import com.example.mongolianconnected.enums.PostStatus;
import com.example.mongolianconnected.enums.PostType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts",
        indexes = {
                @Index(name = "idx_post_region", columnList = "region_id"),
                @Index(name = "idx_post_category", columnList = "category_id")
        })
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private Long viewsCount = 0L;
    private Long likeCount = 0L;
    private Long commentCount = 0L;

    private boolean pinned;
    private boolean boosted;
}