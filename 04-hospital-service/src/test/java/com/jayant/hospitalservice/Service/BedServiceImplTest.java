package com.jayant.hospitalservice.Service;

import com.jayant.hospitalservice.DTO.BedRequestDTO;
import com.jayant.hospitalservice.DTO.BedResponseDTO;
import com.jayant.hospitalservice.Entity.Bed;
import com.jayant.hospitalservice.Entity.Hospital;
import com.jayant.hospitalservice.Enum.BedStatus;
import com.jayant.hospitalservice.Enum.BedType;
import com.jayant.hospitalservice.Repository.BedRepository;
import com.jayant.hospitalservice.Repository.HospitalRepository;
import com.jayant.hospitalservice.Service.Implementation.BedServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BedServiceImplTest {

    @Mock
    private BedRepository bedRepository;

    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private BedServiceImpl bedService;

    private Hospital createHospital(Long id, Boolean active) {
        Hospital hospital = new Hospital();
        hospital.setId(id);
        hospital.setActive(active);
        return hospital;
    }

    private Bed createBed(Long id, BedStatus status, Boolean active, Hospital hospital) {
        Bed bed = new Bed();
        bed.setId(id);
        bed.setRoomNumber("101");
        bed.setBedNumber("B1");
        bed.setBedType(BedType.ICU);
        bed.setStatus(status);
        bed.setActive(active);
        bed.setHospital(hospital);
        return bed;
    }

    // 1. createBed
    @Test
    void createBed_shouldSaveAndReturnBed() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));
        when(bedRepository.existsByHospitalIdAndRoomNumberAndBedNumber(any(), any(), any())).thenReturn(false);
        Bed savedBed = createBed(10L, BedStatus.AVAILABLE, true, hospital);
        when(bedRepository.save(any(Bed.class))).thenReturn(savedBed);

        BedRequestDTO requestDTO = new BedRequestDTO();
        requestDTO.setHospitalId(1L);
        requestDTO.setRoomNumber("101");
        requestDTO.setBedNumber("B1");
        requestDTO.setBedType(BedType.ICU);

        // Act
        BedResponseDTO actualResult = bedService.createBed(requestDTO);

        // Assert
        assertEquals(10L, actualResult.getId());
        verify(bedRepository).save(any(Bed.class));
    }

    // 2. getBedById
    @Test
    void getBedById_shouldReturnBed() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        Bed bed = createBed(10L, BedStatus.AVAILABLE, true, hospital);
        when(bedRepository.findById(10L)).thenReturn(Optional.of(bed));

        // Act
        BedResponseDTO actualResult = bedService.getBedById(10L);

        // Assert
        assertEquals(10L, actualResult.getId());
    }

    // 3. getAllBeds
    @Test
    void getAllBeds_shouldReturnActiveBeds() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        List<Bed> beds = new ArrayList<>();
        beds.add(createBed(10L, BedStatus.AVAILABLE, true, hospital));
        beds.add(createBed(11L, BedStatus.OCCUPIED, true, hospital));
        when(bedRepository.findByActiveTrue()).thenReturn(beds);

        // Act
        List<BedResponseDTO> actualResult = bedService.getAllBeds();

        // Assert
        assertEquals(2, actualResult.size());
        assertEquals(10L, actualResult.get(0).getId());
        assertEquals(11L, actualResult.get(1).getId());
    }

    // 4. getBedsByHospitalId
    @Test
    void getBedsByHospitalId_shouldReturnBeds() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));
        List<Bed> beds = new ArrayList<>();
        beds.add(createBed(10L, BedStatus.AVAILABLE, true, hospital));
        when(bedRepository.findByActiveTrueAndHospitalId(1L)).thenReturn(beds);

        // Act
        List<BedResponseDTO> actualResult = bedService.getBedsByHospitalId(1L);

        // Assert
        assertEquals(1, actualResult.size());
        assertEquals(10L, actualResult.get(0).getId());
    }

    // 5. getAvailableBeds
    @Test
    void getAvailableBeds_shouldReturnAvailableBeds() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));
        List<Bed> beds = new ArrayList<>();
        beds.add(createBed(10L, BedStatus.AVAILABLE, true, hospital));
        when(bedRepository.findByHospitalIdAndActiveTrueAndBedTypeAndStatus(
                1L, BedType.ICU, BedStatus.AVAILABLE)).thenReturn(beds);

        // Act
        List<BedResponseDTO> actualResult = bedService.getAvailableBeds(1L, BedType.ICU);

        // Assert
        assertEquals(1, actualResult.size());
        assertEquals(10L, actualResult.get(0).getId());
    }

    // 6. updateBed
    @Test
    void updateBed_shouldUpdateRoomAndBedNumber() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        Bed bed = createBed(10L, BedStatus.AVAILABLE, true, hospital);
        when(bedRepository.findById(10L)).thenReturn(Optional.of(bed));
        when(bedRepository.existsByHospitalIdAndRoomNumberAndBedNumberAndIdNot(
                any(), any(), any(), any())).thenReturn(false);
        when(bedRepository.save(any(Bed.class))).thenReturn(bed);

        BedRequestDTO requestDTO = new BedRequestDTO();
        requestDTO.setRoomNumber("202");
        requestDTO.setBedNumber("B9");

        // Act
        BedResponseDTO actualResult = bedService.updateBed(10L, requestDTO);

        // Assert
        assertEquals(10L, actualResult.getId());
        assertEquals("202", bed.getRoomNumber());
        assertEquals("B9", bed.getBedNumber());
    }

    // 7. reserveBed
    @Test
    void reserveBed_shouldSetStatusReserved() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        Bed bed = createBed(10L, BedStatus.AVAILABLE, true, hospital);
        when(bedRepository.findById(10L)).thenReturn(Optional.of(bed));
        when(bedRepository.save(any(Bed.class))).thenReturn(bed);

        // Act
        BedResponseDTO actualResult = bedService.reserveBed(10L);

        // Assert
        assertEquals(10L, actualResult.getId());
        assertEquals(BedStatus.RESERVED, bed.getStatus());
    }

    // 8. occupyBed
    @Test
    void occupyBed_shouldSetStatusOccupied() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        Bed bed = createBed(10L, BedStatus.RESERVED, true, hospital);
        when(bedRepository.findById(10L)).thenReturn(Optional.of(bed));
        when(bedRepository.save(any(Bed.class))).thenReturn(bed);

        // Act
        BedResponseDTO actualResult = bedService.occupyBed(10L);

        // Assert
        assertEquals(10L, actualResult.getId());
        assertEquals(BedStatus.OCCUPIED, bed.getStatus());
    }

    // 9. releaseBed
    @Test
    void releaseBed_shouldSetStatusAvailable() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        Bed bed = createBed(10L, BedStatus.OCCUPIED, true, hospital);
        when(bedRepository.findById(10L)).thenReturn(Optional.of(bed));
        when(bedRepository.save(any(Bed.class))).thenReturn(bed);

        // Act
        BedResponseDTO actualResult = bedService.releaseBed(10L);

        // Assert
        assertEquals(10L, actualResult.getId());
        assertEquals(BedStatus.AVAILABLE, bed.getStatus());
    }

    // 10. markBedOutOfService
    @Test
    void markBedOutOfService_shouldSetStatusOutOfService() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        Bed bed = createBed(10L, BedStatus.AVAILABLE, true, hospital);
        when(bedRepository.findById(10L)).thenReturn(Optional.of(bed));
        when(bedRepository.save(any(Bed.class))).thenReturn(bed);

        // Act
        BedResponseDTO actualResult = bedService.markBedOutOfService(10L);

        // Assert
        assertEquals(10L, actualResult.getId());
        assertEquals(BedStatus.OUT_OF_SERVICE, bed.getStatus());
    }

    // 11. markBedAvailable
    @Test
    void markBedAvailable_shouldSetStatusAvailable() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        Bed bed = createBed(10L, BedStatus.OUT_OF_SERVICE, true, hospital);
        when(bedRepository.findById(10L)).thenReturn(Optional.of(bed));
        when(bedRepository.save(any(Bed.class))).thenReturn(bed);

        // Act
        BedResponseDTO actualResult = bedService.markBedAvailable(10L);

        // Assert
        assertEquals(10L, actualResult.getId());
        assertEquals(BedStatus.AVAILABLE, bed.getStatus());
    }

    // 12. deactivateBed
    @Test
    void deactivateBed_shouldReturnTrueAndSetInactive() {
        // Arrange
        Hospital hospital = createHospital(1L, true);
        Bed bed = createBed(10L, BedStatus.AVAILABLE, true, hospital);
        when(bedRepository.findById(10L)).thenReturn(Optional.of(bed));
        Boolean expectedResult = true;

        // Act
        Boolean actualResult = bedService.deactivateBed(10L);

        // Assert
        assertEquals(expectedResult, actualResult);
        assertEquals(false, bed.getActive());
        verify(bedRepository).save(bed);
    }
}