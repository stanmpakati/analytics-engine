package com.stan.analengine.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stan.analengine.analytics.dto.PageViewsDto;
import com.stan.analengine.analytics.dto.RangeGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class AnalyticsControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AnalyticsService analyticsService;

  @Test
  public void check_health_url() throws Exception {

    mockMvc.perform(get("/api/v1/analytics/health"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("Running"));
  }

  @Test
  public void check_PageViews_url_without_date() throws Exception {
    Date startDate = new Date(1);
    Date endDate = new Date();
    RangeGroup range = RangeGroup.MONTHLY;

    List<PageViewsDto> mockResult = List.of(new PageViewsDto("/index", 5L, 1L));

    when(analyticsService.getPageViews(startDate, endDate, range))
        .thenReturn(mockResult);

    // Using Jackson to convert mockResult to JSON string
    ObjectMapper mapper = new ObjectMapper();
    String expectedJson = mapper.writeValueAsString(mockResult);

    mockMvc.perform(get("/api/v1/analytics/pageviews"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));

    verify(analyticsService).getPageViews(startDate, endDate, range);
  }
  @Test
  public void check_PageViews_url_with_date() throws Exception {
    Date startDate = new Date(1);
    Date endDate = new Date();
    RangeGroup range = RangeGroup.MONTHLY;

    List<PageViewsDto> mockResult = List.of(new PageViewsDto("/index", 5L, 1L));

    when(analyticsService.getPageViews(startDate, endDate, range))
        .thenReturn(mockResult);

    // Using Jackson to convert mockResult to JSON string
    ObjectMapper mapper = new ObjectMapper();
    String expectedJson = mapper.writeValueAsString(mockResult);

    mockMvc.perform(get("/api/v1/analytics/pageviews"))
        .andDo(print())
        .andExpect(status().isOk());
//        .andExpect(content().json(expectedJson));

//    verify(analyticsService).getPageViews(startDate, endDate, range);
  }

  @Test
  public void check_PageViews_url_without_range() throws Exception {
    Date startDate = new Date(1);
    Date endDate = new Date();
    RangeGroup range = RangeGroup.MONTHLY;

    List<PageViewsDto> mockResult = List.of(new PageViewsDto("/index", 5L, 1L));

    when(analyticsService.getPageViews(startDate, endDate, range))
        .thenReturn(mockResult);

    // Using Jackson to convert mockResult to JSON string
    ObjectMapper mapper = new ObjectMapper();
    String expectedJson = mapper.writeValueAsString(mockResult);

    mockMvc.perform(get("/api/v1/analytics/pageviews"))
        .andDo(print())
        .andExpect(status().isOk());
//        .andExpect(content().json(expectedJson));

//    verify(analyticsService).getPageViews(startDate, endDate, range);
  }

  @Test
  public void check_PageViews_url_without_date_and_range() throws Exception {
    Date startDate = new Date(1);
    Date endDate = new Date();
    RangeGroup range = RangeGroup.MONTHLY;

    List<PageViewsDto> mockResult = List.of(new PageViewsDto("/index", 5L, 1L));

    when(analyticsService.getPageViews(startDate, endDate, range))
        .thenReturn(mockResult);

    // Using Jackson to convert mockResult to JSON string
    ObjectMapper mapper = new ObjectMapper();
    String expectedJson = mapper.writeValueAsString(mockResult);

    mockMvc.perform(get("/api/v1/analytics/pageviews"))
        .andDo(print())
        .andExpect(status().isOk());
//        .andExpect(content().json(expectedJson));

//    verify(analyticsService).getPageViews(startDate, endDate, range);
  }
}
