#include <iostream>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <string.h>
#include <thread>
#include "BloomFilter.h"
#include "ConsoleMenu.h"
#include "OptionMenu.h"

using namespace std;
int fis;
void initialize_client(int client_sock, BloomFilter*& bloomFilter){
    char buffer[4096];
    int expected_data_len = sizeof(buffer) - 1; // Leave space for null terminator
    std::string buffer_str;
    int read_bytes;

    // Keep reading data until we have a complete message
    read_bytes = recv(client_sock, buffer, expected_data_len, 0);
    while(read_bytes > 0){
        buffer[read_bytes] = '\0'; // Null-terminate the data
        buffer_str += buffer; // Append data to buffer_str

        // Process complete messages
        size_t newline_pos = buffer_str.find('\n');
        if (newline_pos != std::string::npos) {
            std::string message = buffer_str.substr(0, newline_pos);
            cout << "Received message: " << message << '\n';
            buffer_str = buffer_str.substr(newline_pos + 1);
            ConsoleMenu consoleMenu = ConsoleMenu();
            consoleMenu.runMenu(message);
            //int splitIndex1 = message.find(' ');
            int size = consoleMenu.getArraySize();
            int hashTimes = consoleMenu.getHashTimes();
            //int splitIndex2 = rest.find(' ');
            int hash1 = consoleMenu.getHash1();
            int hash2 = consoleMenu.getHash2();
            fis = consoleMenu.getFirstInputSize();
            try {
                bloomFilter = new BloomFilter(size,hash1,hash2,hashTimes);
                cout << "Bloom filter initialized with size " << size << " and hash functions " << hash1 << " and " << hash2 << '\n';
            } catch (std::invalid_argument& e) {
                cout << "Invalid argument: " << e.what() << '\n';
                std::cerr << "Invalid: " << e.what() << '\n';
            } catch (std::out_of_range& e) {
                cout << "Out of range: " << e.what() << '\n';
                std::cerr << "Out of range: " << e.what() << '\n';
            }

            // Stop reading after processing the initialization data
            break;
        }

        read_bytes = recv(client_sock, buffer, expected_data_len, 0);
    }
}

void handle_client(int client_sock, BloomFilter* bloomFilter) {
    char buffer[4096];
    int expected_data_len = sizeof(buffer) - 1; // Leave space for null terminator
    std::string buffer_str;
    int read_bytes = recv(client_sock, buffer, expected_data_len, 0);
    cout << "Client connected" << endl;
    
    while(read_bytes > 0){
        buffer[read_bytes] = '\0'; // Null-terminate the data
        buffer_str += buffer; // Append data to buffer_str

        // Process complete messages
        size_t newline_pos;
        while ((newline_pos = buffer_str.find('\n')) != std::string::npos) {
            std::string message = buffer_str.substr(0, newline_pos);
            buffer_str = buffer_str.substr(newline_pos + 1);
            OptionMenu optionMenu = OptionMenu();
            optionMenu.runMenu(message);
            try {
                int choice = optionMenu.getChoice();
                std::string url = optionMenu.getUserUrl(); 
                int result = bloomFilter->execute(choice, url, fis);
                std::string response = std::to_string(result);

                // Send the response back to the client
                send(client_sock, response.c_str(), response.size(), 0);

            } catch (std::invalid_argument& e) {
                std::cerr << "Invalid argument: " << e.what() << '\n';
            } catch (std::out_of_range& e) {
                std::cerr << "Out of range: " << e.what() << '\n';
            }
            // int splitIndex = message.find(' ');
            // if (splitIndex != std::string::npos) {
            //     std::string choice_str = message.substr(0, splitIndex);
            //     std::string url = message.substr(splitIndex+1);

            //     try {
            //         int choice = std::stoi(choice_str);
            //         bloomFilter->execute(choice, url);
            //     } catch (std::invalid_argument& e) {
            //         std::cerr << "Invalid argument: " << e.what() << '\n';
            //     } catch (std::out_of_range& e) {
            //         std::cerr << "Out of range: " << e.what() << '\n';
            //     }
            // } else {
            //     std::cerr << "Invalid input format\n";
            // } 
            

        }

        read_bytes = recv(client_sock, buffer, expected_data_len, 0);
    }
    close(client_sock);
}

int main()
{
    const int server_port = 5555;
    BloomFilter* bloomFilter = nullptr;
    int sock = socket(AF_INET, SOCK_STREAM,0);
    if (sock < 0)
    {
        perror("error creating socket");
    }
    struct sockaddr_in sin;
    memset(&sin,0, sizeof(sin));
    sin.sin_family = AF_INET;
    sin.sin_addr.s_addr = INADDR_ANY;
    std::vector<std::thread> client_threads; // Declare client_threads here
    sin.sin_port = htons(server_port);
    if (bind(sock,(struct sockaddr*)&sin,sizeof(sin)) < 0)
    {
        perror("error binding socket");
    }
    if (listen(sock,5) < 0)
    {
        perror("error listening to a socket");
    }

    struct sockaddr_in client_sin;
    unsigned int addr_len = sizeof(client_sin);
    int client_sock = accept(sock,(struct sockaddr*)&client_sin,&addr_len);
    if (client_sock < 0)
    {
        perror("error accepting client");
    }
    std::thread client_thread(initialize_client, client_sock, std::ref(bloomFilter));
    client_thread.join(); // Wait for initialize_client to finish
    cout << "Server initialized" << endl;
    while (true){
        cout << "Waiting for client to connect" << endl;
        struct sockaddr_in client_sin;
        unsigned int addr_len = sizeof(client_sin);
        int client_sock = accept(sock,(struct sockaddr*)&client_sin,&addr_len);
        if (client_sock < 0)
        {
            perror("error accepting client");
            //continue; // Skip to the next iteration of the loop
        }
        cout << "Client connected" << endl;

        client_threads.push_back(std::thread(handle_client, client_sock, std::ref(bloomFilter)));
    }
    // Join all client threads before shutting down
    for (auto& t : client_threads) {
        if (t.joinable()) {
            t.join();
        }
    }
    
    close(sock);
    return 0;
}