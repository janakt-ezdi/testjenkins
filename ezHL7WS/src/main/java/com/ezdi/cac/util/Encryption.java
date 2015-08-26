package com.ezdi.cac.util;

import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;

public class Encryption
{
	private static EzdiLogger LOGGER = EzdiLogManager.getLogger(Encryption.class);

	public static void main(String[] args)
	{
		System.out.println(Encryption.decrypt("7s&8^t#V%**tVETC"));
	}

	private static final String[] ALPHABETS = new String[] { "0@", "1#", "2$", "3%", "4^", "5&", "6*", "7 = ", "8-", "9+", "Aa", "Bb", "Cc", "Dd",
			"Ee", "Ff", "Gg", "Hh", "Ii", "Jj", "Kk", "Ll", "Mm", "Nn", "Oo", "Pp", "Qq", "Rr", "Ss", "Tt", "Uu", "Vv", "Ww", "Xx", "Yy", "Zz" };
	private static int x;
	private static int placement;
	private static int ref;
	private static String firstWrite;
	private static String secondWrite;
	private static String thirdWrite;
	private static final String CODE_PHRASE = "Les chaussettes de l'archiduchesse sont elles seches et archi seches";

	private static String charToString(final char character)
	{
		LOGGER.debug("Inside charToString.");
		String str = Character.valueOf(character).toString();
		LOGGER.debug("Exiting from charToString.");
		return str;
	}

	private static boolean check(final String letter)
	{
		LOGGER.debug("Inside check.");
		boolean bill = false;
		for (int i = 0; i < 36; i++)
		{
			if (isSameString(ALPHABETS[i].substring(0, 1), letter) || isSameString(ALPHABETS[i].substring(1, 2), letter))
			{
				bill = true;
			}
		}
		LOGGER.debug("Exiting from check.");
		return bill;
	}

	private static String code1(final String codePhrase, final String inputString, final int longer, final int sage)
	{
		LOGGER.debug("Inside code1.");
		int shift = 0;
		int orig;
		if (check(charToString(inputString.charAt(placement))))
		{
			if (check(charToString(codePhrase.charAt(ref))))
			{
				if (sage == 0)
				{
					shift = findshift(charToString(codePhrase.charAt(ref)));
				}
				if (sage == 1)
				{
					shift = 36 - findshift(charToString(codePhrase.charAt(ref)));
				}
			} else
			{
				shift = 0;
			}
			orig = findshift(charToString(inputString.charAt(placement)));
			firstWrite = firstWrite + ALPHABETS[fixoffset(orig + shift)].charAt(x);
		} else
		{
			firstWrite = firstWrite + inputString.charAt(placement);
		}
		secondWrite = inputString.substring(placement + 1, inputString.length());
		thirdWrite = firstWrite + secondWrite;

		if (placement == inputString.length() - 1)
		{
			return thirdWrite;
		}
		placement++;
		if (ref == longer)
		{
			ref = 0;
		} else
		{
			ref++;
		}
		String code1Str = code1(codePhrase, inputString, longer, sage);
		LOGGER.debug("Exiting from code1.");
		return code1Str;
	}

	public static String decrypt(final String inputString)
	{
		LOGGER.debug("Inside decrypt.");
		String decryptStr = decrypt(CODE_PHRASE, inputString);
		LOGGER.debug("Exiting from decrypt.");
		return decryptStr;
	}

	public static String decrypt(final String codePhrase, final String inputString)
	{
		LOGGER.debug("Inside decrypt.");
		String decryptStr = process(codePhrase, inputString, 1);
		LOGGER.debug("Exiting from decrypt.");
		return decryptStr;
	}

	public static String encrypt(final String inputString)
	{
		LOGGER.debug("Inside encrypt.");
		String encryptStr = encrypt(CODE_PHRASE, inputString);
		LOGGER.debug("Exiting from encrypt.");
		return encryptStr;
	}

	public static String encrypt(final String codePhrase, final String inputString)
	{
		LOGGER.debug("Inside encrypt.");
		String encryptStr = process(codePhrase, inputString, 0);
		LOGGER.debug("Exiting from encrypt.");
		return encryptStr;
	}

	private static int findshift(final String letter)
	{
		LOGGER.debug("Inside findshift.");
		int shift = -1;
		for (int i = 0; i < 36; i++)
		{
			if (isSameString(ALPHABETS[i].substring(0, 1), letter) || isSameString(ALPHABETS[i].substring(1, 2), letter))
			{
				if (isSameString(ALPHABETS[i].substring(0, 1), letter))
				{
					x = 0;
				}
				if (isSameString(ALPHABETS[i].substring(1, 2), letter))
				{
					x = 1;
				}
				shift = i;
				break;
			}
		}
		LOGGER.debug("Exiting from findshift.");
		return shift;
	}

	private static int fixoffset(final int takin)
	{
		LOGGER.debug("Inside fixoffset.");
		int offSet = takin;
		if (takin > 35)
		{
			offSet = takin - 36;
		}
		LOGGER.debug("Exiting from fixoffset.");
		return offSet;
	}

	private static String process(final String codePhrase, final String inputString, final int process)
	{
		LOGGER.debug("Inside process.");
		String phraseStr = null;
		if (isEmptyString(inputString) || isEmptyString(codePhrase))
		{
			phraseStr = inputString;
		} else
		{
			final int longer = codePhrase.length() - 1;
			placement = 0;
			ref = 0;
			secondWrite = "";
			firstWrite = "";
			thirdWrite = "";
			phraseStr = code1(codePhrase, inputString, longer, process);
		}
		LOGGER.debug("Exiting from process.");
		return phraseStr;
	}

	public static boolean isSameString(final String string1, final String string2)
	{
		LOGGER.debug("Inside compare.");
		boolean isSame = false;
		if (string1 == null && string2 == null)
		{
			isSame = true;
		} else if (string1 == null || string2 == null)
		{
			isSame = false;
		} else
		{
			isSame = string1.toString().equals(string2.toString());
		}
		LOGGER.debug("Exiting from compare.");
		return isSame;
	}

	public static boolean isEmptyString(final String obj)
	{
		LOGGER.debug("Inside isEmptyString.");
		boolean isEmpty = false;
		if (obj == null)
		{
			isEmpty = true;
		} else
		{
			isEmpty = obj.trim().isEmpty();
		}
		LOGGER.debug("Exiting from isEmptyString.");
		return isEmpty;
	}
}
