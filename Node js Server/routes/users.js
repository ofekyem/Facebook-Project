const express = require('express');
const router = express.Router();
const userController = require('../controllers/users')
const postController = require('../controllers/posts')
const commentsController = require('../controllers/comments')
//Register
router.route('/')
.post(userController.createUser);

router
  .route("/:id")
  //.get(userController.getUserByIdWithPassword)
  .get(userController.getUserById)
  .put(userController.updateUser)
  .delete(userController.deleteUser);

router.route('/:id/posts')
  .get(postController.getAllPostsByUserId)
  .post(postController.createPost);

router.route("/:id/posts/:pid")
  .put(postController.updatePost)
  .delete(postController.deletePost);

router.route("/:id/friends")
.post(userController.addFriend)
.get(userController.getAllFriends);

router.route("/:id/friendRequests")
 .get(userController.getUserByUsername);

router.route("/:id/friends/:fid")
  .put(userController.approveFriendRequest)
  .delete(userController.deleteFriendRequest);

router.route("/:id/posts/:pid/likes")
  .post(postController.addLikeOrRemoveLike);

router.route("/:id/posts/:pid/comments")
  .post(commentsController.addComment);

router.route("/:id/posts/:pid/comments/:cid")
  .put(commentsController.updateComment)
  .delete(commentsController.deleteComment);
  

module.exports = router;