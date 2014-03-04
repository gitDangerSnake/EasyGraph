public class Trunk {
	Object node_p;
	int bit;
	boolean lcolor, rcolor;
	Object lchild, rchild;
	Leaf leaf;

	public Trunk(int key, Object item) {
		leaf = new Leaf(key, item);
	}

	public int getKey() {
		return leaf.getKey();
	}

	public boolean approved(int key) {
		int min = getKey() & (~(0xffffffff >>> (31 - bit)));
		int max = getKey() | (0x7fffffff >>> (31 - bit));
		return (key >= min && key <= max) ? true : false;
	}
}