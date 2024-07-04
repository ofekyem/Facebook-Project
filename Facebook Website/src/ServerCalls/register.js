//Function that talks with the server.
const registerServer = async (data) => {
    try {
    
    const response = await fetch("http://localhost:8080/api/users", {
        method: "POST",
        headers: {
        "Content-Type": "application/json",
    },
        body: JSON.stringify(data),
    });
    return response.status;
} catch (error) {
    console.error(error);
}
};

module.exports = {
    registerServer,
};
