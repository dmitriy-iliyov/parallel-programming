package org.example;

public class Point {
    
    private double x;
    private double y;

    public Point() {
        this.x = getRandValue();
        this.y = getRandValue();
    }
    
    public double getX(){
        return this.x;
    }
    
    public double getY(){
        return this.y;
    }
    
    @Override
    public boolean equals (Object object){
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Point person = (Point) object;
        if (this.x != person.getX())return false;
        return this.y == person.getY();
    }

    private double getRandValue(){
        return Math.random();
    }
}
