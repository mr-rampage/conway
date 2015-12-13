
public class Launcher {

	public static void main(String[] args) {
		int generations = 10;
		
		LifeState game = new LifeState(4, 8);
        // Puts a blinker in the world
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
