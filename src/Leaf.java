public class Leaf {
	Object node_p;
	int key;
	Object item;

	public Leaf() {
		this.node_p = null;
		this.key = 0;
		this.item = null;
	}

	public Leaf(int key) {
		this.key = key;
		this.node_p = null;
		this.item = null;
	}

	public Leaf(int key, Object Item) {
		this.key = key;
		this.item = Item;
		this.node_p = null;
	}

	public Leaf(int key, Object Item, Object p) {
		this.key = key;
		this.item = Item;
		this.node_p = p;
	}

	public int getKey() {
		return this.key;
	}

	public Object getItem() {
		return item;
	}
}