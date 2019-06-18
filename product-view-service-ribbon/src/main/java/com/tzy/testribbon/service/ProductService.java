package com.tzy.testribbon.service;

import java.util.List;

import com.tzy.testribbon.client.ProductClientRibbon;
import com.tzy.testribbon.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 

@Service
public class ProductService {
    @Autowired
    ProductClientRibbon productClientRibbon;
    public List<Product> listProducts(){
        return productClientRibbon.listProdcuts();
    }
}