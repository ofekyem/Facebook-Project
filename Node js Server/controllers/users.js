const userService = require('../services/users');
const jwt = require('jsonwebtoken');
const postSevice = require('../services/posts');
const commentsService = require('../services/comments');
const User = require('../models/users');
const Post = require('../models/posts'); 
const Comment = require('../models/comments');


const createUser = async (req, res) => {
    const username = req.body.username;
    const password = req.body.password;
    const displayName = req.body.displayName;
    const photo = req.body.photo;
    user = await userService.getUserByUsername(username);
    if (!user) {
        res.status(200).json(await userService.createUser(username, password, displayName, photo));
    }else{
        res.status(404).send('Username already exists');
    }
}

const login = async (req, res) => {
  const username = req.body.username;
  const password = req.body.password;
  let user = await userService.getUser(username, password);
  if (user) {
    const userId = user._id; // get user id
    const token = jwt.sign({ id: userId }, "key");
    res.status(200).json({ userId, token });
  } else {
    res.status(404).send("Username or password is incorrect");
  }
};

const getUser = async (req, res) => {
  const username = req.body.username;
  const password = req.body.password;
  user = await userService.getUser(username,password);
  if (user) {
    res
      .status(200)
      .json(req.body.username, req.body.displayName, req.body.photo);
  } else {
    res.status(404).send("Username not found");
  }
};

const getUserByUsername = async (req, res) => {
  const username = req.params.username;
  user = await userService.getUserByUsername(username);
  if (user) {
    res
      .status(200)
      .json(req.body.username, req.body.displayName, req.body.photo);
  } else {
    res.status(404).send("Username not found");
  }
};

// const getUserById = async (req, res) => {
//   const id = req.params.id;
//   let token = req.headers.authorization;
//    // If the token is prefixed with 'Bearer ', remove the prefix
//    if (token.startsWith("Bearer ")) {
//      token = token.split(" ")[1];
//    }
//   try {
//       // Verify the token
//       const decoded = jwt.verify(token, "key");
//       // Check if the token's user id matches the requested user id
//       if (decoded.id !== id) {
//           const userProfile = await userService.getUserForProfile(id);
//           if (userProfile) {
//               res.status(200).json(userProfile);
//           } else {
//               res.status(404).send('User not found');
//           }
//       }
//       else {
//         const user = await userService.getUserById(id);
//         if (user) {
//           res.status(200).json(user);
//         } else {
//           res.status(404).send('User not found');
//       }
//       }
//   } catch (error) {
//       // If the token is invalid or expired, jwt.verify will throw an error
//       res.status(403).send('Invalid token');
//   }
// }

const getUserById = async (req, res) => {
  const id = req.params.id;
  user = await userService.getUserById(id);
  if (user) {
    res.status(200).json(user);
  } else {
    res.status(404).send("User not found");
  }
};

const getUserByIdWithPassword = async (req, res) => {
  const id = req.params.id;
  user = await userService.getUserByIdWithPassword(id);
  if (user) {
    res.status(200).json(user);
  } else {
    res.status(404).send("User not found");
  }
};

const updateUser = async (req, res) => {
    const id = req.params.id;
    const username = req.body.username;
    const password = req.body.password;
    const displayName = req.body.displayName;
    const photo = req.body.photo;
    const token = req.headers.authorization;
    const decoded = tokendecode(token);
    if (decoded.id !== id) {
      res.status(403).send("Invalid token");
      return;
    }
    user = await userService.updateUser(id,username, password, displayName, photo);
    if (user !== null) {
        res.status(200).send('User updated successfully');
    }else{
        res.status(404).send('User not found / Username already exists');
    }
}

const deleteUser = async (req, res) => {
  const id = req.params.id;
  const token = req.headers.authorization;
  const decoded = tokendecode(token);
  if (decoded.id !== id) {
    res.status(403).send("Invalid token");
    return;
  }
  // Find the user's posts
  const userPosts = await Post.find({ idUserName: id });

  // Delete all comments associated with the user's posts and update the comments array of all users
  for (let post of userPosts) {
    const deletedComments = await commentsService.deletePostComments(post._id);
    for (let comment of deletedComments) {
      await User.updateMany(
        { comments: comment._id },
        { $pull: { comments: comment._id } }
      );
    }
    // Remove the post ID from the likes array of all users who liked this post
    await User.updateMany(
      { likes: post._id },
      { $pull: { likes: post._id } }
    );
    // Delete the post after all associated comments have been deleted
    await post.deleteOne();
  }

  // Delete the user's comments on other users' posts and update the comments array of all users
  const userComments = await Comment.find({ idUserName: id });
  for (let comment of userComments) {
    await User.updateMany(
      { comments: comment._id },
      { $pull: { comments: comment._id } }
    );
    // Find the post associated with the comment and remove the comment ID from its comments array
    await Post.updateMany(
      { comments: comment._id },
      { $pull: { comments: comment._id } }
    );
    await comment.deleteOne();
  }

  // Continue with the rest of the deletion process
  await postSevice.deleteUserLikes(id);
  await userService.removeUserFromFriendsLists(id);
  await userService.removeUserFromFriendRequests(id);
  await userService.removeUserFromFriendRequestsSent(id);
  const isDeleted = await userService.deleteUser(id);

  if (isDeleted) {
    res.status(200).send('User deleted successfully');
  } else {
    res.status(404).send('User not found');
  }
}


const getAllFriends = async (req, res) => {
  const id = req.params.id;
  const friends = await userService.getAllFriends(id);
  if (friends) {
    res.status(200).json(friends);
  } else {
    res.status(404).send("User not found");
  }
}

const addFriend = async (req, res) => {
  const id = req.params.id;
  const friendId = req.body.friendId;
  const token = req.headers.authorization;
  const decoded = tokendecode(token);
  if (decoded.id !== id) {
    res.status(403).send("Invalid token");
    return;
  }
  const user = await userService.addFriend(id, friendId);
  if (user) {
    //res.status(200).json(user);
    res.status(200).send("Friend requst sent successfully");
  } else {
    res.status(404).send("Error adding friend");
  }
};

const approveFriendRequest = async (req, res) => {
  const id = req.params.id;
  const friendId = req.params.fid;
  const token = req.headers.authorization;
  const decoded = tokendecode(token);
  if (decoded.id !== id) {
    res.status(403).send("Invalid token");
    return;
  }
  const user = await userService.approveFriendRequest(id, friendId);
  if (user) {
    //res.status(200).json(user);
    res.status(200).send("Friend request approved");
  } else {
    res.status(404).send("Error approving friend request");
  }
};

const deleteFriendRequest = async (req, res) => {
  const id = req.params.id;
  const friendId = req.params.fid;
  const token = req.headers.authorization;
  const decoded = tokendecode(token);
  if (decoded.id !== id) {
    res.status(403).send("Invalid token");
    return;
  }
  const user = await userService.deleteFriendRequest(id, friendId);
  if (user) {
    //res.status(200).json(user);
    res.status(200).send("Friend request deleted");
  } else {
    res.status(404).send("Error deleting friend request");
  }
};

function tokendecode(token){
  if (token.startsWith("Bearer ")) {
    token = token.split(" ")[1];
  }
  return jwt.verify(token, "key");
}


module.exports = {
  createUser,
  login,
  getUserByUsername,
  getUser,
  getUserById,
  getUserByIdWithPassword,
  updateUser,
  deleteUser,
  getAllFriends,
  addFriend,
  approveFriendRequest,
  deleteFriendRequest,
};