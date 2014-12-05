import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;


public class Life {

	private boolean[][] world;
	
	private BiPredicate<Integer, Integer> isValidCoordinate = 
			(row, column) ->!((row < 0) || 
			(row >= world.length) || 
			(column < 0) || 
			(column >= world[0].length));
			
	private IntPredicate isUnderpopulated = (population) -> population < 2;
	
	private IntPredicate isOverpopulated = (population) -> population > 3;
	
	private Function<Boolean, String> renderCell = (isAlive) -> (isAlive ? "*" : "." );
	
	private BiPredicate<Integer, Integer> isCellAlive = (row, column) -> world[row][column];
	
	BiFunction<Integer, Integer, Integer> countLivingNeighbours = (row, column) -> 
		(isValidCoordinate.test(row, column) ? countNeighbours(row, column, true) : -1);
		
	public Life(boolean[][] seed) {
		this.world = Arrays.copyOf(seed, seed.length);
	}
	
	public Life(final int rows, final int columns) {
		this.world = new boolean[rows][columns];
	}

	boolean[][] getWorld() {
		return world;
	}
	
	boolean setCell(final int row, final int column, final boolean value) {
		if (isValidCoordinate.test(row, column)) {
			world[row][column] = value;
			return true;
		}
		return false;
	}
	
	private int countNeighbours(final int row, final int column, final boolean value) {
		int result = 0;
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = column - 1; j <= column + 1; j++) {
				if (isValidCoordinate.test(i, j) &&
						(row != i || column != j) &&
						isCellAlive.test(i, j)) {
					result++;
				}
			}
		}
		return result;
	}

	public Life nextLife() {
		Life nextLife = new Life(world);
		for (int row = 0; row < world.length; row++) {
			for (int column = 0; column < world[row].length; column++) {
				int neighbours = countLivingNeighbours.apply(row, column);
				if (isCellAlive.test(row, column)) {
					if (isUnderpopulated.or(isOverpopulated).test(neighbours)) {
						nextLife.setCell(row, column, false);
					}
				} else {
					if (neighbours == 3) {
						nextLife.setCell(row, column, true);
					}
				}
			}
		}
		return nextLife;
	}
	
	public void render() {
		StringBuilder output = new StringBuilder();
		for (int row = 0; row < world.length; row++) {
			for (int column = 0; column < world[row].length; column++) {
				output.append(renderCell.apply(world[row][column]));
				if (column + 1 == world[row].length) {
					output.append(System.getProperty("line.separator"));
				}
			}
		}
		System.out.println(output.toString());
	}
}
