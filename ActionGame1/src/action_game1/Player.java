package action_game1;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;


/**
 *	�v���C���[�̃N���X
 *	���������ɃA�j���[�V������o�^
 *�@  �t���[������move�ƃf���[�g����
 */
public class Player{
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	//�X�s�[�h
	public static final int SPEED = 6;
	//�W�����v��
	public static final int JUMP_SPEED = 24;
	//�d��
	public static final int GRAVITY = 1;
	//���n���Ă��邩�ǂ���
	private boolean onGround;
	//���x
	private double vx,vy;
	//�ʒu
	private double x,y;
	
	//�}�b�v�ւ̎Q��
	private Map map; 
	
	/**
	 * �w�肵�����W�Ƀv���C���[�쐬
	 * @param sizeX�@�v���C���[�̕�
	 * @param sizeY�@�v���C���[�̍���
	 * @param x�@�����W
	 * @param y�@�����W
	 */
	Player(int sizeX, int sizeY, int x, int y, Map map) {
		vx = vy = 0;
		this.x = x;
		this.y = y;
		onGround = false;
		this.map = map;
	}
	
	
	/**
	 * ��~����
	 */
	public void stop(){
		vx = 0;
	}
	
	/**
	 * �E�ɉ�������
	 */
	public void accelerateRight(){
		vx = SPEED;
	}
	
	/**
	 * ���ɉ�������
	 */
	public void accelerateLeft(){
		vx = -SPEED;
	}
	
	/**
	 * �W�����v����
	 */
	public void jump(){
		if(onGround){
			//������ɑ��x��������
			vy = -JUMP_SPEED;
			onGround = false;
		}
	}
	
	/**
	 * �����L�[�ɂ��ړ�
	 * �����L�[�ɉ�����vx,vy���L�����N�^�[���ړ�������
	 */
	public void move(){
		//�d�͂ɂ��~��
		vy += Map.GRAVITY;
		
		//x�����̓����蔻��
		//�ړ�����W�����߂�
		double newX = x+vx;
		//�ړ�����W�ŏՓ˂���^�C���̈ʒu���擾
		//x���������l����̂�y���W�͕ω����Ȃ��Ɖ���
		Point tile = map.getTileCollision(this,	newX, y);
		if(tile == null){
			//�Փ˂���^�C�����Ȃ���Έړ�
			x = newX;
		}else{
			//�Փ˂���^�C��������ꍇ
			if(vx > 0){ //�E�ֈړ����Ȃ̂ŉE�̃u���b�N�ƏՓ�
				//�u���b�N�ɂ߂荞�ށ@or�@���Ԃ��Ȃ��悤�Ɉʒu����
				x = Map.tilesToPixels(tile.x)-WIDTH;
			}else if(vx < 0){ //���ֈړ����Ȃ̂ō��u���b�N�ƏՓ�
				//�ʒu����
				x = Map.tilesToPixels(tile.x+1);
			}
			vx = 0;
		}
		
		//y�����̓����蔻��
		//�ړ�����W�����߂�
		double newY = y+vy;
		//�ړ�����W�ŏՓ˂���^�C���̈ʒu���擾
		//y���������l����̂�y���W�͕ω����Ȃ��Ɖ���
		tile = map.getTileCollision(this,x, newY);
		if(tile == null){
			//�Փ˂���^�C�����Ȃ���Έړ�
			y = newY;
			//�Փ˂��Ă��Ȃ��̂ŋ󒆂ɂ���
			onGround = false;
		}else{
			//�Փ˂���^�C��������ꍇ
			if(vy > 0){ //�����ֈړ���
				//�ʒu����
				y = Map.tilesToPixels(tile.y)-HEIGHT;
				//���n����
				vy = 0;
				onGround = true;
			}else if(vy < 0){ //��ֈړ���
				//�ʒu����
				y = Map.tilesToPixels(tile.y+1);
				//�V��ɂԂ������̂�y�������x���O�ɂ���
				vy = 0;
			}
		}
	}
	
	/**
	 * �`�惁�\�b�h�i���W�̈ʒu�̓t�B�[���h�ϐ��ŌŒ�j
	 * @param g �`��I�u�W�F�N�g
	 */
	public void draw(Graphics g){
		g.setColor(Color.red);
		g.fillRect((int)x, (int)y, WIDTH, HEIGHT);
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
}
