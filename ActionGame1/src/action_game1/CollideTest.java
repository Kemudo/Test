package action_game1;

import java.awt.Container;
import javax.swing.JFrame;

/**
 * �S���؋�Ԃ𗘗p���������蔻��N���XCollideManager���e�X�g����
 */

public class CollideTest extends JFrame{
	public CollideTest() {
        // �^�C�g����ݒ�
        setTitle("�A�N�V�����P");

        // ���C���p�l�����쐬���ăt���[���ɒǉ�
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // �p�l���T�C�Y�ɍ��킹�ăt���[���T�C�Y�������ݒ�
        pack();
    }

    public static void main(String[] args) {
    	JFrame frame = new CollideTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
