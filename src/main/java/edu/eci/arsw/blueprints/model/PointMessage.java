package edu.eci.arsw.blueprints.model;

public class PointMessage {
    
    private double x;
    private double y;

    public PointMessage( double x, double y) {

        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    @Override
    public String toString() {
        return "PointMessage{" + "x=" + x + ", y=" + y + '}';
    }
}
