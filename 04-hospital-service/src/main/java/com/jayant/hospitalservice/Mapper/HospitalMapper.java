package com.jayant.hospitalservice.Mapper;

import com.jayant.hospitalservice.DTO.HospitalRequestDTO;
import com.jayant.hospitalservice.DTO.HospitalResponseDTO;
import com.jayant.hospitalservice.Entity.Hospital;

public class HospitalMapper {

    public static Hospital toEntity(HospitalRequestDTO requestDTO) {

        Hospital hospital = new Hospital();

        hospital.setName(requestDTO.getName());
        hospital.setAddress(requestDTO.getAddress());
        hospital.setCity(requestDTO.getCity());
        hospital.setLatitude(requestDTO.getLatitude());
        hospital.setLongitude(requestDTO.getLongitude());
        hospital.setActive(true);

        return hospital;
    }

    public static HospitalResponseDTO toResponseDTO(Hospital hospital) {

        return HospitalResponseDTO.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .address(hospital.getAddress())
                .city(hospital.getCity())
                .latitude(hospital.getLatitude())
                .longitude(hospital.getLongitude())
                .active(hospital.getActive())
                .build();
    }
}