import { render, screen, fireEvent } from "@testing-library/react";
import { BrowserRouter as Router } from "react-router-dom";
import Entrance from "../Entrance/Entrance";

describe("Invalid Login Alert", () => {
  test("Shows alert for invalid login", async () => {
    // Make the function async
    // Mock user data
    const mockUserList = [
      {
        username: "testuser",
        password: "testpassword",
      },
    ];

    window.alert = jest.fn();
    render(
      <Router>
        <Entrance userList={mockUserList}></Entrance>
      </Router>
    );

    // Fill in the username and password fields with invalid credentials
    const usernameInput = screen.getByPlaceholderText("email or phone number");
    const passwordInput = screen.getByPlaceholderText("password");
    fireEvent.change(usernameInput, { target: { value: "invaliduser" } });
    fireEvent.change(passwordInput, { target: { value: "invalidpassword" } });

    // Click on the login button
    const loginButton = screen.getByText("Sign In");
    fireEvent.click(loginButton);

    // Check if the alert was called with the expected content
    expect(window.alert).toHaveBeenCalledWith("Invalid username or password");
  });
});
