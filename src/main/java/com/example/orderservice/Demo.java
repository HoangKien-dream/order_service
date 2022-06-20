package com.example.orderservice;

public class Demo {
    public static void minMax(){
        int[] array = {1,9,10,3,6,7,-3};
        int max = 0;
        int min = 0;
        for (int i = 0; i < array.length; i++) {
            if (max < array[i]){
                max = array[i];
            }
            if (min > array[i])
                min = array[i];
        }
        System.out.println("Max:"+ max);
        System.out.println("Min:"+ min);
    }

    public static void armStrong(int a){
        int number = a;
        int getElementNumber = a;
        int checkedNumber = a;
        int count = 0;
        int result = 0;
        while (number > 0){
         count++;
         number/=10;
        }
        System.out.println("dộ dài của dãy số là "+ count);
        while (getElementNumber > 0){
            result += Math.pow(getElementNumber%10,count);
            getElementNumber/=10;
        }
        System.out.println(result);
        if (result == checkedNumber){
            System.out.println("Đây là số ArmStrong");
        }
        else {
            System.out.println("Không phải là số ArmStrong");
        }
    }


    public static void main(String[] args) {
        minMax();
        armStrong(-1000);
    }
}
