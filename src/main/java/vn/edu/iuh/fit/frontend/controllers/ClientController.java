package vn.edu.iuh.fit.frontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.services.ProductServices;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/")
public class ClientController {
    @Autowired
    private ProductServices productServices;

    @GetMapping({"/", "/index"})
    public ModelAndView index(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        ModelAndView modelAndView = new ModelAndView();

        Integer pageNum = page.orElse(1);
        Integer sizeNum = size.orElse(10);

        Page<Product> products = productServices.findPaging(pageNum, sizeNum, "name", "ASC");
        List<Integer> pages = IntStream.range(1, products.getTotalPages()).boxed().toList();

        modelAndView.addObject("products", products);
        modelAndView.addObject("pages", pages);

//        Start Handle Pagination
        int currentPage = products.getNumber() + 1;
        int totalPages = products.getTotalPages();

        int start = Math.max(1, currentPage - 1);
        int end = Math.min(totalPages, start + 2);

        if (totalPages == end)
            start -= 1;

        modelAndView.addObject("pagesFirst", IntStream.range(1, Math.min(4, start)).boxed().toList());
        modelAndView.addObject("showFirst", start > 4);
        modelAndView.addObject("pagesCurrent", IntStream.range(start, end+1).boxed().toList());
        modelAndView.addObject("showLast", end < Math.max(end+1, totalPages - 2) - 1);
        modelAndView.addObject("pagesLast", IntStream.range(Math.max(end+1, totalPages - 2), totalPages+1).boxed().toList());
//        End Handle Pagination

        modelAndView.setViewName("client/index");

        return modelAndView;
    }

    @GetMapping("/cart")
    public ModelAndView cart() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("client/cart");

        return modelAndView;
    }
}
