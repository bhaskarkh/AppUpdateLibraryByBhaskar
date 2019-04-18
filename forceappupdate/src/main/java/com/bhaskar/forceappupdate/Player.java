package com.bhaskar.forceappupdate;

public class Player {
    int a,b;

    public Player(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int sqr()
    {
        return this.a*this.b;
    }
}
