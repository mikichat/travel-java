package com.travelcrm.service.impl;

import com.travelcrm.dto.TravelRequestDto;
import com.travelcrm.dto.TravelResponseDto;
import com.travelcrm.entity.Travel;
import com.travelcrm.repository.TravelRepository;
import com.travelcrm.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelServiceImpl implements TravelService {

    @Autowired
    private TravelRepository travelRepository;

    @Override
    public TravelResponseDto createTravel(TravelRequestDto travelRequestDto) {
        Travel travel = new Travel();
        travel.setDestination(travelRequestDto.destination());
        travel.setDescription(travelRequestDto.description());
        travel.setPrice(travelRequestDto.price());
        Travel savedTravel = travelRepository.save(travel);
        return new TravelResponseDto(savedTravel.getId(), savedTravel.getDestination(), savedTravel.getDescription(), savedTravel.getPrice());
    }

    @Override
    public List<TravelResponseDto> getAllTravels() {
        return travelRepository.findAll().stream()
                .map(travel -> new TravelResponseDto(travel.getId(), travel.getDestination(), travel.getDescription(), travel.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public TravelResponseDto getTravelById(Long id) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Travel not found with id: " + id));
        return new TravelResponseDto(travel.getId(), travel.getDestination(), travel.getDescription(), travel.getPrice());
    }

    @Override
    @Transactional
    public TravelResponseDto updateTravel(Long id, TravelRequestDto travelRequestDto) {
        Travel existingTravel = travelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Travel not found with id: " + id));

        existingTravel.setDestination(travelRequestDto.destination());
        existingTravel.setDescription(travelRequestDto.description());
        existingTravel.setPrice(travelRequestDto.price());

        Travel updatedTravel = travelRepository.save(existingTravel);
        return new TravelResponseDto(updatedTravel.getId(), updatedTravel.getDestination(), updatedTravel.getDescription(), updatedTravel.getPrice());
    }

    @Override
    public void deleteTravel(Long id) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Travel not found with id: " + id));
        travelRepository.delete(travel);
    }
} 