const loginServer = async (data) => {
  try {
    const res = await fetch("http://localhost:8080/api/tokens", {
      method: "post",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });
    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`);
    }
    const answer = await res.json();
    let statusNum = res.status;
    let token = answer.token;
    let userId = answer.userId; // get userId from response
    //returning the status number, token and userId!!
    return [statusNum, token, userId];
  } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return [404, null, null];
  }
};

const getUserData = async (token,id) => {
  try {
    const res = await fetch(`http://localhost:8080/api/users/${id}`, {
      method: "get",
      headers: {
        "authorization": token,
        'Content-Type': 'application/json',
      },
    });
    return await res.json();
  }
  catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
    return null;
  }
}

module.exports = {
  loginServer,
  getUserData
};
