package vn.edu.iuh.fit.frontend.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.repositories.ProductRepository;
import vn.edu.iuh.fit.backend.services.ProductServices;

import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/dashboard/products")
public class ProductController {
    private final ProductServices productServices;
    private final ProductRepository productRepository;

    public ProductController(ProductServices productServices, ProductRepository productRepository) {
        this.productServices = productServices;
        this.productRepository = productRepository;
    }

    @GetMapping({"", "/"})
    public String index(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, Model model) {
        int pageNum = page.orElse(1);
        int sizeNum = size.orElse(10);

        Page<Product> products = productRepository.findAll(
                PageRequest.of(pageNum-1, sizeNum)
        );

//        Start Handle Pagination
        int currentPage = products.getNumber() + 1;
        int totalPages = products.getTotalPages();

        int start = Math.max(1, currentPage - 1);
        int end = Math.min(totalPages, start + 2);

        if (totalPages == end)
            start -= 1;

        model.addAttribute("pagesFirst", IntStream.range(1, Math.min(4, start)).boxed().toList());
        model.addAttribute("showFirst", start > 4);
        model.addAttribute("pagesCurrent", IntStream.range(start, end + 1).boxed().toList());
        model.addAttribute("showLast", end < Math.max(end + 1, totalPages - 2) - 1);
        model.addAttribute("pagesLast", IntStream.range(Math.max(end + 1, totalPages - 2), totalPages + 1).boxed().toList());
//        End Handle Pagination

        model.addAttribute("products", products);

        return "admin/products/index";
    }

    @Transactional
    @GetMapping("/delete")
    public String delete(@RequestParam("pro-id") Long productId) {
        productServices.softDelete(productId);

        return "redirect:/dashboard/products";
    }
}
