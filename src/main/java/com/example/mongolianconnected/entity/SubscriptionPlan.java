package com.example.mongolianconnected.entity;
import com.example.mongolianconnected.enums.BillingCycle;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscription_plans")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlan extends BaseEntity {

    private String name;

    private Double price;

    @Enumerated(EnumType.STRING)
    private BillingCycle billingCycle;

    @Column(columnDefinition = "TEXT")
    private String featuresJson;

    private boolean active;
}