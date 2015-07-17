package action_game1;

/*
	・キーボードの入力状態を保存するクラス。
	・使い方：
		KeyLestenerを実装したMainPanelにインスタンスを生成する。
		オーバーライドされたkeyPressedにpressed(KeyEvent e),
		keyReleasedにreleased(KeyEvent e)を呼び出させる。
		また、フレームごとにupdateKey()を呼び出す
		入力状態はgetKey(int Keycode),isPushed(int Keycode)で得ることができる。
*/

import java.awt.event.KeyEvent;

public class KeyInput {
	//メンバ
	private static final int KEYSIZE = 256;
	public static int[] Key = new int[KEYSIZE];		//何フレーム連続で押されているかを記録
	public static boolean[] pushKey = new boolean[KEYSIZE];		//キーボードが押されているかどうか
	
	//キーが押されたときに呼び出される
	static public void pressed(KeyEvent e){
		int key = e.getKeyCode();
		//System.out.println("キーが押された");
		//System.out.println("Keycode:"+key);
		//System.out.println("連続フレーム数:"+Key[key]);
		if(key < KEYSIZE)
			pushKey[key] = true;
	}
	
	//キーが離されたときに呼び出される
	static public void released(KeyEvent e){
		int key = e.getKeyCode();
		if(key < KEYSIZE)
			pushKey[key] = false;
	}
	
	//フレームごとにkeyを更新するメソッド
	static public void updateKey(){
		for(int i=0; i<KEYSIZE; i++){
			if(pushKey[i])
				Key[i]++;
			else
				Key[i] = 0;
		}
	}
	
	//Keyの指定された添え字の値を返す
	static public int getKey(int Keycode){
		if(Keycode < KEYSIZE )
			return Key[Keycode];
		return -1;
	}
	
	//pushKeyの指定された添え字の値を返す
	static public boolean isPushed(int Keycode){
		if(Keycode < KEYSIZE )
			return pushKey[Keycode];
		return false;
	}
}
