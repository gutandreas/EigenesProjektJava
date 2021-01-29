import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private Player player0;
    private Player player1;
    private Player winner;
    private int round;
    private final int NUMBEROFSTONES = 3;
    private Player currentPlayer;
    private Field3 field;
    private Field3 oldField;
    private boolean gameOver;
    private Scanner scanner = new Scanner(System.in);

    ArrayList<Player> playerArrayList = new ArrayList<>();

    public Game(Player player0, Player player1) {
        this.player0 = player0;
        this.player1 = player1;
        playerArrayList.add(0, player0);
        playerArrayList.add(1, player1);
        round = 0;
        currentPlayer=playerArrayList.get(0);
        field = new Field3(this);
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void updateCurrentPlayer(){
        currentPlayer = playerArrayList.get(round%2);
    }

    public int getCurrentPlayerIndex(){
        return currentPlayer.equals(playerArrayList.get(0)) ? 0 : 1;
    }

    public Field3 getField() {
        return field;
    }

    public Field3 getOldField() {
        return oldField;
    }

    public void setOldField(Field3 oldField) {
        this.oldField = oldField;
    }

    public void play() throws InvalidFieldException{
        boolean phase1 = true;
        boolean phase2 = false;



        while (!gameOver){

            oldField = (Field3) getField().clone();

            if (phase2==true && getField().numberOfStonesCurrentPlayer() <= 2){
                gameOver = true;
                winner = playerArrayList.get((getCurrentPlayerIndex()+1)%2);
                System.out.println(winner.getName() + " hat das Spiel gewonnen!");
                break;
            }

            System.out.println(getCurrentPlayer().getName() + " ist an der Reihe!");


            if (round == NUMBEROFSTONES*2){
                phase1 = false;
                phase2 = true;}

            if (round >= NUMBEROFSTONES*2 && getField().numberOfStonesCurrentPlayer()<=3){
                getCurrentPlayer().setAllowedToJump(true);
            }

            if (phase1){

                System.out.println("Ring:");
                int ringNum = scanner.nextInt();
                System.out.println("Feld:");
                int fieldNum = scanner.nextInt();

                field.putStone(ringNum,fieldNum);

                //field.putStone(new Random().nextInt(2), new Random().nextInt(7));
            }

            if(phase2){
                System.out.println("Ring Start:");
                int ringNow = scanner.nextInt();
                System.out.println("Feld Start:");
                int fieldNow = scanner.nextInt();
                System.out.println("Ring Ziel:");
                int ringDest= scanner.nextInt();
                System.out.println("Feld Ziel:");
                int fieldDest = scanner.nextInt();
                try {
                    field.move(ringNow,fieldNow,ringDest,fieldDest,getCurrentPlayer().isAllowedToJump());
                }
                catch (InvalidMoveException e){
                    e.printStackTrace();
                }
            }



            field.printField();
            if (field.checkTriple(oldField)){
                System.out.println(currentPlayer.getName() + " hat eine 3er-Reihe!");
                System.out.println("Welchen Stein möchtest du entfernen?");
                System.out.println("Reihe");
                int ringKill = scanner.nextInt();
                System.out.println("Feld");
                int fieldKill = scanner.nextInt();
                try {
                    field.killStone(ringKill,fieldKill);
                } catch (InvalidKillException e) {
                    e.printStackTrace();
                }
            }

            round++;
            updateCurrentPlayer();
        }



    }
}

