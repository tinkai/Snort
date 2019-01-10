import java.util.ArrayList;

public class Node {
    private int playerNum;                      // ルートノードのプレイヤー番号
    private Field field;                        // 盤面
    private int turn;                           // ターンプレイヤー
    private ArrayList<Integer> legalHand;       // 合法手リスト
    private ArrayList<Node> childNodeList;      // 子ノードリスト
    private int threshold;                      // プレイアウト数の閾値
    private int playoutTimes;                   // 実行プレイアウト回数
    private int winTimes;                       // 勝利回数
    
    Node(int playerNum, Field field, int turn, int threshold) {
        this.playerNum = playerNum;
        this.field = field;
        this.turn = turn;
        this.threshold = threshold;
    }
    /*
    Node(int playerNum, Field field, int turn, int threshold, Node parentNode) {
        this(playerNum, field, turn, threshold);
        this.parentNode = parentNode;
    }
    */
    public int getPlayoutTimes() {
        return this.playoutTimes;
    }
    public int getWinTimes() {
        return this.winTimes;
    }

    // モンテカルロ木探索を行うメソッド  返り値は勝利プレイヤ番号
    public int search() {
        int winner;
        
        // プレイアウト回数がノード閾値を超えたら子ノードへ  閾値を超えてなければプレイアウト
        if (playoutTimes >= threshold) {
            // 子ノードを展開していない場合は展開
            if (this.childNodeList == null) expansionNode();

            // 終了局面の場合は子ノードが展開できない
            if (this.childNodeList.size() == 0) {
                winner = Game.nextTurnPlayer(this.turn);     // 勝者は前のターンプレイヤ
            } else {
                // UCB1値を計算
                Node searchNode = MCTSPlayer.searchMaxUCB1ValueNode(this.childNodeList, this.playoutTimes);
                // 値が大きなNodeをsearch
                winner = searchNode.search();
            }

        } else {
            winner = MonteCarloPlayer.playout(this.field.clone(), this.turn);
        }

        this.playoutTimes++;
        // 勝利したら勝利数を+1
        if (winner == this.playerNum) this.winTimes++;

        return winner;
    }

    // 子ノード展開を行うメソッド
    public void expansionNode() {
        // 合法手を格納するリスト
        this.legalHand = RandomPlayer.searchLegalHand(this.field, this.turn); // 合法手を調査

        this.childNodeList = new ArrayList<Node>();      // ノードリストを初期化

        // ノードリストを作成
        for (int i = 0; i < this.legalHand.size(); i++) {
            Field oneHandField = this.field.clone();                // 盤面を複製
            oneHandField.executeHand(this.legalHand.get(i), this.turn);  // 指し手を実行

            this.childNodeList.add(new Node(this.playerNum, oneHandField, Game.nextTurnPlayer(this.turn), this.threshold));   // 子ノードに追加
        }
    }

    public void showInfo(int sum, int deep) {
        String indent = "";
        for (int i = 1; i < deep; i++) indent += "  ";  // 空白調整

        System.out.println(indent + "勝利数 / プレイアウト数 : " + this.winTimes + " / " + this.playoutTimes);
        System.out.println(indent + "UCB1値 : " + MCTSPlayer.calcUCB1Value(this, sum));

        if (this.childNodeList == null || this.childNodeList.size() == 0) {
            System.out.println(indent + "終端ノードです。");
        } else {
            System.out.println(indent + "  手番:Player" + this.turn);
            for (int i = 0; i < this.childNodeList.size(); i++) {
                System.out.println(indent + "  指し手:" + this.legalHand.get(i));
                this.childNodeList.get(i).showInfo(this.playoutTimes, deep+1);
            }
        }
        

    }
}