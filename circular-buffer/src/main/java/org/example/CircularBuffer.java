package org.example;


public class CircularBuffer<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private final int size;
    final T [] data;
    private int flag = 0;
    private volatile int readIndex;
    private volatile int writeIndex;
    public CircularBuffer(int size){
        this.size = (size < 10) ? DEFAULT_CAPACITY : size;
        this.data = (T []) new Object[this.size];
        this.readIndex = 0;
        this.writeIndex = -1;
    }

    public synchronized boolean putIn(T element){
        boolean bufferIsFull = (writeIndex - readIndex) + 1 == this.size;
        if(!bufferIsFull){
            int nextWriteSeq = writeIndex + 1;
            data[nextWriteSeq % size] = element;
            writeIndex++;
            return true;
        }
        return false;
    }

    public synchronized T putOut(){
        boolean bufferIsEmpty = writeIndex < readIndex;
        if(!bufferIsEmpty){
            int currentIndex = readIndex++ % size;
            T element = data[currentIndex];
            data[currentIndex] = null;
            return element;
        }
        return null;
    }
}
