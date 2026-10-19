package com.jayant.hospitalservice.Repository;

import com.jayant.hospitalservice.Entity.Bed;
import com.jayant.hospitalservice.Enum.BedStatus;
import com.jayant.hospitalservice.Enum.BedType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed , Long> {

    boolean existsByHospitalIdAndRoomNumberAndBedNumber(Long hospitalId , String roomNumber , String bedNumber);

    List<Bed> findByActiveTrue();


    List<Bed> findByActiveTrueAndHospitalId(Long hospitalId);

    List<Bed> findByHospitalIdAndActiveTrueAndBedTypeAndStatus(Long hospitalId , BedType bedType , BedStatus status);

    boolean existsByHospitalIdAndRoomNumberAndBedNumberAndIdNot(
            Long hospitalId,
            String roomNumber,
            String bedNumber,
            Long id
    );
}
