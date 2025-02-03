package com.center.paircount;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class PairCountNumberEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private Integer targetNumber;

    @Column(columnDefinition = "TEXT")
    private String numberString;

    @Transient
    @JsonIgnore
    private List<Integer> numbers;

    @Column(columnDefinition = "TEXT")
    private String pairNumberJson;

    @Transient
    private List<PairCountNumberService.Pair<Integer, Integer>> pairNumberList;

    public void setNumbers(List<Integer> numbers) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.numberString = objectMapper.writeValueAsString(numbers);
            this.numbers = numbers;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPairNumberList(List<PairCountNumberService.Pair<Integer, Integer>> pairNumberList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.pairNumberJson = objectMapper.writeValueAsString(pairNumberList);
            this.pairNumberList = pairNumberList;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
