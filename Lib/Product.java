package Lib;

/**
 * ADT ที่ไม่เปลี่ยนรูป (Immutable) สำหรับเก็บข้อมูลสินค้า
 * คลาสนี้เป็น final เพื่อป้องกันการสืบทอดและรับประกัน Immutability
 */
public final class Product {
    private final String productId;
    private final String productName;
    private final double price;

    // Rep Invariant (RI):
    //  - productId and productName are not null or blank.
    //  - price >= 0.
    //
    // Abstraction Function (AF):
    //  - AF(productId, productName, price) = A product with the given ID, name, and price.

    /**
     * ตรวจสอบว่า Rep Invariant เป็นจริงหรือไม่
     */
    private void checkRep() {
        if (productId == null || productId.isBlank()) {
            throw new RuntimeException("RI violated: productId cannot be blank.");
        }
        if (productName == null || productName.isBlank()) {
            throw new RuntimeException("RI violated: productName cannot be blank.");
        }
        if (price < 0) {
            throw new RuntimeException("RI violated: price cannot be negative.");
        }
    }

    /**
     * สร้างอ็อบเจกต์ Product
     * @param productId รหัสสินค้า ห้ามเป็นค่าว่าง
     * @param productName ชื่อสินค้า ห้ามเป็นค่าว่าง
     * @param price ราคา ต้องไม่ติดลบ
     * @throws IllegalArgumentException หากคุณสมบัติของสินค้าไม่ถูกต้อง
     */
    public Product(String productId, String productName, double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        checkRep(); // ตรวจสอบความถูกต้องทุกครั้งที่สร้าง
    }

    /**
     * @return รหัสสินค้า
     */
    public String getProductId() { return productId; }
    
    /**
     * @return ชื่อสินค้า
     */
    public String getProductName() { return productName; }

    /**
     * @return ราคาของสินค้า
     */
    public double getPrice() { return price; }

    /**
     * เปรียบเทียบ Product สองชิ้นโดยใช้ productId
     * @param obj อ็อบเจกต์ที่ต้องการเปรียบเทียบ
     * @return true หาก productId เหมือนกัน
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return productId.equals(product.productId);
    }

    /**
     * สร้าง hash code จาก productId
     * @return ค่า hash code
     */
    @Override
    public int hashCode() {
        return productId.hashCode();
    }
}