import "@testing-library/jest-dom";
import { render, screen } from "@testing-library/react";
import { ForumsPage } from "./forumspage";

describe("ForumsPage", () => {
  it("renders forum list", () => {
    render(<ForumsPage />);
    expect(screen.getByText(/How to learn React/i)).toBeInTheDocument();
    expect(screen.getByText(/Best AI tools for students/i)).toBeInTheDocument();
  });
});
