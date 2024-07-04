import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { BrowserRouter as Router } from "react-router-dom";
import Entrance from "../Entrance/Entrance";

test("Shows password message when clicking on the password input", () => {
  // Mock setUserExists function
  const setUserExists = jest.fn();

  render(
    <Router>
      <Entrance setUserExists={setUserExists} />
    </Router>
  );

  const passwordInput = screen.getByLabelText("Password");

  // Click on the password input
  fireEvent.click(passwordInput);

  // Check if the password message appears
  const passwordMessage = screen.getByText(
    "At least 8 digits long, a combination of characters and letters and capital letters."
  );
  expect(passwordMessage).toBeInTheDocument();
});
