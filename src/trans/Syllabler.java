package trans;

import static trans.Jamo.canChosejoung;
import static trans.Jamo.isConsonant;
import static trans.Jamo.isVowel;

public class Syllabler {
	private void flushSyllable() {
		Jamo ja = null, jb = Jamo.findCh(b), jc = null;
		for (int i = Math.min(2, a.length()); i >= 1; --i) {
			ja = Jamo.findStr(a.substring(a.length() - i, a.length()));
			if (ja != null) {
				put(a.substring(0, a.length() - i));
				break;
			}
		}
		if (ja == null) {
			ja = Jamo.findCh('ᄋ');
		}
		String c2 = c;
		for (int i = Math.min(2, c.length()); i >= 1; --i) {
			jc = Jamo.findStr(c.substring(0, i));
			if (jc != null) {
				c2 = c.substring(i, c.length());
				break;
			}
		}
		put(Jamo.mkSyllable(ja.choseongNo(), jb.vowelNo(), jc == null ? -1 : jc
				.jongseongNo()));
		put(c2);
		a = c = "";
		b = 0;
	}

	private String a, c;

	private char b;

	private int state;

	private static final int STATE_WAIT_A = 0;

	private static final int STATE_WAIT_C = 1;

	private String outStr;

	private void put(char ch) {
		outStr += ch;
	}

	private void put(String s) {
		outStr += s;
	}

	private static final int KIND_VOWEL = 0;

	private static final int KIND_CONSONANT = 1;

	private static final int KIND_OTHER = 2;

	public static int charKind(char ch) {
		// System.out.println(Integer.toHexString((int)ch));
		if (isConsonant(ch)) {
			return KIND_CONSONANT;
		}
		if (isVowel(ch)) {
			return KIND_VOWEL;
		}
		return KIND_OTHER;
	}

	char curChar, nextChar;

	private void processWaitA() {
		switch (charKind(curChar)) {
		case KIND_VOWEL:
			b = curChar;
			c = "";
			state = STATE_WAIT_C;
			break;
		case KIND_CONSONANT:
			a += curChar;
			state = STATE_WAIT_A;
			break;
		default:
			put(a);
			put(curChar);
			a = "";
			state = STATE_WAIT_A;
		}
	}

	private void processWaitC() {
		switch (charKind(curChar)) {
		case KIND_VOWEL:
			String c2 = "";
			if (c.length() > 0) {
				for (int i = Math.min(2, c.length()); i >= 1; --i) {
					c2 = c.substring(c.length() - i, c.length());
					if (canChosejoung(c2)) {
						c = c.substring(0, c.length() - i);
						break;
					} else {
						c2 = "";
					}
				}
			}
			flushSyllable();
			a = c2;
			b = curChar;
			c = "";
			state = STATE_WAIT_C;
			break;
		case KIND_CONSONANT:
			c += curChar;
			state = STATE_WAIT_C;
			break;
		case KIND_OTHER:
			flushSyllable();
			if (curChar != '-' || charKind(nextChar) == KIND_OTHER)
				put(curChar);
			state = STATE_WAIT_A;
			break;
		}
	}

	public String concatSyllables(String inStr) {
		int inStrLen = inStr.length();
		if (inStrLen == 0)
			return "";
		state = STATE_WAIT_A;
		outStr = "";
		a = c = "";
		b = '\0';
		nextChar = inStr.charAt(0);
		for (int i = 1; i <= inStrLen; ++i) {
			curChar = nextChar;
			nextChar = i == inStrLen ? '\0' : inStr.charAt(i);
			switch (state) {
			case STATE_WAIT_A:
				processWaitA();
				break;
			case STATE_WAIT_C:
				processWaitC();
				break;
			}
		}
		switch (state) {
		case STATE_WAIT_A:
			put(a);
			break;
		case STATE_WAIT_C:
			flushSyllable();
			break;
		}
		return outStr;
	}
}
