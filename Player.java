public class Player {
    protected int num;        // プレイヤーの番号 1か2

    Player(int n) {
        this.num = n;
    }

    // プレイヤの作成メソッド
    public static Player createPlayer(int category, int playerNum) {
        Player player = null;

        switch (category) {
            case 0:
                player = new HumanPlayer(playerNum);
                break;
            case 1:
                player = RandomPlayer.createRandomPlayer(playerNum);
                break;
            case 2:
                player = MonteCarloPlayer.createMonteCarloPlayer(playerNum);
                break;
            case 3:
                player = MCTSPlayer.createMCTSPlayer(playerNum);
                break;
            default:
                player = new HumanPlayer(playerNum);
        }

        return player;
    }

    // プレイヤーの指し手番地の入力
    public int hand(Field field) {return 0;}
}