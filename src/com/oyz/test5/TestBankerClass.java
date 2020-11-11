package com.oyz.test5;


import java.util.Scanner;

public class TestBankerClass {


    public static void main(String[] args) {
        // TODO code application logic here
        boolean Choose = true;
        String C;
        Scanner in = new Scanner(System.in);
        BankClass T = new BankClass();
        
        T.setSystemVariable();
        while (Choose == true) {
            T.setRequest();
            System.out.println("您是否还要进行请求：y/n?");
            C = in.nextLine();
            if (C.endsWith("n")) {
                Choose = false;
            }
        }
    }
}
