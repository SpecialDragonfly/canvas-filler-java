package orme.dominic.canvasfiller.dto;

import orme.dominic.canvasfiller.generator.GeneratorInterface;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Canvas {
    private final int width;
    private final int height;
    private final Queue<Point> queue;
    private final GeneratorInterface generator;
    private int pointsRemaining;
    private ArrayList<Point> buffer;

    public Canvas(GeneratorInterface generator, int width, int height) {
        this.width = width;
        this.height = height;
        this.queue = new ConcurrentLinkedQueue<>();
        this.pointsRemaining = width * height;
        this.generator = generator;
    }

    public Queue<Point> getQueue() {
        return queue;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public synchronized void decrementPoint() {
        this.pointsRemaining--;
    }

    public int getRemainingPointCount() {
        return this.pointsRemaining;
    }

    public void start() {
        this.generator.start(this);
    }

    public ArrayList<Point> getBuffer() {
        return this.buffer;
    }

    public void clearBuffer() {
        this.buffer = new ArrayList<>();
    }

    public void setBuffer(ArrayList<Point> saved) {
        this.buffer = saved;
    }

    public ArrayList<Point> getPointsFromQueue() {
        int currentNumberOfPoints = this.queue.size();
        // Attempt to get 10 points unless we have many in the queue, then attempt to get at least a heights worth.
        int quantity = 10;
        if (currentNumberOfPoints > 50) {
            // 5000 points is roughly 125000 bytes in size
            //quantity = Math.min(currentNumberOfPoints / 2, 5000);
            quantity = 5000;
        }

        // Whether we've finished or not, get values from the queue.
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            try {
                points.add(this.queue.remove());
            } catch (NoSuchElementException | NullPointerException e) {
                break;
            }
        }

        this.setBuffer(points);

        return points;
    }

    public Point getPoint(int row, int column) {
        return this.generator.getPoint(row, column);
    }
}
