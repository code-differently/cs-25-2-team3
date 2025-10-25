import "@testing-library/jest-dom";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import { NavBar } from "../navbar";

const renderWithRouter = (component: React.ReactElement) => {
  return render(<MemoryRouter>{component}</MemoryRouter>);
};

describe("NavBar Rendering", () => {
  it("renders without crashing", () => {
    expect(() => renderWithRouter(<NavBar />)).not.toThrow();
  });

  it("renders the Forum Hub brand link correctly", () => {
    renderWithRouter(<NavBar />);
    const brandLink = screen.getByRole("link", { name: /forum hub/i });
    expect(brandLink).toBeInTheDocument();
    expect(brandLink).toHaveAttribute("href", "/");
  });

  it("renders all navigation elements", () => {
    renderWithRouter(<NavBar />);
    expect(screen.getByRole("navigation")).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /forum hub/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /create forum/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /forums/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /sign up/i })).toBeInTheDocument();
  });
});