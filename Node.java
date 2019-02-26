import java.util.Comparator;

public class Node implements Comparable<Node>{
	public Node parent;
	public int g_val, h_val, f_val, row, col;
	public boolean is_blocked, path, visited;

	
	public Node(int row, int col, int h_val, boolean is_blocked) {
		this.parent = null;
		this.row = row;
		this.col = col;
		this.h_val = h_val;
		this.g_val = 0;
		this.f_val = 0;
		this.is_blocked = is_blocked;
		this.visited = false;
		this.path = false;
	}
	
	//getters and setters

	public void set_parent(Node parent) {
		this.parent = parent;
	}
	
	public Node get_parent() {
		return this.parent;
	}
	
	public int calc_h_val(int max_row, int max_col) {
		return (max_row - this.row) + (max_col-this.col);
	}
	
	public void set_g_val(int new_g) {
		this.g_val = new_g;
		this.f_val = this.g_val + this.h_val;
	}
	
	public int get_g_val() {
		return this.g_val;
	}
	
	public int get_h_val() {
		return this.h_val;
	}
	
	public int get_f_val() {
		return this.g_val + this.h_val;
	}
	
	public void did_visit() {
		this.visited = true;
	}
	
	public boolean visited_state() {
		return this.visited;
	}
	
	static final Comparator<Node> small = new Comparator<Node>() {
		@Override
		public int compare(Node one, Node two) {
			if(one.f_val == two.f_val) {
				return one.g_val - two.g_val;
			}
			return one.f_val - two.f_val;
		}
	};
	
	static final Comparator<Node> big = new Comparator<Node>(){
		@Override
		public int compare(Node one, Node two) {
			if(one.f_val == two.f_val) {
				return two.g_val - one.g_val;
			}
			return one.f_val - two.f_val;
		}		
	};
	
	public int compareTo(Node o) {
		if(this.f_val == o.f_val) {
			return this.g_val - o.g_val;
		}
		return this.f_val - o.f_val;
	}
}
