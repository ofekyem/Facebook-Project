const deleteUser = async (token, id) => {
    try {
        const res = await fetch(`http://localhost:8080/api/users/${id}`, {
        method: "delete",
        headers: {
            authorization: token,
            "Content-Type": "application/json",
        },
    });
    return res.status;
    } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return 404;
}
};
const updateUser = async (token, user) => {
    const id = user._id;
    try {
        const res = await fetch(`http://localhost:8080/api/users/${id}`, {
        method: "put",
        headers: {
            authorization: token,
            "Content-Type": "application/json",
        },
        body: JSON.stringify(user),
    });
    return res.status;
    } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return 404;
}
};
const CreatePost = async (token, post, id) => {
  try {
    const res = await fetch(`http://localhost:8080/api/users/${id}/posts`, {
      method: "post",
      headers: {
        authorization: token,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(post),
    });
    let statusNum = res.status;
    if (statusNum === 300) {
      return [300, null];
    }
    let newPost = await res.json();
    return [statusNum, newPost];
  } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return [404, null];
  }
};


const deletePost = async (token, postId,userId) => {
    try {
        const res = await fetch(
          `http://localhost:8080/api/users/${userId}/posts/${postId}`,
          {
            method: "delete",
            headers: {
              authorization: token,
              "Content-Type": "application/json",
            },
          }
        );
          if (!res.ok) {
            throw new Error(`HTTP error! status: ${res.status}`);
          }
    return res.status;

    } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return 404;
}
}

const updatePost = async (token, post,userId) => {
  const postId = post._id;
    try {
        const res = await fetch(
          `http://localhost:8080/api/users/${userId}/posts/${postId}`,
          {
            method: "put",
            headers: {
              authorization: token,
              "Content-Type": "application/json",
            },
            body: JSON.stringify(post),
          }
        );
          // if (!res.ok) {
          //   throw new Error(`HTTP error! status: ${res.status}`);
          // }
          if (res.status === 300) {
            return 300;
          }
    return res.status;
    } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return 404;
}
}

const sendFriendRequestToServer = async (token, userId, friendId) => {
  try {
    const res = await fetch(
      `http://localhost:8080/api/users/${userId}/friends`,
      {
        method: "post",
        headers: {
          authorization: token,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ friendId }),
      }
    );
    // let statusNum = res.status;
    // let user = await res.json();
    // return [statusNum, user];
    return res.status;
  } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return 404;
  }
};

const acceptFriendRequestServer = async (token, userId, friendId) => {
  try {
    const res = await fetch(
      `http://localhost:8080/api/users/${userId}/friends/${friendId}`,
      {
        method: "put",
        headers: {
          authorization: token,
          "Content-Type": "application/json",
        },
      }
    );
    // let statusNum = res.status;
    // let user = await res.json();
    // return [statusNum, user];
    return res.status;
  } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return 404;
  }
};

const deleteFriendRequestServer = async (token, userId, friendId) => {
  try {
    const res = await fetch(
      `http://localhost:8080/api/users/${userId}/friends/${friendId}`,
      {
        method: "delete",
        headers: {
          authorization: token,
          "Content-Type": "application/json",
        },
      }
    );
    return res.status;
  } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return 404;
  }
};

const getAllFriends = async (token, profileId) => {
  try{
    const res = await fetch(
      `http://localhost:8080/api/users/${profileId}/friends`,
      {
        method: "get",
        headers: {
          authorization: token,
          "Content-Type": "application/json",
        },
      }
    );
    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`);
    }
    let friends = await res.json();
    return friends;
  } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return 404;
}
}

const getPostsByUser = async (token, userId) => {
  try{
    const res = await fetch(
      `http://localhost:8080/api/users/${userId}/posts`,
      {
        method: "get",
        headers: {
          authorization: token,
          "Content-Type": "application/json",
        },
      }
    );
    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`);
    }
    let posts = await res.json();
    return posts;
  } catch (error) {
    console.error("this user is not your friend ", error);
    return 404;
}
}

const addOrRemoveLike = async (token, userId, postId) => {
  try {
    const res = await fetch(
      `http://localhost:8080/api/users/${userId}/posts/${postId}/likes`,
      {
        method: "post",
        headers: {
          authorization: token,
          "Content-Type": "application/json",
        },
      }
    );
    return res.status;
  } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return 404;
  }
}


const addComment = async (token, comment) => {
  try {
      const res = await fetch(`http://localhost:8080/api/users/${comment.idUserName}/posts/${comment.idPost}/comments`, {
          method: "post",
          headers: {
              authorization: token,
              "Content-Type": "application/json",
          },
          body: JSON.stringify(comment),
      });
      let statusNum = res.status;
      let newComment = await res.json();
      return [statusNum, newComment];
      } catch (error) {
      console.error("There was a problem with the fetch operation: ", error);
      return [404, null];
  }
};

const editComment = async (token, comment) => {
  try {
      const res = await fetch(`http://localhost:8080/api/users/${comment.idUserName}/posts/${comment.idPost}/comments/${comment._id}`, {
          method: "put",
          headers: {
              authorization: token,
              "Content-Type": "application/json",
          },
          body: JSON.stringify(comment),
      });
      return res.status;
      } catch (error) {
      console.error("There was a problem with the fetch operation: ", error);
      return 404
  }
}

const deleteComment = async (token, comment) => {
  try {
      const res = await fetch(`http://localhost:8080/api/users/${comment.idUserName}/posts/${comment.idPost}/comments/${comment._id}`, {
          method: "delete",
          headers: {
              authorization: token,
              "Content-Type": "application/json",
          },
      });
      return res.status;
      } catch (error) {
      console.error("There was a problem with the fetch operation: ", error);
      return 404
  }
}

export { deleteUser, updateUser , CreatePost, deletePost, updatePost , sendFriendRequestToServer, acceptFriendRequestServer , deleteFriendRequestServer , getAllFriends, getPostsByUser , addOrRemoveLike , addComment , editComment , deleteComment};