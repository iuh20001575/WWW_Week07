package vn.edu.iuh.fit.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.repositories.ProductRepository;

@Service
public class ProductServices {
    @Autowired
    private ProductRepository productRepository;

    public Page<Product> findPaging(int page, int pageSize, String sortBy, String sortDirection) {
        PageRequest pageRequest = PageRequest.of(page-1, pageSize, Sort.by(
                Sort.Direction.valueOf(sortDirection), sortBy));

//        return productRepository.findAll(pageRequest);
        return productRepository.findToRender(ProductStatus.ACTIVE, pageRequest);
    }

    public Integer softDelete(long productId) {
        return productRepository.updateStatus(productId, ProductStatus.TERMINATED);
    }
}
