package com.jayant.hospitalservice.Mapper;

import com.jayant.hospitalservice.DTO.BedRequestDTO;
import com.jayant.hospitalservice.DTO.BedResponseDTO;
import com.jayant.hospitalservice.Entity.Bed;
import com.jayant.hospitalservice.Entity.Hospital;
import com.jayant.hospitalservice.Enum.BedStatus;

public class BedMapper {

    public static Bed toEntity(BedRequestDTO requestDTO, Hospital hospital) {

        Bed bed = new Bed();

        bed.setRoomNumber(requestDTO.getRoomNumber());
        bed.setBedNumber(requestDTO.getBedNumber());
        bed.setBedType(requestDTO.getBedType());
        bed.setHospital(hospital);

        bed.setStatus(BedStatus.AVAILABLE);
        bed.setActive(true);

        return bed;
    }

    public static BedResponseDTO toResponseDTO(Bed bed) {

        return BedResponseDTO.builder()
                .id(bed.getId())
                .roomNumber(bed.getRoomNumber())
                .bedNumber(bed.getBedNumber())
                .bedType(bed.getBedType())
                .status(bed.getStatus())
                .hospitalId(bed.getHospital().getId())
                .hospitalName(bed.getHospital().getName())
                .build();
    }
}
