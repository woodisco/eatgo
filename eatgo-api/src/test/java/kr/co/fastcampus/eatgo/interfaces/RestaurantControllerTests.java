package kr.co.fastcampus.eatgo.interfaces;

import kr.co.fastcampus.eatgo.application.RestaurantService;
import kr.co.fastcampus.eatgo.domain.MenuItem;
import kr.co.fastcampus.eatgo.domain.Restaurant;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestaurantService restaurantService;

    @Test
    public void list() throws Exception {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(Restaurant.builder()
                .id(1004L)
                .name("Bob Jip")
                .address("Seoul")
                .build());

        given(restaurantService.getRestaurants()).willReturn(restaurants);

        mvc.perform(MockMvcRequestBuilders.get("/restaurants"))
                .andExpect(status().isOk());
    }

    @Test
    public void detail() throws Exception {
        Restaurant restaurant = Restaurant.builder()
                .id(1004L)
                .name("Bob Jip")
                .address("Seoul")
                .build();
        MenuItem menuItem = MenuItem.builder()
                .name("Kimchi")
                .build();
        restaurant.setMenuItems(Arrays.asList(menuItem));

        given(restaurantService.getRestaurant(1004L)).willReturn(restaurant);

        mvc.perform(MockMvcRequestBuilders.get("/restaurants/1004"))
                .andExpect(status().isOk())
                .andExpect(content().string(StringContains.containsString("Kimchi")));
    }

    @Test
    public void create() throws Exception {
        given(restaurantService.addRestaurant(any())).will(invocation -> {
            Restaurant restaurant = invocation.getArgument(0);
            return Restaurant.builder()
                    .id(1234L)
                    .name(restaurant.getName())
                    .address(restaurant.getAddress())
                    .build();
        });

        mvc.perform(MockMvcRequestBuilders.post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"BeRyong\", \"address\":\"Seoul\"}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("location", "/restaurants/1234"))
                .andExpect(content().string("{}"));

        verify(restaurantService).addRestaurant(any());
    }

    @Test
    public void update() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch("/restaurants/1004")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Good\", \"address\":\"Seoul\"}"))
                .andExpect(status().isOk());

        verify(restaurantService).updateRestaurant(1004L, "Good", "Seoul");
    }
}