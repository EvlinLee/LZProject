package com.by.lizhiyoupin.app.manager.dhandler;


import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ThreadUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

/**
 * 生产消费模型
 */
@SuppressWarnings("ALL")
public class SimpleTagger<T extends SimpleTagger.SignalTagger> {

    // 顺序执行
    public static final int MODE_SEQUENCE = 1;
    //优先级模式
    public static final int MODE_PRIORITY = 2;

    // 存储全局参数，作为消费者沟通的渠道
    private HashMap<String, Object> globalParams = new HashMap<>();

    // 消费者队列
    private ConcurrentLinkedQueue<Consumer> consumers = new ConcurrentLinkedQueue();

    // 信号管理队列
    private SignalLooper signalLooper = new SignalLooper();

    // 设置队列暂停
    private boolean pause;

    /**
     * 轮询规则
     * {@link #MODE_PRIORITY} 优先级规则，高优先级的SignalTagger被优先处理，{@link SignalTagger#level} 值越小，优先级越高
     * {@link #MODE_SEQUENCE} 顺序轮询规则，按照插入队列序执行
     */
    private int looperMode = MODE_SEQUENCE;

    /**
     * 添加一个消费者
     * @param consumer
     * @return 是否已添加
     */
    @CallSuper
    public boolean addConsumer(Consumer consumer) {
        return !consumers.contains(consumer) ? consumers.add(consumer) : false;
    }

    public void setLooperDuring(long during) {
        signalLooper.TIMEDURINGLOOP = during;
    }

    public void setLooperMode(int looperMode) {
        this.looperMode = looperMode;
    }

    /**
     * 移除一个消费者
     */
    @CallSuper
    public boolean removeConsumer(Consumer consumer) {
        return consumers.remove(consumer);
    }

    public void pause(boolean pause) {
        this.pause = pause;
    }

    public boolean isPause() {
        return pause;
    }

    /**
     * 产生发送一个事件
     * @param tagger
     */
    @CallSuper
    public void tagger(T tagger) {
        signalLooper.inQueue(tagger);
    }

    /**
     * 添加一个全局参数
     * @param key
     * @param params
     */
    public void addGlobalParams(String key, Object params) {
        globalParams.put(key, params);
    }

    /**
     * 移除一个全局参数
     * @param key
     */
    public void remove(String key) {
        globalParams.remove(key);
    }

    public Object global(String key) {
        return globalParams.get(key);
    }

    /**
     * 销毁对象
     */
    public void destory() {
        signalLooper.stop();
        consumers.clear();
    }

