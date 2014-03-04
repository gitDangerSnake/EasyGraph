package cn.hnu.eg.Exceptions;

import java.util.LinkedList;

import kilim.EventSubscriber;
import kilim.Mailbox;

public class MessageBuffer<T> extends Mailbox<T> {

	T[] buffer; // 消息数组
	private int iprod = 0; // 生产者 id
	private int icons = 0; // 消费者 id
	private int numOfMsgs = 0;
	private final int maxSize = 1024; // 允许的最大的消息数量

	// 事件订阅者
	LinkedList<EventSubscriber> srcs = new LinkedList<EventSubscriber>();
	private EventSubscriber sink;

	/*
	 * 默认构造函数，默认消息数组初始化1024 在图计算开始的第一个超级步会有大量的消息 给每个消息缓存合适的大小，可以有效的减少，数组扩容的次数
	 */
	public MessageBuffer() {
		this(256);
	}

	/*
	 * @initialSize : 默认的初始化参数 1024 每个消息缓存的最大容量为 1024*1024 @maxSize
	 */
	public MessageBuffer(int initialSize) {
		if (initialSize > maxSize)
			throw new IllegalArgumentException("initialSize: " + initialSize
					+ " cannot exceed maxSize: " + maxSize);
		buffer = (T[]) new Object[initialSize];
	}

	/*
	 * 非阻塞get
	 * 
	 * @param eo . 如果非空
	 */
	@Override
	public T get(EventSubscriber eo) {
		T msg;
		EventSubscriber producer = null;
		synchronized (this) {
			int n = numOfMsgs;
			if (n > 0) {

				/*
				 * 消費者和生产者
				 */
				int ic = icons;
				msg = buffer[ic];
				buffer[ic] = null;
				icons = (ic + 1) % buffer.length;
				numOfMsgs = n - 1;
				if (srcs.size() > 0) {
					producer = srcs.poll();
				}
			} else {
				msg = null;
				// TODO：等待消息生产者
			}
		}

		if (producer != null) {
			producer.onEvent(this, spaceAvailble);
		}

		return msg;
	}

	/**
	 * 
	 * */
	 
	@Override
	public boolean put(T msg, EventSubscriber eo) {
		boolean ret = true;
		EventSubscriber subscriber;
		synchronized (this) {
			if (msg == null) {
				throw new NullPointerException("Null message supplied to put");
			}
			int ip = iprod;
			int ic = icons;
			int n = numOfMsgs;
			if (n == buffer.length) {
				assert ic == ip : "numElements == msgs.length && ic != ip";
				if (n < maxSize) {
					@SuppressWarnings("unchecked")
					T[] newBuffer = (T[]) new Object[Math.min(n * 2, maxSize)];
					System.arraycopy(buffer, ic, newBuffer, 0, n - ic);
					if (ic > 0) {
						System.arraycopy(buffer, 0, newBuffer, n - ic, ic);
					}
					buffer = null;
					buffer = newBuffer;
					ip = n;
					ic = 0;
				} else {
					ret = false;
				}				
			}
			if (ret) {
				numOfMsgs = n + 1;
				buffer[ip] = msg;
				iprod = (ip + 1) % buffer.length;
				icons = ic;
				subscriber = sink;
				sink = null;
			} else {
				subscriber = null;
				if (eo != null) {
					srcs.add(eo);
				}
			}
		}
		if (subscriber != null) {
			subscriber.onEvent(this, messageAvailable);
		}
		return ret;
	}

}
