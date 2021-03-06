package me.weitao.java.concurrent;

import lombok.extern.slf4j.Slf4j;


import java.util.UUID;
import java.util.concurrent.*;

/**
 * 常量
 *
 * @author Watony Weng
 * @date 2019/09/13
 */

class Constants {

    /**
     * 缓冲区大小
     */
    static final int MAX_BUFFER_SIZE = 10;

    /**
     * 生产者数量
     */
    static final int NUM_OF_PRODUCER = 2;

    /**
     * 消费者数量
     */
    static final int NUM_OF_CONSUMER = 3;

}

/**
 * 任务
 *
 * @author Watony Weng
 * @date 2018/12/03
 */

class Task {

    /**
     * 任务编号
     */
    private String id;

    public Task() {
        id = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Task[" + id + "]";
    }

}

/**
 * 消费者
 *
 * @author Watony Weng
 * @date 2018/12/03
 */

@Slf4j
class Consumer implements Runnable {

    private BlockingQueue<Task> buffer;

    public Consumer(BlockingQueue<Task> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        do {
            try {
                Task task = buffer.take();
                log.info("Consumer[{}] get {}",
                        Thread.currentThread().getName(), task);
            } catch (InterruptedException e) {
                log.error(e.getLocalizedMessage());
                Thread.currentThread().interrupt();
            }
        } while (true);
    }
}

/**
 * 生产者
 *
 * @author Watony Weng
 * @date 2018/12/03
 */

@Slf4j
class Producer implements Runnable {

    private BlockingQueue<Task> buffer;

    public Producer(BlockingQueue<Task> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        do {
            try {
                Task task = new Task();
                buffer.put(task);
                log.info("Producer[{}] put {}",
                        Thread.currentThread().getName(), task);
            } catch (InterruptedException e) {
                log.error(e.getLocalizedMessage());
                Thread.currentThread().interrupt();
            }
        } while (true);
    }

}

/**
 * 阻塞队列
 *
 * @author Watony Weng
 * @date 2018/12/03
 */

public class BlockingQueueApp {

    public static void main(String[] args) {
        BlockingQueue<Task> buffer = new LinkedBlockingQueue<>(Constants.MAX_BUFFER_SIZE);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(
                Constants.NUM_OF_CONSUMER + Constants.NUM_OF_PRODUCER);
        for (int i = 1; i <= Constants.NUM_OF_PRODUCER; ++i) {
            scheduledExecutorService.execute(new Producer(buffer));
        }
        for (int i = 1; i <= Constants.NUM_OF_CONSUMER; ++i) {
            scheduledExecutorService.execute(new Consumer(buffer));
        }
    }

}
