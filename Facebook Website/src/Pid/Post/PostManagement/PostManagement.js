import "./PostManagement.css";
import { useEffect, useState } from "react";
import Comment from "./Comment/Comment";
import {addOrRemoveLike , addComment} from "../../../ServerCalls/userCalls";
import {getComments} from "../../../ServerCalls/commentsCalls";
/*
this component is the post management, it contains the likes, comments and the share button
this component gets the likes, commentsNumber, initialComments,userLoggedIn,idComment,setIdComment as props
*/
function PostManagement({
  postId,
  likes,
  userLoggedIn,
  idComment,
  setIdComment,
  handleAddLike,
  handleRemoveLike,
  token,
  isPostMine,
  handleProfilePage,
}) {
  // Set the initial state of the likes, comments and the new comment text and the show comments
  const [likesCount, setLikesCount] = useState(likes.length);
  const [liked, setLiked] = useState(likes.includes(userLoggedIn._id));
  const [comments, setComments] = useState([]);
  const [newCommentText, setNewCommentText] = useState("");
  const [showComments, setShowComments] = useState(false);
  const [refresh, setRefresh] = useState(false);
  
  useEffect(() => {
    getComments(postId, token).then((result) => setComments(result.data));
  }, [token, postId,refresh]);
  /*Handle the like button click
  if the user liked the post it will decrease the likes count
  and if the user didn't like the post it will increase the likes count.
  */
  const handleLikeClick = async () => {
    const status = await addOrRemoveLike(token, userLoggedIn._id, postId);
    if (status === 200) {
      if (liked) {
        setLikesCount((prevLikesCount) => prevLikesCount - 1);
        handleRemoveLike(userLoggedIn._id, postId);
      } else {
        setLikesCount((prevLikesCount) => prevLikesCount + 1);
        handleAddLike(userLoggedIn._id, postId);
      }
      setLiked(!liked);
    }
  };
  // Handle the show comments button click
  const handleShowComments = () => {
    setShowComments(!showComments);
  };

  // Handle the delete comment button click
  const handleDeleteComment = (commentId) => {
    // Filter out the comment with the specified commentId
    const updatedComments = comments.filter(
      (comment) => comment._id !== commentId
    );
    // Update the comments state with the filtered comments
    setComments(updatedComments);
  };

  // this function is used to create a new comment
  const handleSendComment = async () => {
    if (newCommentText.trim() !== "") {
      const newComment = {
        idUserName: userLoggedIn._id,
        fullname: userLoggedIn.displayName,
        icon: userLoggedIn.photo,
        idPost: postId,
        text: newCommentText
      };
      const [status,comment] = await addComment(token, newComment);
      if (status === 200) {
        setComments([...comments, comment]);
        setNewCommentText("");
      }
      else{
        alert("There was a problem with the fetch operation");
      }
    }
    else{
      alert("Please enter a comment");
    }
  };



  return (
    <div className="postManagement">
      <button
        type="button"
        className="btn btn-light position-relative"
        onClick={handleLikeClick}
      >
        {liked ? (
          <i className="bi bi-hand-thumbs-up-fill"></i>
        ) : (
          <i className="bi bi-hand-thumbs-up"></i>
        )}
        <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-secondary">
          {likesCount} <span className="visually-hidden">unread messages</span>
        </span>
      </button>
      <button
        type="button"
        className="btn btn-light position-relative"
        onClick={handleShowComments}
      >
        <i className="bi bi-chat-text"></i>{" "}
        <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-secondary">
          {comments.length}{" "}
          <span className="visually-hidden">unread messages</span>
        </span>
      </button>
      <div
        className="btn-group "
        role="group"
        aria-label="Button group with nested dropdown"
      >
        <div className="btn-group" role="group">
          <button
            type="button"
            className="btn btn-light"
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            <i className="bi bi-cursor"></i>
          </button>
          <ul className="dropdown-menu">
            <li>
              <a className="dropdown-item">
                <i className="bi bi-reply"></i> Share now
              </a>
            </li>
            <li>
              <a className="dropdown-item">
                <i className="bi bi-messenger"></i> Send in Messenger
              </a>
            </li>
            <li>
              <a className="dropdown-item">
                <i className="bi bi-whatsapp"></i> What's Up
              </a>
            </li>
            <li>
              <a className="dropdown-item">
                <i className="bi bi-twitter"></i> Send in Twitter
              </a>
            </li>
            <li>
              <a className="dropdown-item">
                <i className="bi bi-link-45deg"></i> Copy link
              </a>
            </li>
          </ul>
        </div>
      </div>
      {showComments && (
        <div>
          <div>
            <div className="input-group mb-3">
              <button
                type="button"
                className="btn btn-dark"
                onClick={handleSendComment}
              >
                <i className="bi bi-send"></i>
              </button>
              <textarea
                className="form-control"
                aria-label="With textarea"
                value={newCommentText}
                onChange={(e) => setNewCommentText(e.target.value)}
              ></textarea>
            </div>
          </div>
          <div className="comment">
            {comments.map((comment) => (
              <Comment
                key={comment._id}
                {...comment}
                setRefresh={setRefresh}
                refresh={refresh}
                onDelete={handleDeleteComment}
                token={token}
                userLoggedIn={userLoggedIn}
                isPostMine={isPostMine}
                handleProfilePage={handleProfilePage}
              ></Comment>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default PostManagement;
