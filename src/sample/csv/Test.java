package sample.csv;

public class Test {
    public static void main(String[] args) {
        System.out.println("userAcceleration_x".indexOf("userAcceleration_x"));
        System.out.println("userAcceleration_x|Accelerometer_x".indexOf("LSM330 3-axis Accelerometer_x"));
        System.out.println("userAcceleration_x|Accelerometer_x".contains("Accelerometer_x"));
    }
}
