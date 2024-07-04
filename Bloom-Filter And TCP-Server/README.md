# ex4 - Bloom Filter and Tcp Server - BIU Advanced Programming Course 

### The Tcp BloomFilter
In this program you can see our bloomfilter and our tcp server, the bloom filter and the tcp server were developed in cpp for our part 4 of the project in advanced programming course in BIU. 

The bloom filter is kind of an hash table that stores urls.

You can add url into this bloomfilter and check if an url already exist in it. 

The Tcp server, is a server that is written in cpp and its target is to comunnicate between this bloom filter and our Node js server which will be explained later. 

the node js server is used for our facebook website and android app, you can learn more about them in their folders:  
("Node Js Server", "Facebook Website", "Facebook Android-App" folders)

### Compile and run the bloom filter and the tcp server
- first, download this folder (Bloom-Filter And TCP-Server) in zip format and extract it.
- open the terminal in that folder ( or in vs code).
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

### Some remarks 
In our implementation of the code, the node js server sets the size of the bloom filter and sets the hash functions which will make the insertions of the urls.

Also the urls which will enter into the bloom filter are written in the env file of the node js server. so if you want it to work you need to follow the instructions of the node js server repo. 

*there is a link up there* 

in the node js server there is an env file that contains the size of the bloom filter, the hash functions that will set the insertions and the valid urls.

the node js server is connected to a facebook website and an android app, if some user will try to upload a  new post/ edit a post and put some urls  in it, the node js server will send a request with the urls for the tcp server, and the tcp server will check if those urls are exist in the bloom filter. if they are exist, the tcp server will send back a positive response to the node js server that the user can upload/edit the post. if they are not exist, the tcp server will send back a negative response to the node js server that the post can't be upload/edit. 

If you want to read more about node js server or the website and app, please enter their repos and read their ReadMe files.

## Developed by:

Ofek Yemini  

Eliya Rabia 

Or Shmuel


