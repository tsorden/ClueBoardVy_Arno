import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.junit.*;

import junit.framework.TestCase;

import java.io.FileNotFoundException;




public class ClueBoardTests {
	private static Board board;
	public static final int NUM_ROOMS = 11;
	public static final int NUM_ROWS = 23;
	public static final int NUM_COLUMNS = 23;

	@Before
	public void setUp(){
		board = new Board("ClueLayout.csv", "Legend.txt");
		board.loadConfigFiles();
	}

	@Test
	public void testRoomCount() {
		Map<Character, String> rooms = board.getRooms();
		// Ensure we read the correct number of rooms
		assertEquals(NUM_ROOMS, rooms.size());
	}

	@Test
	public void testRoom() {
		Map<Character, String> rooms = board.getRooms();
		assertEquals("Conservatory", rooms.get('C'));
		assertEquals("Ballroom", rooms.get('B'));
		assertEquals("Billiard Room", rooms.get('R'));
		assertEquals("Dining Room", rooms.get('D'));
	}

	@Test
	public void testDimensions() {
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	@Test
	public void testDoorDirections() {
		RoomCell room = board.getRoomCellAt(6, 2);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.DOWN, room.getDoorDirection());
		room = board.getRoomCellAt(6, 7);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getRoomCellAt(0, 11);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.UP, room.getDoorDirection());
		room = board.getRoomCellAt(16, 10);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.LEFT, room.getDoorDirection());
	}

	@Test
	public void testIsNotDoor() {
		RoomCell room = board.getRoomCellAt(4, 3);
		room = board.getRoomCellAt(12, 14);
		assertFalse(room.isDoorway());
		BoardCell cell = board.getRoomCellAt(5, 0);
		assertFalse(cell.isDoorway());
	}

	@Test
	public void testRoomInitials() {
		assertEquals('C', board.getRoomCellAt(2, 22).getInitial());
		assertEquals('R', board.getRoomCellAt(2, 15).getInitial());
		assertEquals('B', board.getRoomCellAt(11, 21).getInitial());
		assertEquals('O', board.getRoomCellAt(17, 3).getInitial());
		assertEquals('K', board.getRoomCellAt(18, 20).getInitial());
	}

	@Test
	public void testCalcIndex() {		
		assertEquals(1, board.calcIndex(1, 0));
		assertEquals(24, board.calcIndex(1, 1));
		assertEquals(72, board.calcIndex(3, 3));
		assertEquals(0, board.calcIndex(0, 0));
		assertEquals(69, board.calcIndex(0, 3));
		assertEquals(3, board.calcIndex(3, 0));
		assertEquals(52, board.calcIndex(6, 2));
	}

	@Test (expected = BadConfigException.class)
	public void testBadRoom() throws Exception {
		Board b = new Board("ClueLayoutBadRoom.csv", "ClueLegend.txt");
		b.loadLegend();
		b.loadBoard();
	}

	@Test (expected = BadConfigException.class)
	public void testBadColumns() throws Exception {
		Board b = new Board("ClueLayoutBadColumns.csv", "ClueLegend.txt");
		b.loadLegend();
		b.loadBoard();
	}


	@Test (expected = BadConfigException.class)
	public void testBadFormat() throws Exception {
		Board b = new Board("ClueLayouttest.csv", "ClueLegendBadFormat.txt");
		b.loadLegend();
		b.loadBoard();
	}


	@Test
	public void testAdjacenciesInsideRooms()
	{
		// Test a corner
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(0, 0));
		Assert.assertEquals(0, testList.size());
		// Test one that has walkway underneath
		testList = board.getAdjList(board.calcIndex(0, 2));
		Assert.assertEquals(0, testList.size());
		// Test one that has walkway above
		testList = board.getAdjList(board.calcIndex(0, 5));
		Assert.assertEquals(0, testList.size());
		// Test one that is in middle of room
		testList = board.getAdjList(board.calcIndex(2, 7));
		Assert.assertEquals(0, testList.size());
		// Test one beside a door
		testList = board.getAdjList(board.calcIndex(17, 3));
		Assert.assertEquals(0, testList.size());
		// Test one in a corner of room
		testList = board.getAdjList(board.calcIndex(16, 13));
		Assert.assertEquals(0, testList.size());
	}



	@Test
	public void testRoomExit()
	{
		// Take one step, essentially just the adj list
		board.calcTargets(board.calcIndex(17, 7), 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		Assert.assertEquals(1, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 6))));
		// Take two steps
		board.calcTargets(board.calcIndex(18, 17), 2);
		targets= board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(18, 15))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(19, 16))));
	}


	@Test
	public void testAdjacencyDoorways()
	{
		// Test beside a door direction RIGHT
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(6, 14));
		Assert.assertTrue(testList.contains(board.calcIndex(5, 14)));
		Assert.assertTrue(testList.contains(board.calcIndex(6, 13)));
		Assert.assertTrue(testList.contains(board.calcIndex(6, 15)));
		Assert.assertTrue(testList.contains(board.calcIndex(7, 14)));
		Assert.assertEquals(4, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(board.calcIndex(11, 5));
		Assert.assertTrue(testList.contains(board.calcIndex(10, 5)));
		Assert.assertTrue(testList.contains(board.calcIndex(12, 5)));
		Assert.assertTrue(testList.contains(board.calcIndex(11, 4)));
		Assert.assertEquals(3, testList.size());
		// Test beside a door direction LEFT
		testList = board.getAdjList(board.calcIndex(15, 10));
		Assert.assertTrue(testList.contains(board.calcIndex(14, 10)));
		Assert.assertTrue(testList.contains(board.calcIndex(15, 11)));
		Assert.assertTrue(testList.contains(board.calcIndex(15, 9)));
		Assert.assertTrue(testList.contains(board.calcIndex(16, 10)));
		Assert.assertEquals(4, testList.size());
		// Test beside a door direction UP
		testList = board.getAdjList(board.calcIndex(11, 17));
		Assert.assertTrue(testList.contains(board.calcIndex(10, 17)));
		Assert.assertTrue(testList.contains(board.calcIndex(11, 16)));
		Assert.assertTrue(testList.contains(board.calcIndex(11, 18)));
		Assert.assertTrue(testList.contains(board.calcIndex(12, 17)));
		Assert.assertEquals(4, testList.size());
		// Test beside a door that's not the right direction
		testList = board.getAdjList(board.calcIndex(7, 2));
		Assert.assertTrue(testList.contains(board.calcIndex(7, 1)));
		Assert.assertTrue(testList.contains(board.calcIndex(7, 3)));
		Assert.assertTrue(testList.contains(board.calcIndex(8, 2)));
		// This ensures we haven't included cell (4, 3) which is a doorway
		Assert.assertEquals(3, testList.size());		
	}


	@Test
	public void testAdjacencyWalkways()
	{
		// Test on top edge of board, two walkway pieces
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(7, 0));
		Assert.assertTrue(testList.contains(8));
		Assert.assertTrue(testList.contains(30));
		Assert.assertEquals(2, testList.size());

		// Test on left edge of board, two walkway pieces
		testList = board.getAdjList(board.calcIndex(0, 3));
		Assert.assertTrue(testList.contains(board.calcIndex(0, 4)));
		Assert.assertTrue(testList.contains(board.calcIndex(1, 3)));
		Assert.assertEquals(2, testList.size());

		// Test between two rooms, walkways Right and Left
		testList = board.getAdjList(board.calcIndex(12, 5));
		Assert.assertTrue(testList.contains(board.calcIndex(11, 5)));
		Assert.assertTrue(testList.contains(board.calcIndex(13, 5)));
		Assert.assertEquals(2, testList.size());

		// Test surrounded by 4 walkways
		testList = board.getAdjList(board.calcIndex(20,5));
		Assert.assertTrue(testList.contains(board.calcIndex(21, 5)));
		Assert.assertTrue(testList.contains(board.calcIndex(19, 5)));
		Assert.assertTrue(testList.contains(board.calcIndex(20, 6)));
		Assert.assertTrue(testList.contains(board.calcIndex(20, 4)));
		Assert.assertEquals(4, testList.size());

		// Test on bottom edge of board, next to 1 room piece
		testList = board.getAdjList(board.calcIndex(6, 22));
		Assert.assertTrue(testList.contains(board.calcIndex(6, 21)));
		Assert.assertTrue(testList.contains(board.calcIndex(7, 22)));
		Assert.assertEquals(2, testList.size());

		// Test on right edge of board, next to 1 room piece
		testList = board.getAdjList(board.calcIndex(22, 15));
		Assert.assertTrue(testList.contains(board.calcIndex(22, 16)));
		Assert.assertTrue(testList.contains(board.calcIndex(21, 15)));
		Assert.assertEquals(2, testList.size());

		// Test on walkway next to  door that is not in the needed
		// direction to enter
		testList = board.getAdjList(board.calcIndex(4, 17));
		Assert.assertTrue(testList.contains(board.calcIndex(3, 17)));
		Assert.assertTrue(testList.contains(board.calcIndex(5, 17)));
		Assert.assertTrue(testList.contains(board.calcIndex(4, 16)));
		Assert.assertEquals(3, testList.size());
	}

	@Test
	public void testTargetsIntoRoomShortcut() 
	{
		board.calcTargets(board.calcIndex(18,16), 2);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(6, targets.size());
		// directly up and down(into room)
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(18, 14 ))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(18, 17))));
		// directly right and left
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16,16))));
		// right then up
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(19, 15))));
		// left then up
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 15))));		

	}

	@Test
	public void testTargetsOneStep() {
		board.calcTargets(board.calcIndex(7, 11), 1);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(6, 11))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(8, 11))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(7, 12))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(7, 10))));	

		board.calcTargets(board.calcIndex(14, 1), 1);
		targets= board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(15, 1))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(14, 0))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(14, 2))));			
	}

	@Test
	public void testTargetsThreeSteps() {
		board.calcTargets(board.calcIndex(1, 17), 3);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(5, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(4, 17))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(1, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(0, 17))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(3, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(2, 17))));

		board.calcTargets(board.calcIndex(15, 22), 3);
		targets= board.getTargets();
		Assert.assertEquals(5, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(15, 19))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(15, 21))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 20))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 22))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(14, 20))));
	}	
}