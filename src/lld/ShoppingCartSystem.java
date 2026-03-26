package lld;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WAYFAIR LLD: Advanced Shopping Cart & Pricing Engine
 * Focus: Thread-safety for concurrent user sessions, Strategy pattern for shipping rules.
 */

// ==========================================
// Models
// ==========================================

class ProductShopping {
    private String id;
    private String name;
    private double price;
    private double weightLbs; // Crucial for Wayfair shipping calculations

    public ProductShopping(String id, String name, double price, double weightLbs) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.weightLbs = weightLbs;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public double getWeightLbs() { return weightLbs; }
}

class CartItem {
    private ProductShopping productShopping;
    private AtomicInteger quantity;

    public CartItem(ProductShopping productShopping, int initialQuantity) {
        this.productShopping = productShopping;
        this.quantity = new AtomicInteger(initialQuantity);
    }

    public ProductShopping getProduct() { return productShopping; }
    public int getQuantity() { return quantity.get(); }

    // Thread-safe increment
    public void addQuantity(int amount) {
        quantity.addAndGet(amount);
    }

    // Thread-safe update
    public void setQuantity(int newQuantity) {
        quantity.set(newQuantity);
    }
}

// ==========================================
// Strategy Pattern for Shipping Calculation
// ==========================================

interface ShippingStrategy {
    double calculateShipping(List<CartItem> items);
}

class WayfairDynamicShippingStrategy implements ShippingStrategy {
    private static final double HEAVY_ITEM_THRESHOLD_LBS = 50.0;
    private static final double STANDARD_RATE_PER_ITEM = 5.0;
    private static final double FREIGHT_RATE_PER_ITEM = 50.0;

    @Override
    public double calculateShipping(List<CartItem> items) {
        double totalShipping = 0.0;
        for (CartItem item : items) {
            double itemWeight = item.getProduct().getWeightLbs();
            int qty = item.getQuantity();

            if (itemWeight >= HEAVY_ITEM_THRESHOLD_LBS) {
                // Heavy furniture requires expensive freight shipping
                totalShipping += (FREIGHT_RATE_PER_ITEM * qty);
            } else {
                // Small decor items
                totalShipping += (STANDARD_RATE_PER_ITEM * qty);
            }
        }
        return totalShipping;
    }
}

// ==========================================
// Cart Management (Thread-Safe)
// ==========================================

class ShoppingCart {
    private String cartId;
    // ConcurrentHashMap handles thread-safe structural modifications
    private final Map<String, CartItem> items = new ConcurrentHashMap<>();
    private ShippingStrategy shippingStrategy;
    private static final double TAX_RATE = 0.08; // 8% tax

    public ShoppingCart(String cartId, ShippingStrategy shippingStrategy) {
        this.cartId = cartId;
        this.shippingStrategy = shippingStrategy;
    }

    // Handles rapid multi-click "Add to Cart" race conditions
    public void addItem(ProductShopping productShopping, int quantity) {
        items.compute(productShopping.getId(), (key, existingItem) -> {
            if (existingItem == null) {
                System.out.println(Thread.currentThread().getName() + " -> Added new item: " + productShopping.getName());
                return new CartItem(productShopping, quantity);
            } else {
                existingItem.addQuantity(quantity);
                System.out.println(Thread.currentThread().getName() + " -> Updated quantity for: " + productShopping.getName() + " to " + existingItem.getQuantity());
                return existingItem;
            }
        });
    }

    public void removeItem(String productId) {
        items.remove(productId);
    }

    // ==========================================
    // Pricing Engine Logic
    // ==========================================

    public double getSubtotal() {
        return items.values().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public double getTax() {
        return getSubtotal() * TAX_RATE;
    }

    public double getShippingCost() {
        return shippingStrategy.calculateShipping(new ArrayList<>(items.values()));
    }

    public double getTotalPrice() {
        return getSubtotal() + getTax() + getShippingCost();
    }

    public void printReceipt() {
        System.out.println("\n--- Receipt for Cart: " + cartId + " ---");
        for (CartItem item : items.values()) {
            System.out.printf("%s (x%d) - $%.2f\n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getProduct().getPrice() * item.getQuantity());
        }
        System.out.printf("Subtotal: $%.2f\n", getSubtotal());
        System.out.printf("Tax (8%%): $%.2f\n", getTax());
        System.out.printf("Shipping: $%.2f\n", getShippingCost());
        System.out.printf("TOTAL:    $%.2f\n", getTotalPrice());
        System.out.println("-----------------------------------");
    }
}

// ==========================================
// Main Execution / Concurrent Simulation
// ==========================================

public class ShoppingCartSystem {
    public static void main(String[] args) throws InterruptedException {
        // Setup Catalog
        ProductShopping sofa = new ProductShopping("P1", "Sectional Sofa", 1200.00, 150.0); // Heavy!
        ProductShopping lamp = new ProductShopping("P2", "Table Lamp", 45.00, 5.0);       // Light
        ProductShopping rug = new ProductShopping("P3", "Area Rug", 200.00, 30.0);        // Light

        ShippingStrategy wayfairShipping = new WayfairDynamicShippingStrategy();
        ShoppingCart cart = new ShoppingCart("User-Cart-123", wayfairShipping);

        // Simulate a user aggressively clicking "Add to cart" in multiple tabs
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // User clicks add to cart for Sofa 3 times concurrently
        executor.submit(() -> cart.addItem(sofa, 1));
        executor.submit(() -> cart.addItem(sofa, 1));
        executor.submit(() -> cart.addItem(sofa, 1));

        // User adds a lamp and a rug
        executor.submit(() -> cart.addItem(lamp, 2));
        executor.submit(() -> cart.addItem(rug, 1));

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);

        // Print final thread-safe calculated receipt
        cart.printReceipt();
    }
}