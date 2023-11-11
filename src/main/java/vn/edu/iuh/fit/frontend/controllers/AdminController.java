package vn.edu.iuh.fit.frontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import vn.edu.iuh.fit.backend.dto.EmployeeDTO;
import vn.edu.iuh.fit.backend.repositories.OrderRepository;
import vn.edu.iuh.fit.backend.services.OrderServices;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class AdminController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderServices orderServices;
    private final NumberFormat format = NumberFormat.getCurrencyInstance();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

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

        modelAndView.addObject("mapKey", map.keySet().parallelStream().map(day -> {
            YearMonth yearMonth = YearMonth.now();

            return LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day);
        }).collect(Collectors.toList()));
        modelAndView.addObject("mapValue", map.values());

        modelAndView.setViewName("admin/statistics/revenueStatisticsByDay");

        return modelAndView;
    }

    @GetMapping("/statistics/by-time-period")
    public ModelAndView statisticByTimePeriod(@RequestParam(name = "daterange", required = false) String dateRange) {
        ModelAndView modelAndView = new ModelAndView();

        if (dateRange != null) {
            String[] split = dateRange.split(" - ");
            LocalDate startDate = LocalDate.parse(split[0], formatter);
            LocalDate endDate = LocalDate.parse(split[1], formatter);

            Map<LocalDate, Double> map = orderServices.calcRevenueByTimePeriod(startDate, endDate);

            modelAndView.addObject("mapKey", map.keySet());
            modelAndView.addObject("mapValue", map.values());
            modelAndView.addObject("daterange", dateRange);
        }

        modelAndView.setViewName("admin/statistics/revenueStatisticsByTimePeriod");

        return modelAndView;
    }

    @GetMapping("/statistics/by-employee")
    public ModelAndView statisticByEmployee(@RequestParam(name = "daterange", required = false) String dateRange) {
        ModelAndView modelAndView = new ModelAndView();

        if (dateRange != null) {
            String[] split = dateRange.split(" - ");
            LocalDate startDate = LocalDate.parse(split[0], formatter);
            LocalDate endDate = LocalDate.parse(split[1], formatter);

            Map<EmployeeDTO, Double> map = orderServices.calcRevenueByEmployeeInTimePeriod(startDate, endDate);

            modelAndView.addObject("mapKey", map.keySet().parallelStream().map(EmployeeDTO::getName).collect(Collectors.toList()));
            modelAndView.addObject("mapValue", map.values());
            modelAndView.addObject("daterange", dateRange);
        }

        modelAndView.setViewName("admin/statistics/revenueStatisticsByEmployeeInTimePeriod");

        return modelAndView;
    }
}
