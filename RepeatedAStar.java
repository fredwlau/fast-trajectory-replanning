import java.util.Collections;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class RepeatedAStar extends Maze {

	public RepeatedAStar(int row, int col, int start_row, int start_col, int goal_row, int goal_col) {
		super(row, col, start_row, start_col, goal_row, goal_col);
		// TODO Auto-generated constructor stub
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
	
	public boolean repeat_a_star(int increment, int priority) {
		set_values(increment);
		
		if(isPath()) {
			return true;
		}
		
		manipulate_maze(current);
		
		if(opened_list.size() < 1) {
			return is_path;
		}
		
		if(priority == 1) {
			Collections.sort(opened_list, Node.small);
		}
		else {
			Collections.sort(opened_list, Node.big);
		}
		
		current = opened_list.get(0);
		opened_list.remove(0);
		
		repeat_a_star(increment+1, priority);
		
		set_path();
		
		return is_path;	
	}
	
	//potential static issue here
	public void main(String[] args) throws IOException {
		boolean path;
		int num = 0;
		Maze m;
		while(num < 50) {
			m = new Maze(101, 101, 0, 0, 100, 100);
			//forward
			path = forward_backward(0, 2);
			if(path) {
				num++;
				m.clear_path();
				forward_backward(1, 2);
			}
			String buffer = m.output_maze();
			BufferedWriter file = new BufferedWriter(new FileWriter(new File("repeated.txt")));
			file.write(buffer.toString());
			file.flush();
			file.close();
		}
	}
}
