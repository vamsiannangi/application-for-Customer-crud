package com.application.customer.customer.controller;

import com.application.customer.customer.entity.Customer;
import com.application.customer.customer.entity.Token;
import com.application.customer.customer.entity.User;
import com.application.customer.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerApiController {

    private CustomerService customerService;

    public CustomerApiController(CustomerService customerService) {
        this.customerService = customerService;
    }
    List<Customer> customers = new ArrayList<>();
    @GetMapping("/customersApi")
    public String showAllCustomers(HttpSession session, Model model) throws IOException {
        User user = (User) session.getAttribute("user");
        if(user!=null) {
            String loginId =user.getLogin_id() ;
            String password = user.getPassword();
                String token = getBearerToken(loginId, password);
                String queryUrl_get = "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
                URL url = new URL(queryUrl_get);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", token);
                int responseCode = connection.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output;
                String result = "";

                while ((output = reader.readLine()) != null) {
                    result = result + output;
                    model.addAttribute("result", result);
                }

                ObjectMapper objectMapper = new ObjectMapper();
                TypeFactory typeFactory = objectMapper.getTypeFactory();
                customers = objectMapper.readValue(result, typeFactory.constructCollectionType(List.class, Customer.class));

                for (Customer customer : customers) {
                    String customerId = customer.getUuid();
                    Customer customerFromDb = customerService.findById(customerId);
                    if (customerFromDb == null) {
                        customerService.addCustomer(customer);
                    } else {
                        customerFromDb.setFirst_name(customer.getFirst_name());
                        customerFromDb.setLast_name(customer.getLast_name());
                        customerFromDb.setPhone(customer.getPhone());
                        customerFromDb.setEmail(customer.getEmail());
                        customerFromDb.setCity(customer.getCity());
                        customerFromDb.setState(customer.getState());
                        customerFromDb.setStreet(customer.getStreet());
                        customerFromDb.setAddress(customer.getAddress());
                        customerService.addCustomer(customerFromDb);
                    }
                }

                return "redirect:/customers";
            }
            else{
                    return "redirect:/login";

            }
    }

    public String getBearerToken(String login_id,String password) throws IOException {
        User user = new User();
        user.setLogin_id(login_id);
        user.setPassword(password);
        ObjectMapper objectMapper= new ObjectMapper();
        String body=objectMapper.writeValueAsString(user);
        String tokenUrl="https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
        URL url=new URL(tokenUrl);
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept","application/json");
        try (DataOutputStream dos=new DataOutputStream(connection.getOutputStream())){
            dos.writeBytes(body);
        }
        BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String output;
        String result = "";

        while((output=reader.readLine())!=null){
            result =result+ output;
        }

        ObjectMapper tokenObjectMapper = new ObjectMapper();
        Token token = tokenObjectMapper.readValue(result,  Token.class);
        return "Bearer "+ token.getAccess_token();
    }
}
