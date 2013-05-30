package trans;

import static trans.Jamo.isConsonant;
import static trans.Jamo.isVowel;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

class KoncevicC2R {
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

	private static final int KIND_SYLLABLE = 3;

	public static int charKind(char ch) {		
		if (isConsonant(ch)) {
			return KIND_CONSONANT;
		}
		if (isVowel(ch)) {
			return KIND_VOWEL;
		}
		if (Jamo.isSyllable(ch)) {
			return KIND_SYLLABLE;
		}
		return KIND_OTHER;
	}

	char curChar;

	public String koncevicCorToRu(String inStr) {
		inStr = Jamo.concatSyllables(inStr);
		outStr = "";
		for (int i = 0; i < inStr.length(); ++i) {
			curChar = inStr.charAt(i);
			switch (charKind(curChar)) {
			case KIND_VOWEL:
				put(vowelMap.get(Jamo.canonicalize(curChar)));
				break;
			case KIND_CONSONANT:
				curChar = Jamo.canonicalize(curChar);
				put(sylStartMap.containsKey(curChar) ? sylStartMap.get(curChar)
						: wordEndMap.get(curChar));
				break;
			case KIND_SYLLABLE:
				String sepd = Jamo.canonicalize(Jamo.splitSyllable(curChar));
				put(wordStartMap.get(sepd.charAt(0)));
				put(vowelMap.get(sepd.charAt(1)));
				String rest = sepd.length() > 2 ? sepd.substring(2, 3) : "";
				for (;;) {
					++i;
					if (i >= inStr.length())
						break;
					curChar = inStr.charAt(i);
					if (charKind(curChar) != KIND_SYLLABLE) {
						--i;
						break;
					}
					sepd = Jamo.canonicalize(Jamo.splitSyllable(curChar));
					rest += sepd.charAt(0);
					if (c2rMap.containsKey(rest))
						put(c2rMap.get((rest)));
					else
						put(rest);
					put(vowelMap.get(sepd.charAt(1)));
					rest = sepd.length() > 2 ? sepd.substring(2, 3) : "";
				}
				if (rest.length() == 1) {
					put(wordEndMap.get(rest.charAt(0)));
				}
				break;
			case KIND_OTHER:
				if (curChar != '-' || i < inStr.length() - 1
						|| charKind(inStr.charAt(i)) == KIND_OTHER) {
					put(curChar);
				} else
					break;
			}
		}
		return outStr;
	}

	static Map<String, String> c2rMap = new HashMap<String, String>(),
			c2rNoDigraphMap = new HashMap<String, String>();

	static Map<Character, String> wordStartMap = new TreeMap<Character, String>(),
			sylStartMap = new TreeMap<Character, String>(),
			wordEndMap = new TreeMap<Character, String>(),
			vowelMap = new TreeMap<Character, String>();

	static Locale ruLocale = new Locale("ru");

	private static void addKoncevicPair(char a, char b, String ru) {
		String ab = a + "" + b;
		c2rMap.put(ab, ru);
		c2rNoDigraphMap.put(Jamo.splitDigraphs(ab), ru);
	}

	private static void addKoncevicStart(char ch, String wordStart,
			String sylStart) {
		ch = Jamo.canonicalize(ch);
		wordStartMap.put(ch, wordStart);
		sylStartMap.put(ch, sylStart);
		c2rMap.put(ch + "", sylStart);
	}

	private static void addKoncevicEnding(char ch, String wordEnd) {
		wordEndMap.put(ch, wordEnd);
	}

	private static void addKoncevicVowel(char ch, String vowel) {
		vowelMap.put(ch, vowel);
	}

	static {
		addKoncevicPairs();
		addKoncevicEndings();
		addKoncevicStarts();
		addKoncevicVowels();
	}

	private static void addKoncevicVowels() {
		addKoncevicVowel('ㅏ', "а");
		addKoncevicVowel('ㅑ', "я");
		addKoncevicVowel('ㅓ', "о");
		addKoncevicVowel('ㅕ', "ё");
		addKoncevicVowel('ㅗ', "о");
		addKoncevicVowel('ㅛ', "ё");
		addKoncevicVowel('ㅜ', "у");
		addKoncevicVowel('ㅠ', "ю");
		addKoncevicVowel('ㅡ', "ы");
		addKoncevicVowel('ㅣ', "и");		
		addKoncevicVowel('ㅐ', "э");
		addKoncevicVowel('ㅒ', "йя");
		addKoncevicVowel('ㅔ', "е");
		addKoncevicVowel('ㅖ', "йе");
		addKoncevicVowel('ㅚ', "ве");
		addKoncevicVowel('ㅟ', "ви");
		addKoncevicVowel('ㅝ', "во");
		addKoncevicVowel('ㅙ', "вэ");
		addKoncevicVowel('ㅞ', "ве");
		addKoncevicVowel('ㅢ', "ый");
		addKoncevicVowel('ㅘ', "ва");
	}

	private static void addKoncevicStarts() {
		addKoncevicStart('ㄱ', "к", "г");
		addKoncevicStart('ㄲ', "кк", "кк");
		addKoncevicStart('ㄴ', "н", "н");
		addKoncevicStart('ㄷ', "т", "д");
		addKoncevicStart('ㄸ', "тт", "тт");
		addKoncevicStart('ㄹ', "р", "р");
		addKoncevicStart('ㅁ', "м", "м");
		addKoncevicStart('ㅂ', "п", "б");
		addKoncevicStart('ㅃ', "п", "пп");
		addKoncevicStart('ㅅ', "с", "с");
		addKoncevicStart('ㅆ', "сс", "сс");
		addKoncevicStart('ㅇ', "", "");
		addKoncevicStart('ㅈ', "ч", "дж");
		addKoncevicStart('ㅉ', "чч", "чч");
		addKoncevicStart('ㅊ', "чх", "чх");
		addKoncevicStart('ㅋ', "кх", "кх");
		addKoncevicStart('ㅌ', "тх", "тх");
		addKoncevicStart('ㅍ', "пх", "пх");
		addKoncevicStart('ㅎ', "х", "х");
	}

