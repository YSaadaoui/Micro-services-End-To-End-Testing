package org.sid.customerservice.mapper;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.sid.customerservice.dto.CustomerDto;
import org.sid.customerservice.entities.Customer;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {
    CustomerMapper underTest=new CustomerMapper();
    @Test
    public void shouldMapCustomerToCustomerDTO(){
        Customer givenCustomer=Customer.builder().id(1L).firstName("kamal").lastName("ahmed").email("kamal@gmail.com").build();
        CustomerDto expected=CustomerDto.builder().id(1L).firstName("kamal").lastName("ahmed").email("kamal@gmail.com").build();
        CustomerDto result=underTest.fromCustomer(givenCustomer);
        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);

    }
    @Test
    public void shouldMapCustomerDTOToCustomer(){
        CustomerDto givenCustomerDto=CustomerDto.builder().id(1L).firstName("kamal").lastName("ahmed").email("kamal@gmail.com").build();
        Customer expected=Customer.builder().id(1L).firstName("kamal").lastName("ahmed").email("kamal@gmail.com").build();
        Customer result=underTest.fromCustomerDto(givenCustomerDto);
        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);

    }
    @Test
    public void shouldMapListOfCustomersToListOfCustomers(){
        List<Customer> givenCustomers=List.of(
                Customer.builder().firstName("Med").lastName("kamil").email("med@gmail.com").build(),
                Customer.builder().firstName("Rime").lastName("kamil").email("rim@gmail.com").build()
        );

        List<CustomerDto> expected=List.of(
                CustomerDto.builder().firstName("Med").lastName("kamil").email("med@gmail.com").build(),
                CustomerDto.builder().firstName("Rime").lastName("kamil").email("rim@gmail.com").build()
        );
        List<CustomerDto> result=underTest.fromListCustomers(givenCustomers);
        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);

    }

    @Test
    public void shouldNotMapListOfCustomersToListOfCustomers(){
        Customer givenCustomer=null;
        AssertionsForClassTypes.assertThatThrownBy(()->underTest.fromCustomer(givenCustomer)).isInstanceOf(IllegalArgumentException.class);
    }
}