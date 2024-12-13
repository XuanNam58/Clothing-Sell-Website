package com.example.clothing_sell_website.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test {
    @GetMapping({"/", "/index.html"})
    public String index() {
        return "user/index";
    }

    @GetMapping("/about.html")
    public String about() {
        return "user/about";
    }

    @GetMapping("/blog.html")
    public String blog() {
        return "user/blog";
    }

    @GetMapping("/blog-details.html")
    public String blogDetails() {
        return "user/blog-details";
    }

    @GetMapping("/checkout.html")
    public String checkout() {
        return "user/checkout";
    }

    @GetMapping("/contact.html")
    public String contact() {
        return "user/contact";
    }

    @GetMapping("/shop.html")
    public String shop() {
        return "user/shop";
    }

    @GetMapping("/shop-details.html")
    public String shopDetails() {
        return "user/shop-details";
    }

    public String shoppingCart() {
        return "user/shopping-cart";
    }

    @GetMapping("/login.html")
    public String loginPage() {
        return "user/login";
    }

    @GetMapping("/register.html")
    public String registerPage() {return "user/register";}

    @GetMapping("/user-information.html")
    public String profile() {return "user/profile";}
}
