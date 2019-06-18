package com.tzy.data.service;

import com.tzy.data.pojo.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Value("${server.port}")
    String port;

    public List<Product> listProducts() {
        List<Product> ps = new ArrayList<>();
        ps.add(Product.builder().id(1).name("product a " + port + ":").price(50).build());
        ps.add(Product.builder().id(2).name("product b " + port + ":").price(100).build());
        ps.add(Product.builder().id(3).name("product c " + port + ":").price(150).build());
        return ps;
    }
}