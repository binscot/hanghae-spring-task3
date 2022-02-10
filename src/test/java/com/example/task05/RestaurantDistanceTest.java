package com.example.task05;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestaurantDistanceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }


    private final List<RestaurantDto> registeredRestaurants = new ArrayList<>();

    @Nested
    @DisplayName("음식점 3개 등록 및 조회")
    class RegisterRestaurants {
        @Test
        @Order(1)
        @DisplayName("음식점1 등록")
        void test1() throws JsonProcessingException {
            // given
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("쉐이크쉑 청담점")
                    .minOrderPrice(1_000)
                    .deliveryFee(0)
                    .x(4)
                    .y(3)
                    .build();

            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);

            System.out.println("response = " + response.getBody());

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());

            RestaurantDto restaurantResponse = response.getBody();
            assertNotNull(restaurantResponse);
            assertTrue(restaurantResponse.id > 0);
            assertEquals(restaurantRequest.name, restaurantResponse.name);
            assertEquals(restaurantRequest.minOrderPrice, restaurantResponse.minOrderPrice);
            assertEquals(restaurantRequest.deliveryFee, restaurantResponse.deliveryFee);
            assertEquals(4, restaurantResponse.x);
            assertEquals(3, restaurantResponse.y);

            // 음식점 등록 성공 시, registeredRestaurants 에 추가
            registeredRestaurants.add(restaurantResponse);
        }

        @Test
        @org.junit.jupiter.api.Order(2)
        @DisplayName("음식점2 등록")
        void test2() throws JsonProcessingException {
            // given
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("자담치킨 강남점")
                    .minOrderPrice(100_000)
                    .deliveryFee(10_000)
                    .x(2)
                    .y(0)
                    .build();

            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());

            RestaurantDto restaurantResponse = response.getBody();
            assertNotNull(restaurantResponse);
            assertTrue(restaurantResponse.id > 0);
            assertEquals(restaurantRequest.name, restaurantResponse.name);
            assertEquals(restaurantRequest.minOrderPrice, restaurantResponse.minOrderPrice);
            assertEquals(restaurantRequest.deliveryFee, restaurantResponse.deliveryFee);
            assertEquals(2, restaurantResponse.x);
            assertEquals(0, restaurantResponse.y);

            // 음식점 등록 성공 시, registeredRestaurants 에 추가
            registeredRestaurants.add(restaurantResponse);
        }

        @Test
        @Order(3)
        @DisplayName("음식점3 등록")
        void test3() throws JsonProcessingException {
            // given
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("마라하오 위례점")
                    .minOrderPrice(100000)
                    .deliveryFee(10000)
                    .x(4)
                    .y(0)
                    .build();

            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());

            RestaurantDto restaurantResponse = response.getBody();
            assertNotNull(restaurantResponse);
            assertTrue(restaurantResponse.id > 0);
            assertEquals(restaurantRequest.name, restaurantResponse.name);
            assertEquals(restaurantRequest.minOrderPrice, restaurantResponse.minOrderPrice);
            assertEquals(restaurantRequest.deliveryFee, restaurantResponse.deliveryFee);
            assertEquals(4, restaurantResponse.x);
            assertEquals(0, restaurantResponse.y);

            // 음식점 등록 성공 시, registeredRestaurants 에 추가
            registeredRestaurants.add(restaurantResponse);
        }

        @Test
        @Order(4)
        @DisplayName("좌표에 등록된 모든 음식점 조회")
        void test4() {
            // given
            int x = 4;
            int y = 3;

            // when
            ResponseEntity<RestaurantDto[]> response = restTemplate.getForEntity(
                    "/restaurants?x="+x+"&y="+y,
                    RestaurantDto[].class
            );

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            RestaurantDto[] responseRestaurants = response.getBody();
            assertNotNull(responseRestaurants);
            assertEquals(2, responseRestaurants.length);
            for (RestaurantDto responseRestaurant : responseRestaurants) {
                RestaurantDto registerRestaurant = registeredRestaurants.stream()
                        .filter(restaurant -> responseRestaurant.getId().equals(restaurant.getId()))
                        .findAny()
                        .orElse(null);

                assertNotNull(registerRestaurant);
                assertEquals(registerRestaurant.getName(), responseRestaurant.getName());
                assertEquals(registerRestaurant.getDeliveryFee(), responseRestaurant.getDeliveryFee());
                assertEquals(registerRestaurant.getMinOrderPrice(), responseRestaurant.getMinOrderPrice());
            }
        }
    }


    @Getter
    @Setter
    @Builder
    static class RestaurantDto {
        private Long id;
        private String name;
        private int minOrderPrice;
        private int deliveryFee;
        private int x;
        private int y;
    }

}