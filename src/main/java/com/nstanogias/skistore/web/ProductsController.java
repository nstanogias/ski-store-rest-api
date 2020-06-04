package com.nstanogias.skistore.web;

import com.nstanogias.skistore.domain.Product;
import com.nstanogias.skistore.domain.ProductBrand;
import com.nstanogias.skistore.domain.ProductType;
import com.nstanogias.skistore.dtos.ProductBrandDto;
import com.nstanogias.skistore.dtos.ProductToReturn;
import com.nstanogias.skistore.dtos.ProductTypeDto;
import com.nstanogias.skistore.helper.Pagination;
import com.nstanogias.skistore.repository.ProductRepository;
import com.nstanogias.skistore.repository.specs.ProductSpecs;
import com.nstanogias.skistore.repository.specs.SearchCriteria;
import com.nstanogias.skistore.repository.specs.SearchOperation;
import com.nstanogias.skistore.specifications.ProductSpecParams;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductsController {
    @Autowired
    private ModelMapper modelMapper;

    @NonNull
    private final ProductRepository productRepository;

    @GetMapping()
    public Pagination<ProductToReturn> getProducts(ProductSpecParams productSpecParams) {
        Sort sort;
        if (productSpecParams.getSort() != null && !productSpecParams.getSort().isEmpty()) {
            String sortBy = productSpecParams.getSort();
            boolean isAscending = true;
            if (sortBy.contains("Asc")) {
                sortBy = sortBy.replaceAll("Asc", "");
            } else if (sortBy.contains("Desc")) {
                isAscending = false;
                sortBy = sortBy.replaceAll("Desc", "");
            }

            sort = Sort.by(sortBy);
            sort = isAscending ? sort.ascending() : sort.descending();
        } else {
            sort = Sort.unsorted();
        }
        ProductSpecs productSpecs = new ProductSpecs();
        if (productSpecParams.getTypeId() > 0) {
            productSpecs.add(new SearchCriteria("productType", ProductType.values()[productSpecParams.getTypeId() - 1], SearchOperation.EQUAL));
        }
        if (productSpecParams.getBrandId() > 0) {
            productSpecs.add(new SearchCriteria("productBrand", ProductBrand.values()[productSpecParams.getBrandId() - 1], SearchOperation.EQUAL));
        }
        Pageable paging = PageRequest.of(productSpecParams.getPageIndex() - 1, productSpecParams.getPageSize(), sort);
        Page<Product> all = productSpecs.getList().isEmpty() ? productRepository.findAll(paging) : productRepository.findAll(productSpecs, paging);
        return new Pagination<>(productSpecParams.getPageIndex(), productSpecParams.getPageSize(), all.getTotalElements(),
                all.getContent()
                        .stream()
                        .map(product -> {
                            ProductToReturn productToReturn = modelMapper.map(product, ProductToReturn.class);
                            productToReturn.setPictureUrl("https://localhost:8443/" + productToReturn.getPictureUrl());
                            return productToReturn;
                        })
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ProductToReturn getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            ProductToReturn productToReturn =  modelMapper.map(product.get(), ProductToReturn.class);
            productToReturn.setPictureUrl("https://localhost:8443/" + productToReturn.getPictureUrl());
            return productToReturn;
        }
        return null;
    }

    @GetMapping("/types")
    public List<ProductTypeDto> getProductTypes() {
        return Arrays.asList(ProductType.values()).stream().map(value ->
                new ProductTypeDto(ProductType.valueOf(value.name()).ordinal() + 1, value.name())).collect(Collectors.toList());
    }

    @GetMapping("/brands")
    public List<ProductBrandDto> getProductBrands() {
        return Arrays.asList(ProductBrand.values()).stream().map(value ->
                new ProductBrandDto(ProductBrand.valueOf(value.name()).ordinal() + 1, value.name())).collect(Collectors.toList());
    }
}
