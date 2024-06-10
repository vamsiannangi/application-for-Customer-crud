package com.application.customer.customer.service;

import com.application.customer.customer.entity.Customer;
import com.application.customer.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer findById(String id) {
        Optional<Customer> result= customerRepository.findById(id);
        Customer customer = null;
        if(result.isPresent()){
            return customer = result.get();
        }
        else{
            return null;
        }
            }

    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }

 public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);

    }

    public Page<Customer> searchCustomers(String searchTerm, Pageable pageable) {
       return customerRepository.search(searchTerm,pageable);
    }

}
