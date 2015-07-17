package action_game1;

import java.awt.Graphics;
import java.awt.Image;

/**
 * キャラクターの抽象クラス
 * PlayerやEnemyのクラスはこれを継承して制作
 * 弾などのオブジェクトもこれを継承させるようにするかも？
 */
abstract public class Character {
	Anim anim;		//キャラクターのアニメーション
	int sizeX,sizeY;		//キャラクターのサイズ（x成分、ｙ成分）
	int x,y;				//キャラクターの座標
	boolean isDeleted;		//キャラクターがデリート状態か
	CollisionObject collision;	//当たり判定オブジェクト
	
	//コンストラクタ
	//座標を指定せずに作成
	Character(int sizeX, int sizeY, AnimData animData){
		this.anim =  new Anim(animData);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		x = 0;
		y = 0;
		isDeleted = false;
	}
	//座標を指定して作成
	Character(int sizeX, int sizeY, int x, int y, AnimData animData){
		this(sizeX,sizeY,animData);
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * キャラクターを描画
	 */
	public void draw(Graphics g){
		anim.draw(g, x, y);
	}
	
	/**
	 * キャラクターを移動
	 */
	public void move(){
		collision.update(x,y);
	}
	
	/**
	 * キャラクターを削除
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
