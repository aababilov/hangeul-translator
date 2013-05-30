package trans;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Jamo {
	private static Map<Character, Jamo> charMap = new TreeMap<Character, Jamo>();

	private static Map<String, Jamo> stringMap = new HashMap<String, Jamo>();

	public static final char MIN_SYLLABLE = (char) 0xac00;

	public static final char MAX_SYLLABLE = (char) 0xd7a3;

	public static final char MIN_CANONIC = (char) 0x3131;

	public static final char MAX_CANONIC = (char) 0x3163;

	public static final char MIN_CANONIC_CONSONANT = MIN_CANONIC;

	public static final char MAX_CANONIC_CONSONANT = (char) 0x314e;

	public static final char MIN_CHOSEONG = (char) 0x1100;

	public static final char MAX_CHOSEONG = (char) 0x1112;

	public static final char MIN_JONGSEONG = (char) 0x11a8;

	public static final char MAX_JONGSEONG = (char) 0x11c2;

	public static char mkSyllable(int a, int b, int c) {
		return (char) (0xAC00 + (a) * 588 + (b) * 28 + c + 1);
	}

	public static boolean isSyllable(char ch) {
		return (MIN_SYLLABLE <= ch && ch <= MAX_SYLLABLE);
	}

	public static boolean isCanonic(char ch) {
		return (MIN_CANONIC <= ch && ch <= MAX_CANONIC);
	}

	public static boolean isCanonicConsonant(char ch) {
		return (MIN_CANONIC_CONSONANT <= ch && ch <= MAX_CANONIC_CONSONANT);
	}

	public static boolean isChoseong(char ch) {
		return (MIN_CHOSEONG <= ch && ch <= MAX_CHOSEONG);
	}

	public static boolean isJongseong(char ch) {
		return (MIN_JONGSEONG <= ch && ch <= MAX_JONGSEONG);
	}

	public static boolean isConsonant(char ch) {
		return isCanonicConsonant(ch) || isChoseong(ch) || isJongseong(ch);
	}

	public static boolean isVowel(char ch) {
		return (0x1161 <= ch && ch <= 0x1175) || (0x314F <= ch && ch <= 0x3163);
	}

	public static boolean isDouble(char ch) {
		Jamo j = findCh(ch);
		if (j == null)
			return false;
		return j.isDouble();
	}

	public static boolean hasChoseong(char ch) {
		Jamo j = findCh(ch);
		if (j == null)
			return false;
		return j.hasChoseong();
	}

	public static boolean hasJongseong(char ch) {
		Jamo j = findCh(ch);
		if (j == null)
			return false;
		return j.hasJongseong();
	}

	static final int[] shifts = new int[] { 0x1100, 0x1100 + 97, 0x1100 + 168 };

	public static String splitSyllable(char c) {
		int[] iparts = new int[3];
		int ci = (int) c;
		if (!(0xAC00 <= ci && ci <= 0xD7A3))
			return c + "";
		ci -= 0xAC00;
		iparts[2] = ci % 28 - 1;
		ci /= 28;
		iparts[1] = ci % 21;
		ci /= 21;
		iparts[0] = ci;
		String rslt = "";
		for (int j = 0; j < 3; ++j)
			if (iparts[j] != -1)
				rslt += (char) (iparts[j] + shifts[j]);
		return rslt;
	}

	public static char canonicalize(char ch) {
		Jamo j = findCh(ch);
		if (j != null)
			return j.getCanonic();
		else
			return ch;
	}

	public static String canonicalize(String s) {
		String r = "";
		for (int i = 0; i < s.length(); ++i)
			r += canonicalize(s.charAt(i));
		return r;
	}

	public static String splitDigraphs(char ch) {
		Jamo j = findCh(ch);
		if (j != null)
			return j.getSplittedForm();
		else
			return ch + "";
	}

	public static String splitDigraphs(String s) {
		String r = "";
		for (int i = 0; i < s.length(); ++i) {
			r += splitDigraphs(s.charAt(i));
		}
		return r;
	}

	public static String splitSyllable(String s) {
		String rslt = "";
		for (int i = 0; i < s.length(); ++i) {
			rslt += splitSyllable(s.charAt(i));
		}
		return rslt;
	}

	public static String splitDigraphsSyllables(char ch) {
		if (isSyllable(ch)) {
			String sep = splitSyllable(ch);
			String r = "";
			for (int j = 0; j < sep.length(); ++j)
				r += findCh(sep.charAt(j)).getSplittedForm();
			return r;
		} else {
			Jamo j = findCh(ch);
			if (j != null)
				return j.getSplittedForm();
			else
				return ch + "";
		}
	}

	public static String splitDigraphsSyllables(String s) {
		String r = "";
		for (int i = 0; i < s.length(); ++i) {
			r += splitDigraphsSyllables(s.charAt(i));
		}
		return r;
	}

	static Syllabler conv = new Syllabler();

	public static String concatSyllables(String s) {
		return conv.concatSyllables(s);
	}

	public static boolean canChosejoung(String s) {
		Jamo j = findStr(splitDigraphs(s));
		if (j == null)
			return false;
		return j.hasChoseong();
	}

	public static char toChosejoung(String s) {
		Jamo j = findStr(splitDigraphs(s));
		if (j == null)
			return 0;
		return j.choseong;
	}

	public static boolean canJongseong(String s) {
		Jamo j = findStr(splitDigraphs(s));
		if (j == null)
			return false;
		return j.hasJongseong();
	}

	public static boolean canDouble(char ch) {
		Jamo j = findCh(ch);
		if (j == null)
			return false;
		return j.canDouble();
	}

	private char canonic, choseong, jongseong;

	private String splittedForm;

	public int choseongNo() {
		return choseong - 0x1100;
	}

	public int jongseongNo() {
		return jongseong - 0x11a8;
	}

	public int vowelNo() {
		return canonic - 0x314F;
	}

	void checkAdd(char c) {
		if (c != 0)
			charMap.put(c, this);
	}

	public boolean hasChoseong() {
		return choseong != 0;
	}

	public boolean hasJongseong() {
		return jongseong != 0;
	}

	void assertIt(boolean t) {
		if (!t)
			System.err
					.println(Integer.toHexString((int) canonic) + "failed!!!");
	}

	public Jamo(char canonic) {
		this.canonic = canonic;
		this.splittedForm = canonic + "";
		charMap.put(canonic, this);
		charMap.put((char) (canonic - 0x314f + 0x1161), this);
	}

	public Jamo(char canonic, char choseong, char jongseong, String canonicSeq) {
		this.canonic = canonic;
		this.choseong = choseong;
		this.jongseong = jongseong;
		this.splittedForm = canonicSeq;

		/*
		 * assertIt(choseong == 0 || isChosejoung(choseong)); assertIt(jongseong ==
		 * 0 || isJongseong(jongseong)); assertIt(isCanonic(canonic)); for (int
		 * i = 0; i < canonicSeq.length(); ++i)
		 * assertIt(isCanonic(canonicSeq.charAt(i)));
		 */
		charMap.put(canonic, this);
		stringMap.put(canonicSeq, this);
		checkAdd(choseong);
		checkAdd(jongseong);
		if (canonicSeq.length() == 2
				&& canonicSeq.charAt(0) == canonicSeq.charAt(1)) {
			isDbl = true;
			findCh(canonic).canDbl = true;
		}
	}

	public Jamo(char canonic, char choseong, char jongseong) {
		this(canonic, choseong, jongseong, canonic + "");
	}

	public static Jamo findCh(char ch) {
		return charMap.get(ch);
	}

	public static Jamo findStr(String s) {
		return s.length() == 1 ? charMap.get(s.charAt(0)) : stringMap
				.get(splitDigraphs(s));
	}

	boolean isDbl = false, canDbl = false;

	public boolean isDouble() {
		return isDbl;
	}

	public boolean canDouble() {
		return canDbl;
	}

	public char getCanonic() {
		return canonic;
	}

	public char getChoseong() {
		return choseong;
	}

	public char getJongseong() {
		return jongseong;
	}

	public String getSplittedForm() {
		return splittedForm;
	}

	static {
		new Jamo('ㄱ', 'ᄀ', 'ᆨ');
		new Jamo('ㄲ', 'ᄁ', 'ᆩ', "ㄱㄱ");
		new Jamo('ㄳ', '\0', 'ᆪ', "ㄱㅅ");
		new Jamo('ㄴ', 'ᄂ', 'ᆫ');
		new Jamo('ㄵ', '\0', 'ᆬ', "ㄴㅈ");
		new Jamo('ㄶ', '\0', 'ᆭ', "ㄴㅎ");
		new Jamo('ㄷ', 'ᄃ', 'ᆮ');
		new Jamo('ㄸ', 'ᄄ', '\0', "ㄷㄷ");
		new Jamo('ㄹ', 'ᄅ', 'ᆯ');
		new Jamo('ㄺ', '\0', 'ᆰ', "ㄹㄱ");
		new Jamo('ㄻ', '\0', 'ᆱ', "ㄹㅁ");
		new Jamo('ㄼ', '\0', 'ᆲ', "ㄹㅂ");
		new Jamo('ㄽ', '\0', 'ᆳ', "ㄹㅅ");
		new Jamo('ㄾ', '\0', 'ᆴ', "ㄹㅌ");
		new Jamo('ㄿ', '\0', 'ᆵ', "ㄹㅍ");
		new Jamo('ㅀ', '\0', 'ᆶ', "ㄹㅎ");
		new Jamo('ㅁ', 'ᄆ', 'ᆷ');
		new Jamo('ㅂ', 'ᄇ', 'ᆸ');
		new Jamo('ㅃ', 'ᄈ', '\0', "ㅂㅂ");
		new Jamo('ㅄ', '\0', 'ᆹ', "ㅂㅅ");
		new Jamo('ㅅ', 'ᄉ', 'ᆺ');
		new Jamo('ㅆ', 'ᄊ', 'ᆻ', "ㅅㅅ");
		new Jamo('ㅇ', 'ᄋ', 'ᆼ');
		new Jamo('ㅈ', 'ᄌ', 'ᆽ');
		new Jamo('ㅉ', 'ᄍ', '\0', "ㅈㅈ");
		new Jamo('ㅊ', 'ᄎ', 'ᆾ');
		new Jamo('ㅋ', 'ᄏ', 'ᆿ');
		new Jamo('ㅌ', 'ᄐ', 'ᇀ');
		new Jamo('ㅍ', 'ᄑ', 'ᇁ');
		new Jamo('ㅎ', 'ᄒ', 'ᇂ');

		for (char ch = 0x314F; ch <= 0x3163; ++ch)
			new Jamo(ch);
	}
}
