package com.application.customer.customer.repository;

import com.application.customer.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer,String> {

    @Query("select c from Customer c where" +
            " lower(c.first_name) like lower(concat('%', :searchTerm, '%'))" +
            " or lower(c.last_name) like lower(concat('%', :searchTerm, '%'))" +
            " or lower(c.city) like lower(concat('%', :searchTerm, '%'))" +
            " or lower(c.state) like lower(concat('%', :searchTerm, '%'))")
    Page<Customer> search(@Param("searchTerm") String searchTerm, Pageable pageable);

}
