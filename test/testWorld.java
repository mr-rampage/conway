import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;


public class testWorld {

	private LifeState game;
	
	@Before
	public void setup() {
	}
	
	@Test
	public void testCreateWorld() {
		game = new LifeState(4, 4);
		boolean[][] world = game.getWorld();
		assertEquals(world.length, 4);
		assertEquals(world[0].length, 4);
	}
	
	@Test
	public void testCopyWorld() {
		boolean[][] original = new boolean[5][3];
		original[2][2] = true;
		game = new LifeState(original);
		boolean[][] copy = game.getWorld();
		assertEquals(copy.length, original.length);
		assertEquals(copy[0].length, original[0].length);
		original[0][0] = true;
		assertFalse(copy[0][0] != original[0][0]);
		assertTrue(copy[2][2] == original[2][2]);
	}

	@Test
	public void testAllFalseOnDefault() {
		game = new LifeState(2,2);
		boolean[][] world = game.getWorld();
		for(boolean[] row : world) {
			for(boolean cell: row) {
				assertFalse(cell);
			}
		}
	}
	
	@Test
	public void testSetCell() {
		game = new LifeState(2,2);
		boolean[][] world = game.getWorld();
		assertFalse(world[0][0]);
		game.setCell(0, 0, true);
		assertTrue(world[0][0]);
		game.setCell(0, 0, false);
		assertFalse(world[0][0]);
	}
	
	@Test
	public void testCountThreeLivingNeighbours() {
		game = new LifeState(2, 2);
		game.setCell(1, 1, true);
		game.setCell(0, 0, true);
		game.setCell(0, 1, true);
		game.setCell(1, 0, true);
		assertEquals(game.countLivingNeighbours.apply(new Point(0, 0)), new Integer(3));
	}
	
	@Test
	public void testCountTwoLivingNeighbours() {
		game = new LifeState(2, 2);
		game.setCell(1, 1, true);
		game.setCell(0, 0, true);
		game.setCell(0, 1, true);
		assertEquals(game.countLivingNeighbours.apply(new Point(0, 0)), new Integer(2));
	}
	
	@Test
	public void testCountOneLivingNeighbours() {
		game = new LifeState(2, 2);
		game.setCell(1, 1, true);
		game.setCell(0, 0, true);
		assertEquals(game.countLivingNeighbours.apply(new Point(0, 0)), new Integer(1));
	}
	
	@Test
	public void testNextLifeWithOneNeighbour() {
		game = new LifeState(10, 10);
		game.setCell(1, 1, true);
		game.setCell(0, 0, true);
		game.setCell(3, 3, true);
		LifeState nextGeneration = game.nextTick();
		boolean[][] world = nextGeneration.getWorld();
		for(boolean[] row : world) {
			for(boolean cell: row) {
				assertFalse(cell);
			}
		}
	}
	
	@Test
	public void testNextLifeWithTwoNeighbours() {
		game = new LifeState(4, 8);
		game.setCell(1, 5, true);
		game.setCell(2, 4, true);
		game.setCell(2, 5, true);
		LifeState nextGeneration = game.nextTick();
		boolean[][] world = nextGeneration.getWorld();
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[i].length; j++) {
				boolean cell = world[i][j];
				if ( ((i == 1) && (j == 4 )) || 
						((i == 1) && (j == 5 )) ||
						((i == 2) && (j == 4 )) ||
						((i == 2) && (j == 5 ))) {
					assertTrue(cell);
				} else {
					assertFalse(cell);
				}
			}
		}
	}
	
	@Test
	public void testCompareToSelf() {
		game = new LifeState(3,3);
		game.setCell(0, 0, true);
		assertTrue(game.compareTo(game));
	}
	
	@Test
	public void testCompareToCopy() {
		game = new LifeState(3,3);
		game.setCell(0, 0, true);
		LifeState copy = new LifeState(game.getWorld());
		assertTrue(game.compareTo(copy));
	}
	
	@Test
	public void testCompareToDifferentDimensions() {
		game = new LifeState(3,3);
		LifeState game2 = new LifeState(4,5);
		assertFalse(game.compareTo(game2));
	}
	
	@Test
	public void testCompareToDifferentWorldState() {
		game = new LifeState(3,3);
		LifeState game2 = new LifeState(3,3);
		game2.setCell(2, 1, true);
		assertFalse(game.compareTo(game2));
	}
	
	@Test
	public void testCrossPattern() {
		game = new LifeState(5,5);
		game.setCell(2,1, true);
		game.setCell(2,2, true);
		game.setCell(2,3, true);
		LifeState gen1 = game.nextTick();
		LifeState gen2 = gen1.nextTick();
		assertFalse(game.compareTo(gen1));
		assertTrue(game.compareTo(gen2));
	}
	
}
