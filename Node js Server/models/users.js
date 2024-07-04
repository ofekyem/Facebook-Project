const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const UserSchema = new Schema({
    username: {
        type: String,
        required: true,
        unique: true
    },
    password: {
        type: String,
        required: true
    },
    displayName: {
        type: String,
        required: true
    },
    photo: {
        type: Object,
        required: true
    },
    postList: {
        type: Array,
        required: true
    },
    friendsList : {
        type: Array,
        required: true
    },
    friendRequests : {
        type: Array,
        required: true
    },
    friendRequestsSent : {
        type: Array,
        required: true
    },
    likes: {
        type: Array,
        required: true
    },
    comments: {
        type: Array,
        required: true
    },
});
module.exports = mongoose.model('users', UserSchema);