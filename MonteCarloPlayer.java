import java.util.ArrayList;
import java.util.Scanner;

public class MonteCarloPlayer extends RandomPlayer {
    protected int playTimes;        // プレイアウト回数
    private boolean isShowHand;     // モンテカルロ法の指し手を表示するか
    private boolean isShowDetails;  // モンテカルロ法の詳細を表示するか

    MonteCarloPlayer(int n, int playTimes) {
        super(n);

        this.playTimes = playTimes;
    }

    MonteCarloPlayer(int n, int playTimes, boolean isShowHand, boolean isShowDetails) {
        this(n, playTimes);

        this.isShowHand = isShowHand;
        this.isShowDetails = isShowDetails;
    }

    // モンテカルロプレイヤの詳細設定を行うメソッド
    public static MonteCarloPlayer createMonteCarloPlayer(int playerNum) {
        System.out.println("Player" + playerNum + "のモンテカルロプレイヤの設定を行います。");
        int playTimes = 0;
        boolean isShowDetails = false;      // 詳細を表示するか
        boolean isShowHand = false;         // 指し手を表示するか

        Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("プレイアウト回数を入力してください。");
        playTimes = scanner.nextInt();
        System.out.println("モンテカルロ法の詳細結果を表示しますか？ y or n");
        if (scanner.next().equals("y")) {
            isShowDetails = true;
            isShowHand = true;
        } else {
            System.out.println("モンテカルロ法の指し手を表示しますか？ y or n");
            if (scanner.next().equals("y")) isShowHand = true;
        }

        return new MonteCarloPlayer(playerNum, playTimes, isShowHand, isShowDetails);
    }

    @Override
    public int hand(Field field) {
        // 合法手リストを作成
        ArrayList<Integer> legalHand = searchLegalHand(field, this.num);

        // モンテカルロ法
        int[] wins = new int[legalHand.size()];     // 指し手の勝利回数
        int[] times = new int[legalHand.size()];    // 指し手の実行回数

        // プレイアウト回数調査
        for (int i = 0; i < this.playTimes; i++) {
            // 盤面を複製
            Field oneHandField = field.clone();

            // ランダムに合法手を指す
            int r = new java.util.Random().nextInt(legalHand.size());
            oneHandField.executeHand(legalHand.get(r), this.num);

            // 一手指したため相手の手番にする
            int turn = Game.nextTurnPlayer(this.num);

            // プレイアウトを行う
            // 勝利すると勝利回数配列に+1
            if (playout(oneHandField, turn) == this.num) wins[r]++;
            
            times[r]++;     // 実行回数を足す
        }

        // 勝率を求める
        double[] winRate = new double[legalHand.size()];    // 指し手の勝率
        for (int i = 0; i < legalHand.size(); i++) {
            winRate[i] = (double)wins[i] / times[i];
        }

        // モンテカルロ法の詳細を表示
        if (this.isShowDetails) showWinRate(legalHand, times, winRate);       // 指し手とその勝率を表示
        
        int wonHand = legalHand.get(0);     // 最大勝率の指し手
        double wonMaxRate = winRate[0];     // 最大勝率
        for (int i = 1; i < legalHand.size(); i++) {
            // 勝率配列が最大勝率よりも大きい場合は更新
            if (winRate[i] > wonMaxRate) {
                wonHand = legalHand.get(i);
                wonMaxRate = winRate[i];
            }
        }

        // 選ばれた指し手を表示
        if (this.isShowHand) System.out.println("最大勝率手は、" + wonHand + " : " + String.format("%.3f", wonMaxRate));

        return wonHand;
    }

    // Randomプレイヤ同士によるプレイアウトを行い、勝利プレイヤ番号を返すメソッド
    public static int playout(Field field, int turn) {
        Game randomGame = new Game(field, turn, new RandomPlayer(1), new RandomPlayer(2), false);
        return randomGame.match();
    }

    // 指し手とその手のプレイアウトの勝率を表示するメソッド
    private void showWinRate(ArrayList<Integer> legalHand, int[] times, double[] winRate) {
        System.out.println("モンテカルロ法の結果を表示します。");
        System.out.println("全プレイアウト回数:" + this.playTimes);
        System.out.println("番地:プレイアウト回数:勝率, ...");
        for (int i = 0; i < legalHand.size(); i++) {
            System.out.println(legalHand.get(i) + ":" + times[i] + ":" + String.format("%.3f", winRate[i]) + ", ");
        }
    }
}