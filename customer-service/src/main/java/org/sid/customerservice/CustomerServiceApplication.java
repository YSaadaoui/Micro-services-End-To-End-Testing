package org.sid.customerservice;

import org.sid.customerservice.entities.Customer;
import org.sid.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
    @Bean
    @Profile("!test")
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository){
        return args -> {
            customerRepository.save(Customer.builder().firstName("Med").lastName("kamil").email("med@gmail.com").build());
            customerRepository.save(Customer.builder().firstName("Moad").lastName("kamil").email("moad@gmail.com").build());
            customerRepository.save(Customer.builder().firstName("Rim").lastName("kamil").email("rim@gmail.com").build());


        };
    }
}
