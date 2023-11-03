//
// Created by Tal Adar on 22/11/2021.
//

#include <fstream>
#include <sstream>
#include "../include/Studio.h"

Studio::Studio(){


}
Studio::Studio(const std::string &configFilePath){

    // getting info from text file
    std::string line;
    std::ifstream file;
    file.open(configFilePath);

    if(!file.is_open()) {
        perror("Error when trying to open text file");
        exit(EXIT_FAILURE);
        //std::cout << "Error when trying to open text file" << std::endl;
    }

    int parameter = 0;
    //int trainersNumber = -1;
    int workoutId = 0;

    while(getline(file, line)) {
        if (line.empty())
        {
            continue;
        }
        else if(line[0] == '#')
        {
            parameter++;
        }
            //Parameter 1: number of trainers in the studio.
        //else if(parameter == 1){
            //trainersNumber = stoi(line);
        //}
            // Parameter 2: a list of trainer’s capacities (maximum spots in each trainer’s workout session)
            // separated by a comma
        else if(parameter == 2)
        {
            std::stringstream ss(line);

            for (int i; ss >> i;) {
                trainers.push_back(new Trainer(i));
                if (ss.peek() == ',' || ss.peek() == ' ' || ss.peek() == '\n')
                    ss.ignore();
            }

        }
            //Parameter 3: a list of workout options, each workout option is in a separate line, including
            //workout option name, workout type (Anaerobic, Mixed, and Cardio), and a price separated by
            //a comma:
        else if(parameter == 3)
        {
            std::vector<std::string> workoutInfo;
            WorkoutType type;

            std::stringstream ss(line);

            while( ss.good() )
            {
                std::string substr;
                getline( ss, substr, ',' );
                workoutInfo.push_back( substr );
            }


            if(workoutInfo[1].compare(" Anaerobic") == 0)
                type = ANAEROBIC;
            if(workoutInfo[1].compare(" Mixed") == 0 )
                type = MIXED;
            if(workoutInfo[1].compare(" Cardio") == 0 )
                type = CARDIO;


            Workout* w = new Workout(workoutId,workoutInfo[0], stoi(workoutInfo[2]), type);
            workout_options.push_back(*w);
            workoutId++;
            
            free(w);

        }
    }



}

void Studio::start(){
    std::string input = "";

    open = true;
    std::cout << "Studio is now open!" << std::endl;

    //waiting for user to enter action to execute



    getline(std::cin, input);
    while(open) {
        std::vector<Customer*> customers;
        int trainerId = -1;
        std::string action = "";




        // splitting the syntax by space
        std::vector<std::string> syntax;
        std::istringstream iss(input);
        for (std::string s; iss >> s;)
            syntax.push_back(s);



        // open trainer
        if (syntax[0].compare("open") == 0) {
            trainerId = stoi(syntax[1]);
            // 1. check if trainer doesnt exist or trainer workout seassion is already open
            if (getTrainer(trainerId) == nullptr){
                //throw error message

            } else {
                
                for (int i = 2; (std::size_t)i < syntax.size(); i++) {
                    //split <customer_id>,<customer1_strategy> by comma
                    std::vector<std::string> customerInfo;
                    std::stringstream ss(syntax[i]);

                    while (ss.good()) {
                        std::string substr;
                        getline(ss, substr, ',');
                        customerInfo.push_back(substr);
                    }
                    Customer *newCustomer = nullptr;
                    if (customerInfo[1].compare("swt") == 0) {
                        newCustomer = new SweatyCustomer(customerInfo[0], customerId);
                    } else if (customerInfo[1].compare("chp") == 0) {
                        newCustomer = new CheapCustomer(customerInfo[0], customerId);
                    } else if (customerInfo[1].compare("mcl") == 0) {
                        newCustomer = new HeavyMuscleCustomer(customerInfo[0], customerId);
                    } else if (customerInfo[1].compare("fbd") == 0) {
                        newCustomer = new FullBodyCustomer(customerInfo[0], customerId);
                    }

                    if (newCustomer != nullptr) {
                        customers.push_back(newCustomer);
                        customerId++;
                       
                    }
                    
                }
            }
            OpenTrainer* b = new OpenTrainer(trainerId,customers);
            b->act(*this);
            
        }
        else if (syntax[0].compare("order") == 0) {
            trainerId = stoi(syntax[1]);

            Order* order = new Order(trainerId);
            order->act(*this);
        }
        else if (syntax[0].compare("close") == 0) {
            trainerId = stoi(syntax[1]);

            Close* close = new Close(trainerId);
            close->act(*this);
        }
        else if (syntax[0].compare("workout_options") == 0) {
            PrintWorkoutOptions* print = new PrintWorkoutOptions();
            print->act(*this);

        }
        else if (syntax[0].compare("log") == 0) {
            PrintActionsLog* print = new PrintActionsLog();
            print->act(*this);

        }

        else if (syntax[0].compare("status") == 0) {
            trainerId = stoi(syntax[1]);
            PrintTrainerStatus* print = new PrintTrainerStatus(trainerId);
            print->act(*this);

        }
        else if (syntax[0].compare("backup") == 0) {
            BackupStudio* backup = new BackupStudio();
            backup->act(*this);

        }
        else if (syntax[0].compare("closeall") == 0) {
            CloseAll* closeall = new CloseAll();
            closeall->act(*this);
            open = false;
        }
        else if (syntax[0].compare("move") == 0) {
            trainerId = stoi(syntax[1]);
            int destTrainerId = stoi(syntax[2]);
            int customerId = stoi(syntax[3]);
            MoveCustomer* move = new MoveCustomer(trainerId, destTrainerId, customerId);
            move->act(*this);

        }
        if(open)
            getline(std::cin, input);
    }


}

