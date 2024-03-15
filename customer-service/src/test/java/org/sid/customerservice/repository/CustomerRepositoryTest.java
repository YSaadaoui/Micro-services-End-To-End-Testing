package org.sid.customerservice.repository;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.sid.customerservice.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@DataJpaTest
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;
    @BeforeEach
    void setUp() {
        customerRepository.save(Customer.builder().firstName("Med").lastName("kamil").email("med@gmail.com").build());
        customerRepository.save(Customer.builder().firstName("Moad").lastName("kamil").email("moad@gmail.com").build());
        customerRepository.save(Customer.builder().firstName("Rime").lastName("kamil").email("rim@gmail.com").build());

    }
    @Test
    public void shouldFindCustomerByEmail(){
        String givenEmail="med@gmail.com";
        Optional<Customer> result=customerRepository.findByEmail(givenEmail);
        assertThat(result).isPresent();
    }
    @Test
    public void shouldNotFindCustomerByEmail(){
        String givenEmail="xxx@gmail.com";
        Optional<Customer> result=customerRepository.findByEmail(givenEmail);
        assertThat(result).isEmpty();
    }
    @Test
    public void shouldFindCustomersByFirstName(){
        String keyword="e";
        List<Customer> expected=List.of(
                Customer.builder().firstName("Med").lastName("kamil").email("med@gmail.com").build(),
                Customer.builder().firstName("Rime").lastName("kamil").email("rim@gmail.com").build()
        );
        List<Customer> result = customerRepository.findByFirstNameContainsIgnoreCase(keyword);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(expected.size());
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }
}