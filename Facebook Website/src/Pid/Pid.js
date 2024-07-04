import "./Pid.css" 
import Post from "./Post/Post";
import AddPost from "./AddPost/AddPost"
import NaviBar from './NaviBar/NaviBar';
import LeftSide from './LeftSide/LeftSide';
import RightSide from "./RightSide/RightSide";
import EditUser from "./EditUser/EditUser";
import ProfilePage from "./ProfilePage/ProfilePage";
import React, { useState , useRef, useEffect } from "react";
import { getAllPosts } from "../ServerCalls/postsCalls";
import { getUserData } from "../ServerCalls/login";

/*
this component is the main component of the pid page
it will render the navbar, the left side, the right side and the posts and the add post component
it will also handle the delete of the post and the delete of the picture
it gets the userLoggedIn, setUserLoggedIn, postList, setPostList, id,setId, idComment and setIdComment as props
*/
function Pid({ setUserLoggedIn, userLoggedIn,idComment,setIdComment , token, setToken}) { 
   // this state is used to set the dark mode
  const [darkMode, setDarkMode] = useState(false);
  const [mode, setMode] = useState(0);
  const [postList, setPostList] = useState([]);
  const [render, setRender] = useState(false);
  const [renderEdit, setRenderEdit] = useState(false);
  const [profileId, setProfileId] = useState(null);

  useEffect(() => {
    if (token) {
      getAllPosts(token).then((result) => setPostList(result.data));
    }
  }, [token,renderEdit,render]);

  useEffect(() => {
    if (token) {
      getUserData(token, userLoggedIn._id).then((result) => setUserLoggedIn(result));
    }
  }, [render,token,renderEdit]);
      
  // this function is used to delete a post
  const handleDeletePost = (postId) => {
    const updatedPosts = postList.filter((post) => post._id !== postId);
    setPostList(updatedPosts);
    refresh();
  };

  const refreshEdit = () => {
    setRenderEdit(!renderEdit);
  };
  const refresh = () => {
    setRender(!render);
  };

  const handleProfilePage = (Id) => {
    setMode(2);
    setProfileId(Id);
  };
  // this function is used to delete a picture
  const handleDeletePicture = (postId) => {
    // this find the post that has the picture and delete it
    //refresh();
    const updatedPostList = postList.map((post) => {

      if (post._id === postId) {
      
        return { ...post, pictures: null };
      } else {
        
        // if the post does not have the picture we just return the post
        return post;
      }
    // we set the postList with the updated postList
    });
    // we set the postList with the updated postList
    setPostList(updatedPostList);
  };
  // this function is used to add a picture to a post
  const handleAddPicture = (postId,photo) => {
    // this find the need to update the post with the new picture
    //refresh();
    const updatedPostList = postList.map((post) => {  
      if (post._id === postId) {
        // we return the post with the new picture
        return { ...post, pictures: photo };
      // if the post does not have the picture we just return the post
      } else {
        return post;
    }
  });
  // we set the postList with the updated postList
    setPostList(updatedPostList);
  };

  const handleEditText = (postId, newText) => {
    const updatedPostList = postList.map((post) => {
      if (post._id === postId) {
        return { ...post, initialText: newText };
      } else {
        return post;
      }
    });
    setPostList(updatedPostList);
  }

  const addPost = (newPost) => {
    setPostList([newPost, ...postList]);
  }

  const toggleDarkMode = () => {
    setDarkMode(!darkMode);
  };

  const handleAddLike = (userId,postId) => {
    const updatedPostList = postList.map((post) => {
      if (post._id === postId) {
        return { ...post, likes: [...post.likes, userId] };
      } else {
        return post;
      }
    });
    setPostList(updatedPostList);
  };

  const handleRemoveLike = (userId,postId) => {
    const updatedPostList = postList.map((post) => {
      if (post._id === postId) {
        return { ...post, likes: post.likes.filter((like) => like !== userId) };
      } else {
        return post;
      }
    });
    setPostList(updatedPostList);
  };

  return (
    <div className="container-fluid">
      <div className={darkMode ? "dark-mode" : "liweb"}>
        <div className="navbar-fixed">
          <NaviBar
            userLoggedIn={userLoggedIn}
            setUserLoggedIn={setUserLoggedIn}
            toggleDarkMode={toggleDarkMode}
            darkMode={darkMode}
            setMode={setMode}
            token={token}
            setToken={setToken}
            refresh={refresh}
            setProfileId={setProfileId}
          ></NaviBar>
        </div>
        <div className="row">
          <div className="col-3 vh-100 leftSideCol">
            <LeftSide
              userLoggedIn={userLoggedIn}
              token={token}
              setMode={setMode}
              setProfileId={setProfileId}
            ></LeftSide>
          </div>
          <div className="col pidCol">
            {mode === 0 && (
              <div>
                <div>
                  <AddPost
                    addPostState={addPost}
                    userLoggedIn={userLoggedIn}
                    token={token}
                  ></AddPost>
                </div>
                <div>
                  {postList.map((post) => (
                    <Post
                      key={post._id}
                      {...post}
                      deletePostState={handleDeletePost}
                      deletePicture={handleDeletePicture}
                      userLoggedIn={userLoggedIn}
                      addPicture={handleAddPicture}
                      idComment={idComment}
                      setIdComment={setIdComment}
                      token={token}
                      refresh={refresh}
                      handleProfilePage={handleProfilePage}
                      editText={handleEditText}
                      handleAddLike={handleAddLike}
                      handleRemoveLike={handleRemoveLike}
                    ></Post>
                  ))}
                </div>
              </div>
            )}
            {mode === 1 && ( 
              <div>
                <EditUser
                  userLoggedIn={userLoggedIn}
                  setUserLoggedIn={setUserLoggedIn}
                  token={token}
                  setToken={setToken}
                  setMode={setMode}
                  refresh={refreshEdit}
                ></EditUser>
              </div>
            )}
            {mode === 2 && (
              <div>
                <ProfilePage
                  userLoggedIn={userLoggedIn}
                  profileId={profileId}
                  setMode={setMode}
                  token={token}
                  handleDeletePost={handleDeletePost}
                  handleDeletePicture={handleDeletePicture}
                  addPicture={handleAddPicture}
                  idComment={idComment}
                  setIdComment={setIdComment}
                  refresh={refresh}
                  handleProfilePage={handleProfilePage}
                  render={render}
                  setRender={setRender}
                  editText={handleEditText}
                  handleAddLike={handleAddLike}
                  handleRemoveLike={handleRemoveLike}
                  setProfileId={setProfileId}
                ></ProfilePage>
              </div>
              
            )}
          </div>
          <div className="col-3 vh-100 rightSideCol">
            <RightSide></RightSide>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Pid;
