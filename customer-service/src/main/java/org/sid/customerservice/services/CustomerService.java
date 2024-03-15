package org.sid.customerservice.services;

import org.sid.customerservice.dto.CustomerDto;
import org.sid.customerservice.exception.CustomerNotFoundException;
import org.sid.customerservice.exception.EmailAlreadyExistException;

import java.util.List;

public interface CustomerService {
    CustomerDto saveNewCustomer(CustomerDto customerDTO) throws EmailAlreadyExistException;
    List<CustomerDto> getAllCustomers();
    CustomerDto findCustomerById(Long id) throws CustomerNotFoundException;
    List<CustomerDto> searchCustomers(String keyword);
    CustomerDto updateCustomer(Long id, CustomerDto customerDTO)throws CustomerNotFoundException;
    void deleteCustomer(Long id)throws CustomerNotFoundException;

}
