import java.util.*;
interface MyInterface {
    default void defaultMethod() { 
        System.out.println("We love NEO"); 
    }
    static void staticMethod() { 
        System.out.println("Grow with NEO"); 
    }
}

 public class Main implements MyInterface {
    public static void main(String[] args) {
        Main obj = new Main(); 
        obj.defaultMethod();
        MyInterface.staticMethod();
    }
}