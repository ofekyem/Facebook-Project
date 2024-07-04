import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import Entrance from "../Entrance/Entrance";
import { MemoryRouter } from "react-router-dom";

describe("Entrance Component", () => {
  test("Changes password input type to text on mouse over", () => {
    // Wrap your component in MemoryRouter
    render(
      <MemoryRouter>
        <Entrance setUserExists={() => {}} />
      </MemoryRouter>
    );

    const passwordInput = screen.getByLabelText("Password");

    // Initial type should be "password"
    expect(passwordInput).toHaveAttribute("type", "password");

    // Find the eye-slash icon
    const eyeSlashIcon = screen.getByTestId("eye-slash-icon");

    // Simulate onMouseOver event on the eye-slash icon
    fireEvent.mouseOver(eyeSlashIcon);

    // Check if the type changes to "text"
    expect(passwordInput).toHaveAttribute("type", "text");
  });
});
