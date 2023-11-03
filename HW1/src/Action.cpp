//
// Created by Tal Adar on 22/11/2021.
//

#include "../include/Action.h"
#include "../include/Trainer.h"
#include "../include/Studio.h"

BaseAction::BaseAction() {

}

ActionStatus BaseAction::getStatus() const {
    return status;
}

void BaseAction::complete() {
    status = ActionStatus::COMPLETED;
}

void BaseAction::error(std::string errorMsg){
    this->errorMsg = errorMsg;
    std::cout << "Error: " + errorMsg << std::endl;
    status=ActionStatus::ERROR;
}

std::string BaseAction::getErrorMsg() const {
    return errorMsg;
}

BaseAction::~BaseAction() {

}

///Open Trainer

OpenTrainer::OpenTrainer(int id, std::vector<Customer *> &customersList): trainerId(id),customers(customersList) {}

///fixed by radwan
void OpenTrainer::act(Studio &studio) {
    Trainer* t = studio.getTrainer(trainerId);
    info+="open "+std::to_string(trainerId);
        for(Customer* c:customers)
            info+=" "+c->toString();
    if(t== nullptr || (t != nullptr && t->isOpen()) ){
        error("Trainer does not exist or is already open");
        
        for ( Customer* c : customers){
            delete c;
        }
        
    }
    else{
        for ( Customer* c : customers){
            if(t->getCustomers().size() <= (std::size_t)t->getCapacity())
                t->addCustomer(c);
            else
                delete c;
        }

        t->openTrainer();
        complete();

      
        std::cout<<info<< std::endl;
        
    }

    studio.accept(this);

}

std::string OpenTrainer::toString() const {
    std::string s=info;
    if(getStatus() == 0)
        s +="Completed";
    else
        s += "Error: " + getErrorMsg();
    return s;
}
OpenTrainer *OpenTrainer::getCopy() {
    return new OpenTrainer(*this);
}

OpenTrainer::~OpenTrainer() {

}

///Order
Order::Order(int id) : trainerId(id) {}

void Order::act(Studio &studio)
{
    Trainer *trainer = studio.getTrainer(trainerId);
    if (trainer == nullptr ||
        (trainer != nullptr && !trainer->isOpen())) {
        //throw error message
        error("Trainer does not exist or is not open.");
    }
    else {
        for (Customer *c: trainer->getCustomers()) {

            trainer->order(c->getId(), c->order(studio.getWorkoutOptions()), studio.getWorkoutOptions());
        }

        // print orderList
        

        
        complete();
    }
    studio.accept(this);

}


std::string Order::toString() const {
    std::string s="order ";
    s+= std::to_string(trainerId) + " ";
    if(getStatus() == 0)
        s += "Completed";
    else
        s += " Error: " + getErrorMsg();
    return s;
}
Order *Order::getCopy() {
    return new Order(*this);
}
Order::~Order() {

}


///Move Customer
MoveCustomer::MoveCustomer(int src, int dst, int customerId):srcTrainer(src),dstTrainer(dst),id(customerId) {}

void MoveCustomer::act(Studio &studio) {
    if(studio.getTrainer(srcTrainer)== nullptr)
        error("source trainer doesn't exist");
    if(studio.getTrainer(dstTrainer)== nullptr)
        error("destination trainer doesn't exist");
    if(!studio.getTrainer(dstTrainer)->isOpen() || !studio.getTrainer(srcTrainer)->isOpen())
        error("one of the trainer's workout session isn't open");
    if(studio.getTrainer(dstTrainer)->getCustomers().size()==(std::size_t)studio.getTrainer(dstTrainer)->getCapacity())
        error("destination trainer has no open spots");
    else {
        studio.getTrainer(dstTrainer)->getCustomers().push_back(studio.getTrainer(srcTrainer)->getCustomer(id));
        for(OrderPair o : studio.getTrainer(srcTrainer)->getOrders()){
            if(id==o.first){
                studio.getTrainer(dstTrainer)->getOrders().push_back(o);
            }
        }
        studio.getTrainer(srcTrainer)->removeCustomer(id);

        std::vector<OrderPair> temp ;
        for(OrderPair op : studio.getTrainer(srcTrainer)->getOrders()){
            if(op.first!=id)
                temp.push_back(op);
        }
        studio.getTrainer(srcTrainer)->getOrders().swap(temp);
        temp.clear();
        temp.shrink_to_fit();
        if(studio.getTrainer(srcTrainer)->getCustomers().empty())
            studio.getTrainer(srcTrainer)->closeTrainer();

        complete();
    }
    studio.accept(this);



}

std::string MoveCustomer::toString() const {
    std::string s="move ";
    s+=std::to_string(srcTrainer);
    s+=" ";
    s+=std::to_string(dstTrainer);
    s+=" ";
    s+=std::to_string(id);
    s+=" ";
    if(getStatus() == 0)
        s += "Completed";
    else
        s += "Error: " + getErrorMsg();
    return s;
}

MoveCustomer *MoveCustomer::getCopy() {
    return new MoveCustomer(*this);
}
MoveCustomer::~MoveCustomer() {

}

