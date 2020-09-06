package com.nstanogias.skistore.repository;

import com.nstanogias.skistore.domain.Product;
import com.nstanogias.skistore.domain.ProductBrand;
import com.nstanogias.skistore.domain.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByName(String name);
    Page<Product> findByProductBrand(ProductBrand brand, Pageable pageable);
    Page<Product> findByProductType(ProductType type, Pageable pageable);
    Page<Product> findByProductTypeAndProductBrand(ProductType type, ProductBrand brand, Pageable pageable);
}
