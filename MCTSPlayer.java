import java.util.ArrayList;
import java.util.Scanner;

public class MCTSPlayer extends MonteCarloPlayer{
    private int threshold;          // ノード閾値
    private boolean isShowHand;     // モンテカルロ木探索法の指し手を表示するか
    private boolean isShowDetails;  // モンテカルロ木探索法の詳細を表示するか

    MCTSPlayer(int num, int playtimes, int threshold, boolean isShowHand, boolean isShowDetails) {
        super(num, playtimes);

        this.threshold = threshold;
        this.isShowHand = isShowHand;
        this.isShowDetails = isShowDetails;
    }

    // モンテカルロ木探索プレイヤの詳細設定を行うメソッド
    public static MCTSPlayer createMCTSPlayer(int playerNum) {
        System.out.println("Player" + playerNum + "のMCTSプレイヤの設定を行います。");
        int playTimes = 0;
        int threshold = 0;
        boolean isShowDetails = false;      // 詳細を表示するか
        boolean isShowHand = false;         // 指し手を表示するか

        Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("プレイアウト回数を入力してください。");
        playTimes = scanner.nextInt();
        System.out.println("ノード閾値を入力してください。");
        threshold = scanner.nextInt();
        System.out.println("モンテカルロ技探索の詳細結果を表示しますか？ y or n");
        if (scanner.next().equals("y")) {
            isShowDetails = true;
            isShowHand = true;
        } else {
            System.out.println("モンテカルロ技探索の指し手を表示しますか？ y or n");
            if (scanner.next().equals("y")) isShowHand = true;
        }

        return new MCTSPlayer(playerNum, playTimes, threshold, isShowHand, isShowDetails);
    }

    @Override
    public int hand(Field field) {
        // 合法手リストを作成
        ArrayList<Integer> legalHand = searchLegalHand(field, this.num);

        // モンテカルロ木探索
        ArrayList<Node> nodeList = new ArrayList<Node>();      // 子ノードリストを作成

        // 子ノードを調査
        for (int i = 0; i < legalHand.size(); i++) {
            Field oneHandField = field.clone();                // 盤面を複製
            oneHandField.executeHand(legalHand.get(i), this.num);   // 指し手を実行

            nodeList.add(new Node(this.num, oneHandField, Game.nextTurnPlayer(this.num), this.threshold));     // ノードリストに追加
        }

        // プレイアウト回数調査
        for (int i = 0; i < this.playTimes; i++) {
            // UCB1値が最大のNodeを求める
            Node searchNode = searchMaxUCB1ValueNode(nodeList, i);
            // 探索
            searchNode.search();

            /*
            if (this.isShow) {
                System.out.println("プレイアウト回数" + (i+1) + "回目");
                showNode(legalHand, nodeList);
                showUCB1Value(legalHand, nodeList, i+1);
            }
            */
        }

        if (this.isShowDetails) {
            System.out.println("モンテカルロ木探索結果を表示します。");
            for (int i = 0; i < nodeList.size(); i++) {
                System.out.println("指し手:" + legalHand.get(i));
                nodeList.get(i).showInfo(this.playTimes, 1);
                System.out.println();
            }
        }

        // 勝率が最大の指し手を選ぶ
        int maxWinRateNum = legalHand.get(0);
        double maxWinRate = (double)nodeList.get(0).getWinTimes() / nodeList.get(0).getPlayoutTimes();
        for (int i = 1; i < legalHand.size(); i++) {
            Node node = nodeList.get(i);
            double winRate = (double)node.getWinTimes() / node.getPlayoutTimes();
            if (winRate > maxWinRate) {
                maxWinRate = winRate;
                maxWinRateNum = legalHand.get(i);
            }
        }

        if (this.isShowHand) System.out.println("最大勝率手は、" + maxWinRateNum + " : " + String.format("%.3f", maxWinRate));

        return maxWinRateNum;
    }

    // UCB1値が一番高いNodeを返すメソッド
    public static Node searchMaxUCB1ValueNode(ArrayList<Node> nodeList, int sum) {
        Node maxNode = nodeList.get(0);                                 // UCB1値が最大のNode
        double maxUCB1Value = calcUCB1Value(nodeList.get(0), sum);      // 最大のUCB1値

        for (Node node : nodeList) {
            double UCB1Value = calcUCB1Value(node, sum);
            // UCB1値が現在の最大値より大きいなら更新
            if (UCB1Value > maxUCB1Value) {
                maxNode = node;
                maxUCB1Value = UCB1Value;
            }
        }

        return maxNode;
    }

    // UCB1値を返すメソッド
    public static double calcUCB1Value(Node node, int sum) {
        int playoutTimes = node.getPlayoutTimes();
        int winTimes = node.getWinTimes();

        // プレイアウト回数が0なら、最大値を返す
        if (playoutTimes == 0) return Double.MAX_VALUE;

        double winRate = (double)winTimes / playoutTimes;   // 勝率
        double bias = Math.sqrt(2*Math.log((double)sum) / (double)playoutTimes); // バイアス項

        return winRate + bias;
    }

    private void showUCB1Value(ArrayList<Integer> legalHand, ArrayList<Node> nodeList, int sum) {
        for (int i = 0; i < nodeList.size(); i++) {
            System.out.println(legalHand.get(i) + ":" + calcUCB1Value(nodeList.get(i), sum));
        }
    }

    private void showNode(ArrayList<Integer> legalHand, ArrayList<Node> nodeList) {
        for (int i = 0; i < nodeList.size(); i++) {
            System.out.println(legalHand.get(i) + ":" + nodeList.get(i).getPlayoutTimes() + ":" + nodeList.get(i).getWinTimes());
        }
    }   
}