public class PlayingField implements Cloneable{

    private int[][] array = new int[3][8];
    private final Game game;

    public PlayingField(Game game) {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 8; j++){
                array[i][j] = 9;}
        }
        this.game = game;
    }

    public PlayingField(PlayingField field){
        this.array = field.array;
        this.game = field.game;
    }

    public int[][] getArray() {
        return array;
    }

    public void putStone(int row, int field) throws InvalidFieldException{
        if (isFieldOccupied(row, field)){
            throw new InvalidFieldException("Dieses Feld ist besetzt");
        }
        array[row][field] =  game.getCurrentPlayerIndex();
    }

    public void move(int ringNow, int fieldNow, int ringDest, int fieldDest, boolean jump) throws InvalidMoveException, InvalidFieldException{
        if (array[ringNow][fieldNow]!= game.getCurrentPlayerIndex()){
            throw new InvalidMoveException("Kein bzw. nicht dein Stein");
        }

        if (jump){
            putStone(ringDest,fieldDest);
            array[ringNow][fieldNow] = 9;
        }
        else {
            if ((fieldNow%2==1 && fieldNow==fieldDest && Math.abs(ringNow-ringDest)==1)
                    || (ringNow-ringDest==0 && Math.abs((fieldNow)-(fieldDest))==1)
                    || (ringNow-ringDest==0 && Math.abs((fieldNow)-(fieldDest))==7)){
                putStone(ringDest,fieldDest);
                array[ringNow][fieldNow] = 9;}
            else throw new InvalidMoveException("Kein möglicher Zielort");}
    }

    public boolean checkTriple(PlayingField oldField){
        return checkBetweenRings(oldField) || checkInRing(oldField);
    }



    private boolean checkBetweenRings(PlayingField oldField){
        for (int field = 1; field <8;){
            for (int player = 0; player < 2; player++){
                if (array[0][field]==player && array[1][field]==player
                        && array[2][field]==player){
                    if  (!(array[0][field]== oldField.array[0][field] && array[1][field]==oldField.array[1][field]
                            && array[2][field]==oldField.array[2][field])){
                        return true;}
                }
            }
            field+=2;
        }
        return false;
    }


    private boolean checkInRing(PlayingField oldField){

        for (int ring = 0; ring < 3; ring++){
            for (int field = 0; field <4; field++){
                for (int player = 0; player < 2; player++){
                    if (array[ring][field*2]==player && array[ring][field*2+1]==player
                            && array[ring][(field*2+2)%8]==player){
                        if (!(array[ring][field*2]==oldField.array[ring][field*2]
                                && array[ring][field*2+1]==oldField.array[ring][field*2+1]
                                && array[ring][(field*2+2)%8]==oldField.array[ring][(field*2+2)%8])){
                            return true;}}
                }
            }
        }
        return false;
    }

    public boolean checkIfAbleToMove(){
        return checkMovePossibilityInRing() || checkMovePossibilityBetweenRings()
                || game.getCurrentPlayer().isAllowedToJump();
    }

    private boolean checkMovePossibilityBetweenRings(){
        for (int ring = 0; ring < 3; ring++){
            for (int field = 1; field < 8;){
                if (array[ring][field] == game.getCurrentPlayerIndex()){
                    switch (ring){
                        case 0:
                            if (array[ring+1][field] == 9) return true;
                            break;
                        case 1:
                            if (array[ring-1][field] == 9 || array[ring+1][field] == 9) return true;
                            break;
                        case 2:
                            if (array[ring-1][field] == 9) return true;
                    }
                }
                field+=2;
            }
        }
        return false;
    }

    private boolean checkMovePossibilityInRing(){
        for (int ring = 0; ring < 3; ring++){
            for (int field = 0; field < 8; field++){
                if (array[ring][field] == game.getCurrentPlayerIndex()
                        && (array[ring][(field+1)%8] == 9 || array[ring][(field+7)%8] == 9)){
                    return true;
                }
            }
        }
        return false;
    }

    public void killStone(int ring, int field) throws InvalidKillException {
        if (array[ring][field] == (game.getCurrentPlayerIndex()+1)%2 && checkKill(ring, field)){
            array[ring][field] = 9;
            printField();
        }
        else throw new InvalidKillException(
                "Auf diesem Feld befindet sich kein gegnerischer Stein oder er liegt in einer Mühle (3er-Reihe)");
    }

    public boolean isThereStoneToKill(){
        int stone = (game.getCurrentPlayerIndex()+1)%2;
        for (int ring = 0; ring < 3; ring++){
            for (int field = 0; field < 7; field++){
                if (array[ring][field] == stone && !isStoneInTriple(ring,field,stone)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isStoneInTriple(int ring, int field, int stone){
        int fieldPosition = field%2;
        switch (fieldPosition){
            case 0:
                if((stone==array[ring][(field+1)%8] && stone==array[ring][(field+2)%8])
                    || (stone==array[ring][(field+7)%8] && stone==array[ring][(field+6)%8])){
                    return true;}
                    break;
            case 1:
                if((stone==array[ring][(field+7)%8] && stone==array[ring][(field+1)%8])
                        || (stone==array[(ring+1)%3][field] && stone==array[(ring+2)%3][field])){
                    return true;}
                    break;
        }
        return false;


    }

    public boolean checkKill(int ring, int field){

        int stone = array[ring][field];
        return checkKillInRing(ring, field, stone) && checkKillBetweenRing(ring,field,stone);

    }

    private boolean checkKillInRing(int ring, int field, int stone){
        boolean killOkay = true;
        int fieldPos = field%2;
        switch (fieldPos){
            case 0:
                if ((stone == array[ring][(field+1)%8] && stone == array[ring][(field+2)%8])
                    || (stone == array[ring][(field+7)%8] && stone == array[ring][(field+6)%8])){
                    killOkay = false;
                }
                break;
            case 1:
                if (stone == array[ring][(field+7)%8] && stone == array[ring][(field+1)%8]){
                    killOkay = false;
                }
                break;
        }

        return killOkay;
    }

    private boolean checkKillBetweenRing(int ring, int field, int stone){
        return !(stone == array[(ring+1)%3][field] && stone == array[(ring+2)%3][field]);

    }


    public int numberOfStonesCurrentPlayer(){
        int counter = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 8; j++){
                if (game.getCurrentPlayerIndex() == array[i][j]){
                    counter++;
                }
            }
        }
        return counter;
    }

    public boolean isFieldOccupied(int row, int field){
        return array[row][field] != 9;
    }


    public void printField(){
        for (int i = 0; i <= 6; i++){
            printRow(i);}
        System.out.println();
    }

    private void printRow(int row){
        String space;
        switch (row){
            case 0:
                space = "    ";
                for (int i = 0; i < 3; i++){
                    System.out.print(array[row][i]);
                    System.out.print(space);
                }
                System.out.println();
                break;
            case 1:
                space = "   ";
                System.out.print(" ");
                for (int i = 0; i < 3; i++){
                    System.out.print(array[row][i]);
                    System.out.print(space);
                }
                System.out.println();
                break;
            case 2:
                space = "  ";
                System.out.print("  ");
                for (int i = 0; i < 3; i++){
                    System.out.print(array[row][i]);
                    System.out.print(space);
                }
                System.out.println();
                break;
            case 3:
                for (int i = 0; i < 3; i++){
                    System.out.print(array[i][7]);
                }
                System.out.print("     ");
                for (int i = 2; i >= 0; i--){
                    System.out.print(array[i][3]);
                }
                System.out.println();
                break;
            case 4:
                space = "  ";
                System.out.print("  ");
                for (int i = 6; i > 3; i--){
                    System.out.print(array[2][i]);
                    System.out.print(space);
                }
                System.out.println();
                break;
            case 5:
                space = "   ";
                System.out.print(" ");
                for (int i = 6; i > 3; i--){
                    System.out.print(array[1][i]);
                    System.out.print(space);
                }
                System.out.println();
                break;
            case 6:
                space = "    ";
                System.out.print("");
                for (int i = 6; i > 3; i--){
                    System.out.print(array[0][i]);
                    System.out.print(space);
                }
                System.out.println();
                break;
        }}


    @Override
    public Object clone(){
        PlayingField field = new PlayingField(this);
        int[][] tempArray = new int[3][8];

        for (int i = 0; i < field.array.length; i++) {
            System.arraycopy(field.array[i], 0, tempArray[i], 0, field.array[0].length);
        }

        field.array = tempArray;
        return field;
    }}
