import java.util.Collections;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RepeatedAStar extends Maze implements Serializable {
	
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
	
	//main method for creating and storing the mazes
	
	/*public static void main(String[] args) throws IOException {
		boolean path;
		
		RepeatedAStar new_search = new RepeatedAStar(101, 101, 0, 0, 100, 100);

			path = new_search.forward_backward(0, 0);
			//forward
			new_search.print_path(new_search.best_path);
			if(path) {
				//successful maze with path from 0,0 to 100,100
				//save the maze
				try {
					FileOutputStream file_out = new FileOutputStream("./maze50.ser");
					ObjectOutputStream object_out = new ObjectOutputStream(file_out);
					object_out.writeObject(new_search);
					object_out.close();
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
				String buffer = new_search.output_maze();
				BufferedWriter file = new BufferedWriter(new FileWriter(new File("repeated.txt")));
				file.write(buffer.toString());
				file.flush();
				file.close();
				
				new_search.clear_path();
				new_search.forward_backward(1, 2);
				new_search.print_path(new_search.best_path);
				
				try {
					FileInputStream file_in = new FileInputStream("maze50.ser");
					ObjectInputStream object_in = new ObjectInputStream(file_in);
					RepeatedAStar m = (RepeatedAStar) object_in.readObject();
					System.out.print(m.output_maze());
					object_in.close();
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			
		}
	}*/
	
	//main method for running pathfinding on the mazes
	
	public static void main(String [] args) {
		
		try {
			FileInputStream file_in = new FileInputStream("maze1.ser");
			ObjectInputStream object_in = new ObjectInputStream(file_in);
			RepeatedAStar m = (RepeatedAStar) object_in.readObject();
			m.clear_path();
			boolean path = m.forward_backward(0, 0);
			if(path) {
				String buffer = m.output_maze();
				BufferedWriter file = new BufferedWriter(new FileWriter(new File("maze1-forward-path.txt")));
				file.write(buffer.toString());
				file.flush();
				file.close();
				
				
				m.clear_path();
				m.forward_backward(1, 2);
				
				buffer = m.output_maze();
				file = new BufferedWriter(new FileWriter(new File("maze1-backward-path.txt")));
				file.write(buffer.toString());
				file.flush();
				file.close();
			}
			object_in.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
