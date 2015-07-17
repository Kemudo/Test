package action_game1;

import java.awt.Graphics;
import java.awt.Image;

/**
 * �L�����N�^�[�̒��ۃN���X
 * Player��Enemy�̃N���X�͂�����p�����Đ���
 * �e�Ȃǂ̃I�u�W�F�N�g��������p��������悤�ɂ��邩���H
 */
abstract public class Character {
	Anim anim;		//�L�����N�^�[�̃A�j���[�V����
	int sizeX,sizeY;		//�L�����N�^�[�̃T�C�Y�ix�����A�������j
	int x,y;				//�L�����N�^�[�̍��W
	boolean isDeleted;		//�L�����N�^�[���f���[�g��Ԃ�
	CollisionObject collision;	//�����蔻��I�u�W�F�N�g
	
	//�R���X�g���N�^
	//���W���w�肹���ɍ쐬
	Character(int sizeX, int sizeY, AnimData animData){
		this.anim =  new Anim(animData);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		x = 0;
		y = 0;
		isDeleted = false;
	}
	//���W���w�肵�č쐬
	Character(int sizeX, int sizeY, int x, int y, AnimData animData){
		this(sizeX,sizeY,animData);
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * �L�����N�^�[��`��
	 */
	public void draw(Graphics g){
		anim.draw(g, x, y);
	}
	
	/**
	 * �L�����N�^�[���ړ�
	 */
	public void move(){
		collision.update(x,y);
	}
	
	/**
	 * �L�����N�^�[���폜
	 */
	public void delete(){
		isDeleted = true;
		collision.removeThis();
	}
	
	/**
	 *  getter 
	 */
	public int getX(){ return x; }
	public int getY(){ return y; }
	public int getSizeX(){ return sizeX; }
	public int getSizeY(){ return sizeY; }
	public boolean isDeleted(){ return isDeleted; }
}
