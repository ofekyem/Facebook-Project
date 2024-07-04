const getAllPosts = async (token) => {
  try {
    const res = await fetch(`http://localhost:8080/api/posts`, {
      method: "get",
      headers: {
        authorization: token,
        "Content-Type": "application/json",
      },
    });
    const data = await res.json();
    return { status: res.status, data };
  } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return { status: 404, data: [] };
  }
};

export { getAllPosts };
