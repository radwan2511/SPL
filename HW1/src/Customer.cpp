//
// Created by Tal Adar on 22/11/2021.
//

#include "../include/Customer.h"
Customer::Customer(std::string c_name, int c_id) : name(c_name),id(c_id) {

}

std::string Customer::getName() const {
    return name;
}

int Customer::getId() const {
    return id;
}

Customer::~Customer() {

}


///sweaty
SweatyCustomer::SweatyCustomer(std::string name, int id) : Customer(name, id) {}

std::vector<int> SweatyCustomer::order(const std::vector<Workout> &workout_options) {
    std::vector<int> workouts;
    for (auto it = begin (workout_options); it != end (workout_options); ++it) {
        if (it->getType() == CARDIO) {
            workouts.push_back(it->getId());
        }
    }

    return workouts;
}

std::string SweatyCustomer::toString() const {
    return getName()+","+"swt";
}

SweatyCustomer::~SweatyCustomer() {

}
SweatyCustomer *SweatyCustomer::getCopy() {
    return new SweatyCustomer(*this);
}


///cheap
CheapCustomer::CheapCustomer(std::string name, int id) : Customer(name, id) {}

std::vector<int> CheapCustomer::order(const std::vector<Workout> &workout_options) {
    std::vector<int> workouts;
    int cheapestId = -1;
    int cheap = -1;
    for (auto it = begin (workout_options); it != end (workout_options); ++it) {
        if(cheapestId == -1)
        {
            cheap = it->getPrice();
            cheapestId = it->getId();
        }
        if(it->getPrice() < cheap){
            cheapestId = it->getId();
            cheap = it->getPrice();
        }
    }

    workouts.push_back(cheapestId);
    return workouts;
}

std::string CheapCustomer::toString() const {
    return getName()+","+"chp";
}

CheapCustomer::~CheapCustomer() {

}
CheapCustomer *CheapCustomer::getCopy() {
    return new CheapCustomer(*this);
}

///heavy muscle
HeavyMuscleCustomer::HeavyMuscleCustomer(std::string name, int id) : Customer(name, id) {}

HeavyMuscleCustomer::~HeavyMuscleCustomer() {

}
HeavyMuscleCustomer *HeavyMuscleCustomer::getCopy() {
    return new HeavyMuscleCustomer(*this);
}

std::vector<int> HeavyMuscleCustomer::order(const std::vector<Workout> &workout_options) {
    std::vector<int> workouts;
    std::vector<std::pair<int, int>> price;
    for (auto it = begin (workout_options); it != end (workout_options); ++it) {
        if (it->getType() == ANAEROBIC) {
            price.push_back(std::make_pair(it->getPrice(), it->getId()));

        }
    }
    // order pairs by the price from max to min
    std::sort(price.rbegin(), price.rend());

    for (int i=0; (std::size_t)i<price.size(); i++)
    {
        // "first" and "second" are used to access
        // 1st and 2nd element of pair respectively
        workouts.push_back(price[i].second);
    }

    return workouts;
}

std::string HeavyMuscleCustomer::toString() const {
    return getName()+","+"mcl";
}


///full body
FullBodyCustomer::FullBodyCustomer(std::string name, int id) : Customer(name, id) {}

std::vector<int> FullBodyCustomer::order(const std::vector<Workout> &workout_options) {
    std::vector<int> workouts;
    int cardioCheapest=-1;
    int cardioCheapestId=-1;

    int mixExpensive =-1;
    int mixExpensiveId = -1;

    int anaerobicCheapest=-1;
    int anaerobicCheapestId=-1;

    for (auto it = begin (workout_options); it != end (workout_options); ++it) {

        if (it->getType() == CARDIO) {
            if(cardioCheapestId == -1){
                cardioCheapestId = it->getId();
                cardioCheapest = it->getPrice();
            }
            if(cardioCheapest > it->getPrice())
            {
                cardioCheapestId = it->getId();
                cardioCheapest = it->getPrice();
            }
        }

        if (it->getType() == MIXED) {
            if(mixExpensiveId == -1){
                mixExpensiveId = it->getId();
                mixExpensive = it->getPrice();
            }
            if(mixExpensive < it->getPrice())
            {
                mixExpensiveId = it->getId();
                mixExpensive = it->getPrice();
            }
        }

        if (it->getType() == ANAEROBIC) {
            if(anaerobicCheapestId == -1){
                anaerobicCheapestId = it->getId();
                anaerobicCheapest = it->getPrice();
            }
            if(anaerobicCheapest > it->getPrice())
            {
                anaerobicCheapestId = it->getId();
                anaerobicCheapest = it->getPrice();
            }
        }
    }

    workouts.push_back(cardioCheapestId);
    workouts.push_back(mixExpensiveId);
    workouts.push_back(anaerobicCheapestId);
    return workouts;
}

std::string FullBodyCustomer::toString() const {
    return getName()+","+"fbd";
}
FullBodyCustomer::~FullBodyCustomer() {

}
FullBodyCustomer *FullBodyCustomer::getCopy() {
    return new FullBodyCustomer(*this);
}
