CXX=g++
CXXFLAGS=-std=c++11 -pthread

SOURCES=TcpServer.cpp BloomFilter.cpp ConsoleMenu.cpp OptionMenu.cpp Options.cpp Option1.cpp Option2.cpp
OBJECTS=$(SOURCES:.cpp=.o)
EXECUTABLE=ex4

all: $(SOURCES) $(EXECUTABLE)

$(EXECUTABLE): $(OBJECTS)
	$(CXX) $(CXXFLAGS) $(OBJECTS) -o $@
.cpp.o:
	$(CXX) $(CXXFLAGS) -c $< -o $@
clean:
	rm -f $(OBJECTS) $(EXECUTABLE)
