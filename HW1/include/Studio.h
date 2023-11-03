//
// Created by Tal Adar on 22/11/2021.
//

#ifndef ASSIGNMENT_1_STUDIO_H
#define ASSIGNMENT_1_STUDIO_H
#include <vector>
#include <string>
#include "Workout.h"
#include "Trainer.h"
#include "Action.h"


class Studio{
public:
    Studio();
    Studio(const Studio &other);
    Studio(const std::string &configFilePath);
    void start();
    int getNumOfTrainers() const;
    Trainer* getTrainer(int tid);
    const std::vector<BaseAction*>& getActionsLog() const; // Return a reference to the history of actions
    std::vector<Workout>& getWorkoutOptions();
    void accept(BaseAction* action);
    void close();

    ~Studio();
    Studio& operator=(const Studio &other);
    Studio(Studio&& other);
    Studio& operator=(Studio&& other);

private:
    bool open;
    int customerId =0;
    std::vector<Trainer*> trainers;
    std::vector<Workout> workout_options;
    std::vector<BaseAction*> actionsLog;
};

#endif //ASSIGNMENT_1_STUDIO_H
