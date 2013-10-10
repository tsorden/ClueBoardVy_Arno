
public class WalkwayCell extends BoardCell {

	public WalkwayCell(int pcolumn, int prow) {
		super(pcolumn, prow);
		walkway = true;
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		String string = "W (" + column + ", " + row + ")";
		return string;
	}

}
