package vn.edu.iuh.fit.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dashboard")
public class AdminController {

    @GetMapping("")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("admin/index");
        return modelAndView;
    }
}