    /**
     * 为某个信号寻找消费者
     * @param signalTagger
     * @return
     */
    private final synchronized boolean findConsumer(T signalTagger) {
        Iterator<Consumer> it = consumers.iterator();
        while (it.hasNext()) {
            Consumer consumer = it.next();
            // 已被处理
            if (consumer.consumer(signalTagger, globalParams)) {
                LZLog.d("SimpleTagger", " ==================>>>> consumer " + signalTagger.toString());
                signalTagger.onConsumered();
                // 主线程执行
                if (consumer.executeMainThread) {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            consumer.executeTask(signalTagger, globalParams);
                        }
                    });
                } else {
                    consumer.executeTask(signalTagger, globalParams);
                }
                // 是否保存信号继续传递
                if (consumer.keepSignalLive) {
                    continue;
                } else {
                    return true;
                }

            }
        }
        return false;
    }

    protected T nextSignalTagger(LinkedBlockingQueue<T> waitingProcess, ConcurrentHashMap<Long, T> inProcess, int looperMode) {
        // 顺序执行模式
        if (looperMode == MODE_SEQUENCE) {
            return waitingProcess.poll();
        }
        // 优先级模型
        else if (looperMode == MODE_PRIORITY){
            T tagger = waitingProcess.peek();
            // 0 为最高优先级
            if (tagger != null) {
                if (tagger.level != 0) {
                    Iterator<T> it=waitingProcess.iterator();
                    while (it.hasNext()) {
                        T next=it.next();
                        if (next.level < tagger.level) {
                            tagger=next;
                        }
                    }
                }
                waitingProcess.remove(tagger);
            }
            return tagger;
        }
        // 预处理，增加其他的模型
        else {
            return waitingProcess.poll();
        }
    }

    /**
     * 信号轮询处理队列
     */
    private final class SignalLooper {

        // 正在被处理的信号队列
        private ConcurrentHashMap<Long, T> inProcess = new ConcurrentHashMap<>();

        // 等待处理的信号队列
        private LinkedBlockingQueue<T> waitingProcess = new LinkedBlockingQueue<>();

        // 最大并发数
        private int MAXTHREAD = 1;

        // 并发等待时间
        private long TIMEDURING = 1000;

        // 轮询间隔时间，每次轮询完，等待后进入下一个轮询
        private long TIMEDURINGLOOP = 1000;

        /**
         * 此锁用于任务主Looper在将task从waitingProcess中取出并放入inProcess中时导致其他线程判断执行任务数量错误的问题
         */
        private final Object lockForTaskChanged = new Object();

        private LooperHandler mLooperHandler;

        public void inQueue(T signal) {
            if (!waitingProcess.contains(signal)) {
                waitingProcess.add(signal);
                startLooper();
            }
        }

        /**
         * 启动线程looper
         */
        private synchronized void startLooper() {
            if (mLooperHandler == null || mLooperHandler.isInterrupted() || !mLooperHandler.isAlive()) {
                mLooperHandler = new LooperHandler();
                LZLog.d("SimpleTagger", " ===============>>>>>>>> startLooper ");
            }

            if (!mLooperHandler.isAlive()) {
                mLooperHandler.start();
            }
        }

        private synchronized void stop() {
            try {
                if (mLooperHandler != null) {
                    mLooperHandler.interrupt();
                }
                inProcess.clear();
                waitingProcess.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 消费的 主线程,循环Lopper
         */
        private class LooperHandler extends Thread {
            @Override
            public void run() {
                // 检测等待队列中的消息是否已经执行完成
                System.out.println("*************************************** mainLooper start *************************");
                while (waitingProcess.size() > 0) {
                    // 等待正在执行的队列中的消息执行
                    while (inProcess.size() >= MAXTHREAD || isPause()) {
                        try {
                            sleep(TIMEDURING);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    synchronized (lockForTaskChanged) {
                        T signalTagger = nextSignalTagger(waitingProcess, inProcess, looperMode);
                        if(signalTagger != null){
                            long signalId = signalTagger.getSignalID();
                            inProcess.put(signalId, signalTagger);
                            boolean handle = findConsumer(signalTagger);
                            if (handle) {
                                inProcess.remove(signalId);
                            } else if (signalTagger.keepLooper()){
                                inProcess.remove(signalId);
                                waitingProcess.add(signalTagger);
                            } else {
                                inProcess.remove(signalId);
                            }
                        }
                    }

                    // 任务执行完成，等待
                    try {
                        sleep(TIMEDURINGLOOP);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

                // 当所有的任务都轮询完，结束线程
                System.out.println("*************************************** mainLooper interrupt *************************");
            }
        }
    }

    /**
     * 信号触发模型，用于生产事件
     */
    public static class SignalTagger {

        // 全局Thread_id计数器
        private static volatile int CURRENT_SIGNAL_ID = 0;

        // 唯一标识符
        private long signal_id;

        // 被消费次数
        private int consumeredTimes;

        // 是否强制消费
        private boolean forceConsume;

        // 强制消费最大被轮询次数，避免极端情况出现线程卡死现象
        private int forceConsumeLooperTimes = 100;

        private long looperTimes;

        public int what;

        public Object obj;

        public int level = 100;

        public SignalTagger() {
            this(0);
        }

        public SignalTagger(int what) {
            this(what, null);
        }

        public SignalTagger(int what, Object obj) {
            signal_id = ++CURRENT_SIGNAL_ID;
            this.what = what;
            this.obj = obj;
        }

        public SignalTagger forceConsume(boolean force) {
            return forceConsume(force, forceConsumeLooperTimes);
        }

        /**
         * 是否强制消费，强制消费的信号，如果在轮询后，未有消费者处理，将会再次放入等待队列
         *  等待队列轮询超过 {@link #forceConsumeLooperTimes} 次，会被移除
         * @param force
         * @param looperTimes
         * @return
         */
        public SignalTagger forceConsume(boolean force, int looperTimes) {
            forceConsume = force;
            forceConsumeLooperTimes = looperTimes;
            return this;
        }

        public SignalTagger level(int level) {
            this.level = level;
            return this;
        }

        @CallSuper
        public void onConsumered() {
            consumeredTimes++;
        }

        public final long getSignalID() {
            return signal_id;
        }

        public int consumeredTimes() {
            return consumeredTimes;
        }

        protected final boolean keepLooper() {
            looperTimes++;
            return forceConsume && looperTimes < forceConsumeLooperTimes;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            return obj instanceof SignalTagger && ((SignalTagger) obj).getSignalID() == signal_id;
        }

        @Override
        public String toString() {
            return "signal_id " + signal_id + " what = " + what + " obj = " + obj;
        }
    }

    /**
     * 消费者
     */
    public static abstract class Consumer<T extends SignalTagger> {

        // 消费后，依然保存此信号，不拦截
        private boolean keepSignalLive;
        //是否在主线程执行
        private boolean executeMainThread = true;

        public Consumer<T> setKeepSignalLive(boolean keep) {
            this.keepSignalLive = keep;
            return this;
        }

        public Consumer<T> executeMainThread(boolean inMainThread) {
            this.executeMainThread = inMainThread;
            return this;
        }

        /**
         * 是否消费某个信号
         * @param signal
         * @return TRUE 此信号被消费掉，将不再继续轮询消费者； FALSE 此消费者未消费此信号，将继续向下寻找消费者
         */
        protected abstract boolean consumer(T signal, HashMap<String, Object> params);

        /**
         * 执行具体操作
         * @param signal
         * @param params
         */
        public abstract void executeTask(T signal, HashMap<String, Object> params);
    }
}
