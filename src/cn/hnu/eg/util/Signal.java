package cn.hnu.eg.util;
/**
 * Signal作为Master和slave 之间的简单的消息交换 
 * master 用signal控制slave在超级步的同步进行
 * slave们用signal向master 投票，告诉master当前超级步已完成，可以开始下一个超级步
 * 
 * 
 * */
public enum Signal{
	red, // vertex is active
	dark,// vertex halt
	green; // vertex is available in current super step
	
};