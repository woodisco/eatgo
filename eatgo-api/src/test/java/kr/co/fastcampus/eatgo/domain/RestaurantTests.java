package kr.co.fastcampus.eatgo.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class RestaurantTests {

    @Test
    public void creation() {
        Restaurant restaurant = Restaurant.builder()
                        .id(1004L)
                        .name("Bob Jip")
                        .address("Seoul")
                        .build();

        assertThat(restaurant.getName(), is("Bob Jip"));
        assertThat(restaurant.getAddress(), is("Seoul"));
        assertThat(restaurant.getId(), is(1004L));
    }
}