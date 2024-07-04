import "./Comment.css";
import React, { useState } from "react";
import { editComment, deleteComment } from "../../../../ServerCalls/userCalls";
/*
this component is the comment component, it contains the comment and the comment management
*/
function Comment({
  _id,
  idUserName,
  fullname,
  icon,
  idpost,
  text,
  setRefresh,
  refresh,
  onDelete,
  token,
  userLoggedIn,
  isPostMine,
  handleProfilePage,
}) {
  // Set the initial state of the text, the edit mode and the edited text
  const [commentText, setCommentText] = useState(text);
  const [editMode, setEditMode] = useState(false);
  const [originalText, setOriginalText] = useState(text);
  const iconUrl = icon instanceof File ? URL.createObjectURL(icon) : icon;

  const handleDeleteClick = async () => {
    const status = await deleteComment(token, { _id, idUserName, idpost });
    if (status === 200) {
      setRefresh(!refresh);
      onDelete(_id);
    }
  };
  // Handle the input change
  const handleInputChange = (event) => {
    setCommentText(event.target.value);
  };
  // Handle the edit button click
  const handleEditClick = () => {
    setEditMode(true);
    setOriginalText(commentText);
  };
  // Handle the save button click
  // Handle the save button click
  const handleSaveClick = async () => {
    if (commentText.trim() !== "") {
      const status = await editComment(token, {
        _id,
        idUserName,
        idpost,
        text: commentText,
      });
      if (status === 200) {
        setRefresh(!refresh);
      }
      else {
        alert("There was a problem with the fetch operation");
      }
      setEditMode(false);
    } else {
      alert("Comment cannot be empty");
    }
  };
  // Handle the restore button click
  const handleRestoreClick = () => {
    setCommentText(originalText);
    setEditMode(false);
  };
  return (
    <div className="commentDiv">
      <div className="avatar" onClick={() => handleProfilePage(idUserName) }>
        {icon && <img src={iconUrl} className="avatar__img" alt="User" />}
        <span>{fullname}:</span>{" "}
      </div>
      {editMode ? (
        <textarea
          className="textarea"
          value={commentText}
          onChange={handleInputChange}
        />
      ) : (
        <span className="span">{commentText}</span>
      )}
      <div>
        {editMode ? (
          <React.Fragment>
            <button className="editButton" onClick={handleSaveClick}>
              <i className="bi bi-check-lg"></i>
            </button>
            <button className="editButton" onClick={handleRestoreClick}>
              <i className="bi bi-trash3-fill"></i>
            </button>
          </React.Fragment>
        ) : (
          <React.Fragment>
            {userLoggedIn._id === idUserName && (
              <button className="editButton" onClick={handleEditClick}>
                <i className="bi bi-pencil-fill"></i>
              </button>
            )}
            {(userLoggedIn._id === idUserName || isPostMine ) && (
              <button className="editButton" onClick={handleDeleteClick}>
                <i className="bi bi-trash3-fill"></i>
              </button>
            )}
          </React.Fragment>
        )}
      </div>
      <hr></hr>
    </div>
  );
}

export default Comment;
