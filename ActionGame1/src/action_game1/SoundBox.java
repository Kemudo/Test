package action_game1;

import java.applet.AudioClip;
import java.util.HashMap;

/**
 *	SEとBGMを保管し、再生・停止を行うクラス
 *　　SEとBGMをそれぞれハッシュで管理する
 *	BGMはループさせる
 *	ハッシュのキー値には文字列を用いる
 *  このクラスはひとつだけ存在すればよいのですべてのフィールド、メソッドはstaticにしてある
 *  使い方：
 *  	・用意してある音楽データをsetSEかsetBGMで登録する
 *  	・再生するときはそれぞれplaySE,playBGMで再生する
 *  	・BGMには演奏停止するメソッドstopBGMが用意されている
 */
public class SoundBox {
	static HashMap<String, AudioClip> SE = new HashMap<String, AudioClip>();
	static HashMap<String, AudioClip> BGM = new HashMap<String, AudioClip>();
	static AudioClip playingBGM = null;		//現在再生しているBGM
	static boolean	isPlaying = false;		//再生中かどうか
	
	/**
	 * キー値とオーディオデータを受け取り、SEを登録する
	 * @param key
	 * @param audio
	 */
	static public void setSE(String key, AudioClip audio){
		SE.put(key, audio);
	}
	
	/**
	 * キー値とオーディオデータを受け取り、BGMを登録する
	 * @param key
	 * @param audio
	 */
	static public void setBGM(String key, AudioClip audio){
		BGM.put(key, audio);
	}
	
	/**
	 * キー値を受け取り、SEを再生する
	 * @param key 再生するSEのキー値
	 */
	static public void playSE(String key){
		SE.get(key).play();
	}
	
	/**
	 * キー値を受け取り、BGMを再生する
	 * 既に再生しているBGMがある場合は停止して変更
	 * @param key 再生するBGMのキー値
	 */
	static public void playBGM(String key){
		//BGMを再生していれば停止させる
		if(isPlaying)
			stopBGM();
		//新しいBGMを再生させる
		playingBGM = BGM.get(key);
		playingBGM.loop();
		isPlaying = true;
	}
	
	/**
	 * 再生しているBGMを停止する
	 */
	static public void stopBGM(){
		if(playingBGM != null)
			playingBGM.stop();
		isPlaying = false;
	}
}
