import java.util.Scanner;

public class Snort {
    public static void main(String[] args) {
        Scanner scanner = new java.util.Scanner(System.in);

        System.out.println("配列の長さを入力してください。");
        int len = scanner.nextInt();

        System.out.println("Player1（先手）を選んでください。");
        System.out.println("0:人間 1:ランダム 2:モンテカルロ 3:MCTS");
        int p1Category = scanner.nextInt();
        System.out.println("Player2（後手）を選んでください。");
        System.out.println("0:人間 1:ランダム 2:モンテカルロ 3:MCTS");
        int p2Category = scanner.nextInt();

        boolean isShow = true;      // 盤面表示するか 人間がいる場合は表示する いない場合は聞く
        int gameTimes = 1;          // 何回試合を行うか
        if (p1Category != 0 && p2Category != 0) {
            System.out.println("CPU同士の試合です。盤面表示を行いますか？ y or n");
            if (scanner.next().equals("n")) isShow = false;

            System.out.println("何回戦わせますか？");
            gameTimes = scanner.nextInt();
        }

        // プレイヤ作成
        Player p1 = Player.createPlayer(p1Category, 1);
        Player p2 = Player.createPlayer(p2Category, 2);
        int p1Wins = 0;

        for (int i = 0; i < gameTimes; i++) {
            Game game = new Game(new Field(len), p1, p2, isShow);
            if (game.match() == 1) p1Wins++;
        }

        // 複数回試合の時は結果を表示
        if (gameTimes > 1) {
            System.out.println("Player1の勝利数は、" + p1Wins);
            System.out.println("Player2の勝利数は、" + (gameTimes - p1Wins));
        }

        System.exit(0);
    }
}
