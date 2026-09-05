package demo;

import block06_trees_graphs.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Четыре порядка обхода двоичного дерева на одном и том же дереве
 * {@code [1, 2, 3, 4, 5, 6, 7]}: три обхода в глубину (прямой, симметричный,
 * обратный) и обход в ширину. Иллюстрация к справочнику по деревьям,
 * не задача LeetCode.
 *
 * @see <a href="../../docs/datastructures/BinaryTree.md">BinaryTree.md</a>
 */
public class TreeTraversals {

    /* Прямой порядок: узел записывается при входе, до обоих поддеревьев. */
    public static void preOrder(TreeNode node, List<Integer> traversalOrder) {
        if (node == null) {
            return;
        }
        traversalOrder.add(node.val);
        preOrder(node.left, traversalOrder);
        preOrder(node.right, traversalOrder);
    }

    /* Симметричный порядок: узел записывается между левым и правым поддеревом. */
    public static void inOrder(TreeNode node, List<Integer> traversalOrder) {
        if (node == null) {
            return;
        }
        inOrder(node.left, traversalOrder);
        traversalOrder.add(node.val);
        inOrder(node.right, traversalOrder);
    }

    /* Обратный порядок: узел записывается при возврате, после обоих поддеревьев. */
    public static void postOrder(TreeNode node, List<Integer> traversalOrder) {
        if (node == null) {
            return;
        }
        postOrder(node.left, traversalOrder);
        postOrder(node.right, traversalOrder);
        traversalOrder.add(node.val);
    }

    /* Обход в ширину: очередь хранит узлы, чьих потомков ещё не видели. */
    public static List<Integer> levelOrder(TreeNode root) {
        List<Integer> traversalOrder = new ArrayList<>();
        if (root == null) {
            return traversalOrder;
        }
        Queue<TreeNode> pending = new ArrayDeque<>();
        pending.add(root);
        while (!pending.isEmpty()) {
            TreeNode node = pending.remove();
            traversalOrder.add(node.val);
            if (node.left != null) {
                pending.add(node.left);
            }
            if (node.right != null) {
                pending.add(node.right);
            }
        }
        return traversalOrder;
    }

    public static void main(String[] args) {
        TreeNode root = TreeNode.fromLevelOrder(1, 2, 3, 4, 5, 6, 7);

        List<Integer> pre = new ArrayList<>();
        preOrder(root, pre);
        List<Integer> in = new ArrayList<>();
        inOrder(root, in);
        List<Integer> post = new ArrayList<>();
        postOrder(root, post);
        List<Integer> level = levelOrder(root);

        System.out.println("pre-order   : " + pre);
        System.out.println("in-order    : " + in);
        System.out.println("post-order  : " + post);
        System.out.println("level-order : " + level);

        check(pre.equals(List.of(1, 2, 4, 5, 3, 6, 7)), "прямой порядок: 1 2 4 5 3 6 7");
        check(in.equals(List.of(4, 2, 5, 1, 6, 3, 7)), "симметричный порядок: 4 2 5 1 6 3 7");
        check(post.equals(List.of(4, 5, 2, 6, 7, 3, 1)), "обратный порядок: 4 5 2 6 7 3 1");
        check(level.equals(List.of(1, 2, 3, 4, 5, 6, 7)), "обход в ширину: 1 2 3 4 5 6 7");
        check(levelOrder(null).isEmpty(), "обход в ширину пустого дерева: пустой список");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
