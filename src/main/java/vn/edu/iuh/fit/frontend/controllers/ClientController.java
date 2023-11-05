package vn.edu.iuh.fit.frontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.services.ProductServices;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class ClientController {
    @Autowired
    private ProductServices productServices;

    @GetMapping({"/", "/index"})
    public ModelAndView index(@RequestAttribute("page") Optional<Integer> page, @RequestAttribute("size") Optional<Integer> size) {
        ModelAndView modelAndView = new ModelAndView();

        Integer pageNum = page.orElse(1);
        Integer sizeNum = size.orElse(10);
        Page<Product> products = productServices.findPaging(pageNum, sizeNum, "name", "ASC");

        modelAndView.addObject("products", products);
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
