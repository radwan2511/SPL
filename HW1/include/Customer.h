//
// Created by Tal Adar on 22/11/2021.
//

#ifndef ASSIGNMENT_1_CUSTOMER_H
#define ASSIGNMENT_1_CUSTOMER_H
#include <vector>
#include <string>
#include <algorithm>
#include "Workout.h"

class Customer{
public:
    Customer(std::string c_name, int c_id);
    virtual std::vector<int> order(const std::vector<Workout> &workout_options)=0;
    virtual std::string toString() const = 0;
    std::string getName() const;
    int getId() const;

    virtual Customer* getCopy() = 0;
    virtual ~Customer();


private:
    const std::string name;
    const int id;
};


class SweatyCustomer : public Customer {
public:
    SweatyCustomer(std::string name, int id);
    std::vector<int> order(const std::vector<Workout> &workout_options);
    std::string toString() const;

    ~SweatyCustomer();
    SweatyCustomer* getCopy();

private:
};


class CheapCustomer : public Customer {
public:
    CheapCustomer(std::string name, int id);
    std::vector<int> order(const std::vector<Workout> &workout_options);
    std::string toString() const;

    ~CheapCustomer();
    CheapCustomer* getCopy();

private:
};


class HeavyMuscleCustomer : public Customer {
public:
    HeavyMuscleCustomer(std::string name, int id);
    std::vector<int> order(const std::vector<Workout> &workout_options);
    std::string toString() const;

    ~HeavyMuscleCustomer();
    HeavyMuscleCustomer* getCopy();

private:
};


class FullBodyCustomer : public Customer {
public:
    FullBodyCustomer(std::string name, int id);
    std::vector<int> order(const std::vector<Workout> &workout_options);
    std::string toString() const;

    ~FullBodyCustomer();
    FullBodyCustomer* getCopy();
private:
};



#endif //ASSIGNMENT_1_CUSTOMER_H
