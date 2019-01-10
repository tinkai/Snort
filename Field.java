public class Field implements Cloneable {
    private int[] field;            // 試合を行う配列 0:空白 1:p1 2:p2
    private int len;                // 配列の長さ
    private String mark1 = "○";     // p1のマーク
    private String mark2 = "×";     // p2のマーク
    
    Field(int len) {
        this.len = len;
        this.field = new int[len];
    }

    // フィールドクラスの複製
    @Override
    public Field clone() {
        Field clone = null;
        try {
            clone = (Field)super.clone();
            clone.field = this.field.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clone;
    }

    public int getLength() {
        return this.len;
    }
    public int getBlockField(int n) {
        return this.field[n];
    }

    // 指定された番地にマークがあるか判定するメソッド
    public boolean isMark(int hand) {
        if (this.field[hand] != 0) return true;
        return false;
    }

    // 指し手を盤面に反映するメソッド
    public void executeHand(int hand, int turn) {
        this.field[hand] = turn;
    }

    // 盤面の表示するメソッド
    public void showField() {
        // 番地を表示
        for (int i = 0; i < this.len; i++) {
            System.out.print(" " + i);
        }
        System.out.println();

        // 盤面を表示
        for (int i = 0; i < this.len; i++) {
            System.out.print("|");
            if (i >= 10) System.out.print(" ");
            if (i >= 100) System.out.print(" ");
            
            String mark;
            if (this.field[i] == 1) mark = this.mark1;
            else if (this.field[i] == 2) mark = this.mark2;
            else mark = " ";

            System.out.print(mark);
        }
        System.out.println("|");
    }
}