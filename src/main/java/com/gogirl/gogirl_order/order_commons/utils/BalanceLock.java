package com.gogirl.gogirl_order.order_commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  * 分段锁，系统提供一定数量的原始锁，根据传入用户id值获取对应的锁并加锁  * 注意：要锁的用户id值如果发生改变，有可能导致锁无法成功释放!!!
 */
public  class BalanceLock {
	private String startTime = "07:00:00"; //开始时间
	private String endTime = "23:00:00"; //结束时间
	private final static HashMap<String, ReentrantLock> lockMap = new HashMap<>();

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
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		try {
			Date startDateTime = simpleDateFormat.parse(startTime);
			Date endDateTime = simpleDateFormat.parse(endTime);
			Calendar calendar = Calendar.getInstance();
			while(startDateTime.before(endDateTime)){
				calendar.setTime(startDateTime);
				calendar.add(Calendar.MINUTE, 15);
				startTime = simpleDateFormat.format(calendar.getTime());
                lockMap.put(startTime, new ReentrantLock(fair));
                startDateTime = simpleDateFormat.parse(startTime);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void lock(String key) {
		ReentrantLock lock = lockMap.get(key);
		lock.lock();
	}

	public void unlock(String key) {
		ReentrantLock lock = lockMap.get(key);
		lock.unlock();
	}

	@Override
	public String toString() {
		return "BalanceLock{" +
				"startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				'}';
	}
}