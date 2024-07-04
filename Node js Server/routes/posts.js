const express = require("express");
const router = express.Router();
const postController = require("../controllers/posts");
const commentController = require("../controllers/comments");

router.route("/").get(postController.get25Posts);

router.route("/:pid").get(commentController.getCommentsByPostId);


module.exports = router;