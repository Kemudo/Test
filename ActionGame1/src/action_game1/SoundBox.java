package action_game1;

import java.applet.AudioClip;
import java.util.HashMap;

/**
 *	SE��BGM��ۊǂ��A�Đ��E��~���s���N���X
 *�@�@SE��BGM�����ꂼ��n�b�V���ŊǗ�����
 *	BGM�̓��[�v������
 *	�n�b�V���̃L�[�l�ɂ͕������p����
 *  ���̃N���X�͂ЂƂ������݂���΂悢�̂ł��ׂẴt�B�[���h�A���\�b�h��static�ɂ��Ă���
 *  �g�����F
 *  	�E�p�ӂ��Ă��鉹�y�f�[�^��setSE��setBGM�œo�^����
 *  	�E�Đ�����Ƃ��͂��ꂼ��playSE,playBGM�ōĐ�����
 *  	�EBGM�ɂ͉��t��~���郁�\�b�hstopBGM���p�ӂ���Ă���
 */
public class SoundBox {
	static HashMap<String, AudioClip> SE = new HashMap<String, AudioClip>();
	static HashMap<String, AudioClip> BGM = new HashMap<String, AudioClip>();
	static AudioClip playingBGM = null;		//���ݍĐ����Ă���BGM
	static boolean	isPlaying = false;		//�Đ������ǂ���
	
	/**
	 * �L�[�l�ƃI�[�f�B�I�f�[�^���󂯎��ASE��o�^����
	 * @param key
	 * @param audio
	 */
	static public void setSE(String key, AudioClip audio){
		SE.put(key, audio);
	}
	
	/**
	 * �L�[�l�ƃI�[�f�B�I�f�[�^���󂯎��ABGM��o�^����
	 * @param key
	 * @param audio
	 */
	static public void setBGM(String key, AudioClip audio){
		BGM.put(key, audio);
	}
	
	/**
	 * �L�[�l���󂯎��ASE���Đ�����
	 * @param key �Đ�����SE�̃L�[�l
	 */
	static public void playSE(String key){
		SE.get(key).play();
	}
	
	/**
	 * �L�[�l���󂯎��ABGM���Đ�����
	 * ���ɍĐ����Ă���BGM������ꍇ�͒�~���ĕύX
	 * @param key �Đ�����BGM�̃L�[�l
	 */
	static public void playBGM(String key){
		//BGM���Đ����Ă���Β�~������
		if(isPlaying)
			stopBGM();
		//�V����BGM���Đ�������
		playingBGM = BGM.get(key);
		playingBGM.loop();
		isPlaying = true;
	}
	
	/**
	 * �Đ����Ă���BGM���~����
	 */
	static public void stopBGM(){
		if(playingBGM != null)
			playingBGM.stop();
		isPlaying = false;
	}
}
