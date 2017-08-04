//改良点・改善点のメモ

---------------------------------------------------文字列の連結---------------------------------------------------

/* 
 * 〇各ブランドのformatを作成する際の自分の手順
 * 各種Creatorクラスでformatクラスのprivate変数に(文字列に変換した後に)set
 * formatクラスでsetされた各文字列をmapに1,2,3,4,5,6...と連番でput
 * 拡張for文でputされた要素数分連番で文字列変数に+=で連結
 * それを戻り値として返す
 * 
 * ---改善点--- 
 * 要素を連結する順番が変わったり、途中で間に新たな要素が加わった際、
 * ひとつひとつ連番の調整が必要になる　→　クッソ手間
 * そもそも+=での文字連結は処理が無茶苦茶遅いから非推奨
 * (一回きりの連結ならまだしも複数要素を連結するのに+=はだめぽ。。。)
 *
 * 参考:http://d.hatena.ne.jp/nowokay/20140408
 */
---ダメな例---
//Creator.Class
@Autowired
private SampleFormat sampleFormat;

中略

SampleFormat format = new SampleFormat();
format.setBranchId(hoge.get("branchId"));
format.setDealId(hoge.get("dealId"));
....
sampleFormat.createRecord();

中略

//Format.Class

中略

public Map<int, String> createRecord(){
	
	Map<int, String> map = new HashMap<int, String>();
	
  	map.put(1, "branchId");        //店のID
 	map.put(2, "dealId");          //なんかのID
  	map.put(3, "");                //値段etc
  	.....
	return map;
}

public String getByteRecord(){
	Map<int, String> map = createRecord();
	String record = "";
	for(int i = 1; i < map.size(); i++){
		record += map.get(i);
	}
	return record;
}

---改善例--- (利点：間に新しい要素などが追加されても追加したい部分に追記するだけで、他の要素に変更を加える必要がない)
//StringBuilderを使う ===> toString()をformat毎にOverride

protected StringBuilder createBuffer() {
	return new StringBuilder(120);      //文字列を固定幅にしたい場合こういうメソッドを用意するといい(この場合120文字指定)
}

@Override
public String toString(){
  StringBuilder buff = createBuffer();
  
  //branchID
  buff.append(branchId);
  
  //dealId
  buff.append(dealId);
  
  //固定値など
  buff.append("sample");
  ...
  中略
  
  return buff.toString();       //OverrideされたtoString()を処理した後、その処理で出来たStringBuiderを通常のtoStringで文字列に連結して戻す
}
