package com.by.lizhiyoupin.app.common;

import java.util.LinkedList;

/**
 * 循环队列
 * 注意：1，size < maxSize 直接插入到队列尾
 *       2，size == maxSize 插入元素到队尾，移除队列头元素
 * @param <T>
 */
@SuppressWarnings("ALL")
public class CycQueue<T> {
    private int MAXSIZE = 6;
    private LinkedList<T> array;

    public CycQueue(int maxLength) {
        MAXSIZE = maxLength;
        array = new LinkedList<>();
    }

    public CycQueue clearQueue() {
        array.clear();
        return this;
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    public boolean isFull() {
        return length() >= MAXSIZE;
    }

    //求环形队列的元素个数
    public int length() {
        return array.size();
    }

    /**
     * 队列头元素
     * @return Object
     */
    public T head() {
        if (!array.isEmpty()) {
            return array.getFirst();
        }
        return null;
    }

    /**
     * 队列尾元素
     * @return T
     */
    public T tail() {
        if (!array.isEmpty()) {
            return array.getLast();
        }
        return null;
    }

    /**
     * 入队
     * @param element 入队元素
     * @return True / False
     */
    public synchronized void add(T element) {
        //队列头指针在队尾指针的下一位位置上  说明满了
        array.addLast(element);
        if (array.size() > MAXSIZE) {
            array.removeFirst();
        }
    }

    /**
     * 出队前判空
     * @return  出队 返回出队前的对列头元素
     */
    public synchronized T poll() {
        if (isEmpty()) {
            return null;
        }
        return array.pollLast();
    }

    /**
     * 出队前判空
     * @return 查询 返回对列头元素
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return array.peekLast();
    }
    /**
     * 是否包含元素
     * @param element
     * @return
     */
    public boolean contain(T element) {
        return array.contains(element);
    }

    /**
     * 移除中间元素
     * @param element T
     * @return boolean
     */
    public synchronized boolean remove(T element) {
        if (isEmpty()) {
            return false;
        }
        return array.remove(element);
    }
}
