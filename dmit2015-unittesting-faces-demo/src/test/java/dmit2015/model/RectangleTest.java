package dmit2015.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class RectangleTest {

//    @Test
    @ParameterizedTest
    @CsvSource(value = {
            "10, 20, 200",
            "500, 500, 250000",
            "2500, 400, 1000000"
    })
    void area(double length, double width, double expectedArea) {
        // Arrange
        Rectangle currentRectangle = new Rectangle();
        // Act
        currentRectangle.setLength(length);
        currentRectangle.setWidth(width);
        // Assert
//        assertEquals(expectedArea, currentRectangle.area());
        assertThat(currentRectangle.area())
                .isEqualTo(expectedArea);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "10, 20, 60",
            "500, 500, 2000",
            "2500, 400, 5800"
    })
    void perimeter(double length, double width, double expectedPerimeter)
    {
        // Arrange
        Rectangle currentRectangle = new Rectangle();
        // Act
        currentRectangle.setLength(length);
        currentRectangle.setWidth(width);
        // Assert
//        assertEquals(expectedPerimeter, currentRectangle.perimeter());
        assertThat(currentRectangle.perimeter())
                .isEqualTo(expectedPerimeter);
    }
}