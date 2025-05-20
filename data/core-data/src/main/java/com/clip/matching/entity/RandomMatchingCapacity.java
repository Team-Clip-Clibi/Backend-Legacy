package com.clip.matching.entity;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RandomMatchingCapacity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "random_matching_id")
    private RandomMatching randomMatching;

    @Column
    private Integer availableCapacity;

    @Column
    private Integer pendingCapacity;

    @Version
    private Long version;

    @Builder
    public RandomMatchingCapacity(RandomMatching randomMatching, Integer availableCapacity) {
        this.randomMatching = randomMatching;
        this.availableCapacity = availableCapacity;
        this.pendingCapacity = 0;
    }

    public boolean reserve() {
        if (availableCapacity >= 1) {
            availableCapacity -= 1;
            pendingCapacity += 1;
            return true;
        }
        return false;
    }

    public void cancelReservation() {
        pendingCapacity -= 1;
        availableCapacity += 1;
    }

    public int getConfirmedParticipants(int totalCapacity) {
        return totalCapacity - availableCapacity - pendingCapacity;
    }

    // Todo : 결제 승인 시 호출
    public void confirmReservation() {
        pendingCapacity -= 1;
    }
}
