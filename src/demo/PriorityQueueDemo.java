package demo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Иллюстрации к справочнику {@code docs/datastructures/PriorityQueue.md}:
 * чем порядок внутри кучи отличается от порядка выдачи, как задаётся
 * приоритет, почему равные приоритеты требуют второго ключа сравнения,
 * и два приёма, ради которых кучу обычно и берут, — «k самых крупных»
 * и слияние отсортированных потоков.
 * <p>
 * Это не задача из блока, а демонстрация к справочнику.
 *
 * @see <a href="../../docs/datastructures/PriorityQueue.md">PriorityQueue.md</a>
 */
public class PriorityQueueDemo {

    /*
     * Платёж, каким его видит очередь на отправку: класс обслуживания задаёт
     * приоритет (чем меньше номер, тем срочнее), время поступления нужно,
     * чтобы внутри одного класса порядок был предсказуемым.
     */
    private record Payment(String paymentId, int serviceClass, long amountKopecks, long receivedAtMillis) {
    }

    public static void main(String[] args) {
        orderInsideIsNotOrderOfIssue();
        comparatorSetsPriority();
        equalPrioritiesNeedSecondKey();
        threeLargestPayments();
        mergeSortedStatements();
        nullIsNotAllowed();
    }

    /* Порядок элементов в массиве кучи и порядок их выдачи — разные вещи. */
    private static void orderInsideIsNotOrderOfIssue() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (int value : new int[]{5, 3, 8, 1, 9, 2}) {
            queue.offer(value);
        }
        System.out.println("после шести offer, порядок массива : " + queue);
        check(queue.toString().equals("[1, 3, 2, 5, 9, 8]"),
              "массив кучи после offer 5, 3, 8, 1, 9, 2");
        check(queue.peek() == 1, "в корне минимум, peek не снимает");
        check(queue.size() == 6, "peek не меняет размер");

        queue.poll();
        System.out.println("после одного poll, порядок массива  : " + queue);
        check(queue.toString().equals("[2, 3, 8, 5, 9]"), "массив кучи после poll");

        PriorityQueue<Integer> second = new PriorityQueue<>(List.of(5, 3, 8, 1, 9, 2));
        List<Integer> drained = new ArrayList<>();
        while (!second.isEmpty()) {
            drained.add(second.poll());
        }
        System.out.println("порядок выдачи через poll           : " + drained);
        check(drained.equals(List.of(1, 2, 3, 5, 8, 9)), "poll выдаёт по возрастанию");
    }

    /* Приоритет задаёт компаратор: тот же набор чисел выходит в обратном порядке. */
    private static void comparatorSetsPriority() {
        PriorityQueue<Integer> largestFirst = new PriorityQueue<>(Comparator.reverseOrder());
        largestFirst.addAll(List.of(5, 3, 8, 1, 9, 2));
        List<Integer> drained = new ArrayList<>();
        while (!largestFirst.isEmpty()) {
            drained.add(largestFirst.poll());
        }
        System.out.println("тот же набор с reverseOrder         : " + drained);
        check(drained.equals(List.of(9, 8, 5, 3, 2, 1)), "reverseOrder даёт убывание");
    }

    /*
     * У двух платежей одного класса обслуживания приоритет одинаковый,
     * и порядок между ними куча не обещает. Второй ключ сравнения его задаёт.
     */
    private static void equalPrioritiesNeedSecondKey() {
        Payment early = new Payment("P-1", 1, 100_00, 1_000L);
        Payment late = new Payment("P-2", 1, 200_00, 2_000L);
        Payment routine = new Payment("P-3", 3, 50_00, 500L);

        PriorityQueue<Payment> byClassOnly = new PriorityQueue<>(Comparator.comparingInt(Payment::serviceClass));
        byClassOnly.addAll(List.of(late, routine, early));
        List<String> withoutSecondKey = new ArrayList<>();
        while (!byClassOnly.isEmpty()) {
            withoutSecondKey.add(byClassOnly.poll().paymentId());
        }
        System.out.println("только класс обслуживания           : " + withoutSecondKey);
        check(withoutSecondKey.get(2).equals("P-3"), "класс 3 выходит последним в любом случае");

        PriorityQueue<Payment> byClassThenArrival = new PriorityQueue<>(
                Comparator.comparingInt(Payment::serviceClass)
                          .thenComparingLong(Payment::receivedAtMillis));
        byClassThenArrival.addAll(List.of(late, routine, early));
        List<String> withSecondKey = new ArrayList<>();
        while (!byClassThenArrival.isEmpty()) {
            withSecondKey.add(byClassThenArrival.poll().paymentId());
        }
        System.out.println("класс, затем время поступления      : " + withSecondKey);
        check(withSecondKey.equals(List.of("P-1", "P-2", "P-3")),
              "внутри класса порядок стал «кто раньше пришёл»");
    }

    /*
     * Три самых крупных платежа за один проход. В куче лежат ровно три
     * элемента, и минимальный из них стоит в корне — поэтому решение
     * «пришедший больше корня» стоит одно сравнение.
     */
    private static void threeLargestPayments() {
        long[] amounts = {12_00, 900_00, 30_00, 5_000_00, 70_00, 2_000_00, 1_00};
        int wanted = 3;
        PriorityQueue<Long> largest = new PriorityQueue<>();
        for (long amount : amounts) {
            largest.offer(amount);
            if (largest.size() > wanted) {
                largest.poll();
            }
        }
        check(largest.size() == wanted, "в куче остаётся ровно k элементов");
        List<Long> result = new ArrayList<>();
        while (!largest.isEmpty()) {
            result.add(largest.poll());
        }
        System.out.println("три самых крупных, по возрастанию   : " + result);
        check(result.equals(List.of(900_00L, 2_000_00L, 5_000_00L)), "k самых крупных найдены");
    }

    /* Слияние нескольких отсортированных выписок в одну ленту. */
    private static void mergeSortedStatements() {
        List<List<Integer>> statements = List.of(
                List.of(1, 40, 90),
                List.of(5, 6, 7, 80),
                List.of(2, 3));

        record Cursor(int statementIndex, int positionInStatement, int value) {
        }

        PriorityQueue<Cursor> heads = new PriorityQueue<>(Comparator.comparingInt(Cursor::value));
        for (int statementIndex = 0; statementIndex < statements.size(); statementIndex++) {
            heads.offer(new Cursor(statementIndex, 0, statements.get(statementIndex).get(0)));
        }

        List<Integer> merged = new ArrayList<>();
        while (!heads.isEmpty()) {
            Cursor smallest = heads.poll();
            merged.add(smallest.value());
            int nextPosition = smallest.positionInStatement() + 1;
            List<Integer> statement = statements.get(smallest.statementIndex());
            if (nextPosition < statement.size()) {
                heads.offer(new Cursor(smallest.statementIndex(), nextPosition, statement.get(nextPosition)));
            }
        }
        System.out.println("слияние трёх выписок                : " + merged);
        check(merged.equals(List.of(1, 2, 3, 5, 6, 7, 40, 80, 90)), "слияние отсортировано");
        check(merged.size() == 9, "ни один элемент не потерян");
    }

    /*
     * Класть null в кучу нельзя: его не с чем сравнивать. Та же причина,
     * по которой null не принимает ArrayDeque, — см. SymmetricTreeIterative.
     */
    private static void nullIsNotAllowed() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        boolean rejected = false;
        try {
            queue.offer(null);
        } catch (NullPointerException expected) {
            rejected = true;
        }
        check(rejected, "offer(null) отвергается с NullPointerException");
        check(queue.isEmpty(), "очередь осталась пустой");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
