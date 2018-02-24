package test.thread;

/**
 * Created by Administrator on 2017-12-4.
 */
public class MyList {
    private Node head; // 定义一个头节点
    private Node current; // 记录当前节点

    public static void main(String[] args) {
        MyList myList = new MyList();
        for (int i = 0; i < 5; i++) {
            myList.addNode(i); // 循环添加5个元素
        }

        myList.printList(myList.head);   //打印链表
//        myList.printList(myList.resetList1(myList.head));
    }

    private void addNode(int data) {
        if (head == null) {
            head = new Node(data); // 如果头节点为空，就新建一个头结点
            current = head; // 记录当前节点为头结点
        } else {//如果头结点不为空
            current.next = new Node(data);
            current = current.next;
        }
    }

    //倒置增加add
    private void addResNode(int data) {
        if (head == null) {
            head = new Node(data);
            current = head;
        } else {
            Node newNext = new Node(data);//创建一个空的新的
            Node oldNext = current.next;//设置老节点
            newNext.next = oldNext;
            current.next = newNext;
        }
    }

    // 递归倒置链表 反向链表
    private Node resetList1(Node head) {
        if (head == null || head.next == null) {
            return head;                        // 若为空链或者当前结点在尾结点，则直接返回
        } else {
            Node newHead = resetList1(head.next);   //反转后续节点head.next
            head.next.next = head;              //将当前节点的指针域向前移
            head.next = null;                   //前一节点的指针域置空
            return newHead;
        }
    }

    // 遍历方法倒置
    private Node resetList2(Node head) {
        Node current = head;
        Node next = null;
        Node newHead = null;
        if (head == null || head.next == null) {
            return head;
        } else {
            while (current != null) {    //为空时到了尾节点
                next = current.next;     //新结点next缓存下一个节点
                current.next = newHead;  //指针域反转
                newHead = current;       //将当前节点给newHead
                current = next;          //移动到下一个节点
            }
        }
        return newHead;
    }


    // 打印链表
    private void printList(Node head) {
        if (head != null) { // 确定链表不为空
            current = head;
            while (current != null) { // 最后一个节点的为空
                System.out.print(current.data + "-->");
                current = current.next; // 当前节点往后移动一个位置
            }
        }
    }

    class Node { //node节点
        int data;
        Node next;

        public Node(int data) {
            this.data = data;
        }
    }
}
