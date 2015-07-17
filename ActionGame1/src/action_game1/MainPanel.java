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
	//パネルサイズ
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	//ダブルバッファリング（db）用
	private Graphics dbg;
	private Image dbImage = null;
	
	//ゲームループ用スレッド
	private Thread gameLoop;
	
	//FPS計算用
	// 期待するFPS（1秒間に描画するフレーム数）
    private static final int FPS = 60;  //理想のFPSの値
    // 1フレームで使える持ち時間
    private static final long PERIOD = (long) (1.0 / FPS * 1000000000); // 単位: ns
    // FPSを計算する間隔（1s = 10^9ns）
    private static long MAX_STATS_INTERVAL = 1000000000L; // 単位: ns
    // FPS計算用
    private long calcInterval = 0L; // in ns
    private long prevCalcTime;
    // フレーム数
    private long frameCount = 0;
    // 実際のFPS
    private double actualFPS = 0.0;
    private DecimalFormat df = new DecimalFormat("0.0");
    
    //------ここからゲームに関するメンバ------
    
    //マップ
    private Map map;
    
    //プレイヤーオブジェクト
    Player player;
    
    //キャラクターを格納するリスト
    ArrayList<Character> characters;
    
    //エフェクトを格納するリスト
    ArrayList<Effect> effects;
    
//**********************************************************
//初期化処理
//**********************************************************
    
    /**
     * 初期化
     */
	public MainPanel(){
		//パネルの推奨サイズを設定、pack()するときに必要
		setPreferredSize(new Dimension(WIDTH,HEIGHT) );
		setSize(WIDTH,HEIGHT);
		//パネルがキーボードを受け取るようにする
		this.setFocusable(true);
		//キーリスナーを登録
		addKeyListener(this);
		
		//当たり判定クラスを初期化
		new CollisionManager();
		
		//リソース読み込み
		load();
		
		//キャラクターの集合オブジェクト
		characters = new ArrayList<Character>();
		
		//エフェクトの集合オブジェクト
		effects = new ArrayList<Effect>();
		
		//マップ作成
		map = new Map();
		
		//プレイヤーオブジェクト作成
		player = new Player(Player.WIDTH , Player.HEIGHT, 64, 64, map);
		
		
		//ゲームループ開始
		gameLoop = new Thread(this);
		gameLoop.start();
	}

	/**
	 * 画像、サウンド、BGM読み込み
	 */
	private void load(){
		//-------------画像データの読み込み--------------
		//画像読み込み用オブジェクト
		ImageIcon icon;
		BufferedImage bimage;
		
		try{
			//bimage = ImageIO.read(getClass().getResourceAsStream("akasan.png"));
			//ImageBox.setAnimDataAtPartition(72/3, 128/4, 3, 4, 5, "player", bimage);
			System.out.println("画像データ読み込みに成功しました");
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("画像読み込みに失敗しました");
		}
		//-------------音楽データの読み込み--------------
		//音楽データ保管オブジェクトを初期化
		//soundBox = new SoundBox();
		//音楽データ読み込み用オブジェクト
		AudioClip ac;
		try{
			//ac = Applet.newAudioClip(getClass().getResource("attackSound1.wav"));
			//SoundBox.SE.put("hitSound", ac);
			System.out.println("音楽データ読み込みに成功しました");
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("音楽データ読み込みに失敗しました");
		}
	}
	
