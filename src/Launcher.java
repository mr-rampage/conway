
public class Launcher {

	public static void main(String[] args) {
		int generations = 10;
		
		Life game = new Life(4, 8);
		game.setCell(1, 5, true);
		game.setCell(2, 4, true);
		game.setCell(2, 5, true);
		game.setCell(3, 5, true);
		game.setCell(1, 6, true);
		
		while(generations > 0) {
			game.render();
			game = game.nextLife();
			generations--;
		}

	}

}
