package vn.edu.iuh.fit.frontend.controllers;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.edu.iuh.fit.backend.models.*;
import vn.edu.iuh.fit.backend.repositories.CartRepository;
import vn.edu.iuh.fit.backend.repositories.CustomerRepository;
import vn.edu.iuh.fit.backend.repositories.OrderRepository;
import vn.edu.iuh.fit.backend.services.CartServices;
import vn.edu.iuh.fit.backend.services.EmployeeServices;
import vn.edu.iuh.fit.backend.services.ProductServices;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/")
public class ClientController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private ProductServices productServices;
    @Autowired
    private EmployeeServices employeeServices;
    @Autowired
    private CartServices cartServices;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;

    @GetMapping({"/", "/index"})
    public ModelAndView index(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Object object = session.getAttribute("employee");
        modelAndView.addObject("employee", object);


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
        modelAndView.addObject("pagesCurrent", IntStream.range(start, end + 1).boxed().toList());
        modelAndView.addObject("showLast", end < Math.max(end + 1, totalPages - 2) - 1);
        modelAndView.addObject("pagesLast", IntStream.range(Math.max(end + 1, totalPages - 2), totalPages + 1).boxed().toList());
//        End Handle Pagination

        modelAndView.setViewName("client/index");

        return modelAndView;
    }

    @GetMapping("/cart")
    public ModelAndView cart(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Object object = session.getAttribute("employee");

        if (object == null) {
            modelAndView.setViewName("redirect:/login");
        } else {
            Employee employee = (Employee) object;
            List<Cart> carts = cartServices.findByEmployee(employee.getId());

            modelAndView.addObject("employee", object);
            modelAndView.addObject("carts", carts);

            modelAndView.setViewName("client/cart");
        }

        return modelAndView;
    }

    @PostMapping("/cart")
    public ModelAndView addCart(@RequestParam(name = "product-id") Long productId, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        Object object = session.getAttribute("employee");

        if (object == null) {
            modelAndView.setViewName("redirect:/login");
        } else {
            Employee employee = (Employee) object;

            Optional<Cart> cartOptional = cartServices.findByEmployeeAndProduct(employee.getId(), productId);
            Cart cart;

            if (cartOptional.isPresent()) {
                cart = cartOptional.get();

                cart.setQuantity(cart.getQuantity() + 1);
            } else {
                cart = new Cart(
                        employee,
                        new Product(productId),
                        1
                );
            }

            try {
                cartRepository.save(cart);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

            modelAndView.setViewName("redirect:/");
        }

        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Object object = session.getAttribute("employee");
        modelAndView.addObject("employee", object);

        modelAndView.setViewName("client/login");
        modelAndView.addObject("phone", "");

        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute("phone") String phone, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        Optional<Employee> employee = employeeServices.login(phone);

        if (employee.isPresent()) {
            session.setAttribute("employee", employee.get());
            modelAndView.setViewName("redirect:/");
        } else {
            modelAndView.addObject("phone", phone);
            modelAndView.setViewName("redirect:/login");
        }


        return modelAndView;
    }

    @GetMapping("/checkout")
    public ModelAndView checkout(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        Object object = session.getAttribute("employee");

        if (object != null) {
            Employee employee = (Employee) object;
            List<Cart> carts = cartServices.findByEmployee(employee.getId());

            modelAndView.addObject("employee", object);

            if (carts.isEmpty()) {
                modelAndView.setViewName("redirect:/cart");
            } else {
                List<Customer> customers = customerRepository.findAll();

                modelAndView.addObject("customers", customers);
                modelAndView.addObject("carts", carts);
                modelAndView.setViewName("client/checkout");
            }
        } else {
            modelAndView.setViewName("redirect:/login");
        }

        return modelAndView;
    }

    @Transactional
    @PostMapping("/order")
    public ModelAndView order(@RequestParam("customer") Long customer, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        Object object = session.getAttribute("employee");

        if (object != null) {
            Employee employee = (Employee) object;

            List<Cart> carts = cartServices.findByEmployee(employee.getId());

            Order order = new Order(
                    LocalDateTime.now(),
                    employee,
                    customer == 0 ? null : new Customer(customer)
            );

            List<OrderDetail> orderDetails = new ArrayList<>();
            carts.forEach(cart -> {
                OrderDetail orderDetail = new OrderDetail(
                        cart.getQuantity(),
                        cart.getProduct().getPrice(),
                        null,
                        order,
                        cart.getProduct()
                );

                orderDetails.add(orderDetail);
            });

            order.setOrderDetails(orderDetails);

            try {
                if (!orderDetails.isEmpty()) {
                    orderRepository.save(order);
                    cartRepository.deleteByEmployee(employee);
                }

                modelAndView.setViewName("redirect:/");
            } catch (Exception e) {
                logger.error(e.getMessage());
                modelAndView.setViewName("redirect:/checkout");
            }
        } else {
            modelAndView.setViewName("redirect:/login");
        }

        return modelAndView;
    }
}
