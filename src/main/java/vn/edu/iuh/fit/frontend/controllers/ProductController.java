package vn.edu.iuh.fit.frontend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.models.ProductImage;
import vn.edu.iuh.fit.backend.models.ProductPrice;
import vn.edu.iuh.fit.backend.repositories.ProductImageRepository;
import vn.edu.iuh.fit.backend.repositories.ProductPriceRepository;
import vn.edu.iuh.fit.backend.repositories.ProductRepository;
import vn.edu.iuh.fit.backend.services.ProductPriceServices;
import vn.edu.iuh.fit.backend.services.ProductServices;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/dashboard/products")
public class ProductController {
    private final ProductServices productServices;
    private final ProductRepository productRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final ProductPriceRepository productPriceRepository;
    private final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images";
    private final ProductImageRepository productImageRepository;
    private final ProductPriceServices productPriceServices;

    public ProductController(ProductServices productServices, ProductRepository productRepository,
                             ProductPriceRepository productPriceRepository,
                             ProductImageRepository productImageRepository, ProductPriceServices productPriceServices) {
        this.productServices = productServices;
        this.productRepository = productRepository;
        this.productPriceRepository = productPriceRepository;
        this.productImageRepository = productImageRepository;
        this.productPriceServices = productPriceServices;
    }

    @GetMapping({"", "/"})
    public String index(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, Model model) {
        int pageNum = page.orElse(1);
        int sizeNum = size.orElse(10);

        Page<Product> products = productRepository.findAll(
                PageRequest.of(pageNum - 1, sizeNum)
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

    @GetMapping("/add")
    public String add(Model model) {
        Product product = new Product();
        ProductPrice productPrice = new ProductPrice();

        model.addAttribute("product", product);
        model.addAttribute("productPrice", productPrice);
        model.addAttribute("productStatuses", ProductStatus.values());

        return "admin/products/add";
    }

    @Transactional
    @PostMapping("")
    public String add_P(@ModelAttribute("product") Product product, @ModelAttribute("productPrice") ProductPrice productPrice, @RequestParam("image") MultipartFile file) {
        try {
            product.setProduct_id(0);

            productRepository.save(product);

            productPrice.setProduct(product);
            productPrice.setPrice_date_time(LocalDateTime.now());

            productPriceRepository.save(productPrice);

            String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");

            String fileName = product.getProduct_id() + "_" + UUID.randomUUID() + '.' + split[split.length - 1];
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, fileName);

            Files.write(fileNameAndPath, file.getBytes());

            ProductImage productImage = new ProductImage(fileName, product);

            productImageRepository.save(productImage);

            return "redirect:/dashboard/products";
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }

        return "redirect:/dashboard/products/add";
    }

    @GetMapping("/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            Optional<ProductPrice> productPrice = productPriceServices.findNewPrice(id);

            model.addAttribute("product", product);
            model.addAttribute("productPrice", productPrice.get());
            model.addAttribute("productStatuses", ProductStatus.values());

            return "admin/products/update";
        }

        return "redirect:/dashboard/products";
    }

    @GetMapping("/{id}/detail")
    public String detail(@PathVariable("id") long id, Model model) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            return "redirect:/dashboard/products";
        }

        Product product = productOptional.get();
        List<ProductImage> productImages = productImageRepository.findByProduct(product);
        List<ProductPrice> productPrices = productPriceRepository.findByProduct(product);

        model.addAttribute("product", product);
        model.addAttribute("productImages", productImages);
        model.addAttribute("productPrices", productPrices);

        return "admin/products/detail";
    }

    @Transactional
    @PostMapping("/update")
    public String update_P(@ModelAttribute("product") Product product, @ModelAttribute("productPrice") ProductPrice productPrice, @RequestParam("image") MultipartFile file) {
        try {
            productRepository.save(product);

            productPrice.setProduct(product);
            productPrice.setPrice_date_time(LocalDateTime.now());
            productPrice.setNote(null);
            productPriceRepository.save(productPrice);

            String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");

            String fileName = product.getProduct_id() + "_" + UUID.randomUUID() + '.' + split[split.length - 1];
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, fileName);

            Files.write(fileNameAndPath, file.getBytes());

            ProductImage productImage = new ProductImage(fileName, product);

            productImageRepository.save(productImage);

            return "redirect:/dashboard/products";
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return "redirect:/dashboard/products/" + product.getProduct_id();
    }
}
