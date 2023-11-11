package vn.edu.iuh.fit.frontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vn.edu.iuh.fit.backend.repositories.OrderRepository;
import vn.edu.iuh.fit.backend.services.OrderServices;

import java.text.NumberFormat;
import java.time.YearMonth;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class AdminController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderServices orderServices;
    private final NumberFormat format = NumberFormat.getCurrencyInstance();

    @GetMapping("")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();

        Double revenueByMonth = orderRepository.calcAvgRevenueByMonth();
        Double revenueByYear = orderRepository.calcAvgRevenueByYear();
        Map<YearMonth, Double> map = orderServices.calcRevenueByMonth();

        modelAndView.addObject("revenueByMonth", format.format(revenueByMonth));
        modelAndView.addObject("revenueByYear", format.format(revenueByYear));
        modelAndView.addObject("mapKey", map.keySet());
        modelAndView.addObject("mapValue", map.values());

        modelAndView.setViewName("admin/index");
        return modelAndView;
    }

    @GetMapping("/statistics/by-day")
    public ModelAndView statisticByDay() {
        ModelAndView modelAndView = new ModelAndView();

        Map<Integer, Double> map = orderServices.calcRevenueByDay();

        modelAndView.addObject("mapKey", map.keySet());
        modelAndView.addObject("mapValue", map.values());

        modelAndView.setViewName("admin/statistics/revenueStatisticsByDay");

        return modelAndView;
    }
}
