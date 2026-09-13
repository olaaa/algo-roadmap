package demo;

import block06_trees_graphs.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Иллюстрация к справочнику {@code docs/datastructures/BinarySearchTree.md}:
 * вставка и поиск в двоичном дереве поиска, симметричный обход как сортировка,
 * вырождение в цепочку при отсортированной вставке и сборка сбалансированного
 * дерева из отсортированного массива. Не задача блока.
 */
public class BinarySearchTreeDemo {

    /** Вставка по правилу порядка: меньше — налево, больше — направо. Дубликаты не вставляются. */
    static TreeNode insert(TreeNode root, int value) {
        if (root == null) {
            return new TreeNode(value);
        }
        if (value < root.val) {
            root.left = insert(root.left, value);
        } else if (value > root.val) {
            root.right = insert(root.right, value);
        }
        return root;
    }

    /** Поиск — спуск по одной ветке, как бинарный поиск по ссылкам. Возвращает число шагов. */
    static int searchSteps(TreeNode root, int target) {
        int steps = 0;
        TreeNode current = root;
        while (current != null) {
            steps++;
            if (target == current.val) {
                return steps;
            }
            current = target < current.val ? current.left : current.right;
        }
        return -steps;
    }

    static void inOrder(TreeNode node, List<Integer> traversalOrder) {
        if (node == null) {
            return;
        }
        inOrder(node.left, traversalOrder);
        traversalOrder.add(node.val);
        inOrder(node.right, traversalOrder);
    }

    static int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /** Середина отсортированного отрезка становится корнем, половины — поддеревьями. */
    static TreeNode fromSortedArray(int[] sortedValues, int lowIndex, int highIndex) {
        if (lowIndex > highIndex) {
            return null;
        }
        int middleIndex = lowIndex + (highIndex - lowIndex) / 2;
        TreeNode root = new TreeNode(sortedValues[middleIndex]);
        root.left = fromSortedArray(sortedValues, lowIndex, middleIndex - 1);
        root.right = fromSortedArray(sortedValues, middleIndex + 1, highIndex);
        return root;
    }

    public static void main(String[] args) {
        int[] sortedValues = {3, 7, 9, 15, 20};

        /* Дерево поиска той же формы, что у примера [3, 9, 20, null, null, 15, 7] из разборов. */
        TreeNode sameShape = TreeNode.fromLevelOrder(7, 3, 15, null, null, 9, 20);
        List<Integer> order = new ArrayList<>();
        inOrder(sameShape, order);
        check(order.equals(List.of(3, 7, 9, 15, 20)),
              "симметричный обход дерева поиска даёт возрастание: " + order);
        check(searchSteps(sameShape, 9) == 3, "поиск 9: три шага, 7 затем 15 затем 9");
        check(searchSteps(sameShape, 8) == -3, "поиска 8 нет: три шага и тупик");

        /* Отсортированная вставка вырождает дерево в цепочку. */
        TreeNode chain = null;
        for (int value : sortedValues) {
            chain = insert(chain, value);
        }
        check(height(chain) == 5 && chain.left == null && chain.right.right.right.right.val == 20,
              "вставка 3, 7, 9, 15, 20 по порядку: цепочка вправо высоты 5");
        check(searchSteps(chain, 20) == 5, "поиск 20 в цепочке: пять шагов, как в списке");
        order.clear();
        inOrder(chain, order);
        check(order.equals(List.of(3, 7, 9, 15, 20)),
              "цепочка всё равно дерево поиска: симметричный обход по возрастанию");

        /* Та же вставка в перемешанном порядке — уже не цепочка. */
        TreeNode mixed = null;
        for (int value : new int[]{9, 3, 15, 7, 20}) {
            mixed = insert(mixed, value);
        }
        check(height(mixed) == 3, "вставка 9, 3, 15, 7, 20: высота 3");
        check(TreeNode.toLevelOrder(mixed).equals(Arrays.asList(9, 3, 15, null, 7, null, 20)),
              "форма после вставки 9, 3, 15, 7, 20: " + TreeNode.toLevelOrder(mixed));

        /* Сборка из отсортированного массива: середина — корень. */
        TreeNode balanced = fromSortedArray(sortedValues, 0, sortedValues.length - 1);
        check(height(balanced) == 3, "из отсортированного массива: высота 3 = ⌈log₂ 6⌉");
        check(TreeNode.toLevelOrder(balanced).equals(Arrays.asList(9, 3, 15, null, 7, null, 20)),
              "форма: " + TreeNode.toLevelOrder(balanced));
        order.clear();
        inOrder(balanced, order);
        check(order.equals(List.of(3, 7, 9, 15, 20)), "и это дерево поиска: обход по возрастанию");

        /* Предел: 10⁴ отсортированных вставок дают цепочку высоты 10⁴; сборка серединой — высоту 14. */
        int limit = 10_000;
        int[] many = new int[limit];
        for (int index = 0; index < limit; index++) {
            many[index] = index;
        }
        check(height(fromSortedArray(many, 0, limit - 1)) == 14, "10⁴ узлов серединой: высота 14");
        TreeNode longChain = null;
        for (int value : many) {
            longChain = insert(longChain, value);
        }
        check(searchSteps(longChain, limit - 1) == limit, "10⁴ узлов цепочкой: поиск последнего за 10⁴ шагов");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
