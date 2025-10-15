import "@testing-library/jest-dom";
import { render, screen } from "@testing-library/react";
import { NavBar } from "./navbar";

describe("NavBar", () => {
  it("renders navbar and sign in button", () => {
    render(<NavBar />);
    expect(screen.getByText(/TITLE_GOES_HERE/i)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /sign in/i })).toBeInTheDocument();
  });
});
