package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import com.example.demo.dtos.ParkingDto;
import com.example.demo.models.ParkingModel;
import com.example.demo.services.ParkingService;

import org.springframework.beans.BeanUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingController {

    final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    
    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingDto parkingDto){

        if (parkingService.existsByLicensePlateCar(parkingDto.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ja existe uma placa cadastrada em uso ");
        }

        if (parkingService.existsByParkingSpotNumber(parkingDto.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("vaga ja utilizada ");
        }

        if (parkingService.existsByApartmentAndBlock(parkingDto.getApartment(),parkingDto.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("apartamento e bloco ja cadastrado ");
        };

    
        var parkingModel = new ParkingModel();
        BeanUtils.copyProperties(parkingDto, parkingModel);
        parkingModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingService.save(parkingModel));
        

    }


    @GetMapping
    public ResponseEntity<List<ParkingModel>> getAllParkingSpots(){
        return ResponseEntity.status(HttpStatus.OK).body(parkingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingModel> parkingSpotModelOptional = parkingService.findById(id);
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingModel> parkingSpotModelOptional = parkingService.findById(id);
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        parkingService.delete(parkingSpotModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
                                                  @RequestBody @Valid ParkingDto parkingSpotDto){
        Optional<ParkingModel> parkingSpotModelOptional = parkingService.findById(id);
        if (!parkingSpotModelOptional.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        var parkingSpotModel = new ParkingModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(parkingService.save(parkingSpotModel));
}






}
