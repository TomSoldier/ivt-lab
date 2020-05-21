package hu.bme.mit.spaceship;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPrimary;
  private TorpedoStore mockSecondary;

  @BeforeEach
  public void init() {

    this.mockPrimary = mock(TorpedoStore.class);
    this.mockSecondary = mock(TorpedoStore.class);
    this.ship = new GT4500(mockPrimary, mockSecondary);
  }

  @Test
  public void fireTorpedo_Single_Success() {
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(false);
    when(mockSecondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimary, times(1)).isEmpty();
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockSecondary, times(1)).fire(1);
    Assertions.assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Success() {
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimary, times(1)).isEmpty();
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockSecondary, times(1)).fire(1);
    Assertions.assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_PrimaryFails() {
    when(mockPrimary.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    verify(mockPrimary, times(1)).isEmpty();
    Assertions.assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_All_SecondaryFails() {
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    verify(mockPrimary, times(1)).isEmpty();
    verify(mockSecondary, times(1)).isEmpty();
    Assertions.assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_Single_CheckEmptyButNotCallFire() {
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    verify(mockPrimary, times(1)).isEmpty();
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
    Assertions.assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_Fire_ThrowsException() throws IllegalArgumentException {
    when(mockPrimary.getTorpedoCount()).thenReturn(0);
    when(mockPrimary.fire(1)).thenThrow(IllegalArgumentException.class);
    Assertions.assertThrows(IllegalArgumentException.class, () -> ship.fireTorpedo(FiringMode.SINGLE));
    verify(mockPrimary, times(1)).isEmpty();
    verify(mockPrimary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Fire_PrimaryTwice() {
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(true);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(false);

    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    verify(mockPrimary, times(2)).isEmpty();
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockPrimary, times(2)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
    Assertions.assertEquals(true, result1 && result2);
  }

  @Test
  public void fireTorpedo_SingleAll() {
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);
    boolean result1 = ship.fireTorpedo(FiringMode.ALL);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    verify(mockPrimary, times(2)).isEmpty();
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockSecondary, times(1)).fire(1);
    verify(mockPrimary, times(2)).fire(1);
    Assertions.assertEquals(true, result1);
    Assertions.assertEquals(true, result2);
  }

  @Test
  public void fireTorpedo_WasPrimaryFired() {
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    verify(mockPrimary, times(1)).isEmpty();
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockSecondary, times(1)).fire(1);
    Assertions.assertEquals(true, result && result2);
  }

  @Test
  public void fireTorpedo_WasPrimaryFiredSecondaryEmpty() {
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(true);
    when(mockPrimary.fire(1)).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    verify(mockPrimary, times(2)).isEmpty();
    verify(mockPrimary, times(2)).fire(1);
    verify(mockSecondary, times(1)).isEmpty();
    verify(mockSecondary, times(0)).fire(1);
    Assertions.assertEquals(true, result && result2);
  }
}
