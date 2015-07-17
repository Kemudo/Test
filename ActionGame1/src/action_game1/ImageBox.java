package action_game1;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 画像やアニメーションを保存しておく
 * 画像読み込み時にこのクラスに保存しておく
 *　画像とアニメーションをそれぞれハッシュで管理する
 * ハッシュのキー値は文字列で指定することにする
 * このクラスはひとつだけ存在すればよいのですべてのフィールド、メソッドはstaticにしてある
 * 使い方:
 * 	・画像読み込み時に画像データとキー値をクラスに登録する
 *  ・表示させるときはキー値を渡すことで画像データを取り出すことができる
 */
public class ImageBox {
	public static HashMap<String, Image> images = new HashMap<String, Image>();;
	public static HashMap<String, AnimData> anims = new HashMap<String, AnimData>();
	
	/**
	 * 画像をハッシュに登録
	 * @param key
	 * @param image
	 */
	static void setImage(String key, Image image){
		images.put(key, image);
	}
	
	/**
	 * アニメをハッシュに登録
	 * @param key
	 * @param image
	 * @param time
	 */
	static void setAnimData(String key, Image image, int time){
		//ハッシュ内にキー値のデータがある場合は、該当データに画像とフレーム数を登録する
		if( anims.containsKey(key) ){
			System.out.println("key="+key+"に画像データを登録します");
			anims.get(key).add(image, time);
		}
		//ハッシュ内にキー値のデータがない場合はアニメを新規作成しハッシュに登録する
		else{
			System.out.println("key="+key+"のデータが存在しないので新規作成します");
			anims.put(key, new AnimData());
			anims.get(key).add(image, time);
		}
	}
	
	/**
	 * １枚の画像から分割して読み込ませる
	 * @param sizeX,sizeY 分割するサイズ
	 * @param x,y  分割する個数
	 * @param time 表示するフレーム数
	 * @param key ハッシュ値
	 * @param image 画像データ（BufferedImageであることに注意）
	 */
	static public void setAnimDataAtPartition(int sizeX, int sizeY, int x, int y, int time, String key, BufferedImage image){
		//画像がnullであれば何もしない
		if(image == null){
			System.out.println("渡された画像はnullです");
			return;
		}
		
		//画像を分割しながらリストに加える
		for(int i=0; i<y; i++){
			for(int j=0; j<x; j++){
				Image temp;
				temp = image.getSubimage(j*sizeX, i*sizeY, sizeX, sizeY);
				//anims.get(key).add(temp,time);
				setAnimData(key, temp, time);
			}
		}
	}
}

/**
 * アニメーション用のフレームクラス
 * 画像と表示するフレーム数をセットで扱う
 */
class Frame{
	Image image;	//描画する画像
	int time; 	//表示する時間
	Frame(Image image, int time){
		this.image = image;
		this.time = time;
	}
}

/**
 * アニメーションはフレームデータをリストで保管する
 */
class AnimData{
	private ArrayList<Frame> anim;
	
	//コンストラクタ
	AnimData(){
		anim = new ArrayList<Frame>();
	}
	
	/**
	 * アニメーションに画像を登録する
	 * @param image
	 * @param time
	 */
	void add(Image image, int time){
		anim.add(new Frame(image,time));
	}
	
	/**
	 * 外部から渡されたイテレータを初期化する
	 */
	Iterator<Frame> initIterator(Iterator<Frame> it){
		return anim.iterator();
	}
}