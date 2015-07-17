package action_game1;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Stack;


/**
 * �����蔻����Ǘ�����N���X
 * �����蔻��͂��ׂĂ��̃N���X�ōs��
 * �����蔻������I�u�W�F�N�g�����̃N���X�ɓo�^����
 * ������s���A�������Ă���Γ������Ă��邱�Ƃ�ʒm����
 * �����蔻��͏������x����̂��߁A�l���؋�Ԃ𗘗p������@���Ƃ�(�Ƃ肠������4^3=64������)
 */
public class CollisionManager{
	//�������郌�x��
	public static final int LEVEL = 5;
	//��Ԃ̔z��i�l���؂̍\���j  �i��Ԑ�->4��LEVEL��j
	//Level0�i���[�g��ԁj���珇�ɕ���ł���
	public static CCell[] cellTree = new CCell[(int)Math.pow(4, LEVEL)];
	//TODO �e�X�g�I����������
	public static int count = 0;
	
	//�R���X�g���N�^
	public CollisionManager(){
		//cellTree�̏�����
		//�v�f���͌���S�A����LEVEL+1�̓��䐔��̘a
		int work = (int)(1-Math.pow(4, LEVEL+1))/(1-4);
		cellTree = new CCell[work];
		//��ԃI�u�W�F�N�g��o�^
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
	
	//���ׂĂ̓����蔻��I�u�W�F�N�g���m�̓����蔻����s��
	public static void collisionCheck(){
		int cellIndex=0;	//���ݒ��ׂĂ�����
		Stack<CCell> stack = new Stack<CCell>(); //�e�̃I�u�W�F�N�g���܂߂��X�^�b�N
		boolean newComming = true;
		count = 0; //TODO
		while(true){
			if(newComming && !cellTree[cellIndex].isEmpty()){
				//��ԓ��ɃI�u�W�F�N�g���Ȃ���Δ�����
				//��ԓ��ł̏ՓˏՓ˔���
				CollisionObject crnt1,crnt2;
				crnt1 = cellTree[cellIndex].head;
				while(crnt1 != null){
					crnt2 = crnt1;
					while(crnt2.next != null){
						crnt2 = crnt2.next;
						crnt1.isCollide(crnt2);
						count++; //TODO
					}
					
					//�X�^�b�N���̃I�u�W�F�N�g�ƏՓ˔���
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
			
			//���ׂĂ����Ԃ����[�g��Ԃ������Ƃ�
			if(cellIndex==0){
				stack.push(cellTree[cellIndex]);
				cellIndex++;
				continue;
			}
			
			//���ׂĂ����Ԃ��Ō�̋�Ԃ������Ƃ�
			if(cellIndex==cellTree.length-1)
				break;
			
			int checkNum = cellIndex & 0x03;
			//���ׂĂ����Ԃ��Ő[���������Ƃ�
			if(cellTree[cellIndex].level == LEVEL){
				//�E�[�̂Ƃ��͖߂菈��
				if(checkNum==0x00){
					cellIndex = (cellIndex-4)/4;
					newComming = false;
				//�E�[�łȂ��Ƃ��͉E�ƂȂ�ɏ������ڂ�
				}else{
					cellIndex++;
				}
				continue;
			}
			
			//���ׂĂ����Ԃ��r���̃m�[�h�������Ƃ�
			//���̋�Ԃ̃I�u�W�F�N�g���X�^�b�N�Ɋi�[�����[�̎q�Ɉڂ�
			if(cellTree[cellIndex].level < LEVEL){
				//�߂��Ă����Ƃ�
				//�E�[�łȂ���΂ЂƂE�ɂ��炷
				if(!newComming && checkNum!=0x00){
					stack.pop();
					cellIndex++;
					newComming = true;
					continue;
				//�E�[�Ȃ�΃C���f�b�N�X��e�ɖ߂�
				}else if(!newComming && checkNum==0x00){
					stack.pop();
					cellIndex = (cellIndex-4)/4;
					continue;
				}
				//�i��ł���Ƃ�
				//�C���f�b�N�X�����[�̎q�Ɉڂ�
				if(newComming){
					stack.push(cellTree[cellIndex]);
					cellIndex = cellIndex *4 + 1;
				}
			}
		}//end-while		
	}
	
	//�o�^����Ă���I�u�W�F�N�g�̑�����Ԃ�(�e�X�g�p�H)
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
	//�o�^����Ă���I�u�W�F�N�g��񋓂���i�e�X�g�p�j
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

/**�����蔻��I�u�W�F�N�g�i�L�����N�^�[�ɂ������ĂP�I�u�W�F�N�g�����j
 * �폜��e�Ղɍs�����߁A���X�g�̃m�[�h�̍\�����Ƃ��Ă���
 */
//TODO:���̃I�u�W�F�N�g�͎l�p�`�B������p�����ĉ~�������������悤�ɂ���\��
class CollisionObject{
	//�����蔻��̑������`����
	//�i���ꂪ�����I�u�W�F�N�g���m�Ȃ瓖���蔻��`�F�b�N�͍s��Ȃ��j
	public enum Property{
		PLAYER, ENEMY,					//
		PLAYER_ATTACK, ENEMY_ATTACK,	//�G�A�����̍U��
		NO_COLLIDE,						//�N�Ƃ��������ĂȂ�
	}
	Property property;	//�����̓����蔻��̑���
	Property collideObject; //���������Ώە��̑���
	int x,y;			//�����蔻��̍�����W
	int sizeY,sizeX;	//�����蔻��̃T�C�Y
	CCell assign;		//����������
	CollisionObject next;
	CollisionObject prev;
	int id;				//�e�X�g�p
	
	//�R���X�g���N�^
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
	
	//�����蔻�胁�\�b�h
	public void isCollide(CollisionObject other){
		//System.out.print("("+this.x+","+this.y+")");
		//System.out.println("to ("+other.x+","+other.y+")");
		//System.out.println(this.assign.id + " to "+other.assign.id);
		
		Rectangle rect1 = new Rectangle(x,y,sizeX,sizeY);
		Rectangle rect2 = new Rectangle(other.x,other.y,other.sizeX,other.sizeY);
		
		if(rect1.intersects(rect2)){
			//�G�ڐ��������瑊���collideObject�ɒʒm����悤�ɂ���
			if(this.property.equals(Property.ENEMY))
				other.collideObject = property;
			else
				collideObject = other.property;
			return;
		}
		
		collideObject = Property.NO_COLLIDE;
	}
	
	//�����̏�����Ԃ��X�V
	public void update(int x1, int y1){
		this.x = x1;
		this.y = y1;
		int x2,y2;
		int protNum1,protNum2,protXor;
		int level=CollisionManager.LEVEL;
		x2 = x1+sizeX;
		y2 = y1+sizeY;
		//���W���ς���Ă��Ȃ���Ή������Ȃ�
		if(this.x==x1 && this.y==y2)
			return;
		//����ƍ����̃��[�g���ԍ��𓾂�
		int protX,protY;
		protX = x1/CCell.cellSizeX;
		protY = y1/CCell.cellSizeY;
		protNum1 = getMortonNumber(protX, protY);
		protX = x2/CCell.cellSizeX;
		protY = y2/CCell.cellSizeY;
		protNum2 = getMortonNumber(protX, protY);
		//�r���I�_���a���Ƃ�
		protXor = protNum1 ^ protNum2;
		//�E���̒l���E�V�t�g����l�ƃ��x�������߂�
		int shiftNum=0;
		for(int i=0; i<CollisionManager.LEVEL; i++){
			if( (protXor & 0x03) != 0 ){
				level = CollisionManager.LEVEL-i-1;
				shiftNum = (i+1)*2;
			}
			protXor = protXor >> 2;
		}
		//�E���̒l���V�t�g
		protNum2 = protNum2 >> shiftNum;
		//�����蔻��I�u�W�F�N�g���ڂ�
		int work = (int)( (Math.pow(4, level)-1)/3 );
		if( assign == CollisionManager.cellTree[protNum2+work] )
			return;
		removeThis();
		CollisionManager.cellTree[protNum2+work].add(this);
	}
	//�P�r�b�g��тɑg�ݍ��킹���r�b�g���Ԃ�
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
	
	//�������g�����X�g�������
	public void removeThis(){
		//��Ԃɑ����ĂȂ���Δ�����
		if(assign == null){
			return;
		}
		//�������w�b�h�������Ƃ�
		if(assign.head == this){
			assign.removeFirst();
			this.next = null; 
			this.prev = null;
			assign = null;
			return;
		}
		//���������X�g�̓r���ɂ����Ƃ�
		if(this.next!=null)
			this.next.prev = this.prev;
		this.prev.next = this.next;
		this.next = null;
		this.prev = null;
		assign = null;
	}
	
	//�e�X�g�\���p
	@Override
	public String toString(){
		return "id"+id; 
	}
	
	//getter
	public Property getProperty(){
		return this.property;
	}
}

//��ԃI�u�W�F�N�g(�����蔻��I�u�W�F�N�g�����X�g�ŊǗ�)
class CCell{
	int level;	 				//���̋�Ԃ̃��x��
	CollisionObject head;	//�ŏ��̃m�[�h�̎Q��
	static int cellSizeX = (int)(MainPanel.WIDTH/Math.pow(2, CollisionManager.LEVEL));
	static int cellSizeY = (int)(MainPanel.HEIGHT/Math.pow(2, CollisionManager.LEVEL));
	//test�p
	int id;
	
	//�R���X�g���N�^
	CCell(int level){
		this.level = level;
		head = null;
	}
	CCell(int level,int id){
		this(level);
		this.id = id;
	}
	
	//���X�g�̐擪�ɃI�u�W�F�N�g��������
	public void add(CollisionObject co){
		co.assign = this;
		//���X�g����̂Ƃ�
		if( isEmpty() ){
			head = co;
			return;
		}
		//���X�g����łȂ��Ƃ�
		co.next = head;
		co.prev = null;
		head.prev = co;
		head = co;
	}
	
	//�擪�̃I�u�W�F�N�g���폜����
	public void removeFirst(){
		//���X�g�̑傫�����P�������ꍇ
		if(head.next == null){
			head = null;
			return;
		}
		//���̑�
		head.next.prev = null;
		head = head.next;
	}
	
	//�e�X�g�\��
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
	
	//���X�g����ł��邩�ǂ���
	public boolean isEmpty(){
		if(head==null)
			return true;
		return false;
	}
	
	//�e�X�g�\���p
	@Override
	public String toString(){
		return "id:"+String.valueOf(id);
	}
}