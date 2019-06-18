package com.tzy.testribbon.controller;

import java.util.List;

import com.tzy.testribbon.pojo.Product;
import com.tzy.testribbon.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
 

@Controller
public class ProductController {
  
    @Autowired
    ProductService productService;
     
    @RequestMapping("/products")
    public Object products(Model m) {
        List<Product> ps = productService.listProducts();
        m.addAttribute("ps", ps);
        return "products";
    }
}