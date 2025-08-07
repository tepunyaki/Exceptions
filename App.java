import Lib.*;
import Lib.Discount.*;

public class App{

    private static int passedCount = 0;
    private static int failedCount = 0;

    private static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("PASSED: " + testName);
            passedCount++;
        } else {
            System.out.println("FAILED: " + testName);
            failedCount++;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Starting E-commerce System Tests (with Exception Handling) ---");
        
        // --- Setup ---
        Product apple = new Product("P001", "Apple", 10.0);
        Product soda = new Product("P002", "Soda", 5.0);
        Product bread = new Product("P003", "Bread", 20.0);

        ProductCatalog catalog = new ProductCatalog();
        catalog.addProduct(apple);
        catalog.addProduct(soda);
        catalog.addProduct(bread);

        PricingService pricingService = new PricingService();
        pricingService.addStrategy("P001", new BogoDiscountStrategy());
        pricingService.addStrategy("P002", new BulkDiscountStrategy(6, 0.10));

        ShoppingCart cart = new ShoppingCart(pricingService, catalog);

        // --- Test Cases ---
        
        System.out.println("\n--- Testing Normal Operations ---");
        try {
            cart.addItem("P001", 3); // Apple x3
            cart.addItem("P002", 6); // Soda x6
            check("Add valid items", cart.getItemCount() == 2);
            check("Calculate total price correctly", cart.getTotalPrice() == (20.0 + 27.0)); // 47.0
            
            cart.removeItem("P001");
            check("Remove existing item", cart.getItemCount() == 1);
            check("Price updates after removal", cart.getTotalPrice() == 27.0);

        } catch (Exception e) {
            // เทสต์กลุ่มนี้ไม่ควรเกิด Exception
            failedCount += 4; // สมมติว่ามี 4 checks ในนี้
            System.out.println("FAILED: Normal operations should not throw exception: " + e.getMessage());
        }

        // --- Testing Exception Handling ---
        System.out.println("\n--- Testing Exception Handling ---");
        
        // Test adding non-existent product
        boolean caughtNotFound = false;
        try {
            cart.addItem("P999", 1);
        } catch (ProductNotFoundException e) {
            caughtNotFound = true;
            System.out.println("  -> Info: Correctly caught expected exception: " + e.getMessage());
        } catch (Exception e) {
            // Should not catch other exceptions
        }
        check("addItem throws ProductNotFoundException for unknown product", caughtNotFound);

        // Test removing non-existent product from cart
        boolean caughtInvalidOp = false;
        try {
            // P001 ถูกลบไปแล้ว
            cart.removeItem("P001");
        } catch (InvalidOperationException e) {
            caughtInvalidOp = true;
            System.out.println("  -> Info: Correctly caught expected exception: " + e.getMessage());
        } catch (Exception e) {
            // Should not catch other exceptions
        }
        check("removeItem throws InvalidOperationException for item not in cart", caughtInvalidOp);


        // --- สรุปผล ---
        System.out.println("\n--------------------");
        System.out.println("--- Test Summary ---");
        System.out.println("Passed: " + passedCount + ", Failed: " + failedCount);
        if (failedCount == 0) {
            System.out.println("Excellent! All tests passed!");
        } else {
            System.out.println("Some tests failed.");
        }
    }
}