const Post = require('../models/posts');
const User = require('../models/users');
const jwt = require('jsonwebtoken'); 
const commentService = require('../services/comments');
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;
const net = require("net");
process.env.NODE_ENV = 'local';
const customEnv = require('custom-env');
customEnv.env(process.env.NODE_ENV, './config');
const yourIp = process.env.IP;
const portBloom = process.env.PORT_BLOOM;
console.log(yourIp);



const get25Posts = async (user) => {
  // Convert the ObjectIds in the friendsList to strings
  const friendIds = user.friendsList.map((id) => id.toString());

  // Get the 20 most recent posts from the user's friends and the user
  const friendsPosts = await Post.find({
    idUserName: { $in: [...friendIds, user._id.toString()] },
  })
    .sort({ time: -1 })
    .limit(20)
    .exec();

  // Get the IDs of the posts already fetched
  const fetchedPostIds = friendsPosts.map((post) => post._id.toString());

  // Get the 5 most recent posts from users who are not the user's friends and not the user, and which have not already been fetched
  const nonFriendsPosts = await Post.find({
    idUserName: { $nin: [...friendIds, user._id.toString()] },
    _id: { $nin: fetchedPostIds },
  })
    .sort({ time: -1 })
    .limit(5)
    .exec();

  // Combine and sort the posts
  const posts = [...friendsPosts, ...nonFriendsPosts].sort(
    (a, b) => b.time - a.time
  );

  return posts;
};

const extractUrlsFromPost = (post) => {
  const urlRegex = /(https?:\/\/[^\s]+)/g;
  const urls = post.initialText.match(urlRegex);
  return urls;
};


const createPost = async (newPost) => {
  let savedPost = null;
  const urls = extractUrlsFromPost(newPost);

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
        return 1;
      }
    }

    savedPost = await newPost.save();
  } else {
    savedPost = await newPost.save();
  }

  return savedPost;
};

const deleteUserPosts = async (userId) => {
  const posts = await Post.find({ idUserName: userId });
  await Post.deleteMany({ idUserName: userId });
  return posts;
}
const deletePost = async (idUserName, postId) => {
  const postIdObj = new mongoose.Types.ObjectId(postId);
  const post = await Post.findById(postIdObj);
  if (!post) return null;

  // Remove the post from the likes of users who liked it
  await User.updateMany({ likes: postIdObj }, { $pull: { likes: postIdObj } });

  // Find the user and update their postList
  const user = await User.findById(idUserName);
  if (user) {
    const postIndex = user.postList.indexOf(postIdObj);
    if (postIndex > -1) {
      // Remove the post from the postList
      user.postList.splice(postIndex, 1);
    }
  }

  // Delete the comments of the post and remove their IDs from the comments array of all users
  const deletedComments = await commentService.deletePostComments(postId);
  for (let comment of deletedComments) {
    await User.updateMany(
      { comments: comment._id },
      { $pull: { comments: comment._id } }
    );
  }

  // Save the updated user
  await user.save();

  // Delete the post
  await post.deleteOne();

  return post;
}

const updatedPost = async(
  idUserName,
  postId,
  initialText,
  pictures,
) => {
    const post = await Post.findById(postId);
    if (!post) return null;
    post.idUserName = idUserName;
    if(initialText)
      post.initialText = initialText;
    post.pictures = pictures;
    return await post.save();  
}


const updateUserPosts = async (userId, update) => {
  await Post.updateMany({ idUserName: userId }, { $set: update });
}

const getAllPostsByUserId = async (userId) => {
  return await Post.find({ idUserName: userId }).sort({ time: -1 });
}




const addLikeOrRemoveLike = async (userId, postId) => {
  const userIdObj = new ObjectId(String(userId));
  const postIdObj = new ObjectId(String(postId));

  const post = await Post.findById(postIdObj);
  if (!post) return null;
  const user = await User.findById(userIdObj);
  if (!user) return null;

  if (post.likes.includes(userIdObj)) {
    post.likes = post.likes.filter((like) => !like.equals(userIdObj));
    user.likes = user.likes.filter((like) => !like.equals(postIdObj));
  } else {
    post.likes.push(userIdObj);
    user.likes.push(postIdObj);
  }

  await post.save();
  await user.save();
  return post;
}

const deleteUserLikes = async (userId) => {
  const userIdObj = new ObjectId(userId);
  return await Post.updateMany({ likes: userIdObj }, { $pull: { likes: userIdObj } });
}

module.exports = {
  get25Posts,
  createPost,
  deleteUserPosts,
  deletePost,
  updatedPost,
  updateUserPosts,
  getAllPostsByUserId,
  addLikeOrRemoveLike,
  deleteUserLikes,
};