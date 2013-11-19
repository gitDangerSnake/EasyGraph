package cn.hnu.eg.util.test;

import java.io.IOException;

import cn.hnu.eg.util.*;
import cn.hnu.eg.sys.*;

public class MessageTest {
	public static void main(String[] args) throws IOException{
		System.out.println(Utils.size(new InferiorMessage(State.ACTIVE,20)));
	}
}
