package com.gogirl.gogirl_user.util;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  * 分段锁，系统提供一定数量的原始锁，根据传入用户id值获取对应的锁并加锁  * 注意：要锁的用户id值如果发生改变，有可能导致锁无法成功释放!!!
 */
public  class BalanceLock {
	private Integer segments = 10;// 默认分段数量
	private final static HashMap<Integer, ReentrantLock> lockMap = new HashMap<>();

	/*静态内部类实现单例*/
    private static class SingletonHolder{
        private static final BalanceLock instance = new BalanceLock(null,true);
    }
    public static final BalanceLock getInsatance(){
        return SingletonHolder.instance;
    }
	/*静态内部类实现单例*/
	
    private  BalanceLock() {
		init(null, false);
	}

    private  BalanceLock(Integer counts, boolean fair) {
		init(counts, fair);
	}

	private void init(Integer counts, boolean fair) {
		if (counts != null) {
			segments = counts;
		}
		for (int i = 0; i < segments; i++) {
			lockMap.put(i, new ReentrantLock(fair));
		}
	}

	public void lock(int key) {
		ReentrantLock lock = lockMap.get(key % segments);
		lock.lock();
	}

	public void unlock(int key) {
		ReentrantLock lock = lockMap.get(key % segments);
		lock.unlock();
	}

	@Override
	public String toString() {
		return "SegmentLock [segments=" + segments + ", lockMap=" + lockMap
				+ "]";
	}

}