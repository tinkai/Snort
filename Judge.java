public class Judge {

    Judge() {}

    // 敵を調べるメソッド
    public static int findEnemy(int my) {
        if (my == 1) return 2;
        else return 1;
    }

    // 試合の終了判定を行うメソッド
    public static boolean isEndMatch(Field field, int turn) {
        // 全ての指し手が禁じ手だったら試合終了
        // 一つでも合法な指し手があれば続行
        for (int i = 0; i < field.getLength(); i++) {
            if (isFoul(field, turn, i) == 0) return false;
        }
        return true;
    }

    // 禁じ手か判定するメソッド
    // 返り値  0:正常 1:盤外 2:マーク済み 3:隣に敵のマーク
    public static int isFoul(Field field, int turn, int hand) {
        if (isOver(field, hand)) return 1;                      // 盤外か
        else if (field.isMark(hand)) return 2;                  // すでにマークがあるか
        else if (isAdjacentEnemy(field, turn, hand)) return 3;  // 隣に敵のマークがあるか

        return 0;
    }

    // 盤面外を指定していないか判定するメソッド
    private static boolean isOver(Field field, int hand) {
        if (hand < 0 || hand >= field.getLength()) return true;
        
        return false;
    }

    // 隣に相手プレイヤのマークがないか判定するメソッド
    private static boolean isAdjacentEnemy(Field field, int my, int hand) {
        int anemy = findEnemy(my);

        if (hand-1 >= 0 && field.getBlockField(hand-1) == anemy) return true;
        else if (hand+1 < field.getLength() && field.getBlockField(hand+1) == anemy) return true;

        return false;
    }
}