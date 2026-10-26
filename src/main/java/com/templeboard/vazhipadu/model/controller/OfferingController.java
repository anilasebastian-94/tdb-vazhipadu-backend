package com.templeboard.vazhipadu.model.controller;

import com.templeboard.vazhipadu.model.dto.OfferingDto;
import com.templeboard.vazhipadu.model.request.OfferingRequest;
import com.templeboard.vazhipadu.model.service.OfferingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offerings")
@RequiredArgsConstructor
public class OfferingController {

    private final OfferingService offeringService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OfferingDto create(
            @Valid @RequestBody OfferingDto request) {
        return offeringService.create(request);
    }

    @GetMapping
    public List<OfferingDto> findAll() {
        return offeringService.findAll();
    }
}