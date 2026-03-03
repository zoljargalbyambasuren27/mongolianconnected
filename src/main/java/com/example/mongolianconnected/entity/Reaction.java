package com.example.mongolianconnected.entity;
import com.example.mongolianconnected.enums.ReactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.tool.schema.TargetType;

import java.util.UUID;

@Entity
@Table(name = "reactions",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "targetType", "targetId"}
        ))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    private UUID targetId;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
}