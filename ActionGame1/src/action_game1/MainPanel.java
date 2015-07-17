package action_game1;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainPanel extends JPanel implements KeyListener,Runnable{
	//�p�l���T�C�Y
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	//�_�u���o�b�t�@�����O�idb�j�p
	private Graphics dbg;
	private Image dbImage = null;
	
	//�Q�[�����[�v�p�X���b�h
	private Thread gameLoop;
	
	//FPS�v�Z�p
	// ���҂���FPS�i1�b�Ԃɕ`�悷��t���[�����j
    private static final int FPS = 60;  //���z��FPS�̒l
    // 1�t���[���Ŏg���鎝������
    private static final long PERIOD = (long) (1.0 / FPS * 1000000000); // �P��: ns
    // FPS���v�Z����Ԋu�i1s = 10^9ns�j
    private static long MAX_STATS_INTERVAL = 1000000000L; // �P��: ns
    // FPS�v�Z�p
    private long calcInterval = 0L; // in ns
    private long prevCalcTime;
    // �t���[����
    private long frameCount = 0;
    // ���ۂ�FPS
    private double actualFPS = 0.0;
    private DecimalFormat df = new DecimalFormat("0.0");
    
    //------��������Q�[���Ɋւ��郁���o------
    
    //�}�b�v
    private Map map;
    
    //�v���C���[�I�u�W�F�N�g
    Player player;
    
    //�L�����N�^�[���i�[���郊�X�g
    ArrayList<Character> characters;
    
    //�G�t�F�N�g���i�[���郊�X�g
    ArrayList<Effect> effects;
    
//**********************************************************
//����������
//**********************************************************
    
    /**
     * ������
     */
	public MainPanel(){
		//�p�l���̐����T�C�Y��ݒ�Apack()����Ƃ��ɕK�v
		setPreferredSize(new Dimension(WIDTH,HEIGHT) );
		setSize(WIDTH,HEIGHT);
		//�p�l�����L�[�{�[�h���󂯎��悤�ɂ���
		this.setFocusable(true);
		//�L�[���X�i�[��o�^
		addKeyListener(this);
		
		//�����蔻��N���X��������
		new CollisionManager();
		
		//���\�[�X�ǂݍ���
		load();
		
		//�L�����N�^�[�̏W���I�u�W�F�N�g
		characters = new ArrayList<Character>();
		
		//�G�t�F�N�g�̏W���I�u�W�F�N�g
		effects = new ArrayList<Effect>();
		
		//�}�b�v�쐬
		map = new Map();
		
		//�v���C���[�I�u�W�F�N�g�쐬
		player = new Player(Player.WIDTH , Player.HEIGHT, 64, 64, map);
		
		
		//�Q�[�����[�v�J�n
		gameLoop = new Thread(this);
		gameLoop.start();
	}

	/**
	 * �摜�A�T�E���h�ABGM�ǂݍ���
	 */
	private void load(){
		//-------------�摜�f�[�^�̓ǂݍ���--------------
		//�摜�ǂݍ��ݗp�I�u�W�F�N�g
		ImageIcon icon;
		BufferedImage bimage;
		
		try{
			//bimage = ImageIO.read(getClass().getResourceAsStream("akasan.png"));
			//ImageBox.setAnimDataAtPartition(72/3, 128/4, 3, 4, 5, "player", bimage);
			System.out.println("�摜�f�[�^�ǂݍ��݂ɐ������܂���");
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("�摜�ǂݍ��݂Ɏ��s���܂���");
		}
		//-------------���y�f�[�^�̓ǂݍ���--------------
		//���y�f�[�^�ۊǃI�u�W�F�N�g��������
		//soundBox = new SoundBox();
		//���y�f�[�^�ǂݍ��ݗp�I�u�W�F�N�g
		AudioClip ac;
		try{
			//ac = Applet.newAudioClip(getClass().getResource("attackSound1.wav"));
			//SoundBox.SE.put("hitSound", ac);
			System.out.println("���y�f�[�^�ǂݍ��݂ɐ������܂���");
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("���y�f�[�^�ǂݍ��݂Ɏ��s���܂���");
		}
	}
	
//**********************************************************
//�@���C�����[�v
//**********************************************************
	@Override
	public void run(){
		long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        
        beforeTime = System.nanoTime();
        prevCalcTime = beforeTime;
        
		while(true){
			KeyInput.updateKey();	//�L�[�̓��͏�Ԃ��X�V
			CollisionManager.collisionCheck();	//�����蔻��`�F�b�N
			gameUpdata(); //�Q�[����Ԃ��X�V
			gameRender(); //�o�b�t�@�Ƀ����_�����O�i�_�u�������_�����O�j
			paintScreen(); //�o�b�t�@����ʂɕ`��irepaint()�������ł���!�j
	
			
			//---------------FPS�v�Z���X���[�v-----------------------	
			afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            // �O��̃t���[���̋x�~���Ԍ덷�������Ă���
            sleepTime = (PERIOD - timeDiff) - overSleepTime;

            if (sleepTime > 0) {
                // �x�~���Ԃ��Ƃ��ꍇ
                try {
                    Thread.sleep(sleepTime / 1000000L); // nano->ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // sleep()�̌덷
                overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
            } else {
                // ��ԍX�V�E�����_�����O�Ŏ��Ԃ��g���؂��Ă��܂�
                // �x�~���Ԃ��Ƃ�Ȃ��ꍇ
                overSleepTime = 0L;
                // �x�~�Ȃ���16��ȏ㑱������
                if (++noDelays >= 16) {
                    Thread.yield(); // ���̃X���b�h���������s
                    noDelays = 0;
                }
            }

            beforeTime = System.nanoTime();

            // FPS���v�Z
            calcFPS();
            //----------------------------------------------------
		}
		
	}
	
	//�Q�[����Ԃ��X�V
	private void gameUpdata(){
		//���E�̃L�[��������Ă�����v���C���[�����ꂼ��̕����ֈړ�������
		if(KeyInput.getKey(KeyEvent.VK_LEFT)>0){
			player.accelerateLeft();
		}else if(KeyInput.getKey(KeyEvent.VK_RIGHT)>0){
			player.accelerateRight();
		}else{
			player.stop();
		}
		
		//��L�[��������Ă���΃W�����v����
		if(KeyInput.getKey(KeyEvent.VK_UP)>0){
			player.jump();
		}
		
		//�v���C���[�ړ�
		player.move();
		
		//�����蔻��
		Iterator<Character> it = characters.iterator();
		Character cTemp;
		while(it.hasNext()){
			cTemp = it.next();
			cTemp.move();
			if(cTemp.isDeleted()){
				it.remove();
			}
		}
	}

	//FPS�v�Z
    private void calcFPS() {
        frameCount++;
        calcInterval += PERIOD;

        // 1�b������FPS���Čv�Z����
        if (calcInterval >= MAX_STATS_INTERVAL) {
            long timeNow = System.nanoTime();
            // ���ۂ̌o�ߎ��Ԃ𑪒�
            long realElapsedTime = timeNow - prevCalcTime; // �P��: ns

            // FPS���v�Z
            // realElapsedTime�̒P�ʂ�ns�Ȃ̂�s�ɕϊ�����
            actualFPS = ((double) frameCount / realElapsedTime) * 1000000000L;
            //System.out.println(df.format(actualFPS));

            frameCount = 0L;
            calcInterval = 0L;
            prevCalcTime = timeNow;
        }
    }
//**********************************************************
//�`�揈��
//**********************************************************

	//�o�b�t�@�Ƀ����_�����O
	private void gameRender(){
		//����̌Ăяo�����Ƀ_�u���o�b�t�@�����O�I�u�W�F�N�g���쐬
		if(dbImage==null){
			//�o�b�t�@�C���[�W
			dbImage = createImage(WIDTH,HEIGHT);
			if(dbImage == null){
				System.out.println("dbImage is null");
				return;
			}else{
				//�o�b�t�@�C���[�W�̕`��I�u�W�F�N�g
				dbg = dbImage.getGraphics();
			}
		}
		//�o�b�t�@���N���A����
		dbg.setColor(Color.WHITE);
		dbg.fillRect(0, 0, WIDTH, HEIGHT);
		
		//�����蔻���Ԃ̋�؂���e�X�g�\��
		/*int sizeOfX = WIDTH/8;
		int sizeOfY = HEIGHT/8;
		dbg.setColor(Color.BLACK);
		for(int i=0; i<8; i++){
			dbg.drawLine(sizeOfX*i, 0, sizeOfX*i, HEIGHT);
			dbg.drawLine(0, sizeOfY*i, WIDTH, sizeOfY*i);
		}*/
		
		
		//�L�����N�^�[�\��
		Iterator<Character> cit = characters.iterator();
		while(cit.hasNext()){
			cit.next().draw(dbg);
		}
		
		//�S�G�t�F�N�g��\������
		Iterator<Effect> it = effects.iterator();
		while(it.hasNext()){
			Effect ea = it.next();
			ea.draw(dbg, ea.x, ea.y);
			if(ea.isDeleted())
				it.remove();
		}
		
		//�}�b�v�`��
		map.draw(dbg);
		
		//�v���C���[�`��
		player.draw(dbg);
		
		System.out.println("�v���C���[�̍��W-> "+player.getX()+" , "+player.getY());

		//FPS�\��
        dbg.setColor(Color.BLUE);
        dbg.drawString("FPS: " + df.format(actualFPS), 4, 16);
	}
	
	//�o�b�t�@����ʂɕ`��
	private void paintScreen(){
		try{
			Graphics g = getGraphics();
			if((g!=null) && (dbImage!=null)){
				g.drawImage(dbImage, 0, 0, null); //�o�b�t�@�C���[�W����ʂɕ`��
			}
			Toolkit.getDefaultToolkit().sync();
			if(g!=null){
				g.dispose(); //�O���t�B�b�N�I�u�W�F�N�g��j��
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

//**********************************************************
//�L�[����
//**********************************************************
	
	//�L�[�������ꂽ�Ƃ�
	@Override
	public void keyPressed(KeyEvent e) {
		KeyInput.pressed(e);
	}
	
	//�L�[�������ꂽ�Ƃ�
	@Override
	public void keyReleased(KeyEvent e) {
		KeyInput.released(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
//*********************************************************
	
}
