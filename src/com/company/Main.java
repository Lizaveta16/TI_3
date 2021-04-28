package com.company;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Введите простое число p:");
        long p = sc.nextLong();
        System.out.println("Введите простое число q:");
        long q = sc.nextLong();
        sc.nextLine();
        System.out.println("Введите шифруемый текст:");
        String text = sc.nextLine().toUpperCase();
        text = text.replaceAll("\\s", "");

        long r = p * q;
        long f = (p - 1) * (q - 1);

        long e = getE(f);

        Triple temp = getExtendGcd(f, e);     // Расширенный алгоритм Евклида
        long d = temp.y;
        if (d < 0) {
            d += f;
        }

        long hash = getHash(text, r);
        Long S = power(hash, d, r);
        System.out.println("Хеш сообщения: " + hash +
                "  Подпись:" + S.toString());

        System.out.println("\nПроверка\n\nВведите полученное сообщение и подпись:");
        String newText = sc.nextLine().toUpperCase();
        newText = newText.replaceAll("\\s", "");
        int newS = sc.nextInt();
        long newHash = getHash(newText, r);
        if (newHash == power(newS, e, r)) {
            System.out.println("\nПодпись подтверждена");
        } else {
            System.out.println("\nПодпись недействительна");
        }
        System.out.println("Хеш сообщения: " + newHash +
                "  Хэш по подписи:" + power(newS, e, r));

    }

    private static long getHash(String s, long n){
        long h = 100;
        for (int i = 0; i < s.length(); i++){
            h = (h + (int)s.charAt(i))*(h + (int)s.charAt(i)) % n;
        }
        return h;
    }

    public static boolean isCoprime(long a, long b) {  // Являются ли числа взаимно простыми
        if (a == b) {
            return a == 1;
        } else {
            if (a > b) {
                return isCoprime(a - b, b);
            } else {
                return isCoprime(b - a, a);
            }
        }
    }

    private static boolean isPrime(long a) { // Является ли число простым
        for (long i = 2; i <= Math.sqrt(a); i++) {
            if (a % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static long getE(long f) {  // Вычисляем значение открытой экспненты e
        ArrayList<Long> valArr = new ArrayList<Long>();
        long e = f - 1;
        for (int i = 2; i < f; i++) {
            if (isPrime(e) && isCoprime(e, f)) {
                valArr.add(e);
            }
            e--;
        }
        Random random = new Random();
        int index = random.nextInt(valArr.size());
        return valArr.get(index);
    }

    private static Triple getExtendGcd(long a, long b) {  // Расширенный алгоритм Евклида
        if (b == 0) {
            return new Triple(a, 1, 0);
        } else {

            Triple tmp = getExtendGcd(b, a % b);
            long d = tmp.d;
            long y = tmp.x - tmp.y * (a / b);
            long x = tmp.y;

            return new Triple(d, x, y);
        }
    }

    private static long power(long x, long y, long N) {  //Алгоритм быстрого возведения в степень
        if (y == 0) return 1;
        long z = power(x, y / 2, N);
        if (y % 2 == 0)
            return (z * z) % N;
        else
            return (x * z * z) % N;
    }


    private static final class Triple { // Вспомогательный класс для хранения 3-х значений
        long d;
        long x;
        long y;

        Triple(long d, long x, long y) {
            this.d = d;
            this.x = x;
            this.y = y;
        }
    }

}
