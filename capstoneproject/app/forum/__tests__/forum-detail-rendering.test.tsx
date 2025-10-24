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

describe("ForumDetail Rendering", () => {
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

  it("renders without crashing", () => {
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

    expect(() => renderWithRouter(<ForumDetailPage />)).not.toThrow();
  });

  it("renders main page structure when forum is loaded", () => {
    const mockForum = {
      id: "1",
      title: "Test Forum Title",
      description: "Test Description",
      question: "Test Question",
      creatorName: "Test User",
      createdAt: { toDate: () => new Date("2023-01-01") },
      upvotes: 5,
      downvotes: 1,
      commentCount: 3,
    };

    mockUseForum.mockReturnValue({
      forum: mockForum,
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    expect(screen.getAllByRole("navigation")).toHaveLength(2); // Main nav + breadcrumb nav
    expect(screen.getByRole("main")).toBeInTheDocument();
    expect(screen.getByText("Test Forum Title")).toBeInTheDocument();
  });

  it("displays forum title, description, and question correctly", () => {
    const mockForum = {
      id: "1",
      title: "React Best Practices",
      description: "Discussion about React development best practices",
      question: "What are the most important React patterns to follow?",
      creatorName: "Developer",
      createdAt: { toDate: () => new Date("2023-01-01") },
      upvotes: 10,
      downvotes: 2,
      commentCount: 5,
    };

    mockUseForum.mockReturnValue({
      forum: mockForum,
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    expect(screen.getByText("React Best Practices")).toBeInTheDocument();
    expect(screen.getByText("Discussion about React development best practices")).toBeInTheDocument();
    expect(screen.getByText("What are the most important React patterns to follow?")).toBeInTheDocument();
  });

  it("displays creation date and creator information", () => {
    const testDate = new Date("2023-06-15");
    const mockForum = {
      id: "1",
      title: "Test Forum",
      description: "Test Description",
      question: "Test Question",
      creatorName: "Jane Smith",
      createdAt: { toDate: () => testDate },
      upvotes: 8,
      downvotes: 1,
      commentCount: 4,
    };

    mockUseForum.mockReturnValue({
      forum: mockForum,
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    expect(screen.getByText(/Created by Jane Smith/)).toBeInTheDocument();
    expect(screen.getByText(new RegExp(testDate.toLocaleDateString()))).toBeInTheDocument();
  });

  it("displays vote counts and comment count", () => {
    const mockForum = {
      id: "1",
      title: "Vote Test Forum",
      description: "Testing vote display",
      question: "How do votes work?",
      creatorName: "Vote Tester",
      createdAt: { toDate: () => new Date("2023-01-01") },
      upvotes: 25,
      downvotes: 3,
      commentCount: 12,
    };

    mockUseForum.mockReturnValue({
      forum: mockForum,
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    expect(screen.getByText("↑ 25")).toBeInTheDocument();
    expect(screen.getByText("↓ 3")).toBeInTheDocument();
    expect(screen.getByText("12 responses")).toBeInTheDocument();
  });
});
