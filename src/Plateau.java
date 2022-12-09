public class Plateau {

    private char[][] tableau;

    /**
     * créer et retourne un tableau initialisé
     */
    public Plateau(){
        tableau = Plateau.initialisation();
    }

    public Plateau(char[][] table){
        tableau=table;
    }


    public void takeCase(int x, int y, char symbole){
        this.tableau[x-1][y-1]= symbole;
    }

    @Override
    public String toString() {
        int i, j;
        StringBuilder result = new StringBuilder();

        for (i = 0;i < tableau.length; i++) {
            for (j = 0;j < tableau[i].length;j++) {
                result.append(tableau[i][j]);
                result.append(" ");
            }
            result.append("\n");
        }
        result.append("\n");

        return result.toString();
    }

    public static char[][] initialisation() {
        char[][] tableau;
        int i, j;
        tableau = new char[3][3];

        //initialisation du plateau
        for (i = 0; i < tableau.length; i++) {
            for (j = 0; j < tableau[i].length; j++) {
                tableau[i][j] = '_';
            }
        }
        return tableau;
    }

}