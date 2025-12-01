package com.ct08team.artbackendproject.Controller;

import com.ct08team.artbackendproject.DAO.OrderRepository;
import com.ct08team.artbackendproject.DAO.ProductVariantRepository;
import com.ct08team.artbackendproject.Entity.Order;
import com.ct08team.artbackendproject.Entity.OrderItem;
import com.ct08team.artbackendproject.Entity.product.ProductVariant;
import com.ct08team.artbackendproject.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId) {
        Order order = orderService.getOrderById(Long.valueOf(orderId));
        if (order == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");

        // Cập nhật trạng thái
        order.setOrderStatus("CANCELLED");
        order.setPaymentStatus("FAILED");

        // Hoàn lại số lượng tồn kho
        for (OrderItem item : order.getOrderItems()) {
            ProductVariant variant = item.getVariant();
            if (variant != null) {
                variant.setStockQuantity(variant.getStockQuantity() + item.getQuantity());
                productVariantRepository.save(variant);
            }
        }
        // Lưu thay đổi order xuống DB
        orderService.save(order);

        // --- ĐÃ SỬA ---
        // Chỉ trả về thông báo thành công
        return ResponseEntity.ok(Map.of("message", "Đã hủy đơn hàng thành công"));
    }

    // 2. API Xác nhận thanh toán thành công
    @PutMapping("/orders/{orderId}/confirm")
    public ResponseEntity<?> confirmOrderPayment(@PathVariable String orderId) {
        Order order = orderService.getOrderById(Long.valueOf(orderId));
        if (order == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");

        // Cập nhật trạng thái
        order.setOrderStatus("PAID");
        order.setPaymentStatus("SUCCESSFUL");

        // Lưu thay đổi order xuống DB
        orderService.save(order);

        // --- ĐÃ SỬA ---
        // Chỉ trả về thông báo thành công
        return ResponseEntity.ok(Map.of("message", "Xác nhận thanh toán thành công"));
    }

}