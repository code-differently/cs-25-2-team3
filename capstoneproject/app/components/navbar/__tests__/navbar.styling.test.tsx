import "@testing-library/jest-dom";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import { NavBar } from "../navbar";

const renderWithRouter = (component: React.ReactElement) => {
  return render(<MemoryRouter>{component}</MemoryRouter>);
};

describe("NavBar Styling", () => {
  it("renders Sign Up link with correct styling classes", () => {
    renderWithRouter(<NavBar />);
    const signUpLink = screen.getByRole("link", { name: /sign up/i });
    expect(signUpLink).toBeInTheDocument();
    expect(signUpLink).toHaveClass("px-5", "py-2", "rounded-full", "bg-blue-600", "text-white", "font-semibold", "hover:bg-blue-700", "transition-colors", "shadow");
  });

  it("navbar has proper container structure", () => {
    renderWithRouter(<NavBar />);
    const nav = screen.getByRole("navigation");
    expect(nav).toBeInTheDocument();
    // Add specific structure assertions based on your actual implementation
  });

  it("brand link has proper styling", () => {
    renderWithRouter(<NavBar />);
    const brandLink = screen.getByRole("link", { name: /forum hub/i });
    expect(brandLink).toBeInTheDocument();
    // Add specific class assertions based on your actual styling
  });
});