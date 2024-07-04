const express = require("express");
const router = express.Router();
const userController = require("../controllers/users");

//Login
router.route("/").post(userController.login);

module.exports = router;
