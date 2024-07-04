const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const cors = require('cors');
app.use(cors());
const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const usersRoute = require('./routes/users');
const tokenRoutes = require("./routes/tokens.js"); 
const postsRoute = require("./routes/posts");

process.env.NODE_ENV = 'local';
const customEnv = require('custom-env');
customEnv.env(process.env.NODE_ENV, './config');
const urls = process.env.CONNECTION_URL.split(',');
//console.log(urls);
const init = process.env.INITIALIZATION;
const net = require('net');
const yourIp = process.env.IP;
const portBloom = process.env.PORT_BLOOM;

// Create a new TCP client
const client1 = new net.Socket();
const client2 = new net.Socket();
const client3 = new net.Socket();

client1.connect(portBloom, yourIp, () => {
  console.log("Connected to TCP server with client1");
  client1.write(`${init}\n`);

  // Close client1 after sending the initialization message
  client1.destroy();
});

setTimeout(async () => {
  for (let url of urls) {
    // Create a TCP client
    const client2 = new net.Socket();

    // Wrap your socket logic inside a new Promise
    const responseData = await new Promise((resolve, reject) => {
      // Connect to your C++ server
      client2.connect(portBloom, yourIp, function () {
        console.log("Connected to TCP server with client2");

        // Send a message to the C++ server
        console.log(`Sending URL to server: ${url}`);
        client2.write(`1 ${url}\n`);
      });

      // Handle data from the server
      client2.on("data", function (data) {
        console.log("Received: " + data);

        // Save the data in the responseData variable
        const responseData = data.toString();
        console.log(responseData);

        client2.destroy(); // kill client after server's response

        // Resolve the Promise with the responseData
        resolve(responseData);
      });

      // Handle errors
      client2.on("error", function (error) {
        console.error("Error connecting to server: ", error);

        // Reject the Promise on error
        reject(error);
      });
    });

    if (responseData != "1") {
      return null; // If any URL fails, stop processing and return null
    }
  }
}, 2000);

app.use(bodyParser.json({ limit: '10mb' }));
app.use(bodyParser.urlencoded({ limit: '10mb', extended: true }));
mongoose.connect(process.env.CONNECTION_STRING, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
});

app.use(express.static('public'));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.json());
app.use("/api/users", usersRoute); 
app.use("/api/tokens", tokenRoutes); 
app.use("/api/posts", postsRoute);
//app.use("/api/users/:id/posts", postsRoute); 
const { fileURLToPath } = require('url');
const path = require('path');
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});
app.listen(process.env.PORT);

