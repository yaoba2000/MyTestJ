package test.thread;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 链表。
 * Created by Administrator on 2017-11-20.
 *
 * @param <E>
 */
public class LinkedTable<E> extends AbstractCollection<E> implements Iterable<E> {

    private final LinkedNode HeadNode;

    public LinkedTable(Class<E> aClass) {
        HeadNode = new LinkedNode(aClass);
    }

    public LinkedNode headNode() {//指向第一个普通节点的虚拟节点。
        return HeadNode;
    }

    public LinkedNode lastNormalNode() {//线程安全
        LinkedNode firstNormalNode = headNode().next;
        return firstNormalNode == null ? null : firstNormalNode.loopFindNextNormalNode(node -> node.next == null);
    }

    public LinkedNode addNode(E e) {//线程安全//从效率考虑，仅有限支持从头部插入新节点。也就是放弃顺序性，而是逆序。
        Objects.requireNonNull(e);
        return HeadNode.append(e);
    }

    @Override
    public boolean add(E e) {//线程安全
        addNode(e);
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            LinkedNode cur = LinkedTable.this.HeadNode;
            LinkedNode nextModifiedWhenCheckNext;

            @Override
            public boolean hasNext() {
                return (nextModifiedWhenCheckNext = cur.next) != null;//return cur.next != null;
            }

            @Override
            public E next() {
                cur = nextModifiedWhenCheckNext;
                return cur.get();
            }
        };
    }

    @Override
    public int size() {
        LinkedNode cur = HeadNode;
        int cnt = 0;
        while ((cur = cur.next) != null) {
            cnt++;
        }
        return cnt;
    }

    @Override
    public String toString() {
        return toDetailString();//"LinkedTable" + size();
    }

    public String toDetailString() {
        StringBuilder buf = new StringBuilder();
        buf.append("LinkedTable.size:").append(size());
        String itemsToString = this.stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
        buf.append(itemsToString);
        return buf.toString();
    }

    //链表节点
    public class LinkedNode {

        private final E val;
        private LinkedNode next = null;//指向链表下一个，初始值为null

        public LinkedNode(E e) {
            this.val = e;
        }

        public LinkedNode(Class<E> aClass) {
            this.val = null;
        }

        public LinkedNode append(E e) {
            LinkedNode newNext = new LinkedNode(e);//创建一个空的新的
            LinkedNode oldNext = this.next;//设置老节点
            newNext.next = oldNext;
            this.next = newNext;
            return newNext;
        }

        //获取当前下一个节点
        public LinkedNode next() {
            return this.next;
        }

        //获取当前节点
        public E get() {
            return val;
        }

        public LinkedNode loopNext() {//线程安全//循环下一个, 调用时注意不要死循环。
            LinkedNode next2 = this.next;
            if (next2 == null) {
                next2 = headNode().next;
            }
            return next2;
        }

        public boolean isTail() {
            return next() == null;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append(val).append(next == null ? "_nextIsNull" : "").append("_tail:").append(isTail());
            return buf.toString();
        }

        public Iterable<LinkedNode> nextTurnAfterCurrentNode() {
            return () -> new Iterator<LinkedNode>() {

                LinkedNode cur = LinkedNode.this;
                LinkedNode nextModifiedWhenCheckNext;
                int selfCount = 0;

                private LinkedNode _next() {
                    if (LinkedNode.this == headNode()) {
                        return cur.next;
                    } else {
                        if (cur == LinkedNode.this && selfCount == 1) {
                            return null;
                        }
                        return cur.loopNext();
                    }
                }

                @Override
                public boolean hasNext() {
                    return (nextModifiedWhenCheckNext = _next()) != null;
                }

                @Override
                public LinkedNode next() {
                    LinkedNode next = nextModifiedWhenCheckNext;
                    if (next == LinkedNode.this) {
                        selfCount++;
                    }
                    cur = next;
                    return next;
                }
            };
        }

        public LinkedNode loopFindNextNormalNode(Predicate<LinkedNode> test) {
            for (LinkedNode node : nextTurnAfterCurrentNode()) {
                if (test.test(node)) {
                    return node;
                }
            }
            return null;
        }

    }

    public static void main(String[] args) {
        LinkedTable<Long> LinkedTable = new LinkedTable<>(Long.class);
        System.out.println("" + LinkedTable);
        LinkedTable.addNode(123L);
        System.out.println("" + LinkedTable);
        LinkedTable.addNode(1234L);
        System.out.println("" + LinkedTable);
        LinkedTable.addNode(12345L);
        System.out.println("" + LinkedTable);
        LinkedTable.addNode(123456L);
        System.out.println("" + LinkedTable);

        for (Object obj : LinkedTable.HeadNode.nextTurnAfterCurrentNode()) {
            System.out.println("obj:" + obj);
        }
        for (Object obj : LinkedTable.HeadNode.next.nextTurnAfterCurrentNode()) {
            System.out.println("obj2:" + obj);
        }
        for (Object obj : LinkedTable.lastNormalNode().nextTurnAfterCurrentNode()) {
            System.out.println("obj3:" + obj);
        }
        for (Long val : LinkedTable) {
            System.out.println("val:" + val);
        }
    }
}
