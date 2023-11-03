//
// Created by Tal Adar on 22/11/2021.
//

#ifndef ASSIGNMENT_1_TRAINER_H
#define ASSIGNMENT_1_TRAINER_H


#include "Customer.h"

typedef std::pair<int, Workout> OrderPair;

class Trainer{
public:
    Trainer(int t_capacity);
    Trainer(const Trainer &other);
    Trainer(Trainer &&other);
    int getCapacity() const;
    void addCustomer(Customer* customer);
    void removeCustomer(int id);
    Customer* getCustomer(int id);
    std::vector<Customer*>& getCustomers();
    std::vector<OrderPair>& getOrders();
    void order(const int customer_id, const std::vector<int> workout_ids, const std::vector<Workout>& workout_options);
    void openTrainer();
    void closeTrainer();
    int getSalary();
    bool isOpen();
    std::string toString() const;
    virtual ~Trainer();
    Trainer& operator=(const Trainer &other);
    Trainer& operator=(Trainer&& other);



private:
    int capacity;
    bool open;
    std::vector<Customer*> customersList;
    std::vector<OrderPair> orderList; //A list of pairs for each order for the trainer - (customer_id, Workout)
};




#endif //ASSIGNMENT_1_TRAINER_H
