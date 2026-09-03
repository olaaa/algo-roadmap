package block06_trees_graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Узел двоичного дерева — общий для всех задач блока 6.
 * <p>
 * Имена полей {@code val}, {@code left}, {@code right} взяты из условия
 * LeetCode: там этот класс дан заранее, и на собеседовании его тоже приносят
 * готовым. Поэтому правило проекта о раскрывающих именах здесь не действует —
 * менять чужой контракт нельзя.
 */
public class TreeNode {

    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    /**
     * Собирает дерево из перечисления значений по уровням — в том же формате,
     * в каком LeetCode печатает примеры: {@code [3, 9, 20, null, null, 15, 7]}.
     * Пропущенный потомок задаётся значением {@code null}.
     * <p>
     * Нужен только тестам: строить деревья вручную через конструкторы дольше
     * и хуже читается. В решениях задач не используется.
     */
    public static TreeNode fromLevelOrder(Integer... values) {
        if (values.length == 0 || values[0] == null) {
            return null;
        }

        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> parents = new ArrayDeque<>();
        parents.add(root);

        int position = 1;
        while (position < values.length && !parents.isEmpty()) {
            TreeNode parent = parents.remove();

            if (position < values.length) {
                Integer leftValue = values[position];
                position++;
                if (leftValue != null) {
                    parent.left = new TreeNode(leftValue);
                    parents.add(parent.left);
                }
            }

            if (position < values.length) {
                Integer rightValue = values[position];
                position++;
                if (rightValue != null) {
                    parent.right = new TreeNode(rightValue);
                    parents.add(parent.right);
                }
            }
        }
        return root;
    }

    /**
     * Строит вырожденное дерево-цепочку заданной длины: у каждого узла есть
     * только правый потомок. Нужно тестам, чтобы проверить поведение
     * на самом глубоком дереве, какое допускают ограничения задачи.
     */
    public static TreeNode chainOfLength(int length) {
        if (length <= 0) {
            return null;
        }
        TreeNode root = new TreeNode(0);
        TreeNode last = root;
        for (int number = 1; number < length; number++) {
            last.right = new TreeNode(number);
            last = last.right;
        }
        return root;
    }
}
