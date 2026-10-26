import java.util.LinkedList;

/**
 * Producer-Consumer problem using inter-thread communication
 * (wait() and notify()) to synchronize access to a shared buffer.
 */
class PC {

    // Step 1: Shared buffer with fixed capacity
    private final LinkedList<Integer> list = new LinkedList<>();
    private final int capacity = 2;

    // Step 2: Producer method
    public void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            synchronized (this) {
                // Wait while the buffer is full
                // (while-loop guards against spurious wakeups)
                while (list.size() == capacity) {
                    wait();
                }

                System.out.println("Producer produced-" + value);
                list.add(value++);

                // Wake up the consumer
                notify();

                // Sleep for 1 second
                Thread.sleep(1000);
            }
        }
    }

    // Step 3: Consumer method
    public void consume() throws InterruptedException {
        while (true) {
            synchronized (this) {
                // Wait while the buffer is empty
                while (list.isEmpty()) {
                    wait();
                }

                int val = list.removeFirst();
                System.out.println("Consumer consumed-" + val);

                // Wake up the producer
                notify();

                // Sleep for 1 second
                Thread.sleep(1000);
            }
        }
    }
}

public class ProducerConsumerExample {

    public static void main(String[] args) throws InterruptedException {

        // Step 4: Create an instance of PC
        final PC pc = new PC();

        // Producer thread
        Thread producerThread = new Thread(() -> {
            try {
                pc.produce();
            } catch (InterruptedException e) {
                System.out.println("Producer interrupted.");
                Thread.currentThread().interrupt();
            }
        });

        // Consumer thread
        Thread consumerThread = new Thread(() -> {
            try {
                pc.consume();
            } catch (InterruptedException e) {
                System.out.println("Consumer interrupted.");
                Thread.currentThread().interrupt();
            }
        });

        // Start both threads
        producerThread.start();
        consumerThread.start();

        // Join both threads
        producerThread.join();
        consumerThread.join();
    }
}

/*
 * ---------------------------------------------------------------
 * COMPILE & RUN
 * ---------------------------------------------------------------
 * javac ProducerConsumerExample.java
 * java ProducerConsumerExample
 *
 * (The program runs indefinitely. Press Ctrl + C to stop it.)
 *
 * ---------------------------------------------------------------
 * SAMPLE OUTPUT
 * ---------------------------------------------------------------
 * Producer produced-0
 * Producer produced-1
 * Consumer consumed-0
 * Consumer consumed-1
 * Producer produced-2
 * Producer produced-3
 * Consumer consumed-2
 * Consumer consumed-3
 * ...
 */