//**********************************************************
//　メインループ
//**********************************************************
	@Override
	public void run(){
		long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        
        beforeTime = System.nanoTime();
        prevCalcTime = beforeTime;
        
		while(true){
			KeyInput.updateKey();	//キーの入力状態を更新
			CollisionManager.collisionCheck();	//当たり判定チェック
			gameUpdata(); //ゲーム状態を更新
			gameRender(); //バッファにレンダリング（ダブルレンダリング）
			paintScreen(); //バッファを画面に描画（repaint()を自分でする!）
	
			
			//---------------FPS計算＆スリープ-----------------------	
			afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            // 前回のフレームの休止時間誤差も引いておく
            sleepTime = (PERIOD - timeDiff) - overSleepTime;

            if (sleepTime > 0) {
                // 休止時間がとれる場合
                try {
                    Thread.sleep(sleepTime / 1000000L); // nano->ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // sleep()の誤差
                overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
            } else {
                // 状態更新・レンダリングで時間を使い切ってしまい
                // 休止時間がとれない場合
                overSleepTime = 0L;
                // 休止なしが16回以上続いたら
                if (++noDelays >= 16) {
                    Thread.yield(); // 他のスレッドを強制実行
                    noDelays = 0;
                }
            }

            beforeTime = System.nanoTime();

            // FPSを計算
            calcFPS();
            //----------------------------------------------------
		}
		
	}
	
	//ゲーム状態を更新
	private void gameUpdata(){
		//左右のキーが押されていたらプレイヤーをそれぞれの方向へ移動させる
		if(KeyInput.getKey(KeyEvent.VK_LEFT)>0){
			player.accelerateLeft();
		}else if(KeyInput.getKey(KeyEvent.VK_RIGHT)>0){
			player.accelerateRight();
		}else{
			player.stop();
		}
		
		//上キーが押されていればジャンプする
		if(KeyInput.getKey(KeyEvent.VK_UP)>0){
			player.jump();
		}
		
		//プレイヤー移動
		player.move();
		
		//当たり判定
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

	//FPS計算
    private void calcFPS() {
        frameCount++;
        calcInterval += PERIOD;

        // 1秒おきにFPSを再計算する
        if (calcInterval >= MAX_STATS_INTERVAL) {
            long timeNow = System.nanoTime();
            // 実際の経過時間を測定
            long realElapsedTime = timeNow - prevCalcTime; // 単位: ns

            // FPSを計算
            // realElapsedTimeの単位はnsなのでsに変換する
            actualFPS = ((double) frameCount / realElapsedTime) * 1000000000L;
            //System.out.println(df.format(actualFPS));

            frameCount = 0L;
            calcInterval = 0L;
            prevCalcTime = timeNow;
        }
    }
//**********************************************************
//描画処理
//**********************************************************

	//バッファにレンダリング
	private void gameRender(){
		//初回の呼び出し時にダブルバッファリングオブジェクトを作成
		if(dbImage==null){
			//バッファイメージ
			dbImage = createImage(WIDTH,HEIGHT);
			if(dbImage == null){
				System.out.println("dbImage is null");
				return;
			}else{
				//バッファイメージの描画オブジェクト
				dbg = dbImage.getGraphics();
			}
		}
		//バッファをクリアする
		dbg.setColor(Color.WHITE);
		dbg.fillRect(0, 0, WIDTH, HEIGHT);
		
		//当たり判定空間の区切り線テスト表示
		/*int sizeOfX = WIDTH/8;
		int sizeOfY = HEIGHT/8;
		dbg.setColor(Color.BLACK);
		for(int i=0; i<8; i++){
			dbg.drawLine(sizeOfX*i, 0, sizeOfX*i, HEIGHT);
			dbg.drawLine(0, sizeOfY*i, WIDTH, sizeOfY*i);
		}*/
		
		
		//キャラクター表示
		Iterator<Character> cit = characters.iterator();
		while(cit.hasNext()){
			cit.next().draw(dbg);
		}
		
		//全エフェクトを表示する
		Iterator<Effect> it = effects.iterator();
		while(it.hasNext()){
			Effect ea = it.next();
			ea.draw(dbg, ea.x, ea.y);
			if(ea.isDeleted())
				it.remove();
		}
		
		//マップ描画
		map.draw(dbg);
		
		//プレイヤー描画
		player.draw(dbg);
		
		System.out.println("プレイヤーの座標-> "+player.getX()+" , "+player.getY());

		//FPS表示
        dbg.setColor(Color.BLUE);
        dbg.drawString("FPS: " + df.format(actualFPS), 4, 16);
	}
	
	//バッファを画面に描画
	private void paintScreen(){
		try{
			Graphics g = getGraphics();
			if((g!=null) && (dbImage!=null)){
				g.drawImage(dbImage, 0, 0, null); //バッファイメージを画面に描画
			}
			Toolkit.getDefaultToolkit().sync();
			if(g!=null){
				g.dispose(); //グラフィックオブジェクトを破棄
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

//**********************************************************
//キー入力
//**********************************************************
	
	//キーが押されたとき
	@Override
	public void keyPressed(KeyEvent e) {
		KeyInput.pressed(e);
	}
	
	//キーが離されたとき
	@Override
	public void keyReleased(KeyEvent e) {
		KeyInput.released(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
//*********************************************************
	
}
