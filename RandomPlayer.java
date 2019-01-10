import java.util.ArrayList;
import java.util.Scanner;

public class RandomPlayer extends Player {
    private boolean isShowHand;     // ランダムプレイヤの指し手を表示するか
    
    RandomPlayer(int n) {
        super(n);
    }

    RandomPlayer(int n, boolean isShowHand) {
        this(n);

        this.isShowHand = isShowHand;
    }

    public static RandomPlayer createRandomPlayer(int playerNum) {
        boolean isShowHand = false;

        Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("Player" + playerNum + "のランダムプレイヤの指し手を表示しますか？ y or n");
        if (scanner.next().equals("y")) isShowHand = true;

        return new RandomPlayer(playerNum, isShowHand);
    }

    @Override
    public int hand(Field field) {
        // 合法手リストを作成
        ArrayList<Integer> legalHand = searchLegalHand(field, this.num);

        // 合法手リストからランダムな指し手を返す
        int r = new java.util.Random().nextInt(legalHand.size());

        // 選ばれた指し手を表示
        if (this.isShowHand) System.out.println("指し手は、" + legalHand.get(r));

        return legalHand.get(r);
    }

    // 合法手リストを返すメソッド
    public static ArrayList<Integer> searchLegalHand(Field field, int playerNum) {
        ArrayList<Integer> legalHand = new ArrayList<Integer>();

        // 全ての手を指して合法手か確かめる
        for (int hand = 0; hand < field.getLength(); hand++) {
            // 合法手ならリストに追加
            if (Judge.isFoul(field, playerNum, hand) == 0) {
                legalHand.add(hand);
            }
        }

        return legalHand;
    }

}