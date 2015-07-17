package action_game1;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * �摜��A�j���[�V������ۑ����Ă���
 * �摜�ǂݍ��ݎ��ɂ��̃N���X�ɕۑ����Ă���
 *�@�摜�ƃA�j���[�V���������ꂼ��n�b�V���ŊǗ�����
 * �n�b�V���̃L�[�l�͕�����Ŏw�肷�邱�Ƃɂ���
 * ���̃N���X�͂ЂƂ������݂���΂悢�̂ł��ׂẴt�B�[���h�A���\�b�h��static�ɂ��Ă���
 * �g����:
 * 	�E�摜�ǂݍ��ݎ��ɉ摜�f�[�^�ƃL�[�l���N���X�ɓo�^����
 *  �E�\��������Ƃ��̓L�[�l��n�����Ƃŉ摜�f�[�^�����o�����Ƃ��ł���
 */
public class ImageBox {
	public static HashMap<String, Image> images = new HashMap<String, Image>();;
	public static HashMap<String, AnimData> anims = new HashMap<String, AnimData>();
	
	/**
	 * �摜���n�b�V���ɓo�^
	 * @param key
	 * @param image
	 */
	static void setImage(String key, Image image){
		images.put(key, image);
	}
	
	/**
	 * �A�j�����n�b�V���ɓo�^
	 * @param key
	 * @param image
	 * @param time
	 */
	static void setAnimData(String key, Image image, int time){
		//�n�b�V�����ɃL�[�l�̃f�[�^������ꍇ�́A�Y���f�[�^�ɉ摜�ƃt���[������o�^����
		if( anims.containsKey(key) ){
			System.out.println("key="+key+"�ɉ摜�f�[�^��o�^���܂�");
			anims.get(key).add(image, time);
		}
		//�n�b�V�����ɃL�[�l�̃f�[�^���Ȃ��ꍇ�̓A�j����V�K�쐬���n�b�V���ɓo�^����
		else{
			System.out.println("key="+key+"�̃f�[�^�����݂��Ȃ��̂ŐV�K�쐬���܂�");
			anims.put(key, new AnimData());
			anims.get(key).add(image, time);
		}
	}
	
	/**
	 * �P���̉摜���番�����ēǂݍ��܂���
	 * @param sizeX,sizeY ��������T�C�Y
	 * @param x,y  ���������
	 * @param time �\������t���[����
	 * @param key �n�b�V���l
	 * @param image �摜�f�[�^�iBufferedImage�ł��邱�Ƃɒ��Ӂj
	 */
	static public void setAnimDataAtPartition(int sizeX, int sizeY, int x, int y, int time, String key, BufferedImage image){
		//�摜��null�ł���Ή������Ȃ�
		if(image == null){
			System.out.println("�n���ꂽ�摜��null�ł�");
			return;
		}
		
		//�摜�𕪊����Ȃ��烊�X�g�ɉ�����
		for(int i=0; i<y; i++){
			for(int j=0; j<x; j++){
				Image temp;
				temp = image.getSubimage(j*sizeX, i*sizeY, sizeX, sizeY);
				//anims.get(key).add(temp,time);
				setAnimData(key, temp, time);
			}
		}
	}
}

/**
 * �A�j���[�V�����p�̃t���[���N���X
 * �摜�ƕ\������t���[�������Z�b�g�ň���
 */
class Frame{
	Image image;	//�`�悷��摜
	int time; 	//�\�����鎞��
	Frame(Image image, int time){
		this.image = image;
		this.time = time;
	}
}

/**
 * �A�j���[�V�����̓t���[���f�[�^�����X�g�ŕۊǂ���
 */
class AnimData{
	private ArrayList<Frame> anim;
	
	//�R���X�g���N�^
	AnimData(){
		anim = new ArrayList<Frame>();
	}
	
	/**
	 * �A�j���[�V�����ɉ摜��o�^����
	 * @param image
	 * @param time
	 */
	void add(Image image, int time){
		anim.add(new Frame(image,time));
	}
	
	/**
	 * �O������n���ꂽ�C�e���[�^������������
	 */
	Iterator<Frame> initIterator(Iterator<Frame> it){
		return anim.iterator();
	}
}