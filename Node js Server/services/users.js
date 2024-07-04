const User = require('../models/users');
const jwt = require('jsonwebtoken');
const postService = require('../services/posts');
const commentService = require('../services/comments');
const ObjectId = require('mongoose').Types.ObjectId;

const createUser = async (username, password, displayName, photo) => {
  const user = new User({
    username,
    password,
    displayName,
    photo,
    postList: [],
    friendsList: [],
    friendRequests: [],
    friendRequestsSent: [],
    likes: [],
    comments: [],
  });
  try {
    return await user.save();
  } catch (error) {
    console.log(error);
  }
};

const getUser = async (username, password) => {
  try {
    return await User.findOne({ username, password }).exec();
  } catch (error) {
    res.status(404).send(error);
  }
};

const getUserByUsername = async (username) => {
  try {
    return await User.findOne({ username }).exec();
  } catch (error) {
    res.status(404).send(error);
    return null;
  }
};

const getUserById = async (id) => {
  try {
    return await User.findById(id).select('-password').exec();
  } catch (error) {
    res.status(404).send(error);
    return null;
  }
};

const getUserByIdWithPassword = async (id) => {
  try {
    return await User.findById(id).exec();
  } catch (error) {
    res.status(404).send
    return null;
  }
}

const getUserForProfile = async (id) => {
  try {
    return await User.findById(id).select('displayName photo friendsList').exec();
  } catch (error) {
    res.status(404).send
    return null;
  }
}

const updateUser = async (id, username, password, displayName, photo) => {
  const user = await getUserByIdWithPassword(id);
  if (!user) 
    return null;
  if (username !== user.username) {
    const userExists = await getUserByUsername(username);
    if (userExists) 
      return null;
    Object.assign(user, { username });
  }
  if (password !== user.password && password !== "" && password !== undefined && password !== null) {
    Object.assign(user, { password });
  }
  if (displayName !== user.displayName) {
    Object.assign(user, { displayName });
    await postService.updateUserPosts(id, { fullname: displayName });
    await commentService.updateUserComments(id, { fullname: displayName });
    
  }
  if (photo !== user.photo && photo !== "" && photo !== undefined && photo !== null) {
    Object.assign(user, { photo });
    await postService.updateUserPosts(id, { icon: photo });
    await commentService.updateUserComments(id, { icon: photo });
  }
  return await user.save();
};


const deleteUser = async (id) => {
  const user = await getUserById(id);
  if (!user) 
    return null;
  await user.deleteOne();
  return true;
};

const getAllFriends = async (id) => {
  const user = await getUserById(id);
  if (!user) 
    return null;
  const friends = [];
  for (let i = 0; i < user.friendsList.length; i++) {
    const friend = await getUserById(user.friendsList[i]);
    friends.push(friend);
  }
  return friends;
}

const addFriend = async (id, friendId) => {
  const user = await getUserById(id);
  if (!user) 
    return null;
  const friend = await getUserById(friendId);
  if (!friend) 
    return null;
  user.friendRequestsSent.push(friend._id);
  friend.friendRequests.push(user._id);
  await user.save();
  await friend.save();
  return user;
}

const approveFriendRequest = async (id, friendId) => {
  const user = await getUserById(id);
  if (!user) 
    return null;
  const friend = await getUserById(friendId);
  if (!friend) 
    return null;
  if (user.friendRequests.some(request => request.equals(friend._id))) {
    user.friendRequests = user.friendRequests.filter((request) => !request.equals(friend._id));
    user.friendsList.push(friend._id);
    friend.friendRequestsSent = friend.friendRequestsSent.filter((request) => !request.equals(user._id));
    friend.friendsList.push(user._id);
    await user.save();
    await friend.save();
    return user;
  } else {
    return null;
  }
}

const deleteFriendRequest = async (id, friendId) => {
  const user = await getUserById(id);
  if (!user) 
    return null;
  const friend = await getUserById(friendId);
  if (!friend) 
    return null;
  if(user.friendsList.some(friend => friend.equals(friendId))) {
    user.friendsList = user.friendsList.filter((friend) => !friend.equals(friendId));
    friend.friendsList = friend.friendsList.filter((friend) => !friend.equals(id));
    await user.save();
    await friend.save();
    return user;
  }
  else if (user.friendRequests.some(request => request.equals(friend._id))){
    user.friendRequests = user.friendRequests.filter((request) => !request.equals(friend._id));
    friend.friendRequestsSent = friend.friendRequestsSent.filter((request) => !request.equals(user._id));
    await user.save();
    await friend.save();
    return user;
  }
  return null;
}

const removeUserFromFriendsLists = async (userId) => {
  const userIdObj = new ObjectId(userId);
  await User.updateMany({ friendsList: userIdObj }, { $pull: { friendsList: userIdObj } });
}

const removeUserFromFriendRequests = async (userId) => {
  const userIdObj = new ObjectId(userId);
  await User.updateMany({ friendRequests: userIdObj }, { $pull: { friendRequests: userIdObj } });
}

const removeUserFromFriendRequestsSent = async (userId) => {
  const userIdObj = new ObjectId(userId);
  await User.updateMany({ friendRequestsSent: userIdObj }, { $pull: { friendRequestsSent: userIdObj } });
}


module.exports = {
  createUser,
  getUser,
  getUserByUsername,
  getUserById,
  updateUser,
  deleteUser,
  getUserByIdWithPassword,
  getAllFriends,
  addFriend,
  approveFriendRequest,
  deleteFriendRequest,
  removeUserFromFriendsLists,
  removeUserFromFriendRequests,
  removeUserFromFriendRequestsSent,
  getUserForProfile
};