package com.example.demo.services;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;
import com.example.demo.models.ParkingModel;
import com.example.demo.repository.ParkingRepository;
import org.springframework.stereotype.Service;

@Service
public class ParkingService {
    
    final ParkingRepository parkingRepository;

    public ParkingService(ParkingRepository parkingRepository){
        this.parkingRepository = parkingRepository;
    }
     
    @Transactional
    public ParkingModel save(ParkingModel parkingModel) {
        return parkingRepository.save(parkingModel);
    }


    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return parkingRepository.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return parkingRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return parkingRepository.existsByApartmentAndBlock(apartment, block);
    }

    public List<ParkingModel> findAll() {
        return parkingRepository.findAll();
    }

    public Optional<ParkingModel> findById(UUID id) {
        return parkingRepository.findById(id);
    }

    @Transactional
    public void delete(ParkingModel parkingModel) {
        parkingRepository.delete(parkingModel);
    }
    

   
}
