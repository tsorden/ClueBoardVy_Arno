
public class RoomCell extends BoardCell{

	
	public enum DoorDirection {UP, DOWN, LEFT, RIGHT, NONE};
	private DoorDirection doorDirection;
	private char roomInitial;
	
	public RoomCell(int pcolumn, int prow, DoorDirection pdirection, char initial) {
		super(pcolumn, prow);
		room = true;
		roomInitial = initial;
		doorDirection = pdirection;
		
		if(pdirection != DoorDirection.NONE){
			direction = true;
		}
	}
	
	
	public DoorDirection getDoorDirection(){
		return doorDirection;
	}
	
	public char getInitial(){
		return roomInitial;
	}	
	
	
	@Override
	public String toString() {
		String string = roomInitial + " (" + column + ", " + row + ")";
		return string;
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

}
