#include <stdlib.h>
#include <pthread.h>
#include "connectionHandler.h"
#include <thread>
#include <vector>

#include <iostream>
#include <string>
#include <sstream>
#include <cstring>

#include <ctime>
#include <chrono>

static bool socket_connection_open = true;
using namespace std::literals::chrono_literals;

void ReadFromSocket(ConnectionHandler& connectionHandler){

    while(socket_connection_open){
        const short bufsize = 1024;
        char buf[bufsize];
        std::string answer;

        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        if (!connectionHandler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            socket_connection_open = false;
            //break;
        }

        int len = answer.length();
        // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
        // we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
        answer.resize(len - 1);
        std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
        if (answer == "ACK 3") {
            std::cout << "Exiting...\n" << std::endl;
            socket_connection_open = false;
            break;
        }
    }

}

std::vector<std::string> split(std::string str, char delimiter) {
    std::vector<std::string> internal;
    std::stringstream ss(str); // Turn the string into a stream.
    std::string tok;

    while(getline(ss, tok, delimiter)) {
        internal.push_back(tok);
    }

    return internal;
}

std::string getTimeStr(){
    std::time_t now = std::chrono::system_clock::to_time_t(std::chrono::system_clock::now());

    std::string s(30, '\0');
    std::strftime(&s[0], s.size(), "%d-%m-%Y %H:%M", std::localtime(&now));
    return s;
}


void ReadFromUser(ConnectionHandler& connectionHandler){
    while(socket_connection_open) {
        const short bufsize = 1024;
        char buf[bufsize];
        //std::cin.getline(buf, bufsize);


        std::string input;
        std::getline(std::cin, input);
        std::vector<std::string> sep = split(input, ' ');

        if (sep[0].compare("REGISTER") == 0) {
            sep[0] = "01";
        }
        if (sep[0].compare("LOGIN") == 0) {
            sep[0] = "02";
        }
        if (sep[0].compare("LOGOUT") == 0) {
            sep[0] = "03";
        }

        if (sep[0].compare("FOLLOW") == 0) {
            sep[0] = "04";
            if (sep[1].compare("1") == 0)
                sep[1] = "01";
            else
                sep[1] = "00";
        }

        if (sep[0].compare("POST") == 0) {
            sep[0] = "05";
        }

        if (sep[0].compare("PM") == 0) {
            sep[0] = "06";
        }


        if (sep[0].compare("LOGSTAT") == 0) {
            sep[0] = "07";
        }

        if (sep[0].compare("STAT") == 0) {
            sep[0] = "08";
        }

        if (sep[0].compare("BLOCK") == 0) {
            sep[0] = "12";
        }

        std::string s;
        for (std::vector<std::string>::const_iterator i = sep.begin(); i != sep.end(); ++i)
            s += *i + " ";

        if (sep[0].compare("02") == 0) {
            s += "1 "; // adding captcha to login
        }

        if (sep[0].compare("06") == 0) {
            s += getTimeStr() + " "; // adding date and time
        }

        if (sep[0].compare("08") == 0) {
            s = s.substr(0, s.length() - 1);
            s += "| "; // adding date and time
        }

        s = s.substr(0, s.length() - 1);
        strcpy(buf, s.c_str());

        std::string line(buf);
        int len = line.length();
        if (!connectionHandler.sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            socket_connection_open = false;
            break;
        }
        // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
        std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;
    }
    std::this_thread::sleep_for(1s);
}





/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {

// int main () {
    //int argc = 3;
    //char *argv[] = {"BGSclient", "127.0.0.1", "7777"};
    //char *argv[] = {"BGSclient", "132.73.207.161", "7777"};

    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);





    /*
     * int flags = fcntl(sockfd, F_GETFL);
    flags |= O_NONBLOCK;
    fcntl(sockfd, F_SETFL, flags);
     * */


    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }


    std::thread userInput(ReadFromUser, std::ref(connectionHandler));
    std::thread socketInput(ReadFromSocket, std::ref(connectionHandler));

    userInput.join();
    socketInput.join();



    return 0;
}