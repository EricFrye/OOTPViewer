package misc;

public class ArrayReader {
	private String [] op;
	private int index;
	
	public ArrayReader (String [] op) {
		this.op = op;
		this.index = 0;
	}
	
	public String read () {
		
		if (index >= this.op.length) {
			throw new IndexOutOfBoundsException();
		}
		
		else {
			return op[this.index++];
		}
		
	}
	
	public boolean moreOpToRead () {
		return this.index < this.op.length;
	}

}