	private static void addKoncevicEndings() {
		addKoncevicEnding('ㄱ', "к");
		addKoncevicEnding('ㄲ', "кк");
		addKoncevicEnding('ㄳ', "к");
		addKoncevicEnding('ㄴ', "н");
		addKoncevicEnding('ㄵ', "н");
		addKoncevicEnding('ㄶ', "н");
		addKoncevicEnding('ㄷ', "т");
		addKoncevicEnding('ㄹ', "ль");
		addKoncevicEnding('ㄺ', "к");
		addKoncevicEnding('ㄻ', "м");
		addKoncevicEnding('ㄼ', "ль");
		addKoncevicEnding('ㄽ', "ль");
		addKoncevicEnding('ㄾ', "ль");
		addKoncevicEnding('ㄿ', "п");
		addKoncevicEnding('ㅀ', "ль");
		addKoncevicEnding('ㅁ', "м");
		addKoncevicEnding('ㅂ', "п");
		addKoncevicEnding('ㅄ', "п");
		addKoncevicEnding('ㅅ', "т");
		addKoncevicEnding('ㅆ', "т");
		addKoncevicEnding('ㅇ', "нъ");
		addKoncevicEnding('ㅈ', "т");
		addKoncevicEnding('ㅊ', "т");
		addKoncevicEnding('ㅋ', "кх");
		addKoncevicEnding('ㅌ', "т");
		addKoncevicEnding('ㅍ', "п");
		addKoncevicEnding('ㅎ', "т");
	}

