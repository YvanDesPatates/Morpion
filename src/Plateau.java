import java.util.ArrayList;

public class Plateau {

    private final char[][] tableau;

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
        this.tableau[y-1][x-1]= symbole;
    }

    /**
     * @param joueur : the joueur we will test if he won the game or not
     * @return true if this joueur fullfeel the victory conditions, false otherwise
     */
    public boolean isWinner(Joueur joueur){


        ArrayList<String> listPositionGagnate = new ArrayList<>();
        //diagonal 2
        listPositionGagnate.add(this.tableau[0][0] +this.tableau[1][1] + this.tableau[2][2]+"");
        listPositionGagnate.add(this.tableau[0][2] +this.tableau[1][1] + this.tableau[2][0]+"");
        //colone 3
        listPositionGagnate.add(this.tableau[0][0] +this.tableau[1][0] + this.tableau[2][0]+"");
        listPositionGagnate.add(this.tableau[0][1] +this.tableau[1][1] + this.tableau[2][1]+"");
        listPositionGagnate.add(this.tableau[0][2] +this.tableau[1][2] + this.tableau[2][2]+"");
        //ligne 3
        listPositionGagnate.add(this.tableau[0][0] +this.tableau[0][1] + this.tableau[0][2]+"");
        listPositionGagnate.add(this.tableau[1][0] +this.tableau[1][1] + this.tableau[1][2]+"");
        listPositionGagnate.add(this.tableau[2][0] +this.tableau[2][1] + this.tableau[2][2]+"");

        String resultat = joueur.getSymbole()+joueur.getSymbole()+joueur.getSymbole()+"";

        boolean gagnant = false;

        for (String cas: listPositionGagnate) {
            if(cas.equals(resultat)){
                System.out.println(joueur.getSymbole() + " gagne la partie ");
                gagnant = true;
            }

        }



        return gagnant;

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

    public boolean cellIsFree(int x, int y) {
        return tableau[x-1][y-1] == '_';
    }
}