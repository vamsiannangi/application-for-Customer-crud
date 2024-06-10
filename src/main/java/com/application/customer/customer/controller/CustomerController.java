package com.application.customer.customer.controller;

import com.application.customer.customer.entity.Customer;
import com.application.customer.customer.entity.User;
import com.application.customer.customer.service.CustomerService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
public class CustomerController {
    private CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public String getCustomers(HttpSession session,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "100", required = false) int size,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "sort", required = false) String sort,
            Model model) {

        boolean isLoginSuccessful = false;
        if(session!=null && session.getAttribute("isloginSuccessfull") != null)
        {
            isLoginSuccessful = (boolean) session.getAttribute("isloginSuccessfull");
        }
            if (isLoginSuccessful) {

                Pageable pageable = createPageable(page, size, sort);

                // Get customers based on pagination, sorting, and search criteria
                Page<Customer> customerPage;
                if (search != null && !search.isEmpty()) {
                    customerPage = customerService.searchCustomers(search, pageable);
                } else {
                    customerPage = customerService.getAllCustomers(pageable);
                }

                // Add model attributes
                addModelAttributes(model, customerPage, search, sort);
                return "customer-list";
            }

        else {
            return "redirect:/login";
        }
    }

    private void addModelAttributes(Model model, Page<Customer> customerPage, String search, String sort) {
        model.addAttribute("customerPage", customerPage);
        model.addAttribute("sortParam", sort);
        model.addAttribute("searchParam", search);
    }

    private Pageable createPageable(int page, int size, String sortParam) {
        Sort sort = (sortParam != null) ? Sort.by("uuid").ascending(): Sort.by("uuid").descending();
        return PageRequest.of(page, size, sort);
    }

    @GetMapping("/addCustomer")
    public String showCustomerForm(Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user!=null) {
            Customer customer = new Customer();
            model.addAttribute("customer", customer);
            return "add-customer";
        }else{
            return "login";
        }
    }

    @PostMapping("/add-customer")
    public String addCustomer(@ModelAttribute("customer") Customer customer,HttpSession session){
            User user = (User) session.getAttribute("user");
            if(user!=null) {
                customerService.addCustomer(customer);
                return "redirect:/customers";
            }else {
                return "login";
            }
    }

    @GetMapping("/customer/{id}")
    public String customerById(@PathVariable String id,Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user!=null) {
            Customer customer = customerService.findById(id);
            model.addAttribute("customer", customer);
            return "customer";
        }
        else{
            return "login";
        }
    }

    @GetMapping("/update-customer/{id}")
    public String showUpdateCustomerForm(@PathVariable String id,Model model,HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user!=null) {
            Customer customer = customerService.findById(id);

            model.addAttribute("customer", customer);
            return "update-form";
        }
        else{
            return "login";
        }
    }

    @PostMapping("/update-customer")
    public String updateCustomer(@ModelAttribute("customer") Customer customer){
        String id=customer.getUuid();
        Customer existingCustomer=customerService.findById(id);
        existingCustomer.setFirst_name(customer.getFirst_name());
        existingCustomer.setLast_name(customer.getLast_name());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setCity(customer.getCity());
        existingCustomer.setState(customer.getState());
        existingCustomer.setStreet(customer.getStreet());
        existingCustomer.setAddress(customer.getAddress());
        customerService.addCustomer(existingCustomer);
        return "redirect:/customers";
    }

    @PostMapping("/delete-customer/{id}")
    public String DeleteCustomerById(@PathVariable String id,HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user!=null) {
            customerService.deleteCustomer(id);
            return "redirect:/customers";
        }
        else {
            return "login";
        }
    }
}
