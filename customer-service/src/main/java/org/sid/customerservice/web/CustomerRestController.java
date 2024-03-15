package org.sid.customerservice.web;

import jakarta.validation.Valid;
import org.sid.customerservice.dto.CustomerDto;
import org.sid.customerservice.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerRestController {
    private CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping("/customers")
    public List<CustomerDto> getAllCustomers(){
        return customerService.getAllCustomers();
    }
    @GetMapping("/customers/{id}")
    public CustomerDto getCustomerById(@PathVariable Long id){
        return customerService.findCustomerById(id);
    }
    @GetMapping("/customers/search")
    public List<CustomerDto> searchCustomers(@RequestParam String keyword){
        return customerService.searchCustomers(keyword);
    }
    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto saveCustomer(@RequestBody @Valid CustomerDto customerDTO){
        return customerService.saveNewCustomer(customerDTO);
    }
    @PutMapping("/customers/{id}")
    public CustomerDto updateCustomer(@PathVariable Long id,@RequestBody CustomerDto customerDTO){
        return customerService.updateCustomer(id,customerDTO);
    }
    @DeleteMapping("/customers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
    }
}