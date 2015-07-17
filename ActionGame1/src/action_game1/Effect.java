package action_game1;

import java.awt.Graphics;

/**
 *　  エフェクトを表示させるためのクラス
 *　　Animと違う点はtailまで表示したあとは繰り返しコマを表示せずにオブジェクトを削除するところ
 *	使用方法:
 *		・ImageBoxから取り出したAnimDataと座標を引数に渡しコンストラクタを呼び出す
 *		・表示させるときはフレーム毎にdrawを呼び出す
 *		・削除はフレーム毎にisDeletedを呼び出すことで判定し、コレクションからremoveするなどをして削除する
 */
class Effect extends Anim{
	int x,y;					//表示する座標
	private boolean isEnd;		//最後までアニメを表示したか
	
	//コンストラクタ
	Effect(AnimData animData, int x, int y) {
		super(animData);
		this.x = x;
		this.y = y;
	}
	
	/**
	 * １回しか繰り返し表示しないように変更する
	 * @param g 描画オブジェクト
	 * @param x,y 描画する座標
	 */
	@Override
	public void draw(Graphics g, int x, int y){
		//コマを表示させタイマーを進める
		g.drawImage(drawingFrame.image, x, y, null);
		//停止中でなければタイマーを進める
		if(!isStop)
			timer++;
		//決められたフレーム数を表示していたら次のコマに切り替える
		if(timer == drawingFrame.time){
			//最後のコマまで表示していたら削除フラグを立てる
			if(it.hasNext()){
				drawingFrame = it.next();
			}else{
				delete();
			}
			timer=0; //タイマーを初期化
		}
	}
	
	/**
	 * 削除フラグを立てる
	 */
	public void delete(){
		isEnd = true;
	}
	
	/**
	 * getter
	 */
	public boolean isDeleted(){ return isEnd; }
}
