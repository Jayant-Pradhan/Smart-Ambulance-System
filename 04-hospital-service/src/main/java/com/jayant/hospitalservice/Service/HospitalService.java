package com.jayant.hospitalservice.Service;

import com.jayant.hospitalservice.DTO.HospitalRequestDTO;
import com.jayant.hospitalservice.DTO.HospitalResponseDTO;

import java.util.List;

public interface HospitalService {

    HospitalResponseDTO createHospital(HospitalRequestDTO requestDTO);

    HospitalResponseDTO getHospitalById(Long id);

    List<HospitalResponseDTO> getAllHospitals();

    HospitalResponseDTO updateHospital(Long id , HospitalRequestDTO requestDTO);

    Boolean deactivateHospital(Long id);
}
