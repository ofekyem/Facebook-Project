import "./AddPost.css"
import React, { useState , useRef } from "react";
import { CreatePost } from "../../ServerCalls/userCalls";
import {convertToBase64} from "../../UsableFunctions/ImageFunctions";
/*
this component is used to add a post
it gets the setPosts, posts, userLoggedIn,id and setId as props
*/
function AddPost({addPostState,userLoggedIn,token}){
    // all the refs that we get from the form
    const postText= useRef("");
    const [photo, setPhoto] = useState(null);
    const fileInput = useRef(null);
    let currentTime = new Date();
    // this function is user to delete the photo that the user has uploaded
    function handleCancelPhoto() {
      setPhoto(null);
    }
    // this function is used to open the file input when the user clicks on the photo button
    function handleButtonClick() {
        fileInput.current.click();
    }
    // this function is used to get the photo that the user has uploaded
    async function handleFileChange(event) {
      if (event.target.files.length > 0) {
          const file = event.target.files[0];
          const base64 = await convertToBase64(file);
          setPhoto(base64);
      }
      event.target.value = null;
  }
    // this function is used to add a post
    const addPost = async () => {
        // this is used to check if the user has entered some text or not
        if(postText.current.value === ""){
            alert("Please enter some text");
            return;
        }
        const newPost = {
            idUserName: userLoggedIn._id,
            fullname: userLoggedIn.displayName,
            icon: userLoggedIn.photo,
            initialText: postText.current.value,
            pictures: photo,
            time: currentTime.toLocaleString(),
            likes: [],
            commentsNumber: 0,
            comments: []
        }
        const [status,post] = await CreatePost(token,newPost, userLoggedIn._id);
        if (status === 200) {
          // addPostState(newPost);
            alert("Post added successfully");
            //setPosts([post, ...posts]);
            addPostState(post);
        } else if(status === 300){
          alert("The url is in the BloomFilter");
        }else {
            alert("Failed to Add Post", status);
        }
        postText.current.value = "";
        setPhoto(null);
    }
    // this is used to get the photo of the user if the user has uploaded a photo
    //const photoUrl = userLoggedIn && userLoggedIn.photo ? URL.createObjectURL(userLoggedIn.photo) : null;
    return (
      <div className="postadd1">
        <div className="container-post">
          <div className="user-profile">
            <img src={userLoggedIn.photo} className="avatar__img" alt="User avatar" />
            <div>
              <p className="p">{userLoggedIn.displayName}</p>
            </div>
          </div>
          <div className="input-preview">
            <textarea
              className="form-control-post"
              id="formControl"
              rows="3"
              placeholder={`${userLoggedIn.displayName}, what do u think?`}
              ref={postText}
            ></textarea>
          </div>
          <div className="photo-preview">
            {photo && (
            <>
              <button onClick={handleCancelPhoto}>Delete Photo</button>
              <img src={photo} alt="Preview" />
            </>
            )}
          </div>
          <div className="button-group-post">
            <button type="button" className="btn btn-outline-primary">
              <i className="material-icons">video_call</i>Live Video
            </button>
            <button
              type="button"
              className="btn btn-outline-primary"
              onClick={handleButtonClick}
            >
              <i className="material-icons">add_a_photo</i>Photo/Video
            </button>
            <input
              type="file"
              id="fileInput"
              accept="image/*"
              style={{ display: "none" }}
              ref={fileInput}
              onChange={handleFileChange}
            />
            <button type="button" className="btn btn-outline-primary">
              <i className="material-icons">insert_emoticon</i>Felling/Activity
            </button>
          </div>
          <div className="button-post">
            <button type="button" className="btn btn-primary" onClick={addPost}>
              Post
            </button>
          </div>
        </div>
      </div>
    );
}
export default AddPost;