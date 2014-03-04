import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.*;

public class ConcurrentHashMapTest {

	public static void main(String[] args) throws InterruptedException {

		for (int i = 0; i < 200; i++) {
			new WriterHashMap().start();
		}
		Thread.sleep(200000);
		System.out.println(Container.count);
		
	}
}
class WriterHashMap extends Thread {
	@Override
	public void run() {
		Date date = null;
		for (int i = 0; i < 100; i++) {
			date = new Date();
			Container.chm.put(i, date);
			System.out.println(Thread.currentThread().getName() + "~~~~~~~~"
					+ i + "~~~~~~~~~");
			Container.count.incrementAndGet();
			System.out.println(Container.count);

		}
	}
}

class Container {
	public static final ConcurrentHashMap<Integer, Date> chm = new ConcurrentHashMap<Integer, Date>();
	public static  AtomicInteger count = new AtomicInteger(0);
}
