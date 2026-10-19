
package com.jayant.hospitalservice.DTO;

import com.jayant.hospitalservice.Enum.BedType;
import com.jayant.hospitalservice.Enum.BedStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BedRequestDTO {

    private String roomNumber;

    private String bedNumber;

    private BedType bedType;

    private BedStatus status;

    private Long hospitalId;
}
