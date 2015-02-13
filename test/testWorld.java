import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;


public class testWorld {

	private Life game;
	
	@Before
	public void setup() {
	}
	
	@Test
	public void testCreateWorld() {
		game = new Life(4, 4);
		boolean[][] world = game.getWorld();
		assertEquals(world.length, 4);
		assertEquals(world[0].length, 4);
	}
	
	@Test
	public void testCopyWorld() {
		boolean[][] original = new boolean[5][3];
		original[2][2] = true;
		game = new Life(original);
		boolean[][] copy = game.getWorld();
		assertEquals(copy.length, original.length);
		assertEquals(copy[0].length, original[0].length);
		original[0][0] = true;
		assertFalse(copy[0][0] != original[0][0]);
		assertTrue(copy[2][2] == original[2][2]);
	}

	@Test
	public void testAllFalseOnDefault() {
		game = new Life(2,2);
		boolean[][] world = game.getWorld();
		for(boolean[] row : world) {
			for(boolean cell: row) {
				assertFalse(cell);
			}
		}
	}
	
	@Test
	public void testSetCell() {
		game = new Life(2,2);
		boolean[][] world = game.getWorld();
		assertFalse(world[0][0]);
		game.setCell(0, 0, true);
		assertTrue(world[0][0]);
		game.setCell(0, 0, false);
		assertFalse(world[0][0]);
	}
	
	@Test
	public void testCountThreeLivingNeighbours() {
		game = new Life(2, 2);
		game.setCell(1, 1, true);
		game.setCell(0, 0, true);
		game.setCell(0, 1, true);
		game.setCell(1, 0, true);
		assertEquals(game.countLivingNeighbours.apply(new Point(0, 0)), new Integer(3));
	}
	
	@Test
	public void testCountTwoLivingNeighbours() {
		game = new Life(2, 2);
		game.setCell(1, 1, true);
		game.setCell(0, 0, true);
		game.setCell(0, 1, true);
		assertEquals(game.countLivingNeighbours.apply(new Point(0, 0)), new Integer(2));
	}
	
	@Test
	public void testCountOneLivingNeighbours() {
		game = new Life(2, 2);
		game.setCell(1, 1, true);
		game.setCell(0, 0, true);
		assertEquals(game.countLivingNeighbours.apply(new Point(0, 0)), new Integer(1));
	}
	
	@Test
	public void testNextLifeWithOneNeighbour() {
		game = new Life(10, 10);
		game.setCell(1, 1, true);
		game.setCell(0, 0, true);
		game.setCell(3, 3, true);
		Life nextGeneration = game.nextLife();
		boolean[][] world = nextGeneration.getWorld();
		for(boolean[] row : world) {
			for(boolean cell: row) {
				assertFalse(cell);
			}
		}
	}
	
	@Test
	public void testNextLifeWithTwoNeighbours() {
		game = new Life(4, 8);
		game.setCell(1, 5, true);
		game.setCell(2, 4, true);
		game.setCell(2, 5, true);
		Life nextGeneration = game.nextLife();
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
		game = new Life(3,3);
		game.setCell(0, 0, true);
		assertTrue(game.compareTo(game));
	}
	
	@Test
	public void testCompareToCopy() {
		game = new Life(3,3);
		game.setCell(0, 0, true);
		Life copy = new Life(game.getWorld());
		assertTrue(game.compareTo(copy));
	}
	
	@Test
	public void testCompareToDifferentDimensions() {
		game = new Life(3,3);
		Life game2 = new Life(4,5);
		assertFalse(game.compareTo(game2));
	}
	
	@Test
	public void testCompareToDifferentWorldState() {
		game = new Life(3,3);
		Life game2 = new Life(3,3);
		game2.setCell(2, 1, true);
		assertFalse(game.compareTo(game2));
	}
	
	@Test
	public void testCrossPattern() {
		game = new Life(5,5);
		game.setCell(2,1, true);
		game.setCell(2,2, true);
		game.setCell(2,3, true);
		Life gen1 = game.nextLife();
		Life gen2 = gen1.nextLife();
		assertFalse(game.compareTo(gen1));
		assertTrue(game.compareTo(gen2));
	}
	
}
