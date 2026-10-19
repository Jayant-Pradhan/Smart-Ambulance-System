package com.jayant.hospitalservice.Service.Implementation;

import com.jayant.hospitalservice.DTO.BedRequestDTO;
import com.jayant.hospitalservice.DTO.BedResponseDTO;
import com.jayant.hospitalservice.Entity.Bed;
import com.jayant.hospitalservice.Entity.Hospital;
import com.jayant.hospitalservice.Enum.BedStatus;
import com.jayant.hospitalservice.Enum.BedType;
import com.jayant.hospitalservice.Exception.ResourceNotFoundException;
import com.jayant.hospitalservice.Mapper.BedMapper;
import com.jayant.hospitalservice.Repository.BedRepository;
import com.jayant.hospitalservice.Repository.HospitalRepository;
import com.jayant.hospitalservice.Service.BedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class BedServiceImpl implements BedService {

    private final BedRepository bedRepository;
    private final HospitalRepository hospitalRepository;

    @Override
    public BedResponseDTO createBed(BedRequestDTO requestDTO) {

        Optional<Hospital> isAvail = hospitalRepository.findById(requestDTO.getHospitalId());

        if(isAvail.isEmpty()){
            throw new ResourceNotFoundException("hospital is not found with this id : " + requestDTO.getHospitalId());
        }
        Hospital hospital = isAvail.get();

        if(!Boolean.TRUE.equals(hospital.getActive())){
            throw new IllegalStateException("cannot create bed in an inactive hospital ");
        }
        boolean exist = bedRepository.existsByHospitalIdAndRoomNumberAndBedNumber(requestDTO.getHospitalId() , requestDTO.getRoomNumber() , requestDTO.getBedNumber());

        if(exist){
            throw new IllegalStateException("bed already exist in this hospital with the same room and bed number ");
        }
        Bed bed = BedMapper.toEntity(requestDTO,hospital);

        return BedMapper.toResponseDTO(bedRepository.save(bed));
    }

    @Override
    public BedResponseDTO getBedById(Long id) {
        Optional<Bed> isAvail = bedRepository.findById(id);
        if(isAvail.isPresent()){
            Bed bed = isAvail.get();
            return BedMapper.toResponseDTO(bed);
        }
        throw new ResourceNotFoundException("bed not found with this id : " +  id);
    }


    @Override
    public List<BedResponseDTO> getAllBeds() {
        List<BedResponseDTO> requestDTOList = new ArrayList<>();

        List<Bed> bedList = bedRepository.findByActiveTrue();

        for(Bed bed : bedList){

            requestDTOList.add(BedMapper.toResponseDTO(bed));
        }
        return requestDTOList;
    }

    @Override
    public List<BedResponseDTO> getBedsByHospitalId(Long hospitalId) {
        List<BedResponseDTO> responseDTOList = new ArrayList<>();
        Optional<Hospital> isAvail = hospitalRepository.findById(hospitalId);
        if(isAvail.isPresent()){
            List<Bed> bedList = bedRepository.findByActiveTrueAndHospitalId(hospitalId);

            for(Bed bed : bedList){
                responseDTOList.add(BedMapper.toResponseDTO(bed));
            }
        }
        else{
            throw new ResourceNotFoundException("hospital is not found with this id : " +  hospitalId);
        }
        return responseDTOList;
    }

    @Override
    public List<BedResponseDTO> getAvailableBeds(Long hospitalId, BedType bedType) {

        List<BedResponseDTO> responseDTOList = new ArrayList<>();
        Optional<Hospital> isAvail = hospitalRepository.findById(hospitalId);
        if(isAvail.isPresent()){
            List<Bed> bedList = bedRepository.findByHospitalIdAndActiveTrueAndBedTypeAndStatus(hospitalId,bedType,BedStatus.AVAILABLE);

            for(Bed bed : bedList){
                responseDTOList.add(BedMapper.toResponseDTO(bed));
            }
        }
        else{
            throw new ResourceNotFoundException("hospital is not found with this id : " +  hospitalId);
        }
        return responseDTOList;


    }

    @Override
    public BedResponseDTO updateBed(Long id, BedRequestDTO requestDTO) {
        Optional<Bed> isAvail = bedRepository.findById(id);



        if (isAvail.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Bed not found with id: " + id
            );
        }

        Bed bed = isAvail.get();

        boolean exists = bedRepository
                .existsByHospitalIdAndRoomNumberAndBedNumberAndIdNot(
                        bed.getHospital().getId(),
                        requestDTO.getRoomNumber() != null
                                ? requestDTO.getRoomNumber() : bed.getRoomNumber(),
                        requestDTO.getBedNumber() != null
                                ? requestDTO.getBedNumber() : bed.getBedNumber(),
                        id
                );

        if (exists) {
            throw new IllegalStateException(
                    "Another bed already exists with this room and bed number"
            );
        }

        if (requestDTO.getRoomNumber() != null) {
            bed.setRoomNumber(requestDTO.getRoomNumber());
        }

        if (requestDTO.getBedNumber() != null) {
            bed.setBedNumber(requestDTO.getBedNumber());
        }

        if (requestDTO.getBedType() != null) {
            bed.setBedType(requestDTO.getBedType());
        }

        return BedMapper.toResponseDTO(bedRepository.save(bed));
    }

    @Override
    public BedResponseDTO reserveBed(Long id) {
        Optional<Bed> isAvail = bedRepository.findById(id);

        if (isAvail.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Bed not found with id: " + id
            );
        }

        Bed bed = isAvail.get();

        if (bed.getActive() == false) {
            throw new IllegalStateException(
                    "Cannot reserve an inactive bed"
            );
        }

        if (bed.getStatus() != BedStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Bed is not available for reservation"
            );
        }

        if (bed.getHospital().getActive() == false) {
            throw new IllegalStateException(
                    "Cannot reserve a bed in an inactive hospital"
            );
        }

        bed.setStatus(BedStatus.RESERVED);

        return BedMapper.toResponseDTO(bedRepository.save(bed));
    }

    @Override
    public BedResponseDTO occupyBed(Long id) {
        Optional<Bed> isAvail = bedRepository.findById(id);

        if (isAvail.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Bed not found with id: " + id
            );
        }

        Bed bed = isAvail.get();

        if (bed.getStatus() != BedStatus.RESERVED) {
            throw new IllegalStateException(
                    "Only reserved beds can be occupied"
            );
        }

        bed.setStatus(BedStatus.OCCUPIED);

        return BedMapper.toResponseDTO(bedRepository.save(bed));
    }

    @Override
    public BedResponseDTO releaseBed(Long id) {
        Optional<Bed> isAvail = bedRepository.findById(id);

        if(isAvail.isPresent()){
            Bed bed = isAvail.get();
            if(bed.getStatus() == BedStatus.OCCUPIED){
                bed.setStatus(BedStatus.AVAILABLE);
            }
            else {
                throw new IllegalStateException(
                        "Only occupied beds can be released"
                );
            }

            return BedMapper.toResponseDTO(bedRepository.save(bed));

        }
        throw new ResourceNotFoundException("bed is not found with this id : " + id);
    }

    @Override
    public BedResponseDTO markBedOutOfService(Long id) {
        Optional<Bed> isAvail = bedRepository.findById(id);

        if (isAvail.isPresent()) {
            Bed bed = isAvail.get();

            if (bed.getStatus() != BedStatus.AVAILABLE) {
                throw new IllegalStateException(
                        "Only available beds can be marked out of service"
                );
            }

            bed.setStatus(BedStatus.OUT_OF_SERVICE);

            return BedMapper.toResponseDTO(bedRepository.save(bed));
        }

        throw new ResourceNotFoundException(
                "Bed is not found with this id: " + id
        );

    }

    @Override
    public BedResponseDTO markBedAvailable(Long id) {

        Optional<Bed> isAvail = bedRepository.findById(id);

        if (isAvail.isPresent()) {
            Bed bed = isAvail.get();

            if (bed.getStatus() != BedStatus.OUT_OF_SERVICE) {
                throw new IllegalStateException(
                        "Only out-of-service beds can be marked available"
                );
            }

            bed.setStatus(BedStatus.AVAILABLE);

            return BedMapper.toResponseDTO(bedRepository.save(bed));
        }

        throw new ResourceNotFoundException(
                "Bed is not found with this id: " + id
        );
    }

    @Override
    public Boolean deactivateBed(Long id) {

        Optional<Bed> isAvail = bedRepository.findById(id);
        if(isAvail.isPresent()){
            Bed bed = isAvail.get();

            if(bed.getStatus() == BedStatus.AVAILABLE || bed.getStatus() == BedStatus.OUT_OF_SERVICE){
                bed.setActive(false);
                bedRepository.save(bed);
                return true;
            }
            else{
                throw new IllegalStateException("not possible");
            }

        }
        throw new ResourceNotFoundException("bed is not found with this id : " + id);
    }
}
