package com.example.clothing_sell_website.service.customer;
import com.example.clothing_sell_website.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;



public interface ShopService {
    List<Product> getAllProduct();
    List<Product> getProductByBrand(String brandId);
    List<Product> getProductByType(String typeId);
    Product getProductById(String productId);
    List<String> getTop10ProductIds(int n);
    List<Product> getTop10Products(int n);
    Page<Product> getProductsByPage(int page, int size);
}