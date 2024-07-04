const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const PostSchema = new Schema({

    idUserName: {
        type: String,
        required:true
    },
    fullname: {
        type: String,
        required: true
    },
    icon: {
        type: Object,
        required: true
    },
    initialText:{
        type:String,
        required: true
    },
    pictures:{
        type: Object,
        required: false
    },
    time:{
        type:Date,
        required:true,
        default: Date.now
    }, 
    comments: {
        type: Array,
        required: true
    },
    likes: {
        type: Array,
        required: true
    }
}); 

module.exports = mongoose.model('posts', PostSchema);
