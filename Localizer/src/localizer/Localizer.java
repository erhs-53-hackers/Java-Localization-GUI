/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package localizer;

/**
 *
 * @author Michael
 */
public class Localizer {

    /**
     * @param args the command line arguments
     */
    public double[][] p;
    private double[][] world;
    private static final double pHit = .8;
    private static final double pMiss = .2;
    private static final double pExact = .9;
    private static final double pUnder = .1;
    private static final double pOver = .1;
    
    public Localizer(double[][] world) {
        this.world = world;
        p = new double[world.length][world[0].length];
        
        int length = world.length * world[0].length;
        
        for(int y=0;y<p.length;y++){            
            for(int x=0;x<p[y].length;x++) {
                p[y][x] = 1.0/length;
            }            
        }
    }
    
    public void sense(double z) {
        for(int y=0;y<world.length;y++) {
            for(int x=0;x<world[0].length;x++) {
                if(world[y][x] == z) p[y][x] *= pHit;
                else p[y][x] *= pMiss;                
            }
        }        
        normalize();
    }   
    
    public void move(int u1, int u2) {
        double[][] q = new double[p.length][p[0].length];
        for(int row=0;row<q.length;row++){
            System.arraycopy(p[row], 0, q[row], 0, p[row].length);
        }
        
        
        if(u2 != 0) {
            for(int y=0;y<p.length;y++) {
                for(int x=0;x<p[y].length;x++) {
                    double num = q[y][circle(x-u2, q[y].length)] * pExact
                    + q[y][circle(x-u2-1, q[y].length)] * pUnder
                    + q[y][circle(x, q[y].length)] * pOver;
                    
                    p[y][x] = num;
                    
                }
            }
        }
        if(u1 != 0) {
            for(int y=0;y<p.length;y++) {
                for(int x=0;x<p[y].length;x++) {
                    //System.out.println(circle(y-u1, q.length));
                    double num = q[circle(y-u1, q.length)][x] * pExact
                    + q[circle(y-u1-1, q.length)][x] * pUnder
                    + q[circle(y, q.length)][x] * pOver;
                    p[y][x] = num;
                }
            }
        }  
        
        normalize();
    }
    
    
    private int circle(int num, int length) {         
        while(num > length - 1) num -= length;
        while(num < 0) num += length;
        return num;       
    }
    
    private void normalize() {
        double sum = 0;
        for(double[] d : p) {
            for(double i : d) {
                sum += i;
            }            
        }
        for(int y=0;y<p.length;y++){            
            for(int x=0;x<p[y].length;x++) {
                p[y][x] /= sum;
            }            
        }
    }
    
    public int[] getMostLikely() {
        int[] res = new int[2];
        res[0] = 0;
        res[1] = 0;
        for(int y=0;y<p.length;y++) {
            for(int x=0;x<p[y].length;x++) {
                if(p[y][x] > p[res[1]][res[0]]) {
                    res[1] = y;
                    res[0] = x;
                }
            }
        }
        return res;
    }
     
    
    
    private void printArrayP(double[] p) {
        
        System.out.print("[");
        for(int i=0;i<p.length;i++){
            System.out.print(p[i]);
            if(!(i+1 == p.length)) {
               System.out.print(", "); 
            }
        }
        System.out.println("]");
        
    }
    public void printP() {
        printP(p);
    }
    public void printP(double[][] p) {
        for(double[] d : p) {
            printArrayP(d);
        }
        double sum = 0;
        for(double[] d : p) {
            for(double i : d) {
                sum += i;
            }            
        }
        System.out.println("------------;sum = " + sum);
        
    }
}
