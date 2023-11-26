package plethora.thread;

public abstract class GcRunnable implements Runnable {
    public void gc() {
        System.gc();
    }
}
