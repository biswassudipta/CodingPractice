package lld;

import java.util.*;

/**
 * WAYFAIR LLD: Coupon and Category Hierarchy System
 * * To run this file, simply compile and execute CouponCategorySystem.
 * It contains all necessary models, strategies, and the main driver.
 */

// ==========================================
// Models
// ==========================================

record Category(String id , String name , String parentCategoryId) {}
record Product(String id , String name , double price , String categoryId) {}

// ==========================================
// Strategy Pattern for Discounts
// ==========================================

interface DiscountStrategy {
    double applyDiscount(double originalPrice);
}

class PercentageDiscount implements DiscountStrategy {
    private final double percentage; // e.g., 10.0 for 10%

    public PercentageDiscount(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice - (originalPrice * (percentage / 100.0));
    }
}

class FlatDiscount implements DiscountStrategy {
    private final double flatAmount;

    public FlatDiscount(double flatAmount) {
        this.flatAmount = flatAmount;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        return Math.max(0, originalPrice - flatAmount); // Price cannot go below 0
    }
}

record Coupon(String id , String categoryId , DiscountStrategy discountStrategy) {
public double calculateDiscountedPrice(double price) {
 return discountStrategy.applyDiscount(price);
}
 }
// ==========================================
// Service / Manager
// ==========================================

class PromotionService {
    // In-memory databases
    private final Map<String, Category> categoryMap;
    private final Map<String, List<Coupon>> categoryCouponsMap;

    public PromotionService() {
        this.categoryMap = new HashMap<>();
        this.categoryCouponsMap = new HashMap<>();
    }

    public void addCategory(Category category) {
        categoryMap.put(category.id(), category);
    }

    public void addCoupon(Coupon coupon) {
        categoryCouponsMap.computeIfAbsent(coupon.categoryId(), k -> new ArrayList<>()).add(coupon);
    }

    /**
     * Finds the lowest possible price for a product by checking its category
     * and traversing up the parent categories.
     */
    public double getBestPrice(Product product) {
        double bestPrice = product.price();
        String currentCategoryId = product.categoryId();

        // Traverse the category tree upwards
        while (currentCategoryId != null && categoryMap.containsKey(currentCategoryId)) {
            // Check all coupons applicable to this specific category level
            List<Coupon> applicableCoupons = categoryCouponsMap.getOrDefault(currentCategoryId, new ArrayList<>());

            for (Coupon coupon : applicableCoupons) {
                double discountedPrice = coupon.calculateDiscountedPrice(product.price());
                if (discountedPrice < bestPrice) {
                    bestPrice = discountedPrice;
                }
            }

            // Move up to the parent category
            Category currentCategory = categoryMap.get(currentCategoryId);
            currentCategoryId = currentCategory.parentCategoryId();
        }

        return bestPrice;
    }
}

// ==========================================
// Main Execution / Testing
// ==========================================

public class CouponCategorySystem {
    static void main() {
        PromotionService promoService = getPromotionService();

        // 3. Create a Product
        Product leatherSofa = new Product("p-1", "Premium Leather Sofa", 1000.0, "cat-sofas");

        System.out.println("---- Wayfair Coupon System ----");
        System.out.println("Product: " + leatherSofa.name() + " | Original Price: $" + leatherSofa.price());

        // 4. Calculate Best Price
        // Expected Breakdown:
        // - Home Coupon (10%): 1000 - 100 = 900
        // - Furniture Coupon ($50): 1000 - 50 = 950
        // - Sofa Coupon (15%): 1000 - 150 = 850
        // Expected Best Price: 850

        double bestPrice = promoService.getBestPrice(leatherSofa);
        System.out.println("Best Discounted Price: $" + bestPrice);
    }

    private static PromotionService getPromotionService() {
        PromotionService promoService = new PromotionService();

        // 1. Setup Categories (Hierarchy: Home -> Furniture -> Sofas)
        promoService.addCategory(new Category("cat-home", "Home", null));
        promoService.addCategory(new Category("cat-furniture", "Furniture", "cat-home"));
        promoService.addCategory(new Category("cat-sofas", "Sofas", "cat-furniture"));

        // 2. Setup Coupons
        // 10% off all Home items
        promoService.addCoupon(new Coupon("coup-1", "cat-home", new PercentageDiscount(10.0)));
        // $50 flat off Furniture
        promoService.addCoupon(new Coupon("coup-2", "cat-furniture", new FlatDiscount(50.0)));
        // 15% off Sofas
        promoService.addCoupon(new Coupon("coup-3", "cat-sofas", new PercentageDiscount(15.0)));
        return promoService;
    }
}