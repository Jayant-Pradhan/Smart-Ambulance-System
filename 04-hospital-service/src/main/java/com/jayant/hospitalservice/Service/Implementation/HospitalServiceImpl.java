package com.jayant.hospitalservice.Service.Implementation;

import com.jayant.hospitalservice.DTO.HospitalRequestDTO;
import com.jayant.hospitalservice.DTO.HospitalResponseDTO;
import com.jayant.hospitalservice.Entity.Hospital;
import com.jayant.hospitalservice.Exception.ResourceNotFoundException;
import com.jayant.hospitalservice.Mapper.HospitalMapper;
import com.jayant.hospitalservice.Repository.HospitalRepository;
import com.jayant.hospitalservice.Service.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalServiceImpl  implements HospitalService {
    private final HospitalRepository hospitalRepository;
    @Override
    public HospitalResponseDTO createHospital(HospitalRequestDTO requestDTO) {
        return HospitalMapper.toResponseDTO(hospitalRepository.save(HospitalMapper.toEntity(requestDTO)));
    }
    @Override
    public HospitalResponseDTO getHospitalById(Long id) {
        Optional<Hospital> isAVail = hospitalRepository.findById(id);
        if(isAVail.isPresent()){
            Hospital hospital = isAVail.get();
            return HospitalMapper.toResponseDTO(hospital);
        }
        else{
            throw new ResourceNotFoundException("hospital id is not found with this id : " +  id);
        }
    }
    @Override
    public List<HospitalResponseDTO> getAllHospitals() {
        List<HospitalResponseDTO> responseDTOList = new ArrayList<>();
        List<Hospital> available = hospitalRepository.findAll();
        for(Hospital hospital : available) responseDTOList.add(HospitalMapper.toResponseDTO(hospital));
        return responseDTOList;
    }
    @Override
    public HospitalResponseDTO updateHospital(Long id, HospitalRequestDTO requestDTO) {

        Optional<Hospital> isAvail = hospitalRepository.findById(id);

        if(isAvail.isPresent()){
            Hospital hospital = isAvail.get();

            if (requestDTO.getName() != null) {
                hospital.setName(requestDTO.getName());
            }

            if (requestDTO.getAddress() != null) {
                hospital.setAddress(requestDTO.getAddress());
            }

            if (requestDTO.getCity() != null) {
                hospital.setCity(requestDTO.getCity());
            }

            if (requestDTO.getLatitude() != null) {
                hospital.setLatitude(requestDTO.getLatitude());
            }

            if (requestDTO.getLongitude() != null) {
                hospital.setLongitude(requestDTO.getLongitude());
            }

            return HospitalMapper.toResponseDTO(hospitalRepository.save(hospital));


        }
        throw new ResourceNotFoundException("hospital id is not found with this id :  " +  id );
    }
    @Override
    public Boolean deactivateHospital(Long id) {

        Optional<Hospital> isAvail = hospitalRepository.findById(id);

        if(isAvail.isPresent()){
            Hospital hospital = isAvail.get();
            hospital.setActive(false);
            hospitalRepository.save(hospital);
            return true;
        }
        throw new ResourceNotFoundException("hospital id is not found with this id :  " +  id );
    }
}
