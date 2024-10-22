package de.telran.hw_26_18okt;

import java.util.Random;

public class ShootersCompetition{

    // Флаг для проверки, разрушена ли мишень
    private static volatile boolean isHit = false;

    // Массив для хранения всех стрелков (потоков)
    private static Thread[] shooters = new Thread[5];

    public static void main(String[] args) {

        // Создаем 5 стрелков (потоков)
        for (int i = 0; i < 5; i++) {
            shooters[i] = new Thread(new Shooter(i + 1));
            shooters[i].start();
        }

        // Ждем завершения работы потоков
        for (Thread shooter : shooters) {
            try {
                shooter.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(" Соревнование завершено ");
    }

    // Класс, представляющий стрелка
    static class Shooter implements Runnable {
        private final int shooterId;
        private final Random random = new Random();

        public Shooter(int shooterId) {
            this.shooterId = shooterId;
        }

        @Override
        public void run() {
            try {
                // Симуляция времени выстрела
                Thread.sleep(random.nextInt(1000));

                // Если мишень уже разрушена, прерываем поток
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Стрелок " + shooterId + " прерван, мишень уже разрушена.");
                    return;
                }

                // Пытаемся выстрелить в мишень
                boolean hitTarget = random.nextBoolean();
                if (hitTarget && !isHit) {
                    isHit = true; // Мишень разрушена
                    System.out.println("Стрелок " + shooterId + " попал в мишень и разрушил ее!");

                    // Прерываем остальные потоки (стрелков)
                    interruptAllOtherShooters();
                } else if (!isHit) {
                    System.out.println("Стрелок " + shooterId + " промахнулся.");
                } else {
                    System.out.println("Стрелок " + shooterId + " выстрелил, но мишень уже разрушена.");
                }

            } catch (InterruptedException e) {
                System.out.println("Стрелок " + shooterId + " прерван.");
            }
        }

        // Прерываем все остальные потоки, кроме текущего
        private void interruptAllOtherShooters() {
            for (Thread shooter : shooters) {
                if (shooter != Thread.currentThread()) {
                    shooter.interrupt(); // Прерываем поток
                }
            }
        }
    }
}