	private static void addKoncevicPairs() {
		addKoncevicPair('ㄱ', 'ㄱ', "кк");
		addKoncevicPair('ㄱ', 'ㄴ', "нън");
		addKoncevicPair('ㄱ', 'ㄷ', "кт");
		addKoncevicPair('ㄱ', 'ㄹ', "нън");
		addKoncevicPair('ㄱ', 'ㅁ', "нъм");
		addKoncevicPair('ㄱ', 'ㅂ', "кп");
		addKoncevicPair('ㄱ', 'ㅅ', "кс");
		addKoncevicPair('ㄱ', 'ㅈ', "кч");
		addKoncevicPair('ㄱ', 'ㅊ', "кчх");
		addKoncevicPair('ㄱ', 'ㅋ', "ккх");
		addKoncevicPair('ㄱ', 'ㅌ', "кх");
		addKoncevicPair('ㄱ', 'ㅍ', "кпх");
		addKoncevicPair('ㄱ', 'ㅎ', "кх");
		addKoncevicPair('ㄱ', 'ㄲ', "кк");
		addKoncevicPair('ㄱ', 'ㄸ', "ктт");
		addKoncevicPair('ㄱ', 'ㅃ', "кпп");
		addKoncevicPair('ㄱ', 'ㅆ', "ксс");
		addKoncevicPair('ㄱ', 'ㅉ', "кчч");
		addKoncevicPair('ㄱ', 'ㅇ', "г");
		addKoncevicPair('ㄲ', 'ㄱ', "кк");
		addKoncevicPair('ㄲ', 'ㄴ', "нън");
		addKoncevicPair('ㄲ', 'ㄷ', "кт");
		addKoncevicPair('ㄲ', 'ㄹ', "нън");
		addKoncevicPair('ㄲ', 'ㅁ', "нъм");
		addKoncevicPair('ㄲ', 'ㅂ', "кп");
		addKoncevicPair('ㄲ', 'ㅅ', "кс");
		addKoncevicPair('ㄲ', 'ㅈ', "кч");
		addKoncevicPair('ㄲ', 'ㅊ', "кчх");
		addKoncevicPair('ㄲ', 'ㅋ', "ккх");
		addKoncevicPair('ㄲ', 'ㅌ', "кх");
		addKoncevicPair('ㄲ', 'ㅍ', "кпх");
		addKoncevicPair('ㄲ', 'ㅎ', "кх");
		addKoncevicPair('ㄲ', 'ㄲ', "кк");
		addKoncevicPair('ㄲ', 'ㄸ', "ктт");
		addKoncevicPair('ㄲ', 'ㅃ', "кпп");
		addKoncevicPair('ㄲ', 'ㅆ', "ксс");
		addKoncevicPair('ㄲ', 'ㅉ', "кчч");
		addKoncevicPair('ㄲ', 'ㅇ', "г");
		addKoncevicPair('ㄳ', 'ㄱ', "кк");
		addKoncevicPair('ㄳ', 'ㄴ', "нън");
		addKoncevicPair('ㄳ', 'ㄷ', "кт");
		addKoncevicPair('ㄳ', 'ㄹ', "нън");
		addKoncevicPair('ㄳ', 'ㅁ', "нъм");
		addKoncevicPair('ㄳ', 'ㅂ', "кп");
		addKoncevicPair('ㄳ', 'ㅅ', "кс");
		addKoncevicPair('ㄳ', 'ㅈ', "кч");
		addKoncevicPair('ㄳ', 'ㅊ', "кчх");
		addKoncevicPair('ㄳ', 'ㅋ', "ккх");
		addKoncevicPair('ㄳ', 'ㅌ', "кх");
		addKoncevicPair('ㄳ', 'ㅍ', "кпх");
		addKoncevicPair('ㄳ', 'ㅎ', "кх");
		addKoncevicPair('ㄳ', 'ㄲ', "кк");
		addKoncevicPair('ㄳ', 'ㄸ', "ктт");
		addKoncevicPair('ㄳ', 'ㅃ', "кпп");
		addKoncevicPair('ㄳ', 'ㅆ', "ксс");
		addKoncevicPair('ㄳ', 'ㅉ', "кчч");
		addKoncevicPair('ㄳ', 'ㅇ', "г");
		addKoncevicPair('ㄴ', 'ㄱ', "нг");
		addKoncevicPair('ㄴ', 'ㄴ', "нн");
		addKoncevicPair('ㄴ', 'ㄷ', "нд");
		addKoncevicPair('ㄴ', 'ㄹ', "лл");
		addKoncevicPair('ㄴ', 'ㅁ', "нм");
		addKoncevicPair('ㄴ', 'ㅂ', "нб");
		addKoncevicPair('ㄴ', 'ㅅ', "нс");
		addKoncevicPair('ㄴ', 'ㅈ', "ндж");
		addKoncevicPair('ㄴ', 'ㅊ', "нчх");
		addKoncevicPair('ㄴ', 'ㅋ', "нкх");
		addKoncevicPair('ㄴ', 'ㅌ', "нтх");
		addKoncevicPair('ㄴ', 'ㅍ', "нпх");
		addKoncevicPair('ㄴ', 'ㅎ', "нх");
		addKoncevicPair('ㄴ', 'ㄲ', "нкк");
		addKoncevicPair('ㄴ', 'ㄸ', "нтт");
		addKoncevicPair('ㄴ', 'ㅃ', "нпп");
		addKoncevicPair('ㄴ', 'ㅆ', "нсс");
		addKoncevicPair('ㄴ', 'ㅉ', "нчч");
		addKoncevicPair('ㄴ', 'ㅇ', "н");
		addKoncevicPair('ㄵ', 'ㄱ', "нк");
		addKoncevicPair('ㄵ', 'ㄴ', "нн");
		addKoncevicPair('ㄵ', 'ㄷ', "нт");
		addKoncevicPair('ㄵ', 'ㄹ', "лл");
		addKoncevicPair('ㄵ', 'ㅁ', "нм");
		addKoncevicPair('ㄵ', 'ㅂ', "нп");
		addKoncevicPair('ㄵ', 'ㅅ', "нсс");
		addKoncevicPair('ㄵ', 'ㅈ', "нч");
		addKoncevicPair('ㄵ', 'ㅊ', "нчх");
		addKoncevicPair('ㄵ', 'ㅋ', "нкх");
		addKoncevicPair('ㄵ', 'ㅌ', "нтх");
		addKoncevicPair('ㄵ', 'ㅍ', "нпх");
		addKoncevicPair('ㄵ', 'ㅎ', "нчх");
		addKoncevicPair('ㄵ', 'ㄲ', "нкк");
		addKoncevicPair('ㄵ', 'ㄸ', "нтт");
		addKoncevicPair('ㄵ', 'ㅃ', "нпп");
		addKoncevicPair('ㄵ', 'ㅆ', "нсс");
		addKoncevicPair('ㄵ', 'ㅉ', "нчч");
		addKoncevicPair('ㄵ', 'ㅇ', "ндж");
		addKoncevicPair('ㄶ', 'ㄱ', "нкх");
		addKoncevicPair('ㄶ', 'ㄴ', "нн");
		addKoncevicPair('ㄶ', 'ㄷ', "нтх");
		addKoncevicPair('ㄶ', 'ㄹ', "лл");
		addKoncevicPair('ㄶ', 'ㅁ', "нм");
		addKoncevicPair('ㄶ', 'ㅂ', "нпх");
		addKoncevicPair('ㄶ', 'ㅅ', "нсс");
		addKoncevicPair('ㄶ', 'ㅈ', "нчх");
		addKoncevicPair('ㄶ', 'ㅊ', "нчх");
		addKoncevicPair('ㄶ', 'ㅋ', "нкх");
		addKoncevicPair('ㄶ', 'ㅌ', "нтх");
		addKoncevicPair('ㄶ', 'ㅍ', "нпх");
		addKoncevicPair('ㄶ', 'ㅎ', "нх");
		addKoncevicPair('ㄶ', 'ㄲ', "нкк");
		addKoncevicPair('ㄶ', 'ㄸ', "нтт");
		addKoncevicPair('ㄶ', 'ㅃ', "нпп");
		addKoncevicPair('ㄶ', 'ㅆ', "нсс");
		addKoncevicPair('ㄶ', 'ㅉ', "нчч");
		addKoncevicPair('ㄶ', 'ㅇ', "н");
		addKoncevicPair('ㄷ', 'ㄱ', "тк");
		addKoncevicPair('ㄷ', 'ㄴ', "нн");
		addKoncevicPair('ㄷ', 'ㄷ', "тт");
		addKoncevicPair('ㄷ', 'ㄹ', "нн");
		addKoncevicPair('ㄷ', 'ㅁ', "нм");
		addKoncevicPair('ㄷ', 'ㅂ', "тп");
		addKoncevicPair('ㄷ', 'ㅅ', "сс");
		addKoncevicPair('ㄷ', 'ㅈ', "тч");
		addKoncevicPair('ㄷ', 'ㅊ', "тчх");
		addKoncevicPair('ㄷ', 'ㅋ', "ткх");
		addKoncevicPair('ㄷ', 'ㅌ', "ттх");
		addKoncevicPair('ㄷ', 'ㅍ', "тпх");
		addKoncevicPair('ㄷ', 'ㅎ', "чх");
		addKoncevicPair('ㄷ', 'ㄲ', "ткк");
		addKoncevicPair('ㄷ', 'ㄸ', "тт");
		addKoncevicPair('ㄷ', 'ㅃ', "тпп");
		addKoncevicPair('ㄷ', 'ㅆ', "сс");
		addKoncevicPair('ㄷ', 'ㅉ', "чч");
		addKoncevicPair('ㄷ', 'ㅇ', "д");
		addKoncevicPair('ㄹ', 'ㄱ', "льг");
		addKoncevicPair('ㄹ', 'ㄴ', "лл");
		addKoncevicPair('ㄹ', 'ㄷ', "льт");
		addKoncevicPair('ㄹ', 'ㄹ', "лл");
		addKoncevicPair('ㄹ', 'ㅁ', "льм");
		addKoncevicPair('ㄹ', 'ㅂ', "льб");
		addKoncevicPair('ㄹ', 'ㅅ', "льс");
		addKoncevicPair('ㄹ', 'ㅈ', "льч");
		addKoncevicPair('ㄹ', 'ㅊ', "льчх");
		addKoncevicPair('ㄹ', 'ㅋ', "лькх");
		addKoncevicPair('ㄹ', 'ㅌ', "льтх");
		addKoncevicPair('ㄹ', 'ㅍ', "льпх");
		addKoncevicPair('ㄹ', 'ㅎ', "рх");
		addKoncevicPair('ㄹ', 'ㄲ', "лькк");
		addKoncevicPair('ㄹ', 'ㄸ', "льтт");
		addKoncevicPair('ㄹ', 'ㅃ', "льпп");
		addKoncevicPair('ㄹ', 'ㅆ', "льсс");
		addKoncevicPair('ㄹ', 'ㅉ', "льчч");
		addKoncevicPair('ㄹ', 'ㅇ', "р");
		addKoncevicPair('ㄺ', 'ㄱ', "г");
		addKoncevicPair('ㄺ', 'ㄴ', "нъ");
		addKoncevicPair('ㄺ', 'ㄷ', "кт");
		addKoncevicPair('ㄺ', 'ㄹ', "нън");
		addKoncevicPair('ㄺ', 'ㅁ', "нъм");
		addKoncevicPair('ㄺ', 'ㅂ', "кп");
		addKoncevicPair('ㄺ', 'ㅅ', "кс");
		addKoncevicPair('ㄺ', 'ㅈ', "кч");
		addKoncevicPair('ㄺ', 'ㅊ', "кчх");
		addKoncevicPair('ㄺ', 'ㅋ', "ккх");
		addKoncevicPair('ㄺ', 'ㅌ', "кх");
		addKoncevicPair('ㄺ', 'ㅍ', "кпх");
		addKoncevicPair('ㄺ', 'ㅎ', "кх");
		addKoncevicPair('ㄺ', 'ㄲ', "кк");
		addKoncevicPair('ㄺ', 'ㄸ', "ктт");
		addKoncevicPair('ㄺ', 'ㅃ', "кпп");
		addKoncevicPair('ㄺ', 'ㅆ', "ксс");
		addKoncevicPair('ㄺ', 'ㅉ', "кчч");
		addKoncevicPair('ㄺ', 'ㅇ', "льг");
		addKoncevicPair('ㄻ', 'ㄱ', "мг");
		addKoncevicPair('ㄻ', 'ㄴ', "мн");
		addKoncevicPair('ㄻ', 'ㄷ', "мд");
		addKoncevicPair('ㄻ', 'ㄹ', "мн");
		addKoncevicPair('ㄻ', 'ㅁ', "мм");
		addKoncevicPair('ㄻ', 'ㅂ', "мб");
		addKoncevicPair('ㄻ', 'ㅅ', "мс");
		addKoncevicPair('ㄻ', 'ㅈ', "мдж");
		addKoncevicPair('ㄻ', 'ㅊ', "мчх");
		addKoncevicPair('ㄻ', 'ㅋ', "мкх");
		addKoncevicPair('ㄻ', 'ㅌ', "мтх");
		addKoncevicPair('ㄻ', 'ㅍ', "мпх");
		addKoncevicPair('ㄻ', 'ㅎ', "мх");
		addKoncevicPair('ㄻ', 'ㄲ', "мкк");
		addKoncevicPair('ㄻ', 'ㄸ', "мтт");
		addKoncevicPair('ㄻ', 'ㅃ', "мпп");
		addKoncevicPair('ㄻ', 'ㅆ', "мсс");
		addKoncevicPair('ㄻ', 'ㅉ', "мчч");
		addKoncevicPair('ㄻ', 'ㅇ', "льм");
		addKoncevicPair('ㄼ', 'ㄱ', "льг");
		addKoncevicPair('ㄼ', 'ㄴ', "лл");
		addKoncevicPair('ㄼ', 'ㄷ', "льт");
		addKoncevicPair('ㄼ', 'ㄹ', "лл");
		addKoncevicPair('ㄼ', 'ㅁ', "льм");
		addKoncevicPair('ㄼ', 'ㅂ', "льб");
		addKoncevicPair('ㄼ', 'ㅅ', "льсс");
		addKoncevicPair('ㄼ', 'ㅈ', "льч");
		addKoncevicPair('ㄼ', 'ㅊ', "льчх");
		addKoncevicPair('ㄼ', 'ㅋ', "мкх");
		addKoncevicPair('ㄼ', 'ㅌ', "льтх");
		addKoncevicPair('ㄼ', 'ㅍ', "льпх");
		addKoncevicPair('ㄼ', 'ㅎ', "рх");
		addKoncevicPair('ㄼ', 'ㄲ', "лькк");
		addKoncevicPair('ㄼ', 'ㄸ', "льтт");
		addKoncevicPair('ㄼ', 'ㅃ', "льпп");
		addKoncevicPair('ㄼ', 'ㅆ', "льсс");
		addKoncevicPair('ㄼ', 'ㅉ', "льчч");
		addKoncevicPair('ㄼ', 'ㅇ', "льб");
		addKoncevicPair('ㄽ', 'ㄱ', "льк");
		addKoncevicPair('ㄽ', 'ㄴ', "лл");
		addKoncevicPair('ㄽ', 'ㄷ', "льт");
		addKoncevicPair('ㄽ', 'ㄹ', "лл");
		addKoncevicPair('ㄽ', 'ㅁ', "льм");
		addKoncevicPair('ㄽ', 'ㅂ', "льп");
		addKoncevicPair('ㄽ', 'ㅅ', "льсс");
		addKoncevicPair('ㄽ', 'ㅈ', "льч");
		addKoncevicPair('ㄽ', 'ㅊ', "льчх");
		addKoncevicPair('ㄽ', 'ㅋ', "лькх");
		addKoncevicPair('ㄽ', 'ㅌ', "льтх");
		addKoncevicPair('ㄽ', 'ㅍ', "льпх");
		addKoncevicPair('ㄽ', 'ㅎ', "рх");
		addKoncevicPair('ㄽ', 'ㄲ', "лькк");
		addKoncevicPair('ㄽ', 'ㄸ', "льтт");
		addKoncevicPair('ㄽ', 'ㅃ', "льпп");
		addKoncevicPair('ㄽ', 'ㅆ', "льсс");
		addKoncevicPair('ㄽ', 'ㅉ', "льчч");
		addKoncevicPair('ㄽ', 'ㅇ', "льс");
		addKoncevicPair('ㄾ', 'ㄱ', "льк");
		addKoncevicPair('ㄾ', 'ㄴ', "лл");
		addKoncevicPair('ㄾ', 'ㄷ', "льт");
		addKoncevicPair('ㄾ', 'ㄹ', "лл");
		addKoncevicPair('ㄾ', 'ㅁ', "льм");
		addKoncevicPair('ㄾ', 'ㅂ', "льп");
		addKoncevicPair('ㄾ', 'ㅅ', "льсс");
		addKoncevicPair('ㄾ', 'ㅈ', "льч");
		addKoncevicPair('ㄾ', 'ㅊ', "льчх");
		addKoncevicPair('ㄾ', 'ㅋ', "лькх");
		addKoncevicPair('ㄾ', 'ㅌ', "льтх");
		addKoncevicPair('ㄾ', 'ㅍ', "льпх");
		addKoncevicPair('ㄾ', 'ㅎ', "рх");
		addKoncevicPair('ㄾ', 'ㄲ', "лькк");
		addKoncevicPair('ㄾ', 'ㄸ', "льтт");
		addKoncevicPair('ㄾ', 'ㅃ', "льпп");
		addKoncevicPair('ㄾ', 'ㅆ', "льсс");
		addKoncevicPair('ㄾ', 'ㅉ', "льчч");
		addKoncevicPair('ㄾ', 'ㅇ', "льтх");
		addKoncevicPair('ㄿ', 'ㄱ', "пк");
		addKoncevicPair('ㄿ', 'ㄴ', "мн");
		addKoncevicPair('ㄿ', 'ㄷ', "пт");
		addKoncevicPair('ㄿ', 'ㄹ', "мн");
		addKoncevicPair('ㄿ', 'ㅁ', "мм");
		addKoncevicPair('ㄿ', 'ㅂ', "пп");
		addKoncevicPair('ㄿ', 'ㅅ', "пс");
		addKoncevicPair('ㄿ', 'ㅈ', "пч");
		addKoncevicPair('ㄿ', 'ㅊ', "пчх");
		addKoncevicPair('ㄿ', 'ㅋ', "пкх");
		addKoncevicPair('ㄿ', 'ㅌ', "птх");
		addKoncevicPair('ㄿ', 'ㅍ', "ппх");
		addKoncevicPair('ㄿ', 'ㅎ', "пх");
		addKoncevicPair('ㄿ', 'ㄲ', "пкк");
		addKoncevicPair('ㄿ', 'ㄸ', "птт");
		addKoncevicPair('ㄿ', 'ㅃ', "пп");
		addKoncevicPair('ㄿ', 'ㅆ', "псс");
		addKoncevicPair('ㄿ', 'ㅉ', "пчч");
		addKoncevicPair('ㄿ', 'ㅇ', "льпх");
		addKoncevicPair('ㅀ', 'ㄱ', "лькх");
		addKoncevicPair('ㅀ', 'ㄴ', "лл");
		addKoncevicPair('ㅀ', 'ㄷ', "льтх");
		addKoncevicPair('ㅀ', 'ㄹ', "лл");
		addKoncevicPair('ㅀ', 'ㅁ', "льм");
		addKoncevicPair('ㅀ', 'ㅂ', "льпх");
		addKoncevicPair('ㅀ', 'ㅅ', "льсс");
		addKoncevicPair('ㅀ', 'ㅈ', "льчх");
		addKoncevicPair('ㅀ', 'ㅊ', "льчх");
		addKoncevicPair('ㅀ', 'ㅋ', "лькх");
		addKoncevicPair('ㅀ', 'ㅌ', "льтх");
		addKoncevicPair('ㅀ', 'ㅍ', "льпх");
		addKoncevicPair('ㅀ', 'ㅎ', "рх");
		addKoncevicPair('ㅀ', 'ㄲ', "лькк");
		addKoncevicPair('ㅀ', 'ㄸ', "льтт");
		addKoncevicPair('ㅀ', 'ㅃ', "льпп");
		addKoncevicPair('ㅀ', 'ㅆ', "льсс");
		addKoncevicPair('ㅀ', 'ㅉ', "льчч");
		addKoncevicPair('ㅀ', 'ㅇ', "р");
		addKoncevicPair('ㅁ', 'ㄱ', "мг");
		addKoncevicPair('ㅁ', 'ㄴ', "мн");
		addKoncevicPair('ㅁ', 'ㄷ', "мд");
		addKoncevicPair('ㅁ', 'ㄹ', "мн");
		addKoncevicPair('ㅁ', 'ㅁ', "мм");
		addKoncevicPair('ㅁ', 'ㅂ', "мб");
		addKoncevicPair('ㅁ', 'ㅅ', "мс");
		addKoncevicPair('ㅁ', 'ㅈ', "мдж");
		addKoncevicPair('ㅁ', 'ㅊ', "мчх");
		addKoncevicPair('ㅁ', 'ㅋ', "мкх");
		addKoncevicPair('ㅁ', 'ㅌ', "мтх");
		addKoncevicPair('ㅁ', 'ㅍ', "мпх");
		addKoncevicPair('ㅁ', 'ㅎ', "мх");
		addKoncevicPair('ㅁ', 'ㄲ', "мкк");
		addKoncevicPair('ㅁ', 'ㄸ', "мтт");
		addKoncevicPair('ㅁ', 'ㅃ', "мпп");
		addKoncevicPair('ㅁ', 'ㅆ', "мсс");
		addKoncevicPair('ㅁ', 'ㅉ', "мчч");
		addKoncevicPair('ㅁ', 'ㅇ', "м");
		addKoncevicPair('ㅂ', 'ㄱ', "пк");
		addKoncevicPair('ㅂ', 'ㄴ', "мн");
		addKoncevicPair('ㅂ', 'ㄷ', "пт");
		addKoncevicPair('ㅂ', 'ㄹ', "мн");
		addKoncevicPair('ㅂ', 'ㅁ', "пм");
		addKoncevicPair('ㅂ', 'ㅂ', "пп");
		addKoncevicPair('ㅂ', 'ㅅ', "пс");
		addKoncevicPair('ㅂ', 'ㅈ', "пч");
		addKoncevicPair('ㅂ', 'ㅊ', "пчх");
		addKoncevicPair('ㅂ', 'ㅋ', "пкх");
		addKoncevicPair('ㅂ', 'ㅌ', "птх");
		addKoncevicPair('ㅂ', 'ㅍ', "ппх");
		addKoncevicPair('ㅂ', 'ㅎ', "пх");
		addKoncevicPair('ㅂ', 'ㄲ', "пкк");
		addKoncevicPair('ㅂ', 'ㄸ', "птт");
		addKoncevicPair('ㅂ', 'ㅃ', "пп");
		addKoncevicPair('ㅂ', 'ㅆ', "псс");
		addKoncevicPair('ㅂ', 'ㅉ', "пчч");
		addKoncevicPair('ㅂ', 'ㅇ', "б");
		addKoncevicPair('ㅄ', 'ㄱ', "пк");
		addKoncevicPair('ㅄ', 'ㄴ', "мн");
		addKoncevicPair('ㅄ', 'ㄷ', "пт");
		addKoncevicPair('ㅄ', 'ㄹ', "мн");
		addKoncevicPair('ㅄ', 'ㅁ', "пм");
		addKoncevicPair('ㅄ', 'ㅂ', "пп");
		addKoncevicPair('ㅄ', 'ㅅ', "пс");
		addKoncevicPair('ㅄ', 'ㅈ', "пч");
		addKoncevicPair('ㅄ', 'ㅊ', "пчх");
		addKoncevicPair('ㅄ', 'ㅋ', "пкх");
		addKoncevicPair('ㅄ', 'ㅌ', "птх");
		addKoncevicPair('ㅄ', 'ㅍ', "ппх");
		addKoncevicPair('ㅄ', 'ㅎ', "пх");
		addKoncevicPair('ㅄ', 'ㄲ', "пкк");
		addKoncevicPair('ㅄ', 'ㄸ', "птт");
		addKoncevicPair('ㅄ', 'ㅃ', "пп");
		addKoncevicPair('ㅄ', 'ㅆ', "псс");
		addKoncevicPair('ㅄ', 'ㅉ', "пчч");
		addKoncevicPair('ㅄ', 'ㅇ', "пс");
		addKoncevicPair('ㅅ', 'ㄱ', "тк");
		addKoncevicPair('ㅅ', 'ㄴ', "нн");
		addKoncevicPair('ㅅ', 'ㄷ', "тт");
		addKoncevicPair('ㅅ', 'ㄹ', "нн");
		addKoncevicPair('ㅅ', 'ㅁ', "нм");
		addKoncevicPair('ㅅ', 'ㅂ', "тп");
		addKoncevicPair('ㅅ', 'ㅅ', "сс");
		addKoncevicPair('ㅅ', 'ㅈ', "тч");
		addKoncevicPair('ㅅ', 'ㅊ', "тчх");
		addKoncevicPair('ㅅ', 'ㅋ', "ткх");
		addKoncevicPair('ㅅ', 'ㅌ', "ттх");
		addKoncevicPair('ㅅ', 'ㅍ', "тпх");
		addKoncevicPair('ㅅ', 'ㅎ', "тх");
		addKoncevicPair('ㅅ', 'ㄲ', "ткк");
		addKoncevicPair('ㅅ', 'ㄸ', "тт");
		addKoncevicPair('ㅅ', 'ㅃ', "тпп");
		addKoncevicPair('ㅅ', 'ㅆ', "сс");
		addKoncevicPair('ㅅ', 'ㅉ', "чч");
		addKoncevicPair('ㅅ', 'ㅇ', "с");
		addKoncevicPair('ㅆ', 'ㄱ', "тк");
		addKoncevicPair('ㅆ', 'ㄴ', "нн");
		addKoncevicPair('ㅆ', 'ㄷ', "тт");
		addKoncevicPair('ㅆ', 'ㄹ', "нн");
		addKoncevicPair('ㅆ', 'ㅁ', "нм");
		addKoncevicPair('ㅆ', 'ㅂ', "тп");
		addKoncevicPair('ㅆ', 'ㅅ', "сс");
		addKoncevicPair('ㅆ', 'ㅈ', "тч");
		addKoncevicPair('ㅆ', 'ㅊ', "тчх");
		addKoncevicPair('ㅆ', 'ㅋ', "ткх");
		addKoncevicPair('ㅆ', 'ㅌ', "ттх");
		addKoncevicPair('ㅆ', 'ㅍ', "тпх");
		addKoncevicPair('ㅆ', 'ㅎ', "тх");
		addKoncevicPair('ㅆ', 'ㄲ', "ткк");
		addKoncevicPair('ㅆ', 'ㄸ', "тт");
		addKoncevicPair('ㅆ', 'ㅃ', "тпп");
		addKoncevicPair('ㅆ', 'ㅆ', "сс");
		addKoncevicPair('ㅆ', 'ㅉ', "чч");
		addKoncevicPair('ㅆ', 'ㅇ', "сс");
		addKoncevicPair('ㅇ', 'ㄱ', "нъг");
		addKoncevicPair('ㅇ', 'ㄴ', "нън");
		addKoncevicPair('ㅇ', 'ㄷ', "нъд");
		addKoncevicPair('ㅇ', 'ㄹ', "лл");
		addKoncevicPair('ㅇ', 'ㅁ', "нъм");
		addKoncevicPair('ㅇ', 'ㅂ', "нъб");
		addKoncevicPair('ㅇ', 'ㅅ', "нъсс");
		addKoncevicPair('ㅇ', 'ㅈ', "нъдж");
		addKoncevicPair('ㅇ', 'ㅊ', "нъчх");
		addKoncevicPair('ㅇ', 'ㅋ', "нъкх");
		addKoncevicPair('ㅇ', 'ㅌ', "нътх");
		addKoncevicPair('ㅇ', 'ㅍ', "нъпх");
		addKoncevicPair('ㅇ', 'ㅎ', "нъх");
		addKoncevicPair('ㅇ', 'ㄲ', "нъкк");
		addKoncevicPair('ㅇ', 'ㄸ', "нътт");
		addKoncevicPair('ㅇ', 'ㅃ', "нъпп");
		addKoncevicPair('ㅇ', 'ㅆ', "нъсс");
		addKoncevicPair('ㅇ', 'ㅉ', "нъчч");
		addKoncevicPair('ㅇ', 'ㅇ', "нъ");
		addKoncevicPair('ㅈ', 'ㄱ', "тк");
		addKoncevicPair('ㅈ', 'ㄴ', "нн");
		addKoncevicPair('ㅈ', 'ㄷ', "тт");
		addKoncevicPair('ㅈ', 'ㄹ', "нн");
		addKoncevicPair('ㅈ', 'ㅁ', "нм");
		addKoncevicPair('ㅈ', 'ㅂ', "тп");
		addKoncevicPair('ㅈ', 'ㅅ', "сс");
		addKoncevicPair('ㅈ', 'ㅈ', "чч");
		addKoncevicPair('ㅈ', 'ㅊ', "ччх");
		addKoncevicPair('ㅈ', 'ㅋ', "ткх");
		addKoncevicPair('ㅈ', 'ㅌ', "ттх");
		addKoncevicPair('ㅈ', 'ㅍ', "тпх");
		addKoncevicPair('ㅈ', 'ㅎ', "тх");
		addKoncevicPair('ㅈ', 'ㄲ', "ткк");
		addKoncevicPair('ㅈ', 'ㄸ', "тт");
		addKoncevicPair('ㅈ', 'ㅃ', "тпп");
		addKoncevicPair('ㅈ', 'ㅆ', "сс");
		addKoncevicPair('ㅈ', 'ㅉ', "чч");
		addKoncevicPair('ㅈ', 'ㅇ', "дж");
		addKoncevicPair('ㅊ', 'ㄱ', "тк");
		addKoncevicPair('ㅊ', 'ㄴ', "нн");
		addKoncevicPair('ㅊ', 'ㄷ', "тт");
		addKoncevicPair('ㅊ', 'ㄹ', "нн");
		addKoncevicPair('ㅊ', 'ㅁ', "нм");
		addKoncevicPair('ㅊ', 'ㅂ', "тп");
		addKoncevicPair('ㅊ', 'ㅅ', "сс");
		addKoncevicPair('ㅊ', 'ㅈ', "чч");
		addKoncevicPair('ㅊ', 'ㅊ', "ччх");
		addKoncevicPair('ㅊ', 'ㅋ', "ткх");
		addKoncevicPair('ㅊ', 'ㅌ', "ттх");
		addKoncevicPair('ㅊ', 'ㅍ', "тпх");
		addKoncevicPair('ㅊ', 'ㅎ', "тх");
		addKoncevicPair('ㅊ', 'ㄲ', "ткк");
		addKoncevicPair('ㅊ', 'ㄸ', "тт");
		addKoncevicPair('ㅊ', 'ㅃ', "тпп");
		addKoncevicPair('ㅊ', 'ㅆ', "сс");
		addKoncevicPair('ㅊ', 'ㅉ', "чч");
		addKoncevicPair('ㅊ', 'ㅇ', "чх");
		addKoncevicPair('ㅋ', 'ㄱ', "кк");
		addKoncevicPair('ㅋ', 'ㄴ', "нън");
		addKoncevicPair('ㅋ', 'ㄷ', "кт");
		addKoncevicPair('ㅋ', 'ㄹ', "нън");
		addKoncevicPair('ㅋ', 'ㅁ', "нъм");
		addKoncevicPair('ㅋ', 'ㅂ', "кп");
		addKoncevicPair('ㅋ', 'ㅅ', "кс");
		addKoncevicPair('ㅋ', 'ㅈ', "кч");
		addKoncevicPair('ㅋ', 'ㅊ', "кчх");
		addKoncevicPair('ㅋ', 'ㅋ', "ккх");
		addKoncevicPair('ㅋ', 'ㅌ', "кх");
		addKoncevicPair('ㅋ', 'ㅍ', "кпх");
		addKoncevicPair('ㅋ', 'ㅎ', "кх");
		addKoncevicPair('ㅋ', 'ㄲ', "кк");
		addKoncevicPair('ㅋ', 'ㄸ', "ктт");
		addKoncevicPair('ㅋ', 'ㅃ', "кпп");
		addKoncevicPair('ㅋ', 'ㅆ', "ксс");
		addKoncevicPair('ㅋ', 'ㅉ', "кчч");
		addKoncevicPair('ㅋ', 'ㅇ', "кх");
		addKoncevicPair('ㅌ', 'ㄱ', "тк");
		addKoncevicPair('ㅌ', 'ㄴ', "нн");
		addKoncevicPair('ㅌ', 'ㄷ', "тт");
		addKoncevicPair('ㅌ', 'ㄹ', "нн");
		addKoncevicPair('ㅌ', 'ㅁ', "нм");
		addKoncevicPair('ㅌ', 'ㅂ', "тп");
		addKoncevicPair('ㅌ', 'ㅅ', "сс");
		addKoncevicPair('ㅌ', 'ㅈ', "тч");
		addKoncevicPair('ㅌ', 'ㅊ', "тчх");
		addKoncevicPair('ㅌ', 'ㅋ', "ткх");
		addKoncevicPair('ㅌ', 'ㅌ', "ттх");
		addKoncevicPair('ㅌ', 'ㅍ', "тпх");
		addKoncevicPair('ㅌ', 'ㅎ', "тх");
		addKoncevicPair('ㅌ', 'ㄲ', "ткк");
		addKoncevicPair('ㅌ', 'ㄸ', "тт");
		addKoncevicPair('ㅌ', 'ㅃ', "тпп");
		addKoncevicPair('ㅌ', 'ㅆ', "сс");
		addKoncevicPair('ㅌ', 'ㅉ', "чч");
		addKoncevicPair('ㅌ', 'ㅇ', "тх");
		addKoncevicPair('ㅍ', 'ㄱ', "пк");
		addKoncevicPair('ㅍ', 'ㄴ', "мн");
		addKoncevicPair('ㅍ', 'ㄷ', "пт");
		addKoncevicPair('ㅍ', 'ㄹ', "мн");
		addKoncevicPair('ㅍ', 'ㅁ', "пм");
		addKoncevicPair('ㅍ', 'ㅂ', "пп");
		addKoncevicPair('ㅍ', 'ㅅ', "пс");
		addKoncevicPair('ㅍ', 'ㅈ', "пч");
		addKoncevicPair('ㅍ', 'ㅊ', "пчх");
		addKoncevicPair('ㅍ', 'ㅋ', "пкх");
		addKoncevicPair('ㅍ', 'ㅌ', "птх");
		addKoncevicPair('ㅍ', 'ㅍ', "ппх");
		addKoncevicPair('ㅍ', 'ㅎ', "пх");
		addKoncevicPair('ㅍ', 'ㄲ', "пкк");
		addKoncevicPair('ㅍ', 'ㄸ', "птт");
		addKoncevicPair('ㅍ', 'ㅃ', "пп");
		addKoncevicPair('ㅍ', 'ㅆ', "псс");
		addKoncevicPair('ㅍ', 'ㅉ', "пчч");
		addKoncevicPair('ㅍ', 'ㅇ', "пх");
		addKoncevicPair('ㅎ', 'ㄱ', "тк");
		addKoncevicPair('ㅎ', 'ㄴ', "нн");
		addKoncevicPair('ㅎ', 'ㄷ', "тт");
		addKoncevicPair('ㅎ', 'ㄹ', "нн");
		addKoncevicPair('ㅎ', 'ㅁ', "нм");
		addKoncevicPair('ㅎ', 'ㅂ', "тп");
		addKoncevicPair('ㅎ', 'ㅅ', "сс");
		addKoncevicPair('ㅎ', 'ㅈ', "тч");
		addKoncevicPair('ㅎ', 'ㅊ', "тчх");
		addKoncevicPair('ㅎ', 'ㅋ', "ткх");
		addKoncevicPair('ㅎ', 'ㅌ', "ттх");
		addKoncevicPair('ㅎ', 'ㅍ', "тпх");
		addKoncevicPair('ㅎ', 'ㅎ', "тх");
		addKoncevicPair('ㅎ', 'ㄲ', "ткк");
		addKoncevicPair('ㅎ', 'ㄸ', "тт");
		addKoncevicPair('ㅎ', 'ㅃ', "тпп");
		addKoncevicPair('ㅎ', 'ㅆ', "сс");
		addKoncevicPair('ㅎ', 'ㅉ', "чч");
		addKoncevicPair('ㅎ', 'ㅇ', "—");

	}
}
