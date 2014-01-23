/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//package stack;

import java.util.*;

/**
 *
 * @author Sapir
 */
public class Stack {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Stack s=new Stack();
        LinkedList l = new LinkedList();
        Object x = 5;
        System.out.println(x);
        s.push(x);
        x= s.pop();
        System.out.println(x);
        x= s.pop();
        System.out.println(x);
    }

LinkedList list = new LinkedList();

public Stack()
{
    //this.push(x);
}

public synchronized void push(Object x) {
    synchronized (list) {
        list.addLast(x);
        notify();
    }
}

public synchronized Object pop() throws Exception {
    synchronized (list) {
        if (list.size() <= 0) {
            wait();
        }
        return list.removeLast();
    }
}

}
