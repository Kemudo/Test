package action_game1;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Stack;


/**
 * 当たり判定を管理するクラス
 * 当たり判定はすべてこのクラスで行う
 * あたり判定を持つオブジェクトをこのクラスに登録する
 * 判定を行い、当たっていれば当たっていることを通知する
 * 当たり判定は処理速度向上のため、四分木空間を利用する方法をとる(とりあえずは4^3=64分割で)
 */
public class CollisionManager{
	//分割するレベル
	public static final int LEVEL = 5;
	//空間の配列（四分木の構造）  （空間数->4のLEVEL乗）
	//Level0（ルート空間）から順に並んでいる
	public static CCell[] cellTree = new CCell[(int)Math.pow(4, LEVEL)];
	//TODO テスト終わったら消す
	public static int count = 0;
	
	//コンストラクタ
	public CollisionManager(){
		//cellTreeの初期化
		//要素数は公比４、項数LEVEL+1の等比数列の和
		int work = (int)(1-Math.pow(4, LEVEL+1))/(1-4);
		cellTree = new CCell[work];
		//空間オブジェクトを登録
		int level;
		int id=0;
		for(level=0; level<=LEVEL; level++){
			for(int i=0; i<(int)Math.pow(4, level); i++){
				work = (int)( (Math.pow(4, level)-1)/3 );
				cellTree[work+i] = new CCell(level,id);
				id++;
			}
		}
	}
	
	//すべての当たり判定オブジェクト同士の当たり判定を行う
	public static void collisionCheck(){
		int cellIndex=0;	//現在調べている空間
		Stack<CCell> stack = new Stack<CCell>(); //親のオブジェクトを含めたスタック
		boolean newComming = true;
		count = 0; //TODO
		while(true){
			if(newComming && !cellTree[cellIndex].isEmpty()){
				//空間内にオブジェクトがなければ抜ける
				//空間内での衝突衝突判定
				CollisionObject crnt1,crnt2;
				crnt1 = cellTree[cellIndex].head;
				while(crnt1 != null){
					crnt2 = crnt1;
					while(crnt2.next != null){
						crnt2 = crnt2.next;
						crnt1.isCollide(crnt2);
						count++; //TODO
					}
					
					//スタック内のオブジェクトと衝突判定
					CCell tempList;
					CollisionObject stackptr;
					Iterator<CCell> it = stack.iterator();
					it = stack.iterator();
					while(it.hasNext()){
						tempList = it.next();
						stackptr = tempList.head;
						if(stackptr==null)
							continue;
						while(stackptr!=null){
							crnt1.isCollide(stackptr);
							stackptr = stackptr.next;
							count++; //TODO
						}
					}
					crnt1 = crnt1.next;
				}
			}
			
			//調べている空間がルート空間だったとき
			if(cellIndex==0){
				stack.push(cellTree[cellIndex]);
				cellIndex++;
				continue;
			}
			
			//調べている空間が最後の空間だったとき
			if(cellIndex==cellTree.length-1)
				break;
			
			int checkNum = cellIndex & 0x03;
			//調べている空間が最深部だったとき
			if(cellTree[cellIndex].level == LEVEL){
				//右端のときは戻り処理
				if(checkNum==0x00){
					cellIndex = (cellIndex-4)/4;
					newComming = false;
				//右端でないときは右となりに処理を移す
				}else{
					cellIndex++;
				}
				continue;
			}
			
			//調べている空間が途中のノードだったとき
			//その空間のオブジェクトをスタックに格納し左端の子に移る
			if(cellTree[cellIndex].level < LEVEL){
				//戻ってきたとき
				//右端でなければひとつ右にずらす
				if(!newComming && checkNum!=0x00){
					stack.pop();
					cellIndex++;
					newComming = true;
					continue;
				//右端ならばインデックスを親に戻す
				}else if(!newComming && checkNum==0x00){
					stack.pop();
					cellIndex = (cellIndex-4)/4;
					continue;
				}
				//進んでいるとき
				//インデックスを左端の子に移す
				if(newComming){
					stack.push(cellTree[cellIndex]);
					cellIndex = cellIndex *4 + 1;
				}
			}
		}//end-while		
	}
	
	//登録されているオブジェクトの総数を返す(テスト用？)
	public static int getNumOfObject(){
		int num=0;
		for(int i=0; i<cellTree.length; i++){
			CollisionObject crnt;
			crnt = cellTree[i].head;
			while(crnt != null){
				num++;
				crnt = crnt.next;
			}
		}
		return num;
	}
	//登録されているオブジェクトを列挙する（テスト用）
	public static void dump(){
		System.out.println("----------dump---------");
		for(int i=0; i<cellTree.length; i++){
			CollisionObject crnt;
			crnt = cellTree[i].head;
			if(crnt==null) continue;
			System.out.print(i+":");
			while(crnt != null){
				System.out.print(crnt+"->");
				crnt = crnt.next;
			}
			System.out.println();
		}
		System.out.println("-------------------------");
	}
}

/**当たり判定オブジェクト（キャラクターにたいして１つオブジェクトを持つ）
 * 削除を容易に行うため、リストのノードの構造をとっている
 */
//TODO:このオブジェクトは四角形。これを継承して円や線分も扱えるようにする予定
class CollisionObject{
	//当たり判定の属性を定義する
	//（これが同じオブジェクト同士なら当たり判定チェックは行わない）
	public enum Property{
		PLAYER, ENEMY,					//
		PLAYER_ATTACK, ENEMY_ATTACK,	//敵、味方の攻撃
		NO_COLLIDE,						//誰とも当たってない
	}
	Property property;	//自分の当たり判定の属性
	Property collideObject; //当たった対象物の属性
	int x,y;			//当たり判定の左上座標
	int sizeY,sizeX;	//当たり判定のサイズ
	CCell assign;		//所属する空間
	CollisionObject next;
	CollisionObject prev;
	int id;				//テスト用
	
