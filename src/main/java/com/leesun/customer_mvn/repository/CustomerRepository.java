package com.leesun.customer_mvn.repository;

import org.springframework.data.repository.CrudRepository;
import com.leesun.customer_mvn.entity.Customer;
public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
