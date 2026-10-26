package com.templeboard.vazhipadu.model.service;

import com.templeboard.vazhipadu.model.dto.OfferingDto;
import com.templeboard.vazhipadu.model.entity.Offering;
import com.templeboard.vazhipadu.model.repository.OfferingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferingService {

    private final OfferingRepository offeringRepository;

    @Transactional
    public OfferingDto create( OfferingDto request) {
        boolean active = request.getActive() == null
                || request.getActive();
        Offering offering = request.toEntity();
        return offeringRepository.save(offering).toDto();
    }

    @Transactional(readOnly = true)
    public List<OfferingDto> findAll() {
        return offeringRepository.findAll()
                .stream()
                .map(Offering::toDto)
                .toList();
    }
}