	//コンストラクタ
	CollisionObject(int x, int y, int sizeX, int sizeY, Property property){
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		assign = null;
		next = prev = null;
		this.property = property;
		collideObject = Property.NO_COLLIDE;
		update(x,y);
	}
	CollisionObject(int x, int y, int sizeX, int sizeY, Property property, int id){
		this(x,y,sizeX,sizeY,property);
		this.id = id;
	}
	
	//当たり判定メソッド
	public void isCollide(CollisionObject other){
		//System.out.print("("+this.x+","+this.y+")");
		//System.out.println("to ("+other.x+","+other.y+")");
		//System.out.println(this.assign.id + " to "+other.assign.id);
		
		Rectangle rect1 = new Rectangle(x,y,sizeX,sizeY);
		Rectangle rect2 = new Rectangle(other.x,other.y,other.sizeX,other.sizeY);
		
		if(rect1.intersects(rect2)){
			//敵目線だったら相手のcollideObjectに通知するようにする
			if(this.property.equals(Property.ENEMY))
				other.collideObject = property;
			else
				collideObject = other.property;
			return;
		}
		
		collideObject = Property.NO_COLLIDE;
	}
	
	//自分の所属空間を更新
	public void update(int x1, int y1){
		this.x = x1;
		this.y = y1;
		int x2,y2;
		int protNum1,protNum2,protXor;
		int level=CollisionManager.LEVEL;
		x2 = x1+sizeX;
		y2 = y1+sizeY;
		//座標が変わっていなければ何もしない
		if(this.x==x1 && this.y==y2)
			return;
		//左上と左下のモートン番号を得る
		int protX,protY;
		protX = x1/CCell.cellSizeX;
		protY = y1/CCell.cellSizeY;
		protNum1 = getMortonNumber(protX, protY);
		protX = x2/CCell.cellSizeX;
		protY = y2/CCell.cellSizeY;
		protNum2 = getMortonNumber(protX, protY);
		//排他的論理和をとる
		protXor = protNum1 ^ protNum2;
		//右下の値を右シフトする値とレベルを求める
		int shiftNum=0;
		for(int i=0; i<CollisionManager.LEVEL; i++){
			if( (protXor & 0x03) != 0 ){
				level = CollisionManager.LEVEL-i-1;
				shiftNum = (i+1)*2;
			}
			protXor = protXor >> 2;
		}
		//右下の値をシフト
		protNum2 = protNum2 >> shiftNum;
		//当たり判定オブジェクトを移す
		int work = (int)( (Math.pow(4, level)-1)/3 );
		if( assign == CollisionManager.cellTree[protNum2+work] )
			return;
		removeThis();
		CollisionManager.cellTree[protNum2+work].add(this);
	}
	//１ビット飛びに組み合わせたビット列を返す
	public static int bitSeparate(int n){
		n = ((n<<8)|n) & 0x00FF00FF;
		n = ((n<<4)|n) & 0x0F0F0F0F;
		n = ((n<<2)|n) & 0x33333333;
		n = ((n<<1)|n) & 0x55555555;
		return n;
	}
	public static int getMortonNumber(int x,int y )
	{
	    return (bitSeparate(x) | (bitSeparate(y)<<1));
	}
	
	//自分自身をリストから消去
	public void removeThis(){
		//空間に属してなければ抜ける
		if(assign == null){
			return;
		}
		//自分がヘッドだったとき
		if(assign.head == this){
			assign.removeFirst();
			this.next = null; 
			this.prev = null;
			assign = null;
			return;
		}
		//自分がリストの途中にいたとき
		if(this.next!=null)
			this.next.prev = this.prev;
		this.prev.next = this.next;
		this.next = null;
		this.prev = null;
		assign = null;
	}
	
	//テスト表示用
	@Override
	public String toString(){
		return "id"+id; 
	}
	
	//getter
	public Property getProperty(){
		return this.property;
	}
}

//空間オブジェクト(当たり判定オブジェクトをリストで管理)
class CCell{
	int level;	 				//この空間のレベル
	CollisionObject head;	//最初のノードの参照
	static int cellSizeX = (int)(MainPanel.WIDTH/Math.pow(2, CollisionManager.LEVEL));
	static int cellSizeY = (int)(MainPanel.HEIGHT/Math.pow(2, CollisionManager.LEVEL));
	//test用
	int id;
	
	//コンストラクタ
	CCell(int level){
		this.level = level;
		head = null;
	}
	CCell(int level,int id){
		this(level);
		this.id = id;
	}
	
	//リストの先頭にオブジェクトを加える
	public void add(CollisionObject co){
		co.assign = this;
		//リストが空のとき
		if( isEmpty() ){
			head = co;
			return;
		}
		//リストが空でないとき
		co.next = head;
		co.prev = null;
		head.prev = co;
		head = co;
	}
	
	//先頭のオブジェクトを削除する
	public void removeFirst(){
		//リストの大きさが１だった場合
		if(head.next == null){
			head = null;
			return;
		}
		//その他
		head.next.prev = null;
		head = head.next;
	}
	
	//テスト表示
	public void dump(){
		if(isEmpty())
			return;
		CollisionObject crnt = this.head;
		while(crnt != null){
			System.out.print(crnt.toString()+"->");
			crnt = crnt.next;
		}
		System.out.println();
	}
	
	//リストが空であるかどうか
	public boolean isEmpty(){
		if(head==null)
			return true;
		return false;
	}
	
	//テスト表示用
	@Override
	public String toString(){
		return "id:"+String.valueOf(id);
	}
}