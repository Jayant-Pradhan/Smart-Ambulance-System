
package com.jayant.hospitalservice.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponseDTO {

    private Long id;

    private String name;

    private Long hospitalId;

    private String hospitalName;

    private Boolean active;
}
