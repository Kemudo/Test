package action_game1;

import java.awt.Container;
import javax.swing.JFrame;

/**
 * ４分木空間を利用したあたり判定クラスCollideManagerをテストする
 */

public class CollideTest extends JFrame{
	public CollideTest() {
        // タイトルを設定
        setTitle("アクション１");

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
    	JFrame frame = new CollideTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
