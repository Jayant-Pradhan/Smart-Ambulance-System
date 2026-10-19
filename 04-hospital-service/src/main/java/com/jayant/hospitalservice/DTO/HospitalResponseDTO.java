
package com.jayant.hospitalservice.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalResponseDTO {

    private Long id;

    private String name;

    private String address;

    private String city;

    private Double latitude;

    private Double longitude;

    private Boolean active;
}
