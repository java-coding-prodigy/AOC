package shhj;

import java.util.*;

public class LinkedSet<E> implements SortedSet<E>, NavigableSet<E> {

    int size = 0;
    Node<E> first = null;
    Node<E> last = null;

    private void addFirst(E e) {
        Node<E> first = this.first;
        Node<E> newNode = new Node<>(e, null, first);
        this.first = newNode;
        if (first == null) {
            last = newNode;
        } else {
            first.prev = newNode;
        }
    }

    private void addLast(E e) {
        Node<E> last = this.last;
        Node<E> newNode = new Node<>(e, last, null);
        this.last = newNode;
        if(last == null){
            first = newNode;
        }else{
            last.next = newNode;
        }
    }

    @Override public E lower(E e) {
        return null;
    }

    @Override public E floor(E e) {
        return null;
    }

    @Override public E ceiling(E e) {
        return null;
    }

    @Override public E higher(E e) {
        return null;
    }

    @Override public E pollFirst() {
        return null;
    }

    @Override public E pollLast() {
        return null;
    }

    @Override public NavigableSet<E> descendingSet() {
        return null;
    }

    @Override public Iterator<E> descendingIterator() {
        return null;
    }

    @Override public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement,
            boolean toInclusive) {
        return null;
    }

    @Override public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return null;
    }

    @Override public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return null;
    }

    @Override public Comparator<? super E> comparator() {
        return null;
    }

    @Override public SortedSet<E> subSet(E fromElement, E toElement) {
        return null;
    }

    @Override public SortedSet<E> headSet(E toElement) {
        return null;
    }

    @Override public SortedSet<E> tailSet(E fromElement) {
        return null;
    }

    @Override public E first() {
        return first.data;
    }

    @Override public E last() {
        return last.data;
    }

    @Override public int size() {
        return size;
    }

    @Override public boolean isEmpty() {
        return size == 0;
    }

    @Override public boolean contains(Object o) {
        return false;
    }

    @Override public Iterator<E> iterator() {
        return null;
    }

    @Override public Object[] toArray() {
        return new Object[0];
    }

    @Override public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override public boolean add(E e) {
        return false;
    }

    @Override public boolean remove(Object o) {
        return false;
    }

    @Override public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override public void clear() {

    }


    private static class Node<E> {
        E data;
        Node<E> prev;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }

        public Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}
