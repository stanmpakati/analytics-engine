package com.stan.analengine.analytics.dao;

import com.stan.analengine.analytics.dto.DeviceQueryDto;
import com.stan.analengine.analytics.dto.PageViewsDto;
import com.stan.analengine.analytics.dto.RangeGroup;
import com.stan.analengine.model.BrowserEvent;
import com.stan.analengine.model.Device;
import com.stan.analengine.model.PageEvent;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest(
//    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
//    classes = Application.class)
//@AutoConfigureMockMvc
//@TestPropertySource(
//    locations = "classpath:application-integrationtest.properties")
public class DeviceSearchDaoTest {

  @Autowired
  private DeviceSearchDao deviceSearchDao;

  @Autowired
  private EntityManager em;

  @Autowired
  private TestEntityManager tem;

  private Device device1;
  private Device device2;
  private Device device3;

  @BeforeEach
  void setUp() {
    // create some test devices
    device1 = new Device("Chrome", new Date());
    device2 = new Device("Firefox", new Date());
    device3 = new Device("Safari", new Date());

    // persist them using TestEntityManager
    tem.persist(device1);
    tem.persist(device2);
    tem.persist(device3);

    // create some browser events for each device
    BrowserEvent event1 = new BrowserEvent(device1, new Date());
    BrowserEvent event2 = new BrowserEvent(device2, new Date());
    BrowserEvent event3 = new BrowserEvent(device3, new Date());

    // persist them using TestEntityManager
    tem.persist(event1);
    tem.persist(event2);
    tem.persist(event3);

    // create some page events for each browser event
    PageEvent pageEvent1 = new PageEvent(event1, "Home");
    PageEvent pageEvent2 = new PageEvent(event2, "About");
    PageEvent pageEvent3 = new PageEvent(event3, "Contact");

    // persist them using TestEntityManager
    tem.persist(pageEvent1);
    tem.persist(pageEvent2);
    tem.persist(pageEvent3);

    // flush and clear the entity manager
    em.flush();
    em.clear();
  }

  @AfterEach
  void tearDown() {
    // delete all test data using TestEntityManager
    tem.detach(PageEvent.class);
    tem.detach(BrowserEvent.class);
    tem.detach(Device.class);
  }

  @Test
  @DisplayName("should find devices by criteria query")
  void shouldFindDevicesByCriteriaQuery() {
    // given a device query dto with browser name and date range
    DeviceQueryDto deviceQueryDto = new DeviceQueryDto();
    deviceQueryDto.setBrowserName("Chrome");
    deviceQueryDto.setFrom(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)); // yesterday
    deviceQueryDto.setTo(new Date()); // today

    // when calling findDevicesByCriteriaQuery with the dto
    Set<Device> devices = deviceSearchDao.findDevicesByCriteriaQuery(deviceQueryDto);

    // then expect only one device with Chrome browser name in the result set
    assertEquals(1, devices.size());
    assertTrue(devices.contains(device1));
  }

//  @Test
//  @DisplayName("should find visitors by date range and range group")
//  void shouldFindVisitorsByDateRangeAndRangeGroup() {
//    // given a date range from yesterday to today and a range group by month
//    Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); // yesterday
//    Date endDate = new Date(); // today
//    RangeGroup rangeGroup = RangeGroup.MONTHLY;
//
//    // when calling findVisitors with the date range and range group
//    Object visitors = deviceSearchDao.findVisitors(startDate, endDate, rangeGroup);
//
//    // then expect a list of objects with the month number, browser event count and device count
//    assertTrue(visitors instanceof List<?>);
//    List<?> results = (List<?>) visitors;
//    assertEquals(1, results.size()); // only one month in the range
//    assertTrue(results.get(0) instanceof Object[]);
//    Object[] row = (Object[]) results.get(0);
//    assertEquals(3, row.length); // month, browser event count, device count
//    assertEquals(Calendar.getInstance().get(Calendar.MONTH) + 1, row[0]); // current month
//    assertEquals(3L, row[1]); // three browser events in total
//    assertEquals(3L, row[2]); // three devices in total
//  }
//
//  @Test
//  @DisplayName("should get recently active devices by date range")
//  void shouldGetRecentlyActiveDevicesByDateRange() {
//    // given a date range from yesterday to today
//    Date from = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); // yesterday
//    Date to = new Date(); // today
//
//    // when calling getRecentlyActive with the date range
//    Set<Device> devices = deviceSearchDao.getRecentlyActive(from, to);
//
//    // then expect all three devices in the result set
//    assertEquals(3, devices.size());
//    assertTrue(devices.contains(device1));
//    assertTrue(devices.contains(device2));
//    assertTrue(devices.contains(device3));
//  }
//
//  @Test
//  @DisplayName("should find page visit counts by date range")
//  void shouldFindPageVisitCountsByDateRange() {
//    // given a date range from yesterday to today
//    Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); // yesterday
//    Date endDate = new Date(); // today
//
//    // when calling findPageVisitCounts with the date range
//    List<PageViewsDto> pageViews = deviceSearchDao.findPageVisitCounts(startDate, endDate);
//
//    // then expect a list of page views dto with the page name, page event count and device count
//    assertEquals(3, pageViews.size());
//    PageViewsDto homePageViews = pageViews.stream().filter(p -> p.getPageName().equals("Home")).findFirst().orElse(null);
//    assertNotNull(homePageViews);
//    assertEquals(1L, homePageViews.getPageName());
//    assertEquals(1L, homePageViews.getVisits());
//    PageViewsDto aboutPageViews = pageViews.stream().filter(p -> p.getPageName().equals("About")).findFirst().orElse(null);
//    assertNotNull(aboutPageViews);
//    assertEquals(1L, aboutPageViews.getPageEventCount());
//    assertEquals(1L, aboutPageViews.getDeviceCount());
//    PageViewsDto contactPageViews = pageViews.stream().filter(p -> p.getPageName().equals("Contact")).findFirst().orElse(null);
//    assertNotNull(contactPageViews);
//    assertEquals(1L, contactPageViews.getPageEventCount());
//    assertEquals(1L, contactPageViews.getDeviceCount());
//  }
}