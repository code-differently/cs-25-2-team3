import "@testing-library/jest-dom";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import { NavBar } from "../navbar";

const renderWithRouter = (component: React.ReactElement) => {
  return render(<MemoryRouter>{component}</MemoryRouter>);
};

describe("NavBar Navigation Links", () => {
  it("renders Create Forum link with correct href", () => {
    renderWithRouter(<NavBar />);
    const createForumLink = screen.getByRole("link", { name: /create forum/i });
    expect(createForumLink).toBeInTheDocument();
    expect(createForumLink).toHaveAttribute("href", "/create-forum");
  });

  it("renders Forums link with correct href", () => {
    renderWithRouter(<NavBar />);
    const forumsLink = screen.getByRole("link", { name: /forums/i });
    expect(forumsLink).toBeInTheDocument();
    expect(forumsLink).toHaveAttribute("href", "/forums");
  });

  it("brand link navigates to home page", () => {
    renderWithRouter(<NavBar />);
    const brandLink = screen.getByRole("link", { name: /forum hub/i });
    expect(brandLink).toHaveAttribute("href", "/");
  });
});