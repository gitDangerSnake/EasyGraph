package cn.hnu.eg.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import kilim.Pausable;

import cn.hnu.eg.Exceptions.NoEntriesExistException;
import cn.hnu.eg.base.BaseVertex;
import cn.hnu.eg.sys.Message;

public class Chunk<K, V> implements Cache<K, V> {

	private String name;
	private final HashMap<K, V> cache;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();

	private Chunk(String name) {
		this.name = name;
		cache = new HashMap<K, V>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public V get(K key) {
		readLock.lock();
		try {
			if (cache != null)
				return cache.get(key);
		} finally {
			readLock.unlock();
		}
		return null;
	}

	@Override
	public Map<? extends K, ? extends V> getAll(Iterator<? extends K> keys) {
		readLock.lock();
		try {
			HashMap<K, V> map = new HashMap<K, V>();
			ArrayList<K> noEntryKeys = new ArrayList<K>();

			while (keys.hasNext()) {
				K key = keys.next();
				if (isPresent(key)) {
					map.put(key, cache.get(key));
				} else {
					noEntryKeys.add(key);
				}
			}

			if (!noEntryKeys.isEmpty()) {
				// do something
			}
			return map;
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public boolean isPresent(K key) {
		readLock.lock();
		try {
			return cache.containsKey(key);
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public void put(K key, V value) {

		writeLock.lock();
		try {
			cache.put(key, value);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> entries) {
		writeLock.lock();
		try {
			cache.putAll(entries);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public void invalidate(K key) {

		writeLock.lock();
		try {
			if (!isPresent(key)) {
				// do something
				throw new NoEntriesExistException("No Entry exist for " + key);
			}
			cache.remove(key);
		} catch (NoEntriesExistException e) {
			e.printStackTrace();
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public void invalidateAll(Iterator<? extends K> keys) {
		writeLock.lock();
		try {
			while (keys.hasNext()) {
				K key = keys.next();
				if (!isPresent(key)) {
					// do something
					continue;
				}
				cache.remove(key);
			}
		} finally {
			writeLock.unlock();
		}

	}

	@Override
	public void invalidateAll() {

		writeLock.lock();
		try {
			cache.clear();
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		readLock.lock();
		try {
			return cache.isEmpty();
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public int size() {
		readLock.lock();
		try {
			return cache.size();

		} finally {
			readLock.unlock();
		}
	}

	@Override
	public void clear() {
		writeLock.lock();
		try {
			cache.clear();
		} finally {
			writeLock.unlock();
		}

	}

	@Override
	public Map<? extends K, ? extends V> asMap() {
		readLock.lock();
		try {
			return new ConcurrentHashMap<K, V>(cache);
		} finally {
			readLock.unlock();
		}
	}

	public void send(Message msg, int d_id) throws Pausable {
		BaseVertex v = CacheHolder.cache.get(d_id);
		v.getMailbox().put(msg);
	}

	private static class CacheHolder {
		public final static Chunk<Integer, BaseVertex> cache = new Chunk<Integer, BaseVertex>(
				"SingleCache");
	}

	public static Chunk<Integer, BaseVertex> getChunk() {
		return CacheHolder.cache;
	}

}
