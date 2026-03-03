package com.example.mongolianconnected.entity;

import com.example.mongolianconnected.enums.CommentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Long likeCount = 0L;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;
}