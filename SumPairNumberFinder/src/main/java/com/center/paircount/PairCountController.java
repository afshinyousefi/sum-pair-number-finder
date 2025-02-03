package com.center.paircount;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping(value = "/api/v1/pairNumber")
@AllArgsConstructor
public class PairCountController {
    private final IPairCountService iPairCountService;

    @PostMapping("/calculate-pair-count")
    public ResponseEntity<PairCountNumberEntity> save(@RequestBody @Valid NumberDto numberDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iPairCountService.calculate(numberDto));
    }
}
