package com.newline;

public class Test {

    
    public static void main(String[] args) {
        boolean isTrue = false;
        int counter = 0;
        do {
            
            if(isTrue){
                counter ++;
            }
            
            isTrue = true;
        } while (counter <= 0);
    }
}
