package vn.edu.iuh.fit.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backend.repositories.OrderRepository;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class OrderServices {
    @Autowired
    private OrderRepository orderRepository;

    public Map<YearMonth, Double> calcRevenueByMonth() {
        List<Object[]> list = orderRepository.calcRevenueByMonth();

        Collector<Object[], ?, TreeMap<YearMonth, Double>> map = Collectors.toMap((Object[] o) -> YearMonth.of(((Number) o[1]).intValue(), ((Number) o[0]).intValue()),
                (Object[] o) -> ((Number) o[2]).doubleValue(),
                Double::sum,
                TreeMap::new);

        return list.parallelStream().collect(map);
    }

    public Map<Integer, Double> calcRevenueByDay() {
        YearMonth yearMonth = YearMonth.now();
        List<Object[]> list = orderRepository.calcRevenueByDay(yearMonth.getYear(), yearMonth.getMonthValue());

        Collector<Object[], ?, TreeMap<Integer, Double>> collector = Collectors.toMap((Object[] o) -> ((Number) o[0]).intValue(),
                (Object[] o) -> ((Number) o[1]).doubleValue(),
                Double::sum,
                TreeMap::new);

        return list.parallelStream().collect(collector);
    }
}
