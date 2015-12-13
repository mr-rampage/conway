
public class Launcher {

	public static void main(String[] args) {
		int generations = 10;
		
		Life game = new Life(4, 8);
		game.setCell(2, 1, true);
		game.setCell(2, 2, true);
		game.setCell(2, 3, true);
		
		while(generations > 0) {
			game.render();
			game = game.nextTick();
			generations--;
		}
	}
}
