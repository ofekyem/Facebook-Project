const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const CommentSchema = new Schema({
    idUserName: {
        type: String,
        required: true
    },
    fullname: {
        type: String,
        required: true
    },
    icon: {
        type: Object,
        required: true
    },
    idPost: {
        type: String,
        required: true
    },
    text:{
        type:String,
        required: true
    },
});  
module.exports = mongoose.model('comments', CommentSchema);