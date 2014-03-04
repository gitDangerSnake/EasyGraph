
public class ebtree {
	
	/**
	 * if the ebnode's left child or right child is ebnode or a leaf in the ebnode
	 * */
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	
	/**
	 *on left branch or right branch of the parent
	 * */
	private static final boolean LEFT = true;
	private static final boolean RIGHT = false;
	
	/**
	 * if the ebtree support repeat keys
	 * */
	private static final boolean NOTSUPPORTED = false;
	private static final boolean SUPPORTED = true;

	/***************************************************************
	 * define ebnode *
	 * *************************************************************
	 * */
	
	

	static class ebroot implements ebnode{
		ebnode ltree;		
		boolean rtree;
		
		public ebroot(){
			ltree = null;			
			rtree = NOTSUPPORTED;
		}
	}
	// //////////////////End of ebnode/////////////////////////////////////////

	/**
	 * ***********************count bit****************************************
	 * */
	public int countBit(int a, int b) {
		int tmp = a ^ b;
		int count = 0;
		while ((tmp = tmp >> 1) != 1) count++;
		return count;
	}

	public int relativeBit(int a, int bit) {
		// return ((1<<bit) & a) == 0 ? 0 : 1;
		return (a >> bit) & 1;
	}
	/************************* end of count bit **********************************/

	/************************* define a ebtree here ******************************/
	private ebroot root;
	private int size;
	
	public ebtree(){
		root = new ebroot();
		size = 0;
	}
	/*********************** end of define ***************************************/
	
	/*********************** is the tree empty ***********************************/
	public boolean isEmpty(){
		return size == 0;
	}
	/*********************** is the tree allow duplicate keys *********************/
	public boolean isSupportDuplicate(){
		return root.rtree;
	}
	
	/*********************** add an item into the ebtree *************************/
	public void add(int key,Object item){
		if(isEmpty()){
			root.ltree = new Leaf(key,item,root);
			size++;
		}else if(size == 1){			
			Leaf aleaf = (Leaf)root.ltree;
			int bit = countBit(key,aleaf.key);
			Trunk newnode = new Trunk(key,item);
			
			if(relativeBit(aleaf.key, bit)==0){
				newnode.lchild = aleaf;				
				newnode.rchild = newnode.leaf;				
			}else{
				newnode.lchild = newnode.leaf;				
				newnode.rchild = aleaf;				
			}
			aleaf.node_p = newnode;
			newnode.leaf.node_p = newnode;
			root.ltree = newnode;
			newnode.bit = bit;
			newnode.node_p = root;
		}else{
			root.ltree = add(key,item,root.ltree);
		}
		
		
		
	}
	
	
	public ebnode add(int key,Object item,Object branch){
		if(branch instanceof Trunk){
			Trunk atrunk = (Trunk)branch;
			if(atrunk.approved(key)){
				int gotoleftoright = relativeBit(key, atrunk.bit);
				if(gotoleftoright == 0){
					atrunk.lchild = add(key,item,atrunk.lchild);					
				}else{
					atrunk.rchild = add(key,item,atrunk.rchild);
				}
			}else{
				Trunk newtrunk = new Trunk(key,item);
				newtrunk.node_p = atrunk.node_p;
				
				int bit = countBit(key,atrunk.getKey());
				int gotoleftorright = relativeBit(key, bit);
				if(gotoleftorright == 0){
					newtrunk.lchild = newtrunk.leaf;
				}
				return newtrunk;
			}
			
		}else{
			Leaf aleaf = (Leaf)branch;
			int bit = countBit(key,aleaf.key);			
			Trunk atrunk = new Trunk(key,item);
			atrunk.node_p = aleaf.node_p;
			atrunk.bit = bit;
			int gotoleftorright = relativeBit(key, bit);
			if(gotoleftorright == 0){
				atrunk.lchild = atrunk.leaf;
				atrunk.rchild = aleaf;
			}else{
				atrunk.lchild = aleaf;
				atrunk.rchild = atrunk.leaf;
			}
			aleaf.node_p = atrunk;
			atrunk.leaf.node_p = atrunk;
			
			return atrunk;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
