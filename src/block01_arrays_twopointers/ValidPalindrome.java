package block01_arrays_twopointers;

/*
 * Задача: проверить, является ли строка палиндромом.
 * Учитываем только буквы и цифры, регистр игнорируем.
 * (LeetCode 125 — Valid Palindrome)
 *
 * Пример:
 *   "A man, a plan, a canal: Panama" -> true
 *   "race a car"                     -> false
 *
 * Идея (паттерн "два указателя"):
 *   Указатель слева и справа. Пропускаем всё, что не буква/цифра.
 *   Сравниваем символы в нижнем регистре. Не совпали — не палиндром.
 *
 * Сложность: время O(n), память O(1).
 */
public class ValidPalindrome {

    public static boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++;
            while (l < r && !Character.isLetterOrDigit(s.charAt(r))) r--;
            if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }

    public static void main(String[] args) {
        check(isPalindrome("A man, a plan, a canal: Panama"), "классический палиндром");
        check(!isPalindrome("race a car"), "не палиндром");
        check(isPalindrome(""), "пустая строка");
        check(isPalindrome(".,"), "только знаки препинания");
        check(isPalindrome("0P") == false, "цифра vs буква");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
