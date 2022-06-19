package com.example.orderservice.repository;

import com.example.orderservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.parser.Entity;

public interface RepositoryProduct extends JpaRepository<Product,Integer> {
}
