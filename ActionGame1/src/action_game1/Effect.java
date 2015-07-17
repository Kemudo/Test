package action_game1;

import java.awt.Graphics;

/**
 *�@  �G�t�F�N�g��\�������邽�߂̃N���X
 *�@�@Anim�ƈႤ�_��tail�܂ŕ\���������Ƃ͌J��Ԃ��R�}��\�������ɃI�u�W�F�N�g���폜����Ƃ���
 *	�g�p���@:
 *		�EImageBox������o����AnimData�ƍ��W�������ɓn���R���X�g���N�^���Ăяo��
 *		�E�\��������Ƃ��̓t���[������draw���Ăяo��
 *		�E�폜�̓t���[������isDeleted���Ăяo�����ƂŔ��肵�A�R���N�V��������remove����Ȃǂ����č폜����
 */
class Effect extends Anim{
	int x,y;					//�\��������W
	private boolean isEnd;		//�Ō�܂ŃA�j����\��������
	
	//�R���X�g���N�^
	Effect(AnimData animData, int x, int y) {
		super(animData);
		this.x = x;
		this.y = y;
	}
	
	/**
	 * �P�񂵂��J��Ԃ��\�����Ȃ��悤�ɕύX����
	 * @param g �`��I�u�W�F�N�g
	 * @param x,y �`�悷����W
	 */
	@Override
	public void draw(Graphics g, int x, int y){
		//�R�}��\�������^�C�}�[��i�߂�
		g.drawImage(drawingFrame.image, x, y, null);
		//��~���łȂ���΃^�C�}�[��i�߂�
		if(!isStop)
			timer++;
		//���߂�ꂽ�t���[������\�����Ă����玟�̃R�}�ɐ؂�ւ���
		if(timer == drawingFrame.time){
			//�Ō�̃R�}�܂ŕ\�����Ă�����폜�t���O�𗧂Ă�
			if(it.hasNext()){
				drawingFrame = it.next();
			}else{
				delete();
			}
			timer=0; //�^�C�}�[��������
		}
	}
	
	/**
	 * �폜�t���O�𗧂Ă�
	 */
	public void delete(){
		isEnd = true;
	}
	
	/**
	 * getter
	 */
	public boolean isDeleted(){ return isEnd; }
}
