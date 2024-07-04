const Post = require('../models/posts');
const User = require('../models/users');
const Comment = require('../models/comments');
const jwt = require('jsonwebtoken'); 
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;

const addComment = async (postId, newComment,id) => {
    const post = await Post.findById(postId);
    const user = await User.findById(id);
    if (!post || !user) return null;
    const comment = new Comment(newComment);
    await comment.save();
    post.comments.push(comment._id);
    user.comments.push(comment._id);
    await post.save();
    await user.save();
    return comment;
}

const getComments = async (postId) => {
    const comments = await Comment.find({ idPost: postId });
    return comments;
}

const updateComment = async (commentId, text) => {
    const comment = await Comment.findByIdAndUpdate
    (commentId, { text }, { new: true });
    return comment;
}

const deleteComment = async (commentId) => {
    const comment = await Comment.findById(commentId);
    if (!comment) return null;

    const commentIdObj = new mongoose.Types.ObjectId(String(commentId));

    await User.updateOne(
        { comments: commentIdObj },
        { $pull: { comments: commentIdObj } }
    );

    await Post.updateOne(
        { comments: commentIdObj },
        { $pull: { comments: commentIdObj } }
    );

    await Comment.findByIdAndDelete(commentId);

    return comment;
}

const deletePostComments = async (postId) => {
    const comments = await Comment.find({ idPost: postId });
    await Comment.deleteMany({ idPost: postId });
    return comments;
}

const deleteUserComments = async (userId) => {
    const comments = await Comment.find({ idUserName: userId });
    await Comment.deleteMany({ idUserName: userId });
    return comments;
}


const updateUserComments = async (userId, update) => {
    await Comment.updateMany({ idUserName: userId }, { $set: update });
}


// const deleteComment = async (commentId) => {
//     const commentIdObj = new ObjectId(String(commentId));
//     const comment = await Comment.findById(commentIdObj);
//     if (!comment) return null;

//     // Remove the comment from the comments of users and posts who have it
//     await User.updateMany({ comments: commentIdObj }, { $pull: { comments: commentIdObj } });
//     await Post.updateMany({ comments: commentIdObj }, { $pull: { comments: commentIdObj } });

//     await comment.deleteOne();

//     return comment;
// }

module.exports = {
    addComment,getComments,updateComment,deleteComment,deletePostComments,deleteUserComments ,updateUserComments
};