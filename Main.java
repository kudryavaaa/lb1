package org.example;

import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Створення об'єкта scanner для зчитування вводу з клавіатури
        System.out.print("Крок: ");
        int krok = scanner.nextInt(); // Зчитування значення кроку
        System.out.print("Кiлькiсть потокiв: ");
        int potoki = scanner.nextInt(); // Зчитування значення кількості потоків
        int permissionInterval = 10000; // Ініціалізація інтервалу дозволу

        SummingThread[] potok = new SummingThread[potoki]; // Створення масиву потоків

        for (int i = 0; i < potoki; i++) { // Цикл створення і запуску потоків
            potok[i] = new SummingThread(i, krok); // Створення об'єкта SummingThread
            potok[i].start(); // Запуск потоку
        }

        try { // Обробка виключення InterruptedException
            Thread.sleep(permissionInterval); // Затримка виконання поточного потоку
        } catch (InterruptedException e) { // Обробка виключення InterruptedException
            e.printStackTrace(); // Виведення стека викликів виключення
        }

        for (int i = 0; i < potoki; i++) { // Цикл зупинки всіх потоків
            potok[i].setRunning(false); // Встановлення прапорця running в false для зупинки потоку
        }
    }

    private static class SummingThread extends Thread { // Внутрішній клас SummingThread, який наслідує Thread
        private final int id; // Поле для ідентифікатора потоку
        private final int step; // Поле для кроку
        private volatile boolean running = true; // поле для запуску потоків в порядку черги

        public SummingThread(int id, int step) { // Конструктор класу SummingThread
            this.id = id; // Присвоєння значення ідентифікатора потоку
            this.step = step; // Присвоєння значення кроку
        }

        public void run() { // Перевизначення методу run
            BigInteger sum = BigInteger.ZERO; // Ініціалізація змінної для суми
            long count = 0; // Ініціалізація змінної для підрахунку кількості доданків
            long current = 0; // Ініціалізація змінної для поточного значення

            while (running) { // Цикл, який виконується, доки running дорівнює true
                sum = sum.add(BigInteger.valueOf(current)); // Додавання до суми
                count++; // Збільшення лічильника доданків
                current += step; // Збільшення поточного значення на крок
            }

            System.out.printf("Потiк %d: сума = %s, кiлькiсть доданкiв = %d\n", id + 1, sum, count); // Виведення результатів роботи потоку
        }

        public void setRunning(boolean running) { // Метод для встановлення значення прапорця running
            this.running = running; // Присвоєння нового значення прапорця running
        }
    }
}
