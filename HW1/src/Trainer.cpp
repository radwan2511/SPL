//
// Created by Tal Adar on 22/11/2021.
//
#include <iostream>
#include "../include/Trainer.h"


Trainer::Trainer(int t_capacity) : capacity(t_capacity){
    open=false;
}

Trainer::Trainer(const Trainer &other) {
    capacity=other.getCapacity();
    open=other.open;
    for(Customer* c :other.customersList){
        customersList.push_back(c->getCopy());
    }
    for(std::pair<int, Workout> o : other.orderList){
        orderList.emplace_back(OrderPair(o.first,o.second));
    }

}

Trainer::Trainer(Trainer &&other) {
    capacity=other.capacity;
    open=other.open;
    
    for(std::size_t i=0;i<other.customersList.size();i++){
        customersList.push_back(other.customersList[i]);
        other.customersList[i] = nullptr;
    }
    

    for(OrderPair op:other.orderList) {
        orderList.push_back(op);
    }

    other.orderList.clear();
    other.orderList.shrink_to_fit();

}


int Trainer::getCapacity() const {
    return capacity;
}

void Trainer::addCustomer(Customer *customer) {
    if(customersList.size()<(std::size_t)capacity)
        customersList.push_back(customer);

}

void Trainer::removeCustomer(int id) {
    std::vector<Customer*>temp;
    for(Customer* c:customersList){
        if(c->getId()!=id)
            temp.push_back(c);
    }
    customersList.swap(temp);
    temp.clear();
    temp.shrink_to_fit();

}

Customer *Trainer::getCustomer(int id) {

   for(Customer* c: customersList) {
       if (c->getId() == id)
           return c;
   }
   return nullptr;
}


std::vector<Customer *> &Trainer::getCustomers() {
    return customersList;
}

std::vector<OrderPair> &Trainer::getOrders() {
    return orderList;
}

void
Trainer::order(const int customer_id, const std::vector<int> workout_ids, const std::vector<Workout>& workout_options) {



    for (int i : workout_ids){
        orderList.push_back(std::make_pair(customer_id,workout_options.at(i)) );

        
        std::cout << getCustomer(customer_id)->getName() << " Is Doing " << workout_options.at(i).getName() << std::endl;
        
    }


}

void Trainer::openTrainer() {
    open = true;
}

void Trainer::closeTrainer() {
    open = false;
    for( Customer* c : customersList)
        delete c;
    customersList.clear();
    customersList.shrink_to_fit();

}

int Trainer::getSalary() {
    int salary = 0;
    for (int i = 0; (std::size_t)i < orderList.size(); i++) {
        salary += orderList[i].second.getPrice();
    }
    return salary;
}

bool Trainer::isOpen() {
    return open;
}

std::string Trainer::toString() const {
    std::string s="status: ";
    if(open) s+="open";
    else s+="closed";
    s+="\n";
    s+="Customers:\n";
    for(auto c: customersList){
        s+= std::to_string(c->getId()) + " " + c->getName();
        s+="\n";
    }
    s+="Orders:\n";
    for(const auto& o:orderList){
        s+=o.second.getName();
        s+=" "+std::to_string(o.second.getPrice());
        s+="NIS ";
        s+=std::to_string(o.first);
        s+="\n";
    }

    return s;
}

Trainer::~Trainer() {
    for(Customer* c: customersList){
        if(c)
            c= nullptr;
    }
    customersList.clear();


}

Trainer &Trainer::operator=(const Trainer &other) {
    if(this != &other){
        for(Customer* c: customersList){
            if(c)
                c= nullptr;
        }
        customersList.clear();

        orderList.clear();
        orderList.shrink_to_fit();
        capacity=other.capacity;
        open=other.open;
        for(Customer* c: other.customersList){
            customersList.push_back(c->getCopy());
        }
        for(const OrderPair& op : other.orderList){
            orderList.emplace_back(op.first,op.second);
        }
        //delete temp;

    }

    return *this;

}

Trainer &Trainer::operator=(Trainer &&other) {
    for(Customer* c: customersList){
        if(c)
            c= nullptr;
    }
    customersList.clear();

    orderList.clear();
    orderList.shrink_to_fit();
    capacity=other.capacity;
    open=other.open;
    customersList=other.customersList;
    for(OrderPair op : other.orderList){
        orderList.emplace_back(op.first,op.second);
    }
    
    for(std::size_t i=0;i<other.customersList.size();i++){
        other.customersList[i] = nullptr;
    }
    

    other.orderList.clear();
    other.orderList.shrink_to_fit();
    return *this;

}
