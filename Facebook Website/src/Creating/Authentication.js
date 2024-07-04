export const validatePassword = (password) => {
    // Check if the password has a length of 8 or more characters
    const hasMinLength = password.length >= 8;
    // Regular expressions to check for capital letter and special character
    const hasCapitalLetter = /[A-Z]/.test(password);
    // Include a broader range of special characters
    const hasSpecialCharacter = /[!@#$%^&*()_+{}\[\]:;<>,.?~\\-]/.test(password);
    // Password is valid if it has a capital letter and a special character
    return hasMinLength && hasCapitalLetter && hasSpecialCharacter;
};

export const isUsernameExists = (username, userList) => {
    return userList.some((user) => user.username === username);
};