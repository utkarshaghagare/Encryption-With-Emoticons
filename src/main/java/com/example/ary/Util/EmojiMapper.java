package com.example.ary.Util;

import java.util.HashMap;
import java.util.Map;

public class EmojiMapper {
    private static final char[] base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
    private static final String[] emojis = {
            "🌸", "🌼", "🌺", "🌻", "🌹", "🌷", "🌱", "🌲", "🌳", "🌴",
            "🌵", "🌾", "🌿", "🍀", "🍁", "🍂", "🍃", "🍄", "🌰", "🍇",
            "🍈", "🍉", "🍊", "🍋", "🍌", "🍍", "🍎", "🍏", "🍐", "🍑",
            "🍒", "🍓", "🥭", "🍅", "🍆", "🥑", "🥦", "🥬", "🥒", "🌶️",
            "🌽", "🥕", "🧄", "🧅", "🥔", "🍠", "🥯", "🍞", "🥐", "🥖",
            "🧇", "🥞", "🧈", "🍳", "🥚", "🧁", "🍰", "🎂", "🍪", "🍩",
            "🍫", "🍬", "🍭", "🍮","😲"
    };

    private static final Map<Character, String> charToEmojiMap = new HashMap<>();
    private static final Map<String, Character> emojiToCharMap = new HashMap<>();

    static {
        for (int i = 0; i < base64Chars.length; i++) {
            charToEmojiMap.put(base64Chars[i], emojis[i]);
            emojiToCharMap.put(emojis[i], base64Chars[i]);
        }
    }

    public static String toEmojiString(String base64) {
        StringBuilder emojiString = new StringBuilder();
        for (char c : base64.toCharArray()) {
            String emoji = charToEmojiMap.get(c);
                emojiString.append(emoji);
        }
        return emojiString.toString();
    }

    public static String fromEmojiString(String emojiString) {
        StringBuilder base64 = new StringBuilder();
        for (int i = 0; i < emojiString.length(); i += 2) {
            if (i + 2 <= emojiString.length()) {
                String emoji = emojiString.substring(i, i + 2);
                Character character = emojiToCharMap.get(emoji);
                if (character != null) {
                    base64.append(character);
                } else {
                    return "Invalid emoji detected";
                }
            } else {
                return "Invalid emoji string length";
            }
        }
        return base64.toString();
    }

}

