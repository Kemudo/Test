package action_game1;

/*
	�E�L�[�{�[�h�̓��͏�Ԃ�ۑ�����N���X�B
	�E�g�����F
		KeyLestener����������MainPanel�ɃC���X�^���X�𐶐�����B
		�I�[�o�[���C�h���ꂽkeyPressed��pressed(KeyEvent e),
		keyReleased��released(KeyEvent e)���Ăяo������B
		�܂��A�t���[�����Ƃ�updateKey()���Ăяo��
		���͏�Ԃ�getKey(int Keycode),isPushed(int Keycode)�œ��邱�Ƃ��ł���B
*/

import java.awt.event.KeyEvent;

public class KeyInput {
	//�����o
	private static final int KEYSIZE = 256;
	public static int[] Key = new int[KEYSIZE];		//���t���[���A���ŉ�����Ă��邩���L�^
	public static boolean[] pushKey = new boolean[KEYSIZE];		//�L�[�{�[�h��������Ă��邩�ǂ���
	
	//�L�[�������ꂽ�Ƃ��ɌĂяo�����
	static public void pressed(KeyEvent e){
		int key = e.getKeyCode();
		//System.out.println("�L�[�������ꂽ");
		//System.out.println("Keycode:"+key);
		//System.out.println("�A���t���[����:"+Key[key]);
		if(key < KEYSIZE)
			pushKey[key] = true;
	}
	
	//�L�[�������ꂽ�Ƃ��ɌĂяo�����
	static public void released(KeyEvent e){
		int key = e.getKeyCode();
		if(key < KEYSIZE)
			pushKey[key] = false;
	}
	
	//�t���[�����Ƃ�key���X�V���郁�\�b�h
	static public void updateKey(){
		for(int i=0; i<KEYSIZE; i++){
			if(pushKey[i])
				Key[i]++;
			else
				Key[i] = 0;
		}
	}
	
	//Key�̎w�肳�ꂽ�Y�����̒l��Ԃ�
	static public int getKey(int Keycode){
		if(Keycode < KEYSIZE )
			return Key[Keycode];
		return -1;
	}
	
	//pushKey�̎w�肳�ꂽ�Y�����̒l��Ԃ�
	static public boolean isPushed(int Keycode){
		if(Keycode < KEYSIZE )
			return pushKey[Keycode];
		return false;
	}
}
