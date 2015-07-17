package action_game1;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

/**
 *　  １つのアニメーションを表示するためのクラス
 *　　コンストラクタ呼び出し時にImageBoxから持ってきたAnimDataを渡す
 *　　メンバにはアニメを表示するためのタイマーや停止中かどうかのフラグを用意する
 *	使用方法:
 *		・ImageBoxから呼び出したAnimDataを引数に渡し、コンストラクタを呼び出す
 *		・アニメを表示させるときは、フレーム毎にdrawメソッドを呼び出す
 */
public class Anim {
	protected AnimData animData;			//アニメの画像データ
	protected Iterator<Frame> it;			//今表示しているコマを指すイテレータ
	protected Frame drawingFrame;			//今表示しているコマ
	protected int timer;					//今参照しているコマを何フレーム表示させたかを保存
	protected boolean isStop;				//アニメが停止中かどうか
	
	//コンストラクタ
	public Anim(AnimData animData){
		this.animData = animData; 
		it = animData.initIterator(it);			//イテレータを初期化
		drawingFrame = it.next();
		timer = 0;
		isStop = false;
	}
	
	/**
	 * アニメを描画する
	 * １フレーム分の描画を行う
	 * その後、指定されたフレーム数の間表示していたら次のコマに切り替える
	 * @param g 描画オブジェクト
	 * @param x,y 描画する座標
	 */
	public void draw(Graphics g, int x, int y){
		//コマを表示させタイマーを進める
		g.drawImage(drawingFrame.image, x, y, null);
		//停止中でなければタイマーを進める
		if(!isStop)
			timer++;
		//決められたフレーム数を表示していたら次のコマに切り替える
		if(timer == drawingFrame.time){
			//最後のコマまで表示していたら最初のコマに戻す
			if(it.hasNext()){
				drawingFrame = it.next();
			}else{
				it = animData.initIterator(it);
				drawingFrame = it.next();
			}
			timer=0; //タイマーを初期化
		}
	}
	
	/**
	 * アニメを停止させる
	 * isStopをtrueにすることでアニメを停止させる
	 */
	public void stop(){
		isStop = true;
	}
	/**
	 * 停止状態から戻す
	 * isStopをfalseにすることでアニメを再生する
	 */
	public void play(){
		isStop = false;
	}
}
