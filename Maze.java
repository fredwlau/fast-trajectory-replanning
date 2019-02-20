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
	
	public String output_maze() {
		final StringBuffer b = new StringBuffer();
		b.append("\n");
		for(int i = 0; i < max_row; i++) {
			for (int j = 0; j < max_col; j++) {
				if(this.maze[i][j].is_blocked) {
					b.append(block_char);
				}
				else if(this.maze[i][j].path) {
					b.append(path_char);
				}
				else {
					b.append("_");
				}
			}
			b.append("\n");
		}
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
	
	public static void main (String [] args) throws IOException {
		Maze m = new Maze(101, 101, 0, 0, 100, 100);
		String buffer = m.output_maze();
		BufferedWriter file = new BufferedWriter(new FileWriter(new File("output.txt")));
		file.write(buffer.toString());
		file.flush();
		file.close();
	}
	
}
