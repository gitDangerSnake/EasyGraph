import kilim.EventPublisher;
import kilim.PauseReason;
import kilim.Task;


public class NMailbox<T> implements PauseReason , EventPublisher {

	@Override
	public boolean isValid(Task t) {
		synchronized (this) {
			return (t == sink) || srcs.contains(t);
		}
	}

}
