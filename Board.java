import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


//Authors: Arnaud Filliat and Vy Ta
//Make sure to note that for the cells we use (x,y) coordinates so (column, row)
public class Board {

	public static void main(String [ ] args) {

	}

	private ArrayList<BoardCell> cells;
	private int numRows;
	private int numColumns;
	private Map<Character, String> rooms;
	private LinkedList<Integer> adjList;
	private boolean [] visited;
	private Set<BoardCell> targets;
	private String LegendFile;
	private String BoardFile;

	public Board(String file1, String file2) {
		// TODO Auto-generated constructor stub
		LegendFile = file2;
		BoardFile = file1;
	}


	public Board() {
		// TODO Auto-generated constructor stub
	}


	public void loadConfigFiles() {
		try {
			loadLegend();
			loadBoard();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadBoard() throws BadConfigException, FileNotFoundException {
		FileReader reader = new FileReader(BoardFile);
		Scanner in = new Scanner(reader);
		cells = new ArrayList<BoardCell>();
		numRows = 0;
		numColumns = -1;

		while(in.hasNextLine()){
			String line = in.nextLine();
			String[] line2 = line.split(",");

			//check columns
			if(line2.length != numColumns && numColumns != -1){
				throw new BadConfigException();
			}
			numColumns = line2.length;

			//add celldata to arraylist
			for(int i = 0; i < line2.length; i++){

				//adds doors
				if(line2[i].length() == 2){
					if(line2[i].charAt(1) == 'U'){
						cells.add(new RoomCell(i, numRows, RoomCell.DoorDirection.UP, line2[i].charAt(0)));
					}else if(line2[i].charAt(1) == 'D'){
						cells.add(new RoomCell(i, numRows, RoomCell.DoorDirection.DOWN, line2[i].charAt(0)));
					}else if(line2[i].charAt(1) == 'R'){
						cells.add(new RoomCell(i, numRows, RoomCell.DoorDirection.RIGHT, line2[i].charAt(0)));
					}else if(line2[i].charAt(1) == 'L'){
						cells.add(new RoomCell(i, numRows, RoomCell.DoorDirection.LEFT, line2[i].charAt(0)));
					} else {
						throw new BadConfigException();
					}
				} else{
					//add walkway
					if( line2[i].equalsIgnoreCase("w")){
						cells.add(new WalkwayCell(i, numRows));
						//add other rooms
					} else if(rooms.containsKey((Character)line2[i].charAt(0))){
						cells.add(new RoomCell(i, numRows, RoomCell.DoorDirection.NONE, line2[i].charAt(0)));
					} else {
						throw new BadConfigException();
					}
				}
			}
			numRows++;
		}
	}


	public void loadLegend() throws FileNotFoundException, BadConfigException {
		FileReader reader = new FileReader(LegendFile);
		Scanner in = new Scanner(reader);
		rooms = new HashMap<Character, String>();

		while(in.hasNextLine()){
			String line = in.nextLine();
			String line2 = line.substring(3);

			//check format
			if(line.charAt(1) == ',' && !line2.contains(",")) {
				rooms.put(line.charAt(0), line.substring(3));
			} else {
				throw new BadConfigException();
			}
		}
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns(){
		return numColumns;
	}

	public RoomCell getRoomCellAt(int column, int row){
		int index = calcIndex(column, row);
		if(cells.get(index).isRoom()){
			return (RoomCell) cells.get(index);
		} else {
			return null;
		}
	}

	public BoardCell getCellAt(int index){
		BoardCell cell = cells.get(index);
		return cell;
	}
	public int calcIndex(int col, int row) {
		return col + row*(numColumns);
	}

	public int getColumn(int index){
		return index % numColumns;
	}

	public int getRow(int index){
		return index/numColumns;
	}

	public Map<Character, String> getRooms() {
		return rooms;
	}

	public LinkedList<Integer> calcAdjacencies(int index) {
		int column = getColumn(index);
		int row = getRow(index);
		int [] index1 = {-1, -1, -1, -1};
		LinkedList<Integer> list = new LinkedList<Integer>();
		
		//return empty list if inside room and not at doorway
		if(cells.get(calcIndex(column, row)).isRoom() && !cells.get(calcIndex(column, row)).isDoorway())
			return list;

		//set to -1 if we don't want the side to be added to list
		//checks that it is not past the max row/column and that it is either a walkway or doorway.
		//Also checks direction if it is a doorway.

		//checks to the left
		int testcolumn = column-1;
		if(testcolumn >= 0 && (cells.get(calcIndex(testcolumn, row)).isWalkway() || cells.get(calcIndex(testcolumn, row)).isDoorway())){
			RoomCell room = getRoomCellAt(testcolumn, row);
			if(room == null || room.getDoorDirection() == RoomCell.DoorDirection.RIGHT)
				index1[0] = calcIndex(testcolumn, row);
		}
		else {
			index1[0] = -1;
		}

		//checks to the right
		testcolumn = column + 1;
		if(testcolumn < numColumns  && (cells.get(calcIndex(testcolumn, row)).isWalkway() || cells.get(calcIndex(testcolumn, row)).isDoorway())){
			RoomCell room = getRoomCellAt(testcolumn, row);
			if(room == null || room.getDoorDirection() == RoomCell.DoorDirection.LEFT)
				index1[1] = calcIndex(testcolumn, row);
		} else {
			index1[1] = -1;
		}

		//checks up
		int testrow = row - 1;
		if(testrow >= 0  && (cells.get(calcIndex(column, testrow)).isWalkway() || cells.get(calcIndex(column, testrow)).isDoorway())){
			RoomCell room = getRoomCellAt(column, testrow);
			if(room == null || room.getDoorDirection() == RoomCell.DoorDirection.DOWN)
				index1[2] = calcIndex(column, testrow);
		} else {
			index1[2] = -1;
		}


		//checks down
		testrow = row + 1;
		if(testrow < numRows  && (cells.get(calcIndex(column, testrow)).isWalkway() || cells.get(calcIndex(column, testrow)).isDoorway())) {
			RoomCell room = getRoomCellAt(column, testrow);
			if(room == null || room.getDoorDirection() == RoomCell.DoorDirection.UP)
				index1[3] = calcIndex(column, testrow);
		} else {
			index1[3] = -1;
		}


		for(int i = 0; i < 4; i++){
			if(index1[i] >= 0 && index1[i] <= (numRows + numRows*numColumns-1)){
				list.add(index1[i]);
			}
		}

		return list;
	}


	public LinkedList<Integer> getAdjList(int index) {
		adjList = new LinkedList<Integer>();
		adjList = calcAdjacencies(index);
		return adjList;
	}

	public void fillTargets(int index, int steps){
		LinkedList<Integer> list = new LinkedList<Integer>();
		list = calcAdjacencies(index);

		visited[index] = true;

		System.out.println("column: " + getColumn(index) + " row: " + getRow(index) + " steps: " + steps + " index: " + index);

		for(Integer number:list){
			if(visited[number]){
				System.out.println("removed column: " + getColumn(number) + " row: " + getRow(number));
			} else{
				visited[number] = true;
				System.out.println("column: " + getColumn(number) + " row: " + getRow(number) + " steps: " + steps + " number: " + number);
				if(steps == 1 || cells.get(number).isDoorway()){
					System.out.println("added");
					targets.add(cells.get(number));
				} else {
					fillTargets(number, --steps);
					steps++;
				}
				visited[number] = false;
			}
		}
	}

	public void calcTargets(int index, int steps) {
		visited = new boolean [10000];
		targets = new HashSet<BoardCell>();
		fillTargets(index, steps);
	}


	public Set<BoardCell> getTargets() {
		return targets;
	}

}
