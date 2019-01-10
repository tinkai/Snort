import java.util.Scanner;

public class Game {
    private Field field;            // 盤面クラス
    private Player p1;              // プレイヤー1
    private Player p2;              // プレイヤー2
    private int turn = 1;           // 現在のターンプレイヤー 1:p1 2:p2
    private boolean isShow;         // 盤面などを表示するか
    private boolean playout;        // コンピュータのゲームなら文字の全てを表示しない

    Game(Field field, Player p1, Player p2, boolean isShow) {
        this.field = field;
        this.p1 = p1;
        this.p2 = p2;
        this.isShow = isShow;
    }

    // 途中盤面の作成
    Game(Field field, int turn, Player p1, Player p2, boolean isShow) {
        this(field, p1, p2, isShow);

        this.turn = turn;
        this.playout = true;
    }

    // 試合を行うメソッド
    public int match() {
        while(!Judge.isEndMatch(this.field, this.turn)) {
            if (this.isShow) {
                this.field.showField();
                System.out.println();
                System.out.println("Player" + this.turn + "のターンです。");
            }
            
            // 指し手の入力
            int hand;
            if (this.turn == 1) hand = this.p1.hand(this.field);
            else hand = this.p2.hand(this.field);

            // 禁じ手か判定 0以外が返ってきたら禁じ手
            int resultJudge = Judge.isFoul(this.field, this.turn, hand);
            if (resultJudge != 0) {
                if (this.isShow) showErrorHand(resultJudge);
                continue;
            }

            // 指し手を実行
            this.field.executeHand(hand, this.turn);

            // ターン変更
            this.turn = nextTurnPlayer(this.turn);
        }

        // プレイアウトのゲームでなければ終了状態を表示
        if (!playout) endMatch();

        // 勝利プレイヤ番号を返す
        if (this.turn == 1) return 2;
        else return 1;
    }

    // 次のターンプレイヤ番号を返すメソッド
    public static int nextTurnPlayer(int turn) {
        if (turn == 1) return 2;
        else return 1;
    }

    // 禁じ手の種類を表示するメソッド
    private void showErrorHand(int errorNum) {
        if (errorNum == 1) System.out.println("盤外を指定しています。");
        else if (errorNum == 2) System.out.println("既にマークされています。");
        else if (errorNum == 3) System.out.println("隣が敵にマークされています。");

        System.out.println("もう一度入力してください。");
    }

    // 試合終了後の結果表示メソッド
    private void endMatch() {
        System.out.println();
        System.out.println("Player" + this.turn + "は置くことができなくなりました。");

        if (this.turn == 1) System.out.println("Player2の勝利です。");
        else System.out.println("Player1の勝利です。");

        System.out.println();
        System.out.println("終了局面");
        this.field.showField();
        System.out.println();
    }
}