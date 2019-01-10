import java.util.Scanner;

public class HumanPlayer extends Player {

    HumanPlayer(int n) {
        super(n);
    }

    @Override
    public int hand(Field field) {
        System.out.println("番地を入力してください。");

        int inputHand = 0;

        // 数字を入力されるまでループ
        while (true) {
            try {
                Scanner scanner = new java.util.Scanner(System.in);
                inputHand = scanner.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("数字を入力してください。");
                continue;
            }
        }

        return inputHand;
    }
}