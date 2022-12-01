package com.test.read.track;

public class TrackLocation {

    public static void main(String[] args) {
        ThreadLocation T1 = new ThreadLocation( "Thread-1");
        T1.start();

        ThreadLocation T2 = new ThreadLocation( "Thread-2");
        T2.start();

        ThreadLocation T3 = new ThreadLocation( "Thread-3");
        T3.start();
        ThreadLocation T4 = new ThreadLocation( "Thread-4");
        T4.start();
        ThreadLocation T5 = new ThreadLocation( "Thread-5");
        T5.start();
        ThreadLocation T6 = new ThreadLocation( "Thread-6");
        T6.start();
        ThreadLocation T7 = new ThreadLocation( "Thread-7");
        T7.start();
        ThreadLocation T8 = new ThreadLocation( "Thread-8");
        T8.start();
        ThreadLocation T9 = new ThreadLocation( "Thread-9");
        T9.start();
        ThreadLocation T10 = new ThreadLocation( "Thread-10");
        T10.start();
        ThreadLocation T11 = new ThreadLocation( "Thread-11");
        T11.start();
    }

}
