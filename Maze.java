
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class Maze {
	
	public static final char path_char = ' ';
	public static final char block_char = '▓';
	
	Node maze [][];
	
	ArrayList<Node> opened_list;
	Stack<Node> closed_list;
	Stack<Node> best_path;
	
	Node current, start, end;
	
	boolean adaptive, repeat, is_path;
	
	int max_row = 101;
	int max_col = 101;
	int num_expansions;
	
	public Maze(int row, int col, int start_row,
				int start_col, int goal_row, int goal_col) {
		
		this.maze = new Node[row][col];
		
		create_maze();
		
		start = this.maze[start_row][start_col];
		end = this.maze[goal_row][goal_col];
		
		calc_heuristic();
		
		opened_list = new ArrayList<Node>();
		closed_list = new Stack<Node> ();
		best_path = new Stack<Node>();
		
		start.is_blocked = false;
		end.is_blocked = false;
		this.current = start;
		num_expansions = 0;
		is_path = false;
		
	}
	
	//getters
	
	public boolean has_left(Node o) {
		if(o.col > 0 && !this.maze[o.row][o.col-1].is_blocked) {
			return true;
		}
		
		return false;
	}
	
	public boolean has_right(Node o) {
		if(o.col < max_row && !this.maze[o.row][o.col+1].is_blocked) {
			return true;
		}
		
		return false;
	}
	
	public boolean has_up(Node o) {
		if(o.row > 0 && !this.maze[o.row-1][o.col].is_blocked) {
			return true;
		}
		
		return false;
	}
	
	public boolean has_down(Node o) {
		if(o.row > max_col && !this.maze[o.row+1][o.col].is_blocked) {
			return true;
		}
		
		return false;
	}
	
	public Node get_left(Node o) {
		Node temp = this.maze[o.row][o.col-1];
		return temp;
	}
	
	public Node get_right(Node o) {
		Node temp = this.maze[o.row][o.col+1];
		return temp;
	}
	
	public Node get_up(Node o) {
		Node temp = this.maze[o.row-1][o.col];
		return temp;
	}
	
	public Node get_down(Node o) {
		Node temp = this.maze[o.row+1][o.col];
		return temp;
	}
	
	//helper functions
	public void calc_heuristic() {
		for(int i = 0; i < max_row; i++) {
			for(int j = 0; j< max_col; j++) {
				this.maze[i][j].
				h_val = Math.abs(end.row - i) +
						Math.abs(end.col - j);
			}
		}
	}
	
	private void clear_list() {
		opened_list.clear();
		while (!closed_list.isEmpty()) {
			closed_list.pop();
		}
	}
	
	public boolean forward_backward(int dir, int priority) {
		boolean res = false;
		num_expansions = 0;
		
		if(dir == 0) {
			current = start;
			calc_heuristic();
			res = repeat_a_star(0, priority);
			clear_list();
			
			return res;
		}
		
		else {
			Node t = end;
			end = start;
			start = t;
			calc_heuristic();
			current = start;
			
			res = repeat_a_star(0, priority);
			clear_list();
			
			t = end;
			end = start;
			start = t;
			
			return res;
		}
	}
	
	private void check_list(Node t) {
		if(!closed_list.contains(t)) {
			t.set_g_val(current.get_g_val()+1);
			update_open_list(t);
		}
	}
	
	//recursive
	public boolean repeat_a_star(int increment, int priority) {
		num_expansions = increment;
		this.maze[current.row][current.col].f_val = current.f_val;
		this.maze[current.row][current.col].h_val = current.h_val;
		this.maze[current.row][current.col].g_val = current.g_val;
		closed_list.push(current);
		
		if(end.equals(current)) {
			is_path = true;
			return is_path;
		}
		
		if(has_left(current)) {
			Node t = get_left(current);
			check_list(t);
		}
		
		if(has_right(current)) {
			Node t = get_right(current);
			check_list(t);
		}
		
		if(has_up(current)) {
			Node t = get_up(current);
			check_list(t);
		}
		
		if(has_down(current)) {
			Node t = get_down(current);
			check_list(t);
		}
		
		if(opened_list.size() < 1) {
			return is_path;
		}
		
		//tie breaking logic goes here
		//we have to sort opened_list...?
		
		current = opened_list.get(0);
		opened_list.remove(0);
		
		repeat_a_star(increment+1, priority);
		
		if(current != null) {
			this.maze[current.row][current.col].path = true;
			best_path.push(current);
			current = current.get_parent();
		}
		
		return is_path;
		
	}
	
	public void adaptiveAStar(){
		//Previously called A* and saved list of nodes that have been expanded into opened_list

		ArrayList<Node> adaptedList = opened_list;

		for(int i=0;i<adaptedList.size();i++){
	
			//recalculate the heuristics for the nodes in the list of expanded nodes
			//the new heuristic will be (the distance from the start state to the goal) - (distance from start state to s)

			int newH = Math.abs(end.row - start.row) + Math.abs(end.col - start.row);	//distance of start state to goal
			newH -= Math.abs(start.row-adaptedList.get(i).row) + Math.abs(start.col-adaptedList.get(i).col); //minus distance of start state to s
			
			expandedNodes.get(i).h_val = newH;
		}

		//We now run an A* search on this adapted list
		//INSERT CODE HERE -- still working on this
	}
	
	//Possible Jframe usage here to make the maze look better
	public String output_maze() {
		final StringBuffer b = new StringBuffer();
		for(int i = 0; i < max_col + 2; i++) {
			b.append(block_char);
		}
		b.append('\n');
		for(int i = 0; i < max_row; i++) {
			b.append(block_char);
			for (int j = 0; j < max_col; j++) {
				if(this.maze[i][j].is_blocked) {
					b.append(block_char);
				}
				else {
					b.append(path_char);
				}
			}
			b.append(block_char);
			b.append("\n");
		}
		for(int i = 0; i < max_col + 2; i++) {
			b.append(block_char);
		}
		b.append('\n');
		return b.toString();
	}
	
	public void create_maze() {
		int h_val = 0;
		for(int i = 0; i < max_row; i++) {
			for(int j = 0; j< max_col; j++) {
				this.maze[i][j] = new Node(i, j, h_val, block());
			}
		}
	}
	
	public boolean block() {
		if(Math.random() * 10 < 3) {
			return true;
		}
		return false;
	}
	
	public void update_open_list(Node o) {
		if(opened_list
				.get(opened_list.indexOf(o))
				.get_f_val() > o.get_f_val()) {
			opened_list.remove(opened_list.indexOf(o));
			o.set_parent(current);
			this.maze[o.row][o.col]
					.set_parent
					(this.maze[current.row][current.col]);
			opened_list.add(o);
		}
		else {
			o.set_parent(current);
			this.maze[o.row][o.col]
					.set_parent
					(this.maze[current.row][current.col]);
			opened_list.add(o);
		}
	}
	
	public void print_best_path() {
		int count = 0;
		while(!best_path.isEmpty()) {
			System.out.println(best_path.pop());
			count++;
		}
	}
	
	/*public static void main (String [] args) throws IOException {
		Maze m = new Maze(101, 101, 0, 0, 100, 100);
		String buffer = m.output_maze();
		BufferedWriter file = new BufferedWriter(new FileWriter(new File("output.txt")));
		file.write(buffer.toString());
		file.flush();
		file.close();
	}*/
	
}
