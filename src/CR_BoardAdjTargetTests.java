
import java.util.LinkedList;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class CR_BoardAdjTargetTests {
	private static Board board;
	@BeforeClass
	public static void setUp() {
		board = new Board("ClueLayouttest.csv", "ClueLegend.txt");
		board.loadConfigFiles();

	}

	// Ensure that player does not move around within room
	// These cells are ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesInsideRooms()
	{
		// Test a corner
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(0, 0));
		Assert.assertEquals(0, testList.size());
		// Test one that has walkway underneath
		testList = board.getAdjList(board.calcIndex(0, 4));
		Assert.assertEquals(0, testList.size());
		// Test one that has walkway above
		testList = board.getAdjList(board.calcIndex(20, 15));
		Assert.assertEquals(0, testList.size());
		// Test one that is in middle of room
		testList = board.getAdjList(board.calcIndex(11, 18));
		Assert.assertEquals(0, testList.size());
		// Test one beside a door
		testList = board.getAdjList(board.calcIndex(12, 14));
		Assert.assertEquals(0, testList.size());
		// Test one in a corner of room
		testList = board.getAdjList(board.calcIndex(20, 5));
		Assert.assertEquals(0, testList.size());
	}

	// Ensure that the adjacency list from a doorway is only the
	// walkway. NOTE: This test could be merged with door 
	// direction test. 
	// These tests are PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyRoomExit()
	{
		// TEST DOORWAY RIGHT 
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(6, 11));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(7, 11)));
		// TEST DOORWAY LEFT 
		testList = board.getAdjList(board.calcIndex(17, 10));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(16, 10)));
		//TEST DOORWAY DOWN
		testList = board.getAdjList(board.calcIndex(15, 5));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(15, 6)));
		//TEST DOORWAY UP
		testList = board.getAdjList(board.calcIndex(15, 5));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(15, 6)));
		
	}
	
	// Test adjacency at entrance to rooms
	// These tests are GREEN in planning spreadsheet
	@Test
	public void testAdjacencyDoorways()
	{
		// Test beside a door direction RIGHT
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(4, 4));
		Assert.assertTrue(testList.contains(board.calcIndex(3, 4)));
		Assert.assertTrue(testList.contains(board.calcIndex(5, 4)));
		Assert.assertTrue(testList.contains(board.calcIndex(4, 5)));
		Assert.assertEquals(3, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(board.calcIndex(15, 6));
		Assert.assertTrue(testList.contains(board.calcIndex(15, 5)));
		Assert.assertTrue(testList.contains(board.calcIndex(14, 6)));
		Assert.assertTrue(testList.contains(board.calcIndex(16, 6)));
		Assert.assertEquals(3, testList.size());
		// Test beside a door direction LEFT
		testList = board.getAdjList(board.calcIndex(17, 15));
		Assert.assertTrue(testList.contains(board.calcIndex(16, 15)));
		Assert.assertTrue(testList.contains(board.calcIndex(18, 15)));
		Assert.assertTrue(testList.contains(board.calcIndex(17, 14)));
		Assert.assertTrue(testList.contains(board.calcIndex(17, 16)));
		Assert.assertEquals(4, testList.size());
		// Test beside a door direction UP
		testList = board.getAdjList(board.calcIndex(11, 13));
		Assert.assertTrue(testList.contains(board.calcIndex(10, 13)));
		Assert.assertTrue(testList.contains(board.calcIndex(12, 13)));
		Assert.assertTrue(testList.contains(board.calcIndex(11, 12)));
		Assert.assertTrue(testList.contains(board.calcIndex(11, 14)));
		Assert.assertEquals(4, testList.size());
		// Test beside a door that's not the right direction
		testList = board.getAdjList(board.calcIndex(3, 5));
		Assert.assertTrue(testList.contains(board.calcIndex(2, 5)));
		Assert.assertTrue(testList.contains(board.calcIndex(4, 5)));
		Assert.assertTrue(testList.contains(board.calcIndex(3, 6)));
		// This ensures we haven't included cell (4, 3) which is a doorway
		Assert.assertEquals(3, testList.size());		
	}

	// Test a variety of walkway scenarios
	// These tests are LIGHT PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on top edge of board, just one walkway piece
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(4, 0));
		Assert.assertTrue(testList.contains(5));
		Assert.assertEquals(1, testList.size());
		
		// Test on left edge of board, three walkway pieces
		testList = board.getAdjList(board.calcIndex(0, 6));
		Assert.assertTrue(testList.contains(board.calcIndex(0, 5)));
		Assert.assertTrue(testList.contains(board.calcIndex(1, 6)));
		Assert.assertTrue(testList.contains(board.calcIndex(0, 7)));
		Assert.assertEquals(3, testList.size());

		// Test between two rooms, walkways right and left
		testList = board.getAdjList(board.calcIndex(21, 6));
		Assert.assertTrue(testList.contains(board.calcIndex(20, 6)));
		Assert.assertTrue(testList.contains(board.calcIndex(22, 6)));
		Assert.assertEquals(2, testList.size());

		// Test surrounded by 4 walkways
		testList = board.getAdjList(board.calcIndex(7,15));
		Assert.assertTrue(testList.contains(board.calcIndex(8, 15)));
		Assert.assertTrue(testList.contains(board.calcIndex(6, 15)));
		Assert.assertTrue(testList.contains(board.calcIndex(7, 14)));
		Assert.assertTrue(testList.contains(board.calcIndex(7, 16)));
		Assert.assertEquals(4, testList.size());
		
		// Test on bottom edge of board, next to 1 room piece
		testList = board.getAdjList(board.calcIndex(15, 21));
		Assert.assertTrue(testList.contains(board.calcIndex(16, 21)));
		Assert.assertTrue(testList.contains(board.calcIndex(15, 20)));
		Assert.assertEquals(2, testList.size());
		
		// Test on right edge of board, next to 1 room piece
		testList = board.getAdjList(board.calcIndex(22, 14));
		Assert.assertTrue(testList.contains(board.calcIndex(21, 14)));
		Assert.assertTrue(testList.contains(board.calcIndex(22, 13)));
		Assert.assertEquals(2, testList.size());

		// Test on walkway next to  door that is not in the needed
		// direction to enter
		testList = board.getAdjList(board.calcIndex(3, 5));
		Assert.assertTrue(testList.contains(board.calcIndex(2, 5)));
		Assert.assertTrue(testList.contains(board.calcIndex(4, 5)));
		Assert.assertTrue(testList.contains(board.calcIndex(3, 6)));
		Assert.assertEquals(3, testList.size());
	}
	
	// Tests of just walkways, 1 step, includes on edge of board
	// and beside room
	// Have already tested adjacency lists on all four edges, will
	// only test two edges here
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsOneStep() {
		board.calcTargets(board.calcIndex(7, 21), 1);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(7, 20))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(6, 21))));	
		
		board.calcTargets(board.calcIndex(0, 14), 1);
		targets= board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(1, 14))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(0, 13))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(0, 15))));			
	}
	// Tests of just walkways, 2 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsTwoSteps() {
		board.calcTargets(board.calcIndex(7, 21), 2);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(7, 19))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(6, 20))));
		
		board.calcTargets(board.calcIndex(0, 14), 2);
		targets= board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(0, 12))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(2, 14))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(1, 15))));			
	}
	// Tests of just walkways, 4 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsFourSteps() {
		board.calcTargets(board.calcIndex(7, 21), 4);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(7, 17))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(7, 19))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(6, 18))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(6, 20))));
		
		// Includes a path that doesn't have enough length
		board.calcTargets(board.calcIndex(0, 14), 4);
		targets= board.getTargets();
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(4, 14))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(3, 15))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(2, 14))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(1, 15))));	
	}	
	// Tests of just walkways plus one door, 6 steps
	// These are LIGHT BLUE on the planning spreadsheet

	@Test
	public void testTargetsSixSteps() {
		board.calcTargets(board.calcIndex(0, 14), 6);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(7, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(6, 14))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(5, 15))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(3, 15))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(4, 14))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(1, 15))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(2, 14))));	
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(4, 13))));	
	}	
	
	// Test getting into a room
	// These are LIGHT BLUE on the planning spreadsheet

	@Test 
	public void testTargetsIntoRoom()
	{
		// One room is exactly 2 away
		board.calcTargets(board.calcIndex(16, 17), 2);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(7, targets.size());
		// directly left (can't go right 2 steps)
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(14, 17))));
		// directly up and down
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 15))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 19))));
		// one up/down, one left/right
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 18))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(15, 18))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(15, 16))));
	}
	
	// Test getting into room, doesn't require all steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsIntoRoomShortcut() 
	{
		board.calcTargets(board.calcIndex(7, 12), 3);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(12, targets.size());
		// directly up and down
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(7, 15))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(7, 9))));
		// directly right (can't go left)
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(10, 12))));
		// right then down
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(9, 13))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(7, 13))));
		// down then left/right
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(6, 14))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(8, 14))));
		// right then up
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(8, 10))));
		// into the rooms
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(6, 11))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(6, 10))));		
		// 
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(7, 11))));		
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(8, 12))));		
		
	}

	// Test getting out of a room
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testRoomExit()
	{
		// Take one step, essentially just the adj list
		board.calcTargets(board.calcIndex(20, 4), 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		Assert.assertEquals(1, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(19, 4))));
		// Take two steps
		board.calcTargets(board.calcIndex(20, 4), 2);
		targets= board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(19, 3))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(19, 5))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(18, 4))));
	}

}
