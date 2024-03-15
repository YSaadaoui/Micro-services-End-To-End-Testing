package org.sid.customerservice.services.impl;

import org.sid.customerservice.dto.CustomerDto;
import org.sid.customerservice.entities.Customer;
import org.sid.customerservice.exception.CustomerNotFoundException;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sid.customerservice.exception.EmailAlreadyExistException;
import org.sid.customerservice.mapper.CustomerMapper;
import org.sid.customerservice.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerServiceImpl underTest;
    @Test
    void shouldSaveNewCustomer() {
        CustomerDto customerDTO= CustomerDto.builder()
                .firstName("Lina").lastName("ben").email("lina@gmail.com").build();
        Customer customer= Customer.builder()
                .firstName("Lina").lastName("ben").email("lina@gmail.com").build();
        Customer savedCustomer= Customer.builder()
                .id(1L).firstName("Lina").lastName("ben").email("lina@gmail.com").build();
        CustomerDto expected= CustomerDto.builder()
                .id(1L).firstName("Lina").lastName("ben").email("lina@gmail.com").build();
        Mockito.when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.empty());
        Mockito.when(customerMapper.fromCustomerDto(customerDTO)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(savedCustomer);
        Mockito.when(customerMapper.fromCustomer(savedCustomer)).thenReturn(expected);
        CustomerDto result = underTest.saveNewCustomer(customerDTO);
        AssertionsForClassTypes.assertThat(result).isNotNull();
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldNotSaveNewCustomerWhenEmailExist() {
        CustomerDto customerDTO= CustomerDto.builder()
                .firstName("Lina").lastName("Ben").email("xxxxx@gmail.com").build();
        Customer customer= Customer.builder()
                .id(5L).firstName("Lina").lastName("Ben").email("xxxxx@gmail.com").build();
        Mockito.when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.of(customer));
        AssertionsForClassTypes.assertThatThrownBy(()->underTest.saveNewCustomer(customerDTO))
                .isInstanceOf(EmailAlreadyExistException.class);
    }

    @Test
    void shouldGetAllCustomers() {
        List<Customer> customers = List.of(
                Customer.builder().firstName("Ahmed").lastName("Ben").email("Ahmed@gmail.com").build(),
                Customer.builder().firstName("Kamal").lastName("Yassine").email("Kamal@gmail.com").build()
        );
        List<CustomerDto> expected = List.of(
                CustomerDto.builder().firstName("Ahmed").lastName("Ben").email("Ahmed@gmail.com").build(),
                CustomerDto.builder().firstName("Kamal").lastName("Yassine").email("Kamal@gmail.com").build()
        );
        Mockito.when(customerRepository.findAll()).thenReturn(customers);
        Mockito.when(customerMapper.fromListCustomers(customers)).thenReturn(expected);
        List<CustomerDto> result = underTest.getAllCustomers();
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldFindCustomerById() {
        Long customerId = 1L;
        Customer customer=Customer.builder().id(1L).firstName("Amine").lastName("Ben").email("ben@gmail.com").build();
        CustomerDto expected=CustomerDto.builder().id(1L).firstName("Amine").lastName("Ben").email("ben@gmail.com").build();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customerMapper.fromCustomer(customer)).thenReturn(expected);
        CustomerDto result = underTest.findCustomerById(customerId);
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }
    @Test
    void shouldNotFindCustomerById() {
        Long customerId = 8L;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        AssertionsForClassTypes.assertThatThrownBy(()->underTest.findCustomerById(customerId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage(null);
    }

    @Test
    void shouldSearchCustomers() {
        String keyword="m";
        List<Customer> customers = List.of(
                Customer.builder().firstName("Amine").lastName("ben").email("ben@gmail.com").build(),
                Customer.builder().firstName("Ahmed").lastName("kamal").email("ahmed@gmail.com").build()
        );
        List<CustomerDto> expected = List.of(
                CustomerDto.builder().firstName("Amine").lastName("ben").email("ben@gmail.com").build(),
                CustomerDto.builder().firstName("Ahmed").lastName("kamal").email("ahmed@gmail.com").build()
        );
        Mockito.when(customerRepository.findByFirstNameContainsIgnoreCase(keyword)).thenReturn(customers);
        Mockito.when(customerMapper.fromListCustomers(customers)).thenReturn(expected);
        List<CustomerDto> result = underTest.searchCustomers(keyword);
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void updateCustomer() {
        Long customerId= 6L;
        CustomerDto customerDTO= CustomerDto.builder()
                .id(6L).firstName("Ziad").lastName("Ben").email("Ziad@gmail.com").build();
        Customer customer= Customer.builder()
                .id(6L).firstName("Ziad").lastName("Ben").email("Ziad@gmail.com").build();
        Customer updatedCustomer= Customer.builder()
                .id(6L).firstName("Ziad").lastName("Ben").email("Ziad@gmail.com").build();
        CustomerDto expected= CustomerDto.builder()
                .id(6L).firstName("Ziad").lastName("Ben").email("Ziad@gmail.com").build();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customerMapper.fromCustomerDto(customerDTO)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(updatedCustomer);
        Mockito.when(customerMapper.fromCustomer(updatedCustomer)).thenReturn(expected);
        CustomerDto result = underTest.updateCustomer(customerId,customerDTO);
        AssertionsForClassTypes.assertThat(result).isNotNull();
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldDeleteCustomer() {
        Long customerId =1L;
        Customer customer= Customer.builder()
                .id(6L).firstName("Ziad").lastName("Ben").email("Ziad@gmail.com").build();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        underTest.deleteCustomer(customerId);
        Mockito.verify(customerRepository).deleteById(customerId);
    }
    @Test
    void shouldNotDeleteCustomerIfNotExist() {
        Long customerId =9L;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        AssertionsForClassTypes.assertThatThrownBy(()->underTest.deleteCustomer(customerId))
                .isInstanceOf(CustomerNotFoundException.class);
    }
}