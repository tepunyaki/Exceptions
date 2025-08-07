package Lib;
/**
 * Checked Exception ที่่จะถูกโยนเมื่อมีการพยามดำเนินการที่ไม่ถูกต้อง
 * เช่น การลบของที่ไม่สามารถลบได้
 */
public class InvalidOperationException extends Exception {
    public InvalidOperationException (String message){
        super(message);
    }
}
