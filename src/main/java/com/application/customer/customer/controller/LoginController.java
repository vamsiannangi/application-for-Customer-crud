package com.application.customer.customer.controller;

import com.application.customer.customer.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(HttpSession session, @RequestParam String name, @RequestParam String password, Model model) {
        if ("test@sunbasedata.com".equals(name) && "Test@123".equals(password)) {
            session.setAttribute("user", new User(name, password));
            model.addAttribute("authenticatedUser", name);
            session.setAttribute("isloginSuccessfull",true);
            return "redirect:/customers";
        } else {
            session.setAttribute("isloginSuccessfull",false);
            model.addAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}
