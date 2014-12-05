import java.awt.Point;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;


public class Life {

	private final boolean[][] world;
	
	private Predicate<Point> isValidCoordinate = 
			(coordinates) -> !((coordinates.getX() < 0) || 
			(coordinates.getX() >= world.length) || 
			(coordinates.getY() < 0) || 
			(coordinates.getY() >= world[(int)coordinates.getX()].length));
			
	private IntPredicate isUnderpopulated = (population) -> population < 2;
	
	private IntPredicate isOverpopulated = (population) -> population > 3;
	
	private Function<Boolean, String> renderCell = (isAlive) -> (isAlive ? "*" : "." );
	
	private Predicate<Point> isCellAlive = (coordinates) -> world[(int)coordinates.getX()][(int)coordinates.getY()];
	
	Function<Point, Integer> countLivingNeighbours = (coordinates) ->
		(isValidCoordinate.test(coordinates) ? countNeighbours(coordinates, true) : -1);
	
	BiConsumer<Point, Boolean> setCell = (coordinates, value) -> world[(int)coordinates.getX()][(int)coordinates.getY()] = value;
	
	public Life(boolean[][] seed) {
		this.world = Arrays.copyOf(seed, seed.length);
	}
	
	public Life(final int rows, final int columns) {
		this.world = new boolean[rows][columns];
	}

	boolean[][] getWorld() {
		return world;
	}
	
	public void setCell(int row, int column, boolean value) {
		Point location = new Point(row, column);
		if (isValidCoordinate.test(location)) {
			setCell.accept(location, value);
		}
	}
	
	private int countNeighbours(final Point cell, final boolean value) {
		int row = (int)cell.getX();
		int column = (int)cell.getY();
		int result = 0;
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = column - 1; j <= column + 1; j++) {
				Point location = new Point(i, j);
				if (isValidCoordinate.test(location) &&
						!location.equals(cell) &&
						isCellAlive.test(location)) {
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
				Point location = new Point(row, column);
				int neighbours = countLivingNeighbours.apply(location);
				if (isCellAlive.test(location)) {
					if (isUnderpopulated.or(isOverpopulated).test(neighbours)) {
						nextLife.setCell.accept(location, false);
					}
				} else {
					if (neighbours == 3) {
						nextLife.setCell.accept(location, true);
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
