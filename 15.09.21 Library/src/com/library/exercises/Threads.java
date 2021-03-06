package com.library.exercises;

import java.sql.SQLOutput;

public class Threads {
    public void demo() {
        Thread thread1 = new Thread() {
            public void run() {
                System.out.println("Hello from thread 1");
            }
        };
        thread1.start();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Hello from thread 2");
                    }
                }
        ).start();

        Runnable r3 = () -> {
            System.out.println("Hello from thread 3");
        };
        new Thread(r3).start();

        new Thread(() -> System.out.println("Thread 4")).start();

        new NumberedThread(5).start();
        new NumberedThread(6).start();
        new NumberedThread(7).start();
    }

    private double res;  // non-reference type, not valid for synchronization
    private final Object semaphore = new Object();

    public void demo2() {
        Runnable plus10percent = () -> {
            double oldValue;
            double newValue;
            synchronized (semaphore) {
                oldValue = res;  // read
                newValue = oldValue + 0.1 * oldValue;
                res = newValue; // write
            }
            System.out.println(newValue);
        };
        res = 100;

        for (int i = 0; i < 12; ++i) {
            new Thread(plus10percent).start();
        }
    }

    public void demo3() {
        Runnable plus10percent = () -> {
            double oldValue = res;  // read
            double newValue = oldValue + 0.1 * oldValue;
            res = newValue; // write
        };
        res = 100;

        for (int i = 0; i < 12; ++i) {
            new Thread(plus10percent).start();
        }
    }

    class NumberedThread extends Thread {
        int num;

        public NumberedThread(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            System.out.println("Hello from thread " + num);
        }
    }
}

/*
    Thread (??????????) - ?????????? ????????????????, ?????????? ?????????????????? ???? ?????????????? (????????????).
    ?? Java ???????????? ???? ???????????????? ????????????????????????????????, ?? ?????????? ???????????????????? ???????????? ?? ?????????? ??????????????.
    ???????????????? ?????????????? ?????? ???????????????????? ???????????????? ???????????????? Thread. ???????????????? ??????????????, ?????????????? ??????????????????????, ???????????????? run().
    Runnable - ?????????????????? ?????? ????????????????, ?????????????????????? ?? ?????????????????? ??????????????.

    II ?????????????????????????? ?????????????? - ?????????????? ????????????????: ???????????? ?? ?????????? ?????????????? (?? ?????????? ?????????? ????????????????????).
    ???????? - ???????????????????????? ???? ?????????? ???????????????? ???????????? ?? ????????????.
    ?? ???????????? ????????, ?????? ???????????????? ???????????????????????? ???? ???????? ?????????????????? ???? ?????????????? (???? ???????????????????? - ???????????? ?? ????????????), ????
    ?????????? ???????????????? ???????????????????????? (?? ?????????? ????????????????????) ?????? ?????????????????????????????? ???????????????? ??????????????.
        ???????? ???? ?????????????????????? ?????????????? ???????????????? - ?????????????????? ????????:
    AtomicBoolean, AtomicInteger, ..., ?????????????? ???? ???????????????????? ???????????? ?????????????? ???????????????????? ???????????? - ????????????.
        ???????????? ???????????? - ??????????????????????????: ???????????????? ?????????????????????? ????????????????????
    ???? ?????????????????????????? (~ ?????? ???????????????????? ?????????????????? ??????????) + ???????????????? ?? ???????????????????????? ?????????????? ?????????????????? ??????????????.
    ???????????????????? .NET, ?????? ?????????????? (?????????????????? ????????) ?? Java ?????????? ?????????????? ???????????????????? ????????????.
    ! ???????? ?????? ???????? ???????????? ?????????????????? ?? ????????????-??????????, ???? ?????????????? ?????????????????????????????? ???? ?????????? ??????????????.
    ! ?????????????????????????? ???? ???? ???????????????????????? (???? ????????????????????) ??????????????
    ???????????????? ?????????? ???????????????????? ???????????? ???? ???????????? ??????????????, ?? ?????????????? ???????????????? ????????????:
        synchronized (label) { label.text = "..." }
    ????! ?????????? ???????????? ?????????? ???????????????? ?? ????????????????.
        synchronized (str) { str += "..." }
    ! str += ???????????????????? ?????????? ???????????? (?????????? ????????????)
    ?? ?????????? ???????????? ???????????? ???????????????? "????????????????" (?? ???? ?????????? ????????????????), ?? ?????????? ???? ?????????????? ???? ???????????? ??????????????.
 */
