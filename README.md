**Introduction**<br>
- In this full stack Spring Boot tutorial, we learn how to implement a web application using Spring Boot as back end restful API and Vue.js at the front end side. Via step by step guide we show you how to implement the sample project named Customer Manager to manage customer information.
**Create New Spring Boot Restful API Project
**
- Open Spring Tool Suite IDE, select menu **File > New > Spring Starter Project**.

- On the **New Spring Starter Project** popup input new project information as below and click Next.

  - Name: customer-api
  - Group: dev.simplesolution
  - Artifact: customer-api
  - Version: 1.0.0
  - Description: Customer Management API
  - Package: dev.simplesolution.customer
  <br>
![image](https://github.com/trungsin/demo_crm_java_spring_boot_api_vue_frontend/assets/6958230/9d5a8efd-a3be-47dc-9662-bc1d2a1eaf98)
- On the **New Spring Starter Project Dependencies** popup choose dependencies as below and click **Next**.
  - Spring Data JPA
  - MySQL Driver
  - Spring Web <br><br>
![image](https://github.com/trungsin/demo_crm_java_spring_boot_api_vue_frontend/assets/6958230/5f89e959-a1a1-4206-a599-a3a68de35e63)
- Keep the information on the next popup as default and click Finish.<br><br>
![image](https://github.com/trungsin/demo_crm_java_spring_boot_api_vue_frontend/assets/6958230/d154f1e9-6ccc-4950-82bc-fbe526b262a0)<br><br>
You can also creating new Spring Boot project using Spring initializr online tool at start.spring.io <br>
**Create new MySQL database to manage customer information**<br>
On your local MySQL database server, create a new database schema named customerdb.<br>
Execute the following SQL script on the customerdb database to create a table named customer.<br>

```sh
CREATE TABLE `customer` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `first_name` VARCHAR(100) NULL,
  `last_name` VARCHAR(100) NULL,
  `email` VARCHAR(100) NULL,
  `phone` VARCHAR(20) NULL,
  PRIMARY KEY (`id`));
```

- <b>Configure MySQL connection string for the Spring Boot project<b>

Add the following configurations to **application.properties** file in order to connect to the customerdb database on your MySQL server.<br>
customer-api/src/main/resources/application.properties

```sh
spring.datasource.url=jdbc:mysql://localhost:3306/customerdb
spring.datasource.username=root
spring.datasource.password=123@abc
```

  - spring.datasource.url to configure the URL to your database. For example we have the database named customerdb at localhost server which is running on default MySQL port 3306.
  - spring.datasource.username to configure the user name to access the MySQL database.
  - spring.datasource.password to configure password of your MySQL user.
- **Implement Entity class and Repository interface
**<br>

Create a new Java package named **dev.simplesolution.customer.entity**, and add a new class named **Customer**.<br>

```sh
package dev.simplesolution.customer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "phone")
	private String phone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}	
}
```

- Create a new Java package named **dev.simplesolution.customer.repository**, and implement a new interface named **CustomerRepository**.<br>
customer-api/src/main/java/dev/simplesolution/customer/repository/CustomerRepository.java <br>

```sh
package dev.simplesolution.customer.repository;

import org.springframework.data.repository.CrudRepository;

import dev.simplesolution.customer.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
```

- <b>Implement Restful API Controller</b>
**

Create a new Java package named **dev.simplesolution.customer.controller**, and implement a new class named **CustomerController**.<br>
customer-api/src/main/java/dev/simplesolution/customer/controller/CustomerController.java
<br>

```sh
package dev.simplesolution.customer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.simplesolution.customer.entity.Customer;
import dev.simplesolution.customer.repository.CustomerRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class CustomerController {
	
	private Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@GetMapping("/customers")
	public ResponseEntity<Object> getAllCustomers(){
		try {
			Iterable<Customer> customers = customerRepository.findAll();
			return new ResponseEntity<Object>(customers, HttpStatus.OK);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/customers/{id}")
	public ResponseEntity<Object> getCustomerById(@PathVariable("id") Long id) {
		try {
			Customer customer = customerRepository.findById(id).get();
			if(customer != null) {
				return new ResponseEntity<Object>(customer, HttpStatus.OK);				
			} else {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/customers")
	public ResponseEntity<Object> createCustomer(@RequestBody Customer customer) {
		try {
			Customer savedCustomer = customerRepository.save(customer);
			return new ResponseEntity<Object>(savedCustomer, HttpStatus.OK);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/customers/{id}")
	public ResponseEntity<Object> updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customer) {
		try {
			customer.setId(id);
			Customer savedCustomer = customerRepository.save(customer);
			return new ResponseEntity<Object>(savedCustomer, HttpStatus.OK);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/customers/{id}")
	public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") Long id) {
		try {
			customerRepository.deleteById(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
```

- <b>Final Spring Boot Restful API Source Code Structure</b>
**<Br>
With the above steps, we have finished implementing the Spring Boot Restful API project to manage customer information with the source code structure as below screenshot.


![image](https://github.com/trungsin/demo_crm_java_spring_boot_api_vue_frontend/assets/6958230/760d3e62-ac4a-4062-813d-f9be78fcbaad)

<b>Run Spring Boot Restful API Application</b>

Execute the Spring Boot application we have the Restful API started at http://localhost:8080 which exposes the following APIs.<br>

|URL	          |HTTP Method|Description|
|---------------|-----------|------------|
| /api/customers | GET | Get all customers |
| /api/customers/{id}	| GET	| Get customer by ID |
| /api/customers	| POST	| Create a new customer |
| /api/customers/{id}	| PUT	| Update existing customer |
| /api/customers/{id}	| DELETE	| Delete customer by ID |
