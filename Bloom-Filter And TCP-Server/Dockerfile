
FROM gcc:latest

WORKDIR /usr/src/ex1

COPY ./src/ .

RUN g++ -std=c++11 -pthread TcpServer.cpp BloomFilter.cpp ConsoleMenu.cpp OptionMenu.cpp Options.cpp Option1.cpp Option2.cpp -o ex4 

EXPOSE 5555

CMD ["./ex4"] 
