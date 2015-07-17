package action_game1;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

/**
 *�@  �P�̃A�j���[�V������\�����邽�߂̃N���X
 *�@�@�R���X�g���N�^�Ăяo������ImageBox���玝���Ă���AnimData��n��
 *�@�@�����o�ɂ̓A�j����\�����邽�߂̃^�C�}�[���~�����ǂ����̃t���O��p�ӂ���
 *	�g�p���@:
 *		�EImageBox����Ăяo����AnimData�������ɓn���A�R���X�g���N�^���Ăяo��
 *		�E�A�j����\��������Ƃ��́A�t���[������draw���\�b�h���Ăяo��
 */
public class Anim {
	protected AnimData animData;			//�A�j���̉摜�f�[�^
	protected Iterator<Frame> it;			//���\�����Ă���R�}���w���C�e���[�^
	protected Frame drawingFrame;			//���\�����Ă���R�}
	protected int timer;					//���Q�Ƃ��Ă���R�}�����t���[���\������������ۑ�
	protected boolean isStop;				//�A�j������~�����ǂ���
	
	//�R���X�g���N�^
	public Anim(AnimData animData){
		this.animData = animData; 
		it = animData.initIterator(it);			//�C�e���[�^��������
		drawingFrame = it.next();
		timer = 0;
		isStop = false;
	}
	
	/**
	 * �A�j����`�悷��
	 * �P�t���[�����̕`����s��
	 * ���̌�A�w�肳�ꂽ�t���[�����̊ԕ\�����Ă����玟�̃R�}�ɐ؂�ւ���
	 * @param g �`��I�u�W�F�N�g
	 * @param x,y �`�悷����W
	 */
	public void draw(Graphics g, int x, int y){
		//�R�}��\�������^�C�}�[��i�߂�
		g.drawImage(drawingFrame.image, x, y, null);
		//��~���łȂ���΃^�C�}�[��i�߂�
		if(!isStop)
			timer++;
		//���߂�ꂽ�t���[������\�����Ă����玟�̃R�}�ɐ؂�ւ���
		if(timer == drawingFrame.time){
			//�Ō�̃R�}�܂ŕ\�����Ă�����ŏ��̃R�}�ɖ߂�
			if(it.hasNext()){
				drawingFrame = it.next();
			}else{
				it = animData.initIterator(it);
				drawingFrame = it.next();
			}
			timer=0; //�^�C�}�[��������
		}
	}
	
	/**
	 * �A�j�����~������
	 * isStop��true�ɂ��邱�ƂŃA�j�����~������
	 */
	public void stop(){
		isStop = true;
	}
	/**
	 * ��~��Ԃ���߂�
	 * isStop��false�ɂ��邱�ƂŃA�j�����Đ�����
	 */
	public void play(){
		isStop = false;
	}
}