int Studio::getNumOfTrainers() const{
    return trainers.size();
}

Trainer* Studio::getTrainer(int tid){
    if(tid < getNumOfTrainers() && tid >= 0)
        return trainers[tid];
    else
        return nullptr;
}

const std::vector<BaseAction*>& Studio::getActionsLog() const{
    return actionsLog;
} // Return a reference to the history of actions


std::vector<Workout>& Studio::getWorkoutOptions() {
    return workout_options;
}

void Studio::accept(BaseAction* action) {
    actionsLog.push_back(action);
}

void Studio::close() {
    open=false;
}

Studio::Studio(const Studio &other) {
    open=other.open;
    customerId=other.customerId;
    for(Trainer* t:other.trainers){

        trainers.push_back(new Trainer(*(t)));
        
    }
    

    for(Workout w:other.workout_options){
        Workout* temp=new Workout(w);
        workout_options.push_back(*temp);
    }

    for(BaseAction* ba:other.actionsLog)
    {
        actionsLog.push_back(ba->getCopy());
    }
}

Studio::~Studio() {
    for(Trainer* t:trainers)
        delete t;
    for(BaseAction* ba:actionsLog)
        delete ba;
}

Studio &Studio::operator=(const Studio &other) {
    open=other.open;
    customerId=other.customerId;
    for(Trainer* t:trainers)
        delete t;
    workout_options.clear();
    for(BaseAction* ba:actionsLog)
        delete ba;
    for(Trainer* t:other.trainers){
        trainers.push_back(new Trainer(*(t)));
    }

    for(Workout w:other.workout_options){
        Workout* temp=new Workout(w);
        workout_options.push_back(*(temp));
    }

    for(BaseAction* ba:other.actionsLog)
    {
        actionsLog.push_back(ba->getCopy());
    }
    return *this;
}

Studio::Studio(Studio &&other) {
    open=other.open;
    customerId=other.customerId;
    

    for(std::size_t i=0;i<other.trainers.size();i++){
        trainers.push_back(other.trainers[i]);
        other.trainers[i] = nullptr;
    }
    

    for(Workout w:other.workout_options){
        workout_options.push_back(w);
    }
    other.workout_options.clear();
    for(BaseAction* ba:other.actionsLog)
        actionsLog.push_back(ba);
    for(BaseAction* ba:other.actionsLog)
        delete ba;
}

Studio &Studio::operator=(Studio &&other) {
    open=other.open;
    customerId=other.customerId;
    for(Trainer* t:trainers)
        delete t;

    workout_options.clear();
    workout_options.shrink_to_fit();
    for(BaseAction* ba:actionsLog)
        delete ba;
    
    for(std::size_t i=0;i<other.trainers.size();i++){
        trainers.push_back(other.trainers[i]);
        other.trainers[i] = nullptr;
    }
    
    for(Workout w:other.workout_options){
        workout_options.push_back(w);
    }
    other.workout_options.clear();
    for(BaseAction* ba:other.actionsLog)
        actionsLog.push_back(ba);
    for(BaseAction* ba:other.actionsLog)
        delete ba;

    return *this;
}
