package org.sid.customerservice.web;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sid.customerservice.dto.CustomerDto;
import org.sid.customerservice.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;
import java.util.stream.Collectors;

//@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class CustomerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;

//    @Container
//    @ServiceConnection
//    private static PostgreSQLContainer postgreSQLContainer=new PostgreSQLContainer("postgres:16");

    List<CustomerDto> customers;

    @BeforeEach
    void setUp() {
        this.customers = new ArrayList<>();
        this.customers.add(CustomerDto.builder()
                .id(1L).firstName("Med").lastName("kamil").email("med@gmail.com").build());
        this.customers.add(CustomerDto.builder()
                .id(2L).firstName("Moad").lastName("kamil").email("moad@gmail.com").build());
        this.customers.add(CustomerDto.builder()
                .id(3L) .firstName("Rim").lastName("kamil").email("rim@gmail.com").build());
    }

    @Test
    void shouldGetAllCustomers(){
        ResponseEntity<CustomerDto[]> response = testRestTemplate.exchange("/api/customers", HttpMethod.GET, null, CustomerDto[].class);
        List<CustomerDto> content = Arrays.asList(response.getBody());
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AssertionsForClassTypes.assertThat(content.size()).isEqualTo(3);
        AssertionsForClassTypes.assertThat(content).usingRecursiveComparison().isEqualTo(customers);
    }
    @Test
    void shouldSearchCustomersByFirstName(){
        String keyword="m";
        ResponseEntity<CustomerDto[]> response = testRestTemplate.exchange("/api/customers/search?keyword="+keyword, HttpMethod.GET, null, CustomerDto[].class);
        List<CustomerDto> content = Arrays.asList(response.getBody());
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AssertionsForClassTypes.assertThat(content.size()).isEqualTo(3);
        List<CustomerDto> expected = customers.stream().filter(c -> c.getFirstName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
        AssertionsForClassTypes.assertThat(content).usingRecursiveComparison().isEqualTo(expected);
    }
    @Test
    void shouldGetCustomerById(){
        Long customerId = 1L;
        ResponseEntity<CustomerDto> response = testRestTemplate.exchange("/api/customers/"+customerId, HttpMethod.GET, null, CustomerDto.class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AssertionsForClassTypes.assertThat(response.getBody()).isNotNull();
        AssertionsForClassTypes.assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(customers.get(0));
    }
    @Test
    void shouldNotFindCustomerById(){
        Long customerId = 9L;
        ResponseEntity<CustomerDto> response = testRestTemplate.exchange("/api/customers/"+customerId, HttpMethod.GET, null, CustomerDto.class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @Rollback
    void shouldSaveValidCustomer(){
        CustomerDto customerDTO = CustomerDto.builder().firstName("Amine").lastName("med").email("amine@gmail.com").build();
        ResponseEntity<CustomerDto> response = testRestTemplate.exchange("/api/customers", HttpMethod.POST, new HttpEntity<>(customerDTO), CustomerDto.class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        AssertionsForClassTypes.assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(customerDTO);
    }

    @Test
    @Rollback
    void shouldNotSaveInValidCustomer() throws JsonProcessingException {
        CustomerDto customerDTO = CustomerDto.builder().firstName("").lastName("").email("").build();
        ResponseEntity<String> response = testRestTemplate.exchange("/api/customers", HttpMethod.POST, new HttpEntity<>(customerDTO), String.class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, ArrayList<String>> errors = objectMapper.readValue(response.getBody(), HashMap.class);

        System.out.println("Errors Map: " + errors);
        Assertions.assertThat(errors.keySet().size()).isEqualTo(3);
        Assertions.assertThat(errors.get("firstName").size()).isEqualTo(2);
        Assertions.assertThat(errors.get("lastName").size()).isEqualTo(2);
        Assertions.assertThat(errors.get("email").size()).isEqualTo(2);
    }

    @Test
    @Rollback
    void shouldUpdateValidCustomer(){
        Long customerId = 2L;
        CustomerDto customerDTO = CustomerDto.builder()
                .id(3L).firstName("Rim").lastName("kamil").email("rim@gmail.com").build();
        ResponseEntity<CustomerDto> response = testRestTemplate.exchange("/api/customers/"+customerId, HttpMethod.PUT, new HttpEntity<>(customerDTO), CustomerDto.class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AssertionsForClassTypes.assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(customerDTO);
    }
    @Test
    @Rollback
    void shouldDeleteCustomer(){
        Long customerId = 3L;
        ResponseEntity<String> response = testRestTemplate.exchange("/api/customers/"+customerId, HttpMethod.DELETE, null, String.class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}