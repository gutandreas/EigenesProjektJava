public class TestPlay {

    public static void main(String[] args) {
        Game game = new Game(new Player("Theres"));
        try {
            game.play();
        } catch (InvalidFieldException e) {
            e.printStackTrace();
        }
    }
}
