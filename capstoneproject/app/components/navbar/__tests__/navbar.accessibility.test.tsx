import "@testing-library/jest-dom";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import { NavBar } from "../navbar";

const renderWithRouter = (component: React.ReactElement) => {
  return render(<MemoryRouter>{component}</MemoryRouter>);
};

describe("NavBar Accessibility", () => {
  it("has proper navigation landmark", () => {
    renderWithRouter(<NavBar />);
    const nav = screen.getByRole("navigation");
    expect(nav).toBeInTheDocument();
  });

  it("all links have accessible names", () => {
    renderWithRouter(<NavBar />);
    expect(screen.getByRole("link", { name: /forum hub/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /create forum/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /forums/i })).toBeInTheDocument();
  });

  it("sign up link has accessible name", () => {
    renderWithRouter(<NavBar />);
    const signUpLink = screen.getByRole("link", { name: /sign up/i });
    expect(signUpLink).toBeInTheDocument();
  });

  it("links have proper href attributes for screen readers", () => {
    renderWithRouter(<NavBar />);
    const links = screen.getAllByRole("link");
    links.forEach(link => {
      expect(link).toHaveAttribute("href");
    });
  });
});