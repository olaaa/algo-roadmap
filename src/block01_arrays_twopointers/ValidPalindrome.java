package block01_arrays_twopointers;

/**
 * LeetCode 125 — Valid Palindrome (Easy). Паттерн «два указателя».
 * <p>
 * Проверить, читается ли строка одинаково в обе стороны, если учитывать только
 * буквы и цифры и игнорировать регистр. Указатели слева и справа идут навстречу,
 * пропуская не-буквенно-цифровые символы и сравнивая остальные в нижнем регистре.
 * Строку не копируем. Время O(n), память O(1).
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block01_arrays_twopointers/ValidPalindrome.md}
 *
 * @see <a href="../../docs/problems/block01_arrays_twopointers/ValidPalindrome.md">ValidPalindrome.md</a>
 */
public class ValidPalindrome {

    public static boolean isPalindrome(String s) {
        int leftIndex = 0;
        int rightIndex = s.length() - 1;
        while (leftIndex < rightIndex) {
//  проматываем, а не удаляем несравниваемые символы
            while (leftIndex < rightIndex && !Character.isLetterOrDigit(s.charAt(leftIndex))) {
                leftIndex++;
            }
            while (leftIndex < rightIndex && !Character.isLetterOrDigit(s.charAt(rightIndex))) {
                rightIndex--;
            }
            if (Character.toLowerCase(s.charAt(leftIndex)) != Character.toLowerCase(s.charAt(rightIndex))) {
                return false;
            }

//  пара проверена. двигаем указатели.
            leftIndex++;
            rightIndex--;
        }
        return true;
    }

    public static void main(String[] args) {
        check(isPalindrome("A man, a plan, a canal: Panama"), "классический палиндром");
        check(!isPalindrome("race a car"), "не палиндром");
        check(isPalindrome(" "), "пробел: после чистки не остаётся ни одного символа");
        check(isPalindrome(".,"), "только знаки препинания");
        check(isPalindrome("0P") == false, "цифра vs буква");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
