import java.util.Collections;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class AdaptiveAStar extends Maze {

	public AdaptiveAStar(int row, int col, int start_row, int start_col, int goal_row, int goal_col) {
		super(row, col, start_row, start_col, goal_row, goal_col);
		// TODO Auto-generated constructor stub
	}
	
	public boolean adaptive(int start_row, int start_col) {
		boolean res = false;
		num_expansions = 0;
		
		current = maze[start_row][start_col];
		if(!adaptive) {
			calc_heuristic();
			adaptive = true;
		}
		else {
			repeat = true;
		}
		
		res = adaptive_a_star(0);
		opened_list.clear();
		
		if(res) {
			Node t;
			while(!closed_list.isEmpty()) {
				t = closed_list.pop();
				//int new_h = this.maze[t.row][t.col].h_val;
				this.maze[t.row][t.col].h_val
					= this.end.get_g_val()
					-t.get_g_val();
			}
		}
		return res;	
	}
	
	public boolean adaptive_a_star(int increment) {
		set_values(increment);
		
		if(isPath()) {
			return true;
		}
		
		manipulate_maze(current);
		
		if(opened_list.size() < 1) {
			return is_path;
		}
		
		Collections.sort(opened_list, Node.big);
		
		current = opened_list.get(0);
		opened_list.remove(0);
		
		adaptive_a_star(increment+1);
		
		set_path();
		
		return is_path;
		
	}
	
	public static void main(String[] args) throws IOException {
		//Maze m;
		boolean path;
		AdaptiveAStar newSearch;

		newSearch = new AdaptiveAStar(101, 101, 0, 0, 100, 100);
		path = newSearch.adaptive(0, 0);
			
		if(path) {
			newSearch.clear_path();
			newSearch.adaptive(40, 35);	
		}
		
		String buffer = newSearch.output_maze();
		BufferedWriter file = new BufferedWriter(new FileWriter(new File("adaptive.txt")));
		file.write(buffer.toString());
		file.flush();
		file.close();
	}

}
