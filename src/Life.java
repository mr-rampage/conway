import java.awt.Point;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
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

	private Predicate<Point> isCellAlive = (coordinates) -> world[(int)coordinates.getX()][(int)coordinates.getY()];			
	private IntPredicate isUnderpopulated = (population) -> population < 2;
	private IntPredicate isOverpopulated = (population) -> population > 3;	
	private IntPredicate canGrow = (population) -> population == 3;
	
	private Function<Boolean, String> renderCell = (isAlive) -> (isAlive ? "*" : "." );
	private Function<Point, Boolean> getCellState = (coordinates) -> world[(int)coordinates.getX()][(int)coordinates.getY()];
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
						getCellState.apply(location) == value) {
					result++;
				}
			}
		}
		return result;
	}
	
	void setCellState(Life world, Point location) {
		int neighbours = countLivingNeighbours.apply(location);
		world.setCell.accept(location, 
				(isCellAlive.test(location) ? 
						!(isUnderpopulated.or(isOverpopulated).test(neighbours)) :
							canGrow.test(neighbours)));
	}
	
	Consumer<Point> cellConsumerFactory(Life world, BiConsumer<Life, Point> transformer) {
		return (location) -> transformer.accept(world, location);
	}
	
	void transformWorld(boolean[][] world, Consumer<Point> cellConsumer) {
		for (int row = 0; row < world.length; row++) {
			for (int column = 0; column < world[row].length; column++) {
				cellConsumer.accept(new Point(row, column));
			}
		}
	}
	
	public Life nextLife() {
		Life nextLife = new Life(world);
		Consumer<Point> setCellValue = cellConsumerFactory(nextLife, this::setCellState);
		transformWorld(nextLife.getWorld(), setCellValue);
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

	public boolean compareTo(Life other) {
		return Arrays.deepEquals(this.world, other.getWorld());
	}
}
