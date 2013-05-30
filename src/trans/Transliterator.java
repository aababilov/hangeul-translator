package trans;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class Transliterator {
	static Charset cs = Charset.forName("UTF-8");

	Map<String, String> toRuMap = new HashMap<String, String>(),
			toCoMap = new HashMap<String, String>();

	int toRuLargest, toCoLargest;

	void clear() {
		toRuMap.clear();
		toCoMap.clear();
		toRuLargest = 0;
		toCoLargest = 0;
	}

	public void loadFromFile(String fileName) {
		String s = "";
		try {
			InputStreamReader ireader = new InputStreamReader(
					new FileInputStream(fileName), "UTF-8");
			final int BUF_LEN = 1024;
			char[] buf = new char[BUF_LEN];
			int count_read;
			for (;;) {
				count_read = ireader.read(buf);
				if (count_read == -1)
					break;
				if (count_read != 0)
					s += new String(buf, 0, count_read);
			}
			ireader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadFromString(s);
	}

	public void loadFromString(String s) {
		clear();
		StringTokenizer totalTokenizer = new StringTokenizer(s, "\n\t\r\f");
		while (totalTokenizer.hasMoreTokens()) {
			String line = totalTokenizer.nextToken();
			StringTokenizer pairTokenizer = new StringTokenizer(line.trim(),
					"=");
			String toRuPart, toCoPart;
			if (pairTokenizer.hasMoreTokens())
				toRuPart = pairTokenizer.nextToken();
			else
				continue;
			if (pairTokenizer.hasMoreTokens())
				toCoPart = pairTokenizer.nextToken();
			else
				continue;
			toRuPart = Jamo.splitDigraphs(toRuPart);
			if (!toRuMap.containsKey(toRuPart))
				toRuMap.put(toRuPart, toCoPart);
			if (!toCoMap.containsKey(toCoPart))
				toCoMap.put(toCoPart, toRuPart);
			if (toRuLargest < toRuPart.length())
				toRuLargest = toRuPart.length();
			if (toCoLargest < toCoPart.length())
				toCoLargest = toCoPart.length();
		}
	}

	int largestFoundLength, i;

	String largestKey, inStr;

	static Locale ruLocale = new Locale("ru");

	void findLargest(int largest, Map<String, String> map) {
		largestKey = null;
		for (largestFoundLength = Math.min(largest, inStr.length() - i); largestFoundLength >= 1; --largestFoundLength) {
			String subStr = inStr.substring(i, i + largestFoundLength)
					.toLowerCase(ruLocale);
			largestKey = map.get(subStr);
			if (largestKey != null)
				break;
		}
	}

	public String transToRu(String s) {
		String r = "";
		inStr = Jamo.splitDigraphsSyllables(s);
		for (i = 0; i < inStr.length();) {
			if (inStr.charAt(i) == 'ㅇ' && i + 1 < inStr.length()
					&& Jamo.isVowel(inStr.charAt(i + 1))) {
				if (i > 0 && Jamo.isConsonant(inStr.charAt(i - 1)))
					r += "-";
				++i;
				continue;
			}
			findLargest(toRuLargest, toRuMap);
			if (largestKey == null) {
				r += inStr.charAt(i);
				++i;
			} else {
				r += largestKey;
				i += largestFoundLength;
			}
		}
		return r;
	}

	static boolean isRusVowel(char c) {
		return "аоэуиыйяёею".indexOf(c) != -1;
	}

	static boolean isRMakingConsonant(char c) {
		return "тсх".indexOf(c) != -1;
	}

	public String transToCo(String s) {
		String r = "";
		inStr = s;
		for (i = 0; i < inStr.length();) {
			if (inStr.charAt(i) == 'л') {				
				char prev = i > 0 ? inStr.charAt(i - 1) : 'f';
				char next = i < inStr.length() - 1 ? inStr.charAt(i + 1) : 'f';
				++i;
				if (next != 'л') {
					if (isRusVowel(prev) || isRusVowel(next)
							|| isRMakingConsonant(next)) {
						r += "ㄹㄹ";
					} else {
						r += "ㄹ";
					}
				} else {
					++i;
					r += "ㄹㄹ";
				}
			} else {
				findLargest(toCoLargest, toCoMap);
				if (largestKey == null) {
					r += inStr.charAt(i);
					++i;
				} else {
					r += largestKey;
					i += largestFoundLength;
				}
			}
		}
		return r;
	}

	KoncevicC2R koncevic = new KoncevicC2R();

	public String koncevicCorToRu(String inStr) {
		return koncevic.koncevicCorToRu(inStr);
	}
}
