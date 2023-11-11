package vn.edu.iuh.fit.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backend.dto.EmployeeDTO;
import vn.edu.iuh.fit.backend.models.Employee;
import vn.edu.iuh.fit.backend.repositories.OrderRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    public Map<LocalDate, Double> calcRevenueByTimePeriod(LocalDate startDate, LocalDate endDate) {
        return orderRepository.calcRevenueByTimePeriod(
                LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX)
        ).parallelStream().collect(Collectors.toMap(
                (Object[] o) -> ((Date) o[0]).toLocalDate(),
                (Object[] o) -> ((Number) o[1]).doubleValue()
        ));
    }

    public Map<EmployeeDTO, Double> calcRevenueByEmployeeInTimePeriod(LocalDate startDate, LocalDate endDate) {
        return orderRepository.calcRevenueByEmployeeInTimePeriod(
                LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX)
        ).parallelStream().collect(Collectors.toMap(
                (Object[] o) -> {
                    Employee employee = (Employee) o[0];

                    return new EmployeeDTO(employee.getId(), employee.getFullname());
                },
                (Object[] o) -> ((Number) o[1]).doubleValue()
        ));
    }
}
