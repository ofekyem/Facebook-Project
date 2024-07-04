# Page 1 - Run The Tcp Server 

## About The Tcp server and BloomFilter
In this page you will run our TCP server that also is running the Bloom Filter. 

*The Bloom Filter* is some kind of array that holds URl adresses. those url adresses that is this bloom filter are considered "Invalid Urls" because,
if some user will try to publish a facebook Post with one of these urls, he will get rejected. the bloom filter code was developed in cpp and its working with hash functions that are in charge of the insertion of urls. the urls that will enter the bloom filter are written in the env file of the node js server which you will read more about in the next page. we developed this bloom filter during the first step of the project (read more about it in main branch in the repository), and it got updated in step 4 (part4 branch in the repository). in the first part the bloom filter worked diffrently with the terminal (you can see it there in the main branch) and now its working with the tcp server. 

*The TCP server* is a server that was developed in C++ and its purpose is to manage the bloom filter and help it comunnicate with the Node js server that controls the facebook app and the facebook website. this server will help us know if some user entered an invalid url in his post. it was developed in step 4 of the project (part4 branch).

## Before running:
You need to make sure you have Vs-Code software which will help you to run this code,

You also need to make sure you have some tools that are used here to compile c++ program: 

open the terminal and run these: 

* sudo apt-get update
* sudo apt-get install -y g++
* sudo apt-get install libgtest-dev cmake 

## Compile and run the bloom filter and the tcp server
- first, download this folder (Bloom-Filter and TCP-Server) from repository in zip format and extract it.
- open the terminal in that folder (or in terminal in Vs-Code with this folder opened).
- run the following commands:
- cd src
- make
- ./ex4
- now the server and the bloom filter are running!

### Use the Docker to run 
There is also an option to run it with Docker: 
- enter the terminal in the folder of this repo (or in vscode terminal).
- run the following commands:
- sudo docker build -t ex4 .
- sudo docker run -it -p 5555:5555 ex4
- now the server and the bloom filter are running!
- there maybe a case when you try to run the server in second time and you will get an error from the run command,
in this case just do this command:
- sudo systemctl restart docker
- and then try to run again and it will work!.

## Some remarks 

We suggest you to also read the readMe file in the repository! 

when you run this server don't close it! you will need this running for the next part! 

## Now 

So now that your Tcp server is running you can continue to the next page


