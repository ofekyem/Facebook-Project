const postService = require('../services/posts');
const jwt = require('jsonwebtoken');
const Post = require('../models/posts');
const User = require('../models/users');
const userService = require('../services/users');
const commentService = require('../services/comments');
const net = require("net");
process.env.NODE_ENV = 'local';
const customEnv = require('custom-env');
customEnv.env(process.env.NODE_ENV, './config');
const yourIp = process.env.IP;
const portBloom = process.env.PORT_BLOOM; 

const get25Posts = async(req, res) => {
   let token = req.headers.authorization;
   // If the token is prefixed with 'Bearer ', remove the prefix
   if (token.startsWith("Bearer ")) {
     token = token.split(" ")[1];
   }
   // Decode the JWT to get the current user's ID
   const decodedToken = jwt.verify(token, "key"); // Use the same secret key that you used to sign the JWT
   const currentUserId = decodedToken.id;
   const user = await userService.getUserById(currentUserId);
    if (!user) {
      return res.status(404).send("User not found");
    }
    const posts = await postService.get25Posts(user);
    if (posts) {
      res.status(200).json(posts);
    } else {
      res.status(404).send("Error getting posts");
    }
}

const createPost = async(req, res) => {
    const idUserName = req.params.id;
    const fullname = req.body.fullname;
    const icon = req.body.icon;
    const initialText = req.body.initialText;
    const pictures = req.body.pictures;
    const time = new Date();
    const likes = [];
    const comments = [];
    const token = req.headers.authorization;
    const decodedToken = tokendecode(token);
    if (idUserName !== decodedToken.id) {
      return res.status(403).send("You can only create a post for yourself");
    }
    const newPost = new Post({
        idUserName,
        fullname,
        icon,
        initialText,
        pictures,
        time,
        likes,
        comments

    });
    const post = await postService.createPost(newPost);
    if (post == 1) {
      res.status(300).send("Error url found in BloomFilter");
    } else if (post) {
      const user = await User.findById(idUserName);
      user.postList.push(post._id);
      await user.save();
      res.status(200).json(post);
    } else {
      res.status(404).send("Error creating post");
    }
}

const deletePost = async(req, res) => {
    const idUserName = req.params.id;
    const postId = req.params.pid;
    const token = req.headers.authorization;
    const decodedToken = tokendecode(token);
    if (idUserName !== decodedToken.id) {
      return res.status(403).send("You can only delete your own posts");
    }
    const deletedPost = await postService.deletePost(idUserName, postId);
    if (deletedPost) {
      res.status(200).send("post deleted successfully");
    } else {
      res.status(404).send("Error deleting post");
    }
}

const extractUrlsFromText = (initialText) => {
  const urlRegex = /(https?:\/\/[^\s]+)/g;
  const urls = initialText.match(urlRegex);
  return urls;
};

const updatePost = async (req, res) => {
  const idUserName = req.params.id;
  const postId = req.params.pid;
  const initialText = req.body.initialText;
  const pictures = req.body.pictures;
  const token = req.headers.authorization;
  const decodedToken = tokendecode(token);
  if (idUserName !== decodedToken.id) {
    return res.status(403).send("You can only update your own posts");
  }

  const post = await Post.findById(postId);
  if (!post) return res.status(404).send("Error updating post");

  const urls = extractUrlsFromText(initialText);

  if (urls) {
    for (let url of urls) {
      // Create a TCP client
      const client = new net.Socket();

      // Wrap your socket logic inside a new Promise
      const responseData = await new Promise((resolve, reject) => {
        // Connect to your C++ server
        client.connect(portBloom, yourIp, function () {
          // replace "localhost" with your server's IP address
          console.log("Connected to C++ server");

          // Send a message to the C++ server
          client.write(`2 ${url}\n`);
        });

        // Handle data from the server
        client.on("data", function (data) {
          console.log("Received: " + data);

          // Save the data in the responseData variable
          const responseData = data.toString();
          console.log(responseData);
          client.destroy(); // kill client after server's response

          // Resolve the Promise with the responseData
          resolve(responseData);
        });

        // Handle errors
        client.on("error", function (error) {
          console.error("Error connecting to server: ", error);

          // Reject the Promise on error
          reject(error);
        });
      });

      if (responseData == "2") {
        return res.status(300).send("Error url found in BloomFilter");
      }
    }

    const updatedPost = await postService.updatedPost(
      idUserName,
      postId,
      initialText,
      pictures
    );
    if (updatedPost) {
      res.status(200).send("post updated successfully");
    } else {
      res.status(404).send("Error updating post");
    }
  } else {
    const updatedPost = await postService.updatedPost(
      idUserName,
      postId,
      initialText,
      pictures
    );
    if (updatedPost) {
      res.status(200).send("post updated successfully");
    } else {
      res.status(404).send("Error updating post");
    }
  }
};



const getAllPostsByUserId = async (req, res) => {
  const userId = req.params.id;
  // Get the JWT from the Authorization header
  let token = req.headers.authorization;
  // If the token is prefixed with 'Bearer ', remove the prefix
  if (token.startsWith('Bearer ')) {
    token = token.split(' ')[1];
  }
  // Decode the JWT to get the current user's ID
  const decodedToken = jwt.verify(token, 'key'); // Use the same secret key that you used to sign the JWT
  const currentUserId = decodedToken.id;
  const user = await userService.getUserById(userId);
  const currentUser = await userService.getUserById(currentUserId);
  if (!user || !currentUser) 
    return res.status(404).send("User not found");
  // Check if the current user is the same as the user whose posts are being requested or a friend of them
  if (userId !== currentUserId && !user.friendsList.some(friendId => friendId.equals(currentUserId))) 
    return res.status(403).send("You must be a friend to view the posts");
  const posts = await postService.getAllPostsByUserId(userId);
  if (posts) {
    res.status(200).json(posts);
  } else {
    res.status(404).send("Error getting posts");
  }
}

const addLikeOrRemoveLike = async(req, res) => {
    const userId = req.params.id;
    const postId = req.params.pid;
    const token = req.headers.authorization;
    const decodedToken = tokendecode(token);
    if (userId !== decodedToken.id) {
      return res.status(403).send("You can only like or unlike posts for yourself");
    }
    const post = await postService.addLikeOrRemoveLike(userId, postId);
    if (post) {
        res.status(200).json(post);
    } else {
        res.status(404).send("Error adding or removing like");
    }
}

function tokendecode(token){
  if (token.startsWith("Bearer ")) {
    token = token.split(" ")[1];
  }
  return jwt.verify(token, "key");
}



module.exports = {
  get25Posts,
  createPost,
  deletePost,
  updatePost,
  getAllPostsByUserId,
  addLikeOrRemoveLike,
};