package com.camper.rental.service.fleet;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camper.rental.dto.fleet.CamperRequestDto;
import com.camper.rental.dto.fleet.CamperResponseDto;
import com.camper.rental.entity.fleet.Camper;
import com.camper.rental.entity.fleet.CamperModel;
import com.camper.rental.mapper.fleet.CamperMapper;
import com.camper.rental.repository.fleet.CamperModelRepository;
import com.camper.rental.repository.fleet.CamperRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CamperService {

    private final CamperRepository camperRepository;
    private final CamperModelRepository camperModelRepository;
    private final CamperMapper camperMapper;

    @Transactional(readOnly = true)
    public List<CamperResponseDto> getAllCampers() {
        return camperRepository.findAll().stream()
            .map(camperMapper::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public CamperResponseDto getCamperById(Long id) {
        Camper camper = camperRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Camper not found for id: " + id));
        return camperMapper.toDto(camper);
    }

    public CamperResponseDto createCamper(CamperRequestDto dto) {
        CamperModel model = findModelById(dto.getModelId());
        Camper camper = camperMapper.toEntity(dto);
        camper.setCamperModel(model);
        Camper saved = camperRepository.save(camper);
        return camperMapper.toDto(saved);
    }

    public CamperResponseDto updateCamper(Long id, CamperRequestDto dto) {
        Camper camper = camperRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Camper not found for id: " + id));

        CamperModel model = findModelById(dto.getModelId());
        camperMapper.updateEntity(dto, camper);
        camper.setCamperModel(model);

        Camper updated = camperRepository.save(camper);
        return camperMapper.toDto(updated);
    }

    public void deleteCamper(Long id) {
        Camper camper = camperRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Camper not found for id: " + id));
        camperRepository.delete(camper);
    }

    private CamperModel findModelById(Long modelId) {
        return camperModelRepository.findById(modelId)
            .orElseThrow(() -> new EntityNotFoundException("Camper model not found for id: " + modelId));
    }
}
