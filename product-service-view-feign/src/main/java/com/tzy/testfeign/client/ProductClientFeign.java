package com.tzy.testfeign.client;

import com.tzy.data.pojo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
//@FeignClient(value = "PRODUCT-DATA-SERVICE")
@FeignClient(value = "PRODUCT-DATA-SERVICE",fallback = ProductClientFeignHystrix.class)
public interface ProductClientFeign {
 
    @GetMapping("/products")
    public List<Product> listProdcuts();
}