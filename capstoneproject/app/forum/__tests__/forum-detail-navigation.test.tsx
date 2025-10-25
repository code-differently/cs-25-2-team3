import "@testing-library/jest-dom";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import ForumDetailPage from "../forum-detail";

// Mock the hooks
const mockUseForum = jest.fn();
const mockUseComments = jest.fn();
const mockUseFirestore = jest.fn();

jest.mock("../../hooks/useFirestore", () => ({
  useForum: () => mockUseForum(),
  useComments: () => mockUseComments(),
  useFirestore: () => mockUseFirestore(),
}));

// Mock useParams
const mockUseParams = jest.fn();
jest.mock("react-router", () => ({
  ...jest.requireActual("react-router"),
  useParams: () => mockUseParams(),
}));

const renderWithRouter = (component: React.ReactElement) => {
  return render(<MemoryRouter>{component}</MemoryRouter>);
};

describe("ForumDetail Navigation", () => {
  beforeEach(() => {
    jest.clearAllMocks();
    mockUseParams.mockReturnValue({ forumId: "test-forum-id" });
    mockUseComments.mockReturnValue({
      comments: [],
      loading: false,
      error: null,
    });
    mockUseFirestore.mockReturnValue({
      createComment: jest.fn(),
    });
  });

  it("displays breadcrumb navigation back to forums", () => {
    const mockForum = {
      id: "1",
      title: "Test Forum",
      description: "Test Description",
      question: "Test Question",
      creatorName: "Test User",
      createdAt: { toDate: () => new Date("2023-01-01") },
      upvotes: 0,
      downvotes: 0,
      commentCount: 0,
    };

    mockUseForum.mockReturnValue({
      forum: mockForum,
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    const backToForumsLink = screen.getByRole("link", { name: /back to forums/i });
    expect(backToForumsLink).toBeInTheDocument();
    expect(backToForumsLink).toHaveAttribute("href", "/forums");
  });

  it("error state provides navigation back to forums", () => {
    mockUseForum.mockReturnValue({
      forum: null,
      loading: false,
      error: "Forum not found",
    });

    renderWithRouter(<ForumDetailPage />);

    const backToForumsButton = screen.getByRole("link", { name: /back to forums/i });
    expect(backToForumsButton).toBeInTheDocument();
    expect(backToForumsButton).toHaveAttribute("href", "/forums");
  });

  it("error state with failed to load provides retry button", () => {
    mockUseForum.mockReturnValue({
      forum: null,
      loading: false,
      error: "Failed to load forum",
    });

    renderWithRouter(<ForumDetailPage />);

    const retryButton = screen.getByRole("button", { name: /retry/i });
    expect(retryButton).toBeInTheDocument();
    
    const backToForumsLink = screen.getByRole("link", { name: /back to forums/i });
    expect(backToForumsLink).toBeInTheDocument();
  });

    it("renders navbar component for consistent navigation", () => {
    mockUseForum.mockReturnValue({
      forum: {
        id: "1",
        title: "Test Forum",
        description: "Test Description",
        question: "Test Question",
        creatorName: "Test User",
        createdAt: { toDate: () => new Date("2023-01-01") },
        upvotes: 0,
        downvotes: 0,
        commentCount: 0,
      },
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    expect(screen.getAllByRole("navigation")).toHaveLength(2); // Main nav + breadcrumb nav
  });

  it("page has proper semantic structure for navigation", () => {
    mockUseForum.mockReturnValue({
      forum: {
        id: "1",
        title: "Test Forum",
        description: "Test Description",
        question: "Test Question",
        creatorName: "Test User",
        createdAt: { toDate: () => new Date("2023-01-01") },
        upvotes: 0,
        downvotes: 0,
        commentCount: 0,
      },
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    expect(screen.getAllByRole("navigation")).toHaveLength(2); // Main nav + breadcrumb nav
    expect(screen.getByRole("main")).toBeInTheDocument();
    expect(screen.getByText("← Back to Forums")).toBeInTheDocument();
  });
});
