package com.center.paircount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PairCountNumberServiceTest {

    @Mock
    private PairCountRepository pairCountRepository;
    @InjectMocks
    private PairCountNumberService serviceUnderTest;
    @Mock
    private PairCountNumberService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Spy the service to override the generateRandomList method
        serviceUnderTest = new PairCountNumberService(pairCountRepository);
        serviceUnderTest = spy(serviceUnderTest);
        lenient().doReturn(Arrays.asList(1, 2, 3, 4, 5, 5, 6, 7, 8, 9))
            .when(serviceUnderTest).generateRandomList(anyInt(), anyInt(), anyInt());
        when(pairCountRepository.save(any(PairCountNumberEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testCalculate_ValidInputWithPairs() {
        // Arrange
        NumberDto numberDto = new NumberDto();
        numberDto.setTargetNumber(10); // Target number to find pairs for

        // Act
        PairCountNumberEntity result = serviceUnderTest.calculate(numberDto);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getPairNumberList().size()); // Expected pairs: (1,9), (5,5)
        assertTrue(result.getPairNumberList().contains(new PairCountNumberService.Pair<>(1, 9)));
        assertTrue(result.getPairNumberList().contains(new PairCountNumberService.Pair<>(5, 5)));
        assertTrue(result.getPairNumberList().contains(new PairCountNumberService.Pair<>(3, 7)));
        assertTrue(result.getPairNumberList().contains(new PairCountNumberService.Pair<>(4, 6)));
        assertTrue(result.getPairNumberList().contains(new PairCountNumberService.Pair<>(2, 8)));
        verify(pairCountRepository).save(any(PairCountNumberEntity.class));

    }

    @Test
    void testCalculate_NoPairsFound() {
        // Arrange
        NumberDto numberDto = new NumberDto();
        numberDto.setTargetNumber(20); // Target number with no possible pairs

        // Act
        PairCountNumberEntity result = serviceUnderTest.calculate(numberDto);

        // Assert
        assertNotNull(result);
        assertTrue(result.getPairNumberList().isEmpty()); // No pairs should be found
    }

    @Test
    void testCalculate_EdgeCaseEmptyList() {
        // Arrange
        lenient().when(service.generateRandomList(anyInt(), anyInt(), anyInt()))
            .thenReturn(Collections.emptyList()); // Return an empty list
        NumberDto numberDto = new NumberDto();
        numberDto.setTargetNumber(10);

        // Act
        PairCountNumberEntity result = serviceUnderTest.calculate(numberDto);

        // Assert
        assertNotNull(result);
        assertFalse(result.getPairNumberList().isEmpty()); // No pairs should be found
    }

    @Test
    void testCalculate_DuplicateNumbers() {
        // Arrange
        lenient().doReturn(Arrays.asList(2, 2, 3, 3, 4, 4))
            .when(serviceUnderTest).generateRandomList(anyInt(), anyInt(), anyInt());
        // List with duplicates
        NumberDto numberDto = new NumberDto();
        numberDto.setTargetNumber(5); // Target number to find pairs for

        // Act
        PairCountNumberEntity result = serviceUnderTest.calculate(numberDto);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getPairNumberList().size()); // Expected pairs: (2,3), (2,3)
        assertTrue(result.getPairNumberList().contains(new PairCountNumberService.Pair<>(2, 3)));
    }

    @Test
    void testCalculate_NegativeNumbers() {
        // Arrange
        lenient().doReturn(Arrays.asList(-1, -2, -3, -4, 5, 6))
            .when(serviceUnderTest).generateRandomList(anyInt(), anyInt(), anyInt()); // List with negative numbers
        NumberDto numberDto = new NumberDto();
        numberDto.setTargetNumber(1); // Target number to find pairs for

        // Act
        PairCountNumberEntity result = serviceUnderTest.calculate(numberDto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPairNumberList().size());// Expected pair: (-4, 5)
        assertTrue(result.getPairNumberList().contains(new PairCountNumberService.Pair<>(-4, 5)));
    }
}
