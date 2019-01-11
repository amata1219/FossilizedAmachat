package amata1219.amachat.processor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum LanguageCode {

	IS,
	GA,
	AZ,
	AF,
	AM,
	AR,
	SQ,
	HY,
	IT,
	YI,
	IG,
	ID,
	CY,
	UK,
	UZ,
	UR,
	ET,
	EO,
	NL,
	KK,
	CA,
	GL,
	KN,
	EL,
	KY,
	GU,
	KM,
	KU,
	HR,
	XH,
	CO,
	SM,
	JV,
	KA,
	SN,
	SD,
	SI,
	SV,
	ZU,
	GD,
	ES,
	SK,
	SL,
	SW,
	SU,
	SR,
	SX,
	SO,
	TH,
	TL,
	TG,
	TA,
	CS,
	NY,
	TE,
	DA,
	DE,
	TR,
	NE,
	NO,
	HT,
	HA,
	PS,
	EU,
	HW,
	HU,
	PA,
	HI,
	FI,
	FR,
	FY,
	BG,
	VI,
	HE,
	BE,
	FA,
	BN,
	PL,
	BS,
	PT,
	MI,
	MK,
	MR,
	MG,
	ML,
	MT,
	MS,
	MY,
	MN,
	YO,
	LO,
	LA,
	LV,
	LT,
	RO,
	LB,
	RU,
	EN,
	KO,
	ZH,
	JA;

	/*
	IS("IS"),//アイスランド語
	GA("GA"),//アイルランド語
	AZ("AZ"),//アゼルバイジャン語
	AF("AF"),//アフリカーンス語
	AM("AM"),//アムハラ語
	AR("AR"),//アラビア語
	SQ("SQ"),//アルバニア語
	HY("HY"),//アルメニア語
	IT("IT"),//イタリア語
	YI("YI"),//イディッシュ語
	IG("IG"),//イボ語
	ID("ID"),//インドネシア語
	CY("CY"),//ウェールズ語
	UK("UK"),//ウクライナ語
	UZ("UZ"),//ウズベク語
	UR("UR"),//ウルドゥ語
	ET("ET"),//エストニア語
	EO("EO"),//エスペラント語
	NL("NL"),//オランダ語
	KK("KK"),//カザフ語
	CA("CA"),//カタルーニャ語
	GL("GL"),//ガリシア語
	KN("KN"),//カンナダ語
	EL("EL"),//ギリシャ語
	KY("KY"),//キルギス語
	GU("GU"),//グジャラト語
	KM("KM"),//クメール語
	KU("KU"),//クルド語
	HR("HR"),//クロアチア語
	XH("XH"),//コーサ語
	CO("CO"),//コルシカ語
	SM("SM"),//サモア語
	JV("JY"),//ジャワ語
	KA("KA"),//ジョージア語
	SN("SN"),//ショナ語
	SD("SD"),//シンド語
	SI("SI"),//シンハラ語
	SV("SV"),//スウェーデン語
	ZU("ZU"),//ズールー語
	GD("GD"),//スコットランド ゲール語
	ES("ES"),//スペイン語
	SK("SK"),//スロバキア語
	SL("SL"),//スロベニア語
	SW("SW"),//スワヒリ語
	SU("SU"),//スンダ語
	CB("CEB"),//セブアノ語(CEB)
	SR("SR"),//セルビア語
	SX("SX"),//ソト語
	SO("SO"),//ソマリ語
	TH("TH"),//タイ語
	TL("TL"),//タガログ語
	TG("TG"),//タジク語
	TA("TA"),//タミル語
	CS("CS"),//チェコ語
	NY("NY"),//チェワ語
	TE("TE"),//テルグ語
	DA("DA"),//デンマーク語
	DE("DE"),//ドイツ語
	TR("TR"),//トルコ語
	NE("NE"),//ネパール語
	NO("NO"),//ノルウェー語
	HT("HT"),//ハイチ語
	HA("HA"),//ハウサ語
	PS("PS"),//パシュト語
	EU("EU"),//バスク語
	HW("HW"),//ハワイ語(HAW)
	HU("HU"),//ハンガリー語
	PA("PA"),//パンジャブ語
	HI("HI"),//ヒンディー語
	FI("FI"),//フィンランド語
	FR("FR"),//フランス語
	FY("FY"),//フリジア語
	BG("BG"),//ブルガリア語
	VI("VI"),//ベトナム語
	HE("HE"),//ヘブライ語
	BE("BE"),//ベラルーシ語
	FA("FA"),//ペルシャ語
	BN("BN"),//ベンガル語
	PL("PL"),//ポーランド語
	BS("BS"),//ボスニア語
	PT("PT"),//ポルトガル語
	MI("MI"),//マオリ語
	MK("MK"),//マケドニア語
	MR("MR"),//マラーティー語
	MG("MG"),//マラガシ語
	ML("ML"),//マラヤーラム語
	MT("MT"),//マルタ語
	MS("MS"),//マレー語
	MY("MY"),//ミャンマー語
	MN("MN"),//モンゴル語
	HN("HMN"),//モン語(HMN)
	YO("YO"),//ヨルバ語
	LO("LO"),//ラオ語
	LA("LA"),//ラテン語
	LV("LV"),//ラトビア語
	LT("LT"),//リトアニア語
	RO("RO"),//ルーマニア語
	LB("LB"),//ルクセンブルク語
	RU("RU"),//ロシア語
	EN("EN"),//英語
	KO("KO"),//韓国語
	ZH("ZH"),//中国語
	JA("JA");//日本語
	 */

	private static final Map<String, LanguageCode> codes = Collections.unmodifiableMap(createCodeMap());

	public static Map<String, LanguageCode> createCodeMap(){
		Map<String, LanguageCode> codes = new HashMap<>();
		Arrays.asList(LanguageCode.values()).forEach(code -> codes.put(code.name().toLowerCase(), code));
		return codes;
	}

	public static LanguageCode matchedCode(String text){
		if(text.indexOf("[") != 0 || text.indexOf("]") != 3 || text.length() <= 4)
			return null;

		return codes.get(text.substring(1, 4).toLowerCase());
	}

	static boolean has(String text){
		if(text.indexOf("[") != 0 || text.indexOf("]") != 3 || text.length() <= 4)
			return false;

		return true;
	}

	static LanguageCode get(String text){
		return codes.get(text.substring(1, 4).toLowerCase());
	}

}
