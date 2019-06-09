//
// Created by andre on 06-Jun-19.
//

#ifndef LINESORTER_QUEUE_H
#define LINESORTER_QUEUE_H


#include <stddef.h>

template <typename T>
class node {
public:
    node(T* item, node* left, node* right) {this->item = item; this->left = left; this->right = right;};
    ~node() {};

    T *item;
    node *left;
    node *right;
};

template <typename T>
class Queue {

public:
    Queue();
    ~Queue();

    void Push(T *item);

    T* Pop();

    size_t Size();

    void Clear();

private:
    size_t size_;
    node<T> *first_;
    node<T> *last_;

};

template <typename T>
Queue<T>::Queue() {
    size_ = 0;
    first_ = nullptr;
    last_ = nullptr;
}

template <typename T>
Queue<T>::~Queue() {
    ;
}

template <typename T>
void Queue<T>::Push(T *item) {
    if (size_ == 0) {
        node<T> *n = new node<T>(item, nullptr, nullptr);
        first_ = n;
    } else if (size_ == 1) {
        node<T> *n = new node<T>(item, first_, nullptr);
        first_->right = n;
        last_ = n;
    } else {
        node<T> *n = new node<T>(item, last_, nullptr);
        last_->right = n;
        last_ = n;
    }
    size_++;
}

template <typename T>
T* Queue<T>::Pop() {
    T* item = first_->item;
    if (first_->right) {
        node<T> *new_first = first_->right;
        new_first->left = nullptr;
        first_->right->left = nullptr;
        delete first_;
        first_ = new_first;
    } else {
        delete first_;
    }
    size_--;
    if (size_ < 2) {
        last_ = nullptr;
    }

    return item;
}

template <typename T>
size_t Queue<T>::Size() {
    return size_;
}

template <typename T>
void Queue<T>::Clear() {
    ;
}


#endif //LINESORTER_QUEUE_H
