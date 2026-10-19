
package com.jayant.hospitalservice.Entity;

import com.jayant.hospitalservice.Enum.BedType;
import com.jayant.hospitalservice.Enum.BedStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "beds",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"room_number", "bed_number", "hospital_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Column(name = "bed_number", nullable = false)
    private String bedNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BedType bedType;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BedStatus status = BedStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;
}
