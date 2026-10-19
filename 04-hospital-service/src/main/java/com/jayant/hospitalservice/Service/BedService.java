package com.jayant.hospitalservice.Service;

import com.jayant.hospitalservice.DTO.BedRequestDTO;
import com.jayant.hospitalservice.DTO.BedResponseDTO;
import com.jayant.hospitalservice.Enum.BedType;

import java.util.List;

public interface BedService {

    BedResponseDTO createBed(BedRequestDTO requestDTO);

    BedResponseDTO getBedById(Long id);

    List<BedResponseDTO> getAllBeds();

    List<BedResponseDTO> getBedsByHospitalId(Long hospitalId);

    List<BedResponseDTO> getAvailableBeds(Long hospitalId , BedType bedType);

    BedResponseDTO updateBed(Long id  , BedRequestDTO requestDTO);

    BedResponseDTO reserveBed(Long id);

    BedResponseDTO occupyBed(Long id);

    BedResponseDTO releaseBed(Long id);

    BedResponseDTO markBedOutOfService(Long id);

    BedResponseDTO markBedAvailable(Long id);

    Boolean deactivateBed(Long id);

}
