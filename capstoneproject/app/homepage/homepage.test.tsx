import "@testing-library/jest-dom";
import { render, screen } from "@testing-library/react";
import { HomePage } from "./homepage";

describe("HomePage", () => {
  it("renders hero section and buttons", () => {
    render(<HomePage />);
    expect(screen.getByRole("button", { name: /create forum/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /browse forums/i })).toBeInTheDocument();
  });
});
