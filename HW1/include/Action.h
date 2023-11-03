//
// Created by Tal Adar on 22/11/2021.
//

#ifndef ASSIGNMENT_1_ACTION_H
#define ASSIGNMENT_1_ACTION_H
#include <string>
#include <iostream>
#include "Customer.h"

enum ActionStatus{
    COMPLETED, ERROR
};

//Forward declaration
class Studio;
extern Studio* backup;

class BaseAction{
public:
    BaseAction();
    ActionStatus getStatus() const;
    virtual void act(Studio& studio)=0;
    virtual std::string toString() const=0;
    virtual BaseAction* getCopy() = 0;
    virtual ~BaseAction();
protected:
    void complete();
    void error(std::string errorMsg);
    std::string getErrorMsg() const;
private:
    std::string errorMsg;
    ActionStatus status;

};


class OpenTrainer : public BaseAction { ///tal
public:
    OpenTrainer(int id, std::vector<Customer *> &customersList);
    void act(Studio &studio);
    std::string toString() const;
    OpenTrainer* getCopy();
    ~OpenTrainer();
private:
    std::string info="";
    const int trainerId;
    std::vector<Customer *> customers;
};


class Order : public BaseAction { ///raduan
public:
    Order(int id);
    void act(Studio &studio);
    std::string toString() const;
    Order* getCopy();
     ~Order();
private:
    const int trainerId;
};


class MoveCustomer : public BaseAction { ///tal
public:
    MoveCustomer(int src, int dst, int customerId);
    void act(Studio &studio);
    std::string toString() const;
    MoveCustomer* getCopy();
     ~MoveCustomer();
private:
    const int srcTrainer;
    const int dstTrainer;
    const int id;
};


class Close : public BaseAction {///raduan
public:
    Close(int id);
    void act(Studio &studio);
    std::string toString() const;
    Close* getCopy();
     ~Close();
private:
    const int trainerId;
};


class CloseAll : public BaseAction {///tal
public:
    CloseAll();
    void act(Studio &studio);
    std::string toString() const;
    CloseAll* getCopy();
     ~CloseAll();
private:
};


class PrintWorkoutOptions : public BaseAction {///raduan
public:
    PrintWorkoutOptions();
    void act(Studio &studio);
    std::string toString() const;
    PrintWorkoutOptions* getCopy();
     ~PrintWorkoutOptions();
private:
};


class PrintTrainerStatus : public BaseAction {///tal
public:
    PrintTrainerStatus(int id);
    void act(Studio &studio);
    std::string toString() const;
    PrintTrainerStatus* getCopy();
     ~PrintTrainerStatus();
private:
    const int trainerId;
};


class PrintActionsLog : public BaseAction {///raduan
public:
    PrintActionsLog();
    void act(Studio &studio);
    std::string toString() const;
    PrintActionsLog* getCopy();
     ~PrintActionsLog();
private:
};


class BackupStudio : public BaseAction {///tal
public:
    BackupStudio();
    void act(Studio &studio);
    std::string toString() const;
    BackupStudio* getCopy();
     ~BackupStudio();
private:
};


class RestoreStudio : public BaseAction {///raduan
public:
    RestoreStudio();
    void act(Studio &studio);
    std::string toString() const;
    RestoreStudio* getCopy();
    ~RestoreStudio();

};



#endif //ASSIGNMENT_1_ACTION_H
