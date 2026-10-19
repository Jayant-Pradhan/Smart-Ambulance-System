
package com.jayant.hospitalservice.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentRequestDTO {

    private String name;

    private Long hospitalId;
}
