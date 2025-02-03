package com.center.paircount;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Data
public class PairCountNumberService implements IPairCountService {
    private final PairCountRepository pairCountRepository;
    private static final Logger logger = LoggerFactory.getLogger(PairCountNumberService.class);

    @Override
    public PairCountNumberEntity calculate(NumberDto numberDto) {
        List<Integer> numberList = generateRandomList(
            numberDto.getNumberListSize() != null ? numberDto.getNumberListSize() : 10,
            numberDto.getMinNumber() != null ? numberDto.getMinNumber() : 1,
            numberDto.getMaxNumber() != null ? numberDto.getMaxNumber() : 10
        );
        Map<Integer, List<Integer>> numberHashMap = numberList.stream()
            .collect(Collectors.groupingBy(n -> n));
        List<Pair<Integer, Integer>> pairNumberList = new ArrayList<>();

        Iterator<Map.Entry<Integer, List<Integer>>> iterator = numberHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<Integer>> entry = iterator.next();
            Integer key = entry.getKey();
            int targetPair = numberDto.getTargetNumber() - key;
            if (targetPair > 0 && numberHashMap.containsKey(targetPair)) {
                List<Integer> keyCount = numberHashMap.get(key);
                List<Integer> pairCount = numberHashMap.get(targetPair);
                int minSize = Math.min(keyCount.size(), pairCount.size());
                if (key == targetPair) {
                    minSize /= 2;
                }
                pairNumberList.addAll(Collections.nCopies(minSize, new Pair<>(key, targetPair)));
                keyCount.clear();
                pairCount.clear();
                iterator.remove();
            }
        }

        logger.info("list of numbers: {}", numberList);
        logger.info("-------------------------------------------------");
        logger.info("target number is: {}", numberDto.getTargetNumber());
        logger.info("-------------------------------------------------");
        logger.info("count of pair number list: {}", pairNumberList);

        PairCountNumberEntity pairCountNumberEntity = new PairCountNumberEntity();
        pairCountNumberEntity.setTargetNumber(numberDto.getTargetNumber());
        pairCountNumberEntity.setNumbers(numberList);
        pairCountNumberEntity.setPairNumberList(pairNumberList);
        return pairCountRepository.save(pairCountNumberEntity);
    }

    public static class Pair<T, U> {
        private T first;
        private U second;

        @JsonCreator
        public Pair(@JsonProperty("first") T first, @JsonProperty("second") U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) obj;
            return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }

    public List<Integer> generateRandomList(int size, int min, int max) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size of number list must be greater than 0");
        }
        if (min >= max) {
            throw new IllegalArgumentException("Min number must be less than max");
        }
        List<Integer> randomList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int randomNumber = random.nextInt(max - min + 1) + min;
            randomList.add(randomNumber);
        }
        return randomList;
    }
}
