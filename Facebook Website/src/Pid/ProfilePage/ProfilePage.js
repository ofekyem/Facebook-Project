import "./ProfilePage.css"
import React, { useState , useRef , useEffect } from "react";
import { getUserData } from "../../ServerCalls/login";
import { getAllFriends , getPostsByUser,sendFriendRequestToServer , acceptFriendRequestServer , deleteFriendRequestServer } from "../../ServerCalls/userCalls";
import Post from "../Post/Post";
import FriendsView from "./FriendsView/FriendsView";
function ProfilePage({userLoggedIn , profileId , setMode , token,handleDeletePost,handleDeletePicture,addPicture,idComment,setIdComment,refresh,handleProfilePage,render,setRender,editText,handleAddLike,handleRemoveLike,setProfileId}) {
    const [profileData, setProfileData] = useState({});
    const [posts, setPosts] = useState([]);
    const [friends, setFriends] = useState([]);
    useEffect(() => {
        if (token) {
            getUserData(token, profileId).then((result) => setProfileData(result));
        }
    }, [profileId,token,render]);
    useEffect(() => {
        if (token) {
            getAllFriends(token, profileId).then((result) => setFriends(result));
        }
    }, [profileId,token,render]);
    useEffect(() => {
        if (token) {
            getPostsByUser(token, profileId).then((result) => setPosts(result));
        }
    }, [profileId,token,render]);
    const sendFriendRequest = async () => {
        //const [res,user] = await sendFriendRequestToServer(token, userLoggedIn._id, idUserName);
        const res = await sendFriendRequestToServer(token, userLoggedIn._id, profileId);
        if (res === 200) {
            alert("Friend request sent successfully");
            setRender(!render)
          refresh();
        } else {
          alert("There was a problem with the fetch operation: ", res);
        }
      };
      const acceptFriendRequest = async () => {
        //const [res,user] = await acceptFriendRequestServer(token, userLoggedIn._id, idUserName);
        const res = await acceptFriendRequestServer(token, userLoggedIn._id, profileId);
        if (res === 200) {
          alert("Friend request accepted successfully");
          setRender(!render)
          refresh();
        } else {
          alert("There was a problem with the fetch operation: ", res);
        }
      }
      const handleDeletePostProfile = (postId) => {
        const updatedPosts = posts.filter((post) => post._id !== postId);
        setPosts(updatedPosts);
        handleDeletePost(postId);
    };
    const handleDeletePictureProfile = (postId) => {
        const updatedPosts = posts.map((post) => {
            if (post._id === postId) {
                return { ...post, pictures: null };
            } else {
                return post;
            }
        });
        setPosts(updatedPosts);
        handleDeletePicture(postId);
    };
    const handleAddPictureProfile = (postId, picture) => {
        const updatedPosts = posts.map((post) => {
            if (post._id === postId) {
                return { ...post, pictures: picture };
            } else {
                return post;
            }
        });
        setPosts(updatedPosts);
        addPicture(postId, picture);
    };
    const handleEditTextProfile = (postId, newText) => {
        const updatedPosts = posts.map((post) => {
            if (post._id === postId) {
                return { ...post, initialText: newText };
            } else {
                return post;
            }
        });
        setPosts(updatedPosts);
        editText(postId, newText);
    };
      const deleteFriendRequest = async () => {
        const res = await deleteFriendRequestServer(token, userLoggedIn._id, profileId);
        if (res === 200) {
          alert("Friend deleted successfully");
          setRender(!render)
          refresh();
        } else {
          alert("There was a problem with the fetch operation: ", res);
        }
      }
    const handleAddLikeProfile = (userId, postId) => {
        const updatedPosts = posts.map((post) => {
            if (post._id === postId) {
                return { ...post, likes: [...post.likes, userId] };
            } else {
                return post;
            }
        });
        setPosts(updatedPosts);
        handleAddLike(userId, postId);
    };
    const handleRemoveLikeProfile = (userId, postId) => {
        const updatedPosts = posts.map((post) => {
            if (post._id === postId) {
                return { ...post, likes: post.likes.filter((id) => id !== userId) };
            } else {
                return post;
            }
        });
        setPosts(updatedPosts);
        handleRemoveLike(userId, postId);
    };
    return(
        
        <div className="profile-container">
            <div className="warper">
            <div className="top-section">
                <div className="pd-left">
                    <div className="pd-row">
                        <img src={profileData.photo} className="pd-image" alt=""></img>
                        <div className="op">
                            <h3>{profileData.displayName}</h3>
                            <p>{profileData.friendsList?.length} friends</p>
                        </div>
                    </div>
                </div>
                    <div className="pd-right">
                        {userLoggedIn._id !== profileId && (
                            userLoggedIn.friendsList.includes(profileId) ? (
                                <button className="fb" onClick={deleteFriendRequest}><i className="bi bi-person-x"></i> Delete Friend </button>
                            ) : userLoggedIn.friendRequestsSent.includes(profileId) ? (
                                <div> <i className="bi bi-hourglass-split"></i> Friend request sent </div>
                            ) : userLoggedIn.friendRequests.includes(profileId) ? (
                                <button className="fb" onClick={acceptFriendRequest}><i className="bi bi-person-plus-fill"></i> Aproove</button>
                            ) : (
                                <button className="fb" onClick={sendFriendRequest}><i className="material-icons">person_add</i> Add friend</button>
                            )
                        )}
                    </div>
                </div>
            </div>
            <div className="bottom-section">
                <FriendsView
                    userLoggedIn={userLoggedIn}
                    setProfileId={setProfileId}
                    friends={friends}>
                </FriendsView>
            </div>
            <div className="bottom-section">
                {Array.isArray(posts) && posts.length > 0 && posts.map((post) => (
                    <Post
                        key={post._id}
                        {...post}
                        deletePostState={handleDeletePostProfile}
                        deletePicture={handleDeletePictureProfile}
                        userLoggedIn={userLoggedIn}
                        addPicture={handleAddPictureProfile}
                        idComment={idComment}
                        setIdComment={setIdComment}
                        token={token}
                        refresh={refresh}
                        handleProfilePage={handleProfilePage}
                        editText={handleEditTextProfile}
                        handleAddLike={handleAddLikeProfile}
                        handleRemoveLike={handleRemoveLikeProfile}
                    ></Post>
                ))}
                {!Array.isArray(posts) && <p className="ermes">You need to be his friend to see his posts</p>}
                {Array.isArray(posts) && posts.length === 0 && <p className="ermes">No posts available</p>}
            </div>
            
        </div>
    )
}
export default ProfilePage;