public class Plateau {

    private char[][] tableau;

    /**
     * créer et retourne un tableau initialisé
     */
    public Plateau(){
        tableau = Plateau.initialisation();
    }

    /**
     * @param x is the Horizontal coordinate
     * @param y is the vertical coordinate
     * @param symbole to fill the tableau with
     */
    public void takeCase(int x, int y, char symbole){
        this.tableau[x-1][y-1]= symbole;
    }

    /**
     * @param symbole : the symbol we will test the victory conditions on (exemple X)
     * @return true if this symbole fullfeel the victory conditions, false otherwise
     */
    public boolean won(char symbole){

    }

    /**
     * @return true if the tableau has at least one cell empty (without symbole)
     */
    public boolean isFull(){

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