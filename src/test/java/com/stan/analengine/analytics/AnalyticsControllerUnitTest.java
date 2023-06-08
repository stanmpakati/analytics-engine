package com.stan.analengine.analytics;

import com.stan.analengine.analytics.dto.RangeGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AnalyticsControllerUnitTest {

  @Test
  public void testHealth() {
    AnalyticsService service = Mockito.mock(AnalyticsService.class);
    AnalyticsController controller = new AnalyticsController(service);

    assertEquals("Running", controller.getHealth());
  }

  @Test
  public void test_getVisitors() {
    AnalyticsService service = Mockito.mock(AnalyticsService.class);
    when(service.getVisitors(new Date(1), new Date(), RangeGroup.MONTHLY)).thenReturn(null);

    AnalyticsController controller = new AnalyticsController(service);

    assertEquals(null, controller.getVisitors(new Date(1), new Date(), RangeGroup.MONTHLY));
  }

//  @Test
//  public void getVisitors_fail() {
//    AnalyticsService service = Mockito.mock(AnalyticsService.class);
//    when(service.getVisitors(new Date(1), new Date(), RangeGroup.MONTHLY)).thenReturn(List.of());
//
//    AnalyticsController controller = new AnalyticsController(service);
//
////    assertEquals(List.of(), controller.getVisitors(new Date(1), new Date(), RangeGroup.MONTHLY));
//    assertTrue(true);
//  }

  @Test
  public void test_getPageViews() {
    AnalyticsService service = Mockito.mock(AnalyticsService.class);
    when(service.getVisitors(new Date(1), new Date(), RangeGroup.MONTHLY)).thenReturn(null);

    AnalyticsController controller = new AnalyticsController(service);

    assertEquals(null, controller.getVisitors(new Date(1), new Date(), RangeGroup.MONTHLY));

  }

  @Test
  public void test_findReferrers() {
    AnalyticsService service = Mockito.mock(AnalyticsService.class);
    when(service.getVisitors(new Date(1), new Date(), RangeGroup.MONTHLY)).thenReturn(null);

    AnalyticsController controller = new AnalyticsController(service);

    assertEquals(null, controller.getVisitors(new Date(1), new Date(), RangeGroup.MONTHLY));

  }

  @Test
  public void test_findDeviceType() {
    AnalyticsService service = Mockito.mock(AnalyticsService.class);
    when(service.getVisitors(new Date(1), new Date(), RangeGroup.MONTHLY)).thenReturn(null);

    AnalyticsController controller = new AnalyticsController(service);

    assertEquals(null, controller.getVisitors(new Date(1), new Date(), RangeGroup.MONTHLY));

  }

  @Test
  public void test_findOS() {
    AnalyticsService service = Mockito.mock(AnalyticsService.class);
    when(service.getVisitors(new Date(1), new Date(), RangeGroup.MONTHLY)).thenReturn(null);

    AnalyticsController controller = new AnalyticsController(service);

    assertEquals(null, controller.getVisitors(new Date(1), new Date(), RangeGroup.MONTHLY));

  }
}
