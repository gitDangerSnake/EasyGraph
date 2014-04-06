package cn.hnu.eg.util;

import java.util.EventListener;

public interface SignalListener  extends EventListener{
	public void onEvent(SignalEvent se);
}
