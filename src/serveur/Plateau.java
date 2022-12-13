package serveur;

public class Plateau {
    /** square matrice of char */
    private final char[][] tableau;

    /**
     * créer et retourne un tableau initialisé
     */
    public Plateau(int taille){
        tableau = Plateau.initialisation(taille);
    }

    /**
     * @param x is the Horizontal coordinate
     * @param y is the vertical coordinate
     * @param symbole to fill the tableau with
     */
    public void takeCase(int x, int y, char symbole){
        this.tableau[y-1][x-1]= symbole;
    }

    /**
     * @param joueur : the joueur we will test if he won the game or not
     * @return true if this joueur fullfeel the victory conditions, false otherwise
     */
    public boolean isWinner(Joueur joueur){


//        ArrayList<String> listPositionGagnate = new ArrayList<>();
//        //diagonal 2
//        listPositionGagnate.add(this.tableau[0][0] +this.tableau[1][1] + this.tableau[2][2]+"");
//        listPositionGagnate.add(this.tableau[0][2] +this.tableau[1][1] + this.tableau[2][0]+"");
//        //colone 3
//        listPositionGagnate.add(this.tableau[0][0] +this.tableau[1][0] + this.tableau[2][0]+"");
//        listPositionGagnate.add(this.tableau[0][1] +this.tableau[1][1] + this.tableau[2][1]+"");
//        listPositionGagnate.add(this.tableau[0][2] +this.tableau[1][2] + this.tableau[2][2]+"");
//        //ligne 3
//        listPositionGagnate.add(this.tableau[0][0] +this.tableau[0][1] + this.tableau[0][2]+"");
//        listPositionGagnate.add(this.tableau[1][0] +this.tableau[1][1] + this.tableau[1][2]+"");
//        listPositionGagnate.add(this.tableau[2][0] +this.tableau[2][1] + this.tableau[2][2]+"");
//
//        String resultat = joueur.getSymbole()+joueur.getSymbole()+joueur.getSymbole()+"";
//
//        boolean gagnant = false;
//
//        for (String cas: listPositionGagnate) {
//            if(cas.equals(resultat)){
//                System.out.println(joueur.getSymbole() + " gagne la partie ");
//                gagnant = true;
//            }
//
//
//        }
//
//
//
//        return gagnant;
        return hasThreeInARow(joueur.getSymbole()) || hasThreeInAColumn(joueur.getSymbole()) || hasThreeInADiagonal(joueur.getSymbole());
    }

    /**
     * @return true if their is the char symbole three times in a row and all stuck together
     * exemple : (OXXXOO) return true for the symbole 'X'
     */
    private boolean hasThreeInARow(char symbole){
        if (tableau[0].length < 2){
            return false;
        }
        boolean win = false;
        for (int i = 0; i < tableau.length && !win; i++) {
            for (int j = 2; j < tableau[i].length && !win; j++) {
                if (tableau[i][j]==symbole &&
                    tableau[i][j-1]==symbole &&
                    tableau[i][j-2]==symbole){
                    win = true;
                }
            }

        }
        return win;
    }

    /**
     * @return true if their is the char symbole three times in a column and all stuck together
     * exemple : (OXXXOO) return true for the symbole 'X' (imagine that the characters are in a column, not in a row)
     */
    private boolean hasThreeInAColumn(char symbole){
        if (tableau.length < 2){
            return false;
        }
        boolean win = false;
        for (int i = 0; i < tableau[0].length && !win; i++) {
            for (int j = 2; j < tableau.length && !win; j++) {
                if (tableau[j][i]==symbole &&
                    tableau[j-1][i]==symbole&&
                    tableau[j-2][i]==symbole){
                    win=true;
                }
            }
        }
        return win;
    }

    /**
     * @return true if their is the char symbole three times in a diagonal and all stuck together
     * exemple : (OXXXOO) return true for the symbole 'X' (imagine that the characters are in a diagonal, not in a row)
     */
    private boolean hasThreeInADiagonal(char symbole) {
        if (tableau.length < 2 || tableau[0].length < 2) {
            return false;
        }
        //première diagonale 0,0 -> 2,2(de en haut à gauche jusqu'en bas à droite)
        boolean win = false;
        for (int i = 2; i < tableau.length && !win; i++) {
            for (int j = 2; j < tableau[0].length && !win; j++) {
                if (tableau[i][j] == symbole &&
                        tableau[i - 1][j - 1] == symbole &&
                        tableau[i - 2][j - 2] == symbole) {
                    win = true;
                }
            }
        }
        // seconde diagonale 0,2 -> 2,0
        for (int i = tableau.length - 3; i >= 0 && !win; i--) {
            for (int j = 2; j < tableau[0].length && !win; j++) {
                if (tableau[i][j] == symbole &&
                        tableau[i + 1][j - 1] == symbole &&
                        tableau[i + 2][j - 2] == symbole) {
                    win = true;
                }
            }
        }
        return win;
    }


    /**
     * @return true if the tableau has at least one cell empty (without symbole)
     */
    public boolean isFull(){
        int i, j;
        boolean isFull = true;
        for (i = 0;i < tableau.length; i++) {
            for (j = 0;j < tableau[i].length;j++) {
                if(tableau[i][j]=='_'){
                    isFull = false;
                }
            }

        }
        return isFull;
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

    public static char[][] initialisation(int taille) {
        char[][] tableau;
        int i, j;
        tableau = new char[taille][taille];

        //initialisation du plateau
        for (i = 0; i < tableau.length; i++) {
            for (j = 0; j < tableau[i].length; j++) {
                tableau[i][j] = '_';
            }
        }
        return tableau;
    }

    public boolean cellIsFree(int x, int y) {
        return tableau[y-1][x-1] == '_';
    }
}