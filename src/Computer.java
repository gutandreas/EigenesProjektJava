public class Computer extends Player {

    public Computer(String name) {
        super(name);
    }

    //Besetzt den nächsten freien Stein
    public int[] compPutStone(Field3 field) {
        int[] RingAndField = new int[2];

        int i;
        int j;

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 8; j++) {
                if (field.getArray()[i][j] == 9) {
                    RingAndField[0] = i;
                    RingAndField[1] = j;

                    return RingAndField;
                }
            }
        }
        return RingAndField;
    }

    //Entfernt den nächsten gegnerischen Stein
    public int[] compKillStone(Field3 field) {
        int[] RingAndField = new int[2];
        int i;
        int j;

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 8; j++) {
                if (field.getArray()[i][j] == 0 && field.checkKill(i,j)) {
                    RingAndField[0] = i;
                    RingAndField[1] = j;

                    return RingAndField;
                }
            }
        }
        return RingAndField;
    }

}
