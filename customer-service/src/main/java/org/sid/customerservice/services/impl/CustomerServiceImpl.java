package org.sid.customerservice.services.impl;

import lombok.AllArgsConstructor;
import org.sid.customerservice.dto.CustomerDto;
import org.sid.customerservice.entities.Customer;
import org.sid.customerservice.exception.CustomerNotFoundException;
import org.sid.customerservice.exception.EmailAlreadyExistException;
import org.sid.customerservice.mapper.CustomerMapper;
import org.sid.customerservice.repository.CustomerRepository;
import org.sid.customerservice.services.CustomerService;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerMapper customerMapper;
    private CustomerRepository customerRepository;

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customerDTO) throws EmailAlreadyExistException {
        log.info(String.format("Saving new Customer => %s ", customerDTO.toString()));
        Optional<Customer> byEmail = customerRepository.findByEmail(customerDTO.getEmail());
        if(byEmail.isPresent()) {
            log.error(String.format("This email %s already exist", customerDTO.getEmail()));
            throw new EmailAlreadyExistException();
        }
        Customer customerToSave = customerMapper.fromCustomerDto(customerDTO);
        Customer savedCustomer = customerRepository.save(customerToSave);
        CustomerDto result = customerMapper.fromCustomer(savedCustomer);
        return result;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> allCustomers = customerRepository.findAll();
        return customerMapper.fromListCustomers(allCustomers);
    }

    @Override
    public CustomerDto findCustomerById(Long id) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) throw new CustomerNotFoundException();
        return customerMapper.fromCustomer(customer.get());
    }

    @Override
    public List<CustomerDto> searchCustomers(String keyword) {
        List<Customer> customers = customerRepository.findByFirstNameContainsIgnoreCase(keyword);
        return customerMapper.fromListCustomers(customers);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDTO) throws CustomerNotFoundException {
        Optional<Customer> customer=customerRepository.findById(id);
        if(customer.isEmpty()) throw new CustomerNotFoundException();
        customerDTO.setId(id);
        Customer customerToUpdate = customerMapper.fromCustomerDto(customerDTO);
        Customer updatedCustomer = customerRepository.save(customerToUpdate);
        return customerMapper.fromCustomer(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) throws CustomerNotFoundException {
        Optional<Customer> customer=customerRepository.findById(id);
        if(customer.isEmpty()) throw new CustomerNotFoundException();
        customerRepository.deleteById(id);
    }
}
