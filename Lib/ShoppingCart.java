package Lib;
import java.util.ArrayList ;

/**
 * ADT สำหรับจัดการตะกร้าสินค้า
 */
public class ShoppingCart {
    private ArrayList<CartItem> items;
    private PricingService pricingService;
    private ProductCatalog productCatalog;

    // Rep Invariant (RI):
    //  - items, pricingService, productCatalog are not null.
    //  - no element in items is null.
    //  - no two CartItems in items have the same Product.
    //
    // Abstraction Function (AF):
    //  - AF(items, ...) = A shopping cart containing the specified items.

    private void checkRep() {
        if (items == null || pricingService == null || productCatalog == null) {
            throw new RuntimeException("RI violated: core components cannot be null.");
        }
        ArrayList<Product> seenProducts = new ArrayList<>();
        for (CartItem item : items) {
            if (item == null) {
                throw new RuntimeException("RI violated: cart contains a null item.");
            }
            if (seenProducts.contains(item.getProduct())) {
                throw new RuntimeException("RI violated: duplicate product found in cart: " + item.getProduct().getProductId());
            }
            seenProducts.add(item.getProduct());
        }
    }

    /**
     * สร้างตะกร้าสินค้า
     * @param pricingService service สำหรับคำนวณราคา
     * @param productCatalog service สำหรับค้นหาสินค้า
     */
    public ShoppingCart(PricingService pricingService, ProductCatalog productCatalog) {
        this.items = new ArrayList<>();
        this.pricingService = pricingService;
        this.productCatalog = productCatalog;
        checkRep();
    }

    /**
     * เพิ่มสินค้าเข้าตะกร้าโดยใช้รหัสสินค้า
     * หากมีสินค้าชนิดเดียวกันอยู่แล้ว จะเพิ่มจำนวนเข้าไป
     * @param productId รหัสของสินค้า
     * @param quantity จำนวนที่ต้องการเพิ่ม (ต้องมากกว่า 0)
     * @throws ProductNotFoundException หากไม่พบไม่พบสินค้านั้นในแคตตาล็อก
     */
    public void addItem(String productId, int quantity) throws ProductNotFoundException{
        // findById จะโยนException ถ้าไม่พบสินค้า 
        Product p = productCatalog.findById(productId);
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be possitive.");
        }
        for (CartItem item : items) {
            if (item.getProduct().equals(p)) {
                item.increaseQuantity(quantity);
                checkRep(); // ตรวจสอบหลังการเปลี่ยนแปลง
                return;
            }
        }
        items.add(new CartItem(p, quantity));
        checkRep(); // ตรวจสอบหลังการเปลี่ยนแปลง
    }

    /**
     * ลบสินค้า (ทั้งรายการ) ออกจากตะกร้า
     * @param productId รหัสของสินค้าที่ต้องการลบ
     */
    public void removeItem(String productId) throws InvalidOperationException {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null.");
        }
        CartItem itemToRemove = null;
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(productId)) {
                itemToRemove = item;
                break;
            }
        }
        if (itemToRemove != null) {
            items.remove(itemToRemove);
            checkRep(); 
        } else
            throw new InvalidOperationException("Cannot remove item. Product ID\r\n" +  "'\" + productId + \"' not found in cart.");
       
    }

    /**
     * คำนวณราคารวมของสินค้าทั้งหมดในตะกร้า
     * @return ราคารวมสุทธิ
     */
    public double getTotalPrice() {
        double total = 0.0;
        for (CartItem item : items) {
            total += pricingService.calculateItemPrice(item);
        }
        return total;
    }
    
    /**
     * @return จำนวนรายการสินค้าในตะกร้า
     */
    public int getItemCount() { return items.size(); }
    
    /**
     * ล้างสินค้าทั้งหมดออกจากตะกร้า
     */
    public void clearCart() {
        items.clear();
        checkRep(); // ตรวจสอบหลังการเปลี่ยนแปลง
    }
}