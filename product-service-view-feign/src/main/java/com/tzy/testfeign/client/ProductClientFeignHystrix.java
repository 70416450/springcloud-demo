package com.tzy.testfeign.client;

import java.util.ArrayList;
import java.util.List;

import com.tzy.data.pojo.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductClientFeignHystrix implements ProductClientFeign{
    public List<Product> listProdcuts(){
        List<Product> result = new ArrayList<>();
        result.add(new Product(0,"产品数据微服务不可用",0));
        return result;
    }
}