///Close
Close::Close(int id) : trainerId(id){}

void Close::act(Studio &studio){
    Trainer *trainer = studio.getTrainer(trainerId);
    if (trainer == nullptr ||
        (trainer != nullptr && !trainer->isOpen())) {
        //throw error message
        error("Trainer does not exist or is not open.");
    }
    else {
        trainer->closeTrainer();
        std::cout << "Trainer " + std::to_string(trainerId) + " closed. Salary " + std::to_string(trainer->getSalary()) + "NIS"
             << std::endl;
        complete();
    }
    studio.accept(this);
}

std::string Close::toString() const{
    std::string s="close ";
    s+= std::to_string(trainerId) + " ";
    if(getStatus() == 0)
        s += "Completed";
    else
        s += "Error: " + getErrorMsg();
    return s;
}
Close *Close::getCopy() {
    return new Close(*this);
}

Close::~Close() {

}


///Close All

CloseAll::CloseAll() {

}

void CloseAll::act(Studio &studio) {

    for(int i=0;i<studio.getNumOfTrainers();i++) {
        Trainer* t = studio.getTrainer(i);
        if(t->isOpen()){
            std::string output = "Trainer " + std::to_string(i) + " ";
            output += "closed. ";
            output += "Salary " + std::to_string(t->getSalary()) +"NIS";
            std::cout << output << std::endl;
        }

        t->closeTrainer();

    }
    studio.close();
    this->complete();
    studio.accept(this);
    
}

std::string CloseAll::toString() const {
    return "closeall Completed";
}
CloseAll *CloseAll::getCopy() {
    return new CloseAll(*this);
}
CloseAll::~CloseAll() {

}

//PrintWorkoutOptions
PrintWorkoutOptions::PrintWorkoutOptions() {  }

void PrintWorkoutOptions::act(Studio &studio){
    std::vector<Workout> option = studio.getWorkoutOptions();
    for (Workout w : option){
        std::string type = "";
        int i = w.getType();
        if (i == 0)
            type = "Anaerobic";
        else if (i == 1)
            type = "Mixed";
        else if (i == 2)
            type = "Cardio";
        std::cout << w.getName() + ", " + type + ", " + std::to_string(w.getPrice()) << std::endl;
    }
    studio.accept(this);
}

std::string PrintWorkoutOptions::toString() const {
    return "workout_options Completed";
}
PrintWorkoutOptions *PrintWorkoutOptions::getCopy() {
    return new PrintWorkoutOptions(*this);
}
PrintWorkoutOptions::~PrintWorkoutOptions() {

}


///Print Trainer Status
PrintTrainerStatus::PrintTrainerStatus(int id) : trainerId(id) {

}

void PrintTrainerStatus::act(Studio &studio) {
    std::string s="Trainer ";
    s+=std::to_string(trainerId)+" ";
    if(studio.getTrainer(trainerId)->isOpen()){
        
        s+=studio.getTrainer(trainerId)->toString();
        
        s+="Current Trainer's Salary: ";
        s+= std::to_string(studio.getTrainer(trainerId)->getSalary()) +"NIS";
    }
    else{
        s+="Status: closed";
    }
    std::cout<<s<< std::endl;
    complete();
    studio.accept(this);
}

std::string PrintTrainerStatus::toString() const {
    return "status "+ std::to_string(trainerId) + " Completed";
}

PrintTrainerStatus *PrintTrainerStatus::getCopy() {
    return new PrintTrainerStatus(*this);
}
PrintTrainerStatus::~PrintTrainerStatus() {

}

///PrintActionsLog
PrintActionsLog::PrintActionsLog(){}
void PrintActionsLog::act(Studio &studio) {
    for(BaseAction* b : studio.getActionsLog())
    {
        std::cout << b->toString() << std::endl;
    }
    studio.accept(this);
}

std::string PrintActionsLog::toString() const {
    return "log Completed";
}
PrintActionsLog *PrintActionsLog::getCopy() {
    return new PrintActionsLog(*this);
}
PrintActionsLog::~PrintActionsLog() {

}

///Backup Studio
BackupStudio::BackupStudio() {

}

void BackupStudio::act(Studio &studio) {
    backup = &studio;
    complete();
    studio.accept(this);
}

std::string BackupStudio::toString() const {
    std::string s = "backup Completed";
    return s;
}
BackupStudio *BackupStudio::getCopy() {
    return new BackupStudio(*this);
}
BackupStudio::~BackupStudio() {

}


///RestoreStudio Studio
RestoreStudio::RestoreStudio() {

}

void RestoreStudio::act(Studio &studio) {
    if(backup == nullptr){
        error("No backup available.");
    }
    else{
        studio = *backup;
        complete();
    }
    studio.accept(this);
}

std::string RestoreStudio::toString() const {
    std::string s = "restore";
    if(getStatus() == 0)
        s += "Completed";
    else
        s += "Error: " + getErrorMsg();
    return s;
}

RestoreStudio *RestoreStudio::getCopy() {
    return new RestoreStudio(*this);
}
RestoreStudio::~RestoreStudio() {

}
