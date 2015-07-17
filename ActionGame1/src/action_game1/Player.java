package action_game1;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;


/**
 *	プレイヤーのクラス
 *	初期化時にアニメーションを登録
 *　  フレーム毎にmoveとデリート判定
 */
public class Player{
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	//スピード
	public static final int SPEED = 6;
	//ジャンプ力
	public static final int JUMP_SPEED = 24;
	//重力
	public static final int GRAVITY = 1;
	//着地しているかどうか
	private boolean onGround;
	//速度
	private double vx,vy;
	//位置
	private double x,y;
	
	//マップへの参照
	private Map map; 
	
	/**
	 * 指定した座標にプレイヤー作成
	 * @param sizeX　プレイヤーの幅
	 * @param sizeY　プレイヤーの高さ
	 * @param x　ｘ座標
	 * @param y　ｙ座標
	 */
	Player(int sizeX, int sizeY, int x, int y, Map map) {
		vx = vy = 0;
		this.x = x;
		this.y = y;
		onGround = false;
		this.map = map;
	}
	
	
	/**
	 * 停止する
	 */
	public void stop(){
		vx = 0;
	}
	
	/**
	 * 右に加速する
	 */
	public void accelerateRight(){
		vx = SPEED;
	}
	
	/**
	 * 左に加速する
	 */
	public void accelerateLeft(){
		vx = -SPEED;
	}
	
	/**
	 * ジャンプする
	 */
	public void jump(){
		if(onGround){
			//上向きに速度を加える
			vy = -JUMP_SPEED;
			onGround = false;
		}
	}
	
	/**
	 * 方向キーによる移動
	 * 方向キーに応じてvx,vy分キャラクターを移動させる
	 */
	public void move(){
		//重力による降下
		vy += Map.GRAVITY;
		
		//x方向の当たり判定
		//移動先座標を求める
		double newX = x+vx;
		//移動先座標で衝突するタイルの位置を取得
		//x方向だけ考えるのでy座標は変化しないと仮定
		Point tile = map.getTileCollision(this,	newX, y);
		if(tile == null){
			//衝突するタイルがなければ移動
			x = newX;
		}else{
			//衝突するタイルがある場合
			if(vx > 0){ //右へ移動中なので右のブロックと衝突
				//ブロックにめり込む　or　隙間がないように位置調整
				x = Map.tilesToPixels(tile.x)-WIDTH;
			}else if(vx < 0){ //左へ移動中なので左ブロックと衝突
				//位置調整
				x = Map.tilesToPixels(tile.x+1);
			}
			vx = 0;
		}
		
		//y方向の当たり判定
		//移動先座標を求める
		double newY = y+vy;
		//移動先座標で衝突するタイルの位置を取得
		//y方向だけ考えるのでy座標は変化しないと仮定
		tile = map.getTileCollision(this,x, newY);
		if(tile == null){
			//衝突するタイルがなければ移動
			y = newY;
			//衝突していないので空中にいる
			onGround = false;
		}else{
			//衝突するタイルがある場合
			if(vy > 0){ //したへ移動中
				//位置調整
				y = Map.tilesToPixels(tile.y)-HEIGHT;
				//着地処理
				vy = 0;
				onGround = true;
			}else if(vy < 0){ //上へ移動中
				//位置調整
				y = Map.tilesToPixels(tile.y+1);
				//天井にぶつかったのでy方向速度を０にする
				vy = 0;
			}
		}
	}
	
	/**
	 * 描画メソッド（座標の位置はフィールド変数で固定）
	 * @param g 描画オブジェクト
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
