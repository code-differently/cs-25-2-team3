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

describe("ForumDetail States", () => {
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

  describe("Loading State", () => {
    it("displays loading spinner and text when loading forum", () => {
      mockUseForum.mockReturnValue({
        forum: null,
        loading: true,
        error: null,
      });

      renderWithRouter(<ForumDetailPage />);

      expect(screen.getByText("Loading forum...")).toBeInTheDocument();
      const spinner = document.querySelector('.animate-spin');
      expect(spinner).toBeInTheDocument();
    });

    it("shows only loading content when in loading state", () => {
      mockUseForum.mockReturnValue({
        forum: null,
        loading: true,
        error: null,
      });

      renderWithRouter(<ForumDetailPage />);

      expect(screen.getByText("Loading forum...")).toBeInTheDocument();
      expect(screen.queryByText("Back to Forums")).not.toBeInTheDocument();
    });
  });

  describe("Error States", () => {
    it("handles forum not found error", () => {
      mockUseForum.mockReturnValue({
        forum: null,
        loading: false,
        error: "Forum not found",
      });

      renderWithRouter(<ForumDetailPage />);

      expect(screen.getByText("Forum not found")).toBeInTheDocument();
      expect(screen.getByText("The forum you're looking for doesn't exist or has been deleted.")).toBeInTheDocument();
      expect(screen.getByRole("link", { name: /back to forums/i })).toBeInTheDocument();
    });

    it("handles failed to load forum error with retry option", () => {
      mockUseForum.mockReturnValue({
        forum: null,
        loading: false,
        error: "Failed to load forum",
      });

      renderWithRouter(<ForumDetailPage />);

      expect(screen.getByText("Failed to load forum")).toBeInTheDocument();
      expect(screen.getByText("There was an error loading the forum. Please try again.")).toBeInTheDocument();
      expect(screen.getByRole("button", { name: /retry/i })).toBeInTheDocument();
      expect(screen.getByRole("link", { name: /back to forums/i })).toBeInTheDocument();
    });

    it("handles null forum without error message", () => {
      mockUseForum.mockReturnValue({
        forum: null,
        loading: false,
        error: null,
      });

      renderWithRouter(<ForumDetailPage />);

      expect(screen.getByText("Forum Not Found")).toBeInTheDocument();
      expect(screen.getByRole("link", { name: /back to forums/i })).toBeInTheDocument();
    });
  });

  describe("Forum Status", () => {
    it("shows Open status when no endTime is provided", () => {
      const mockForum = {
        id: "1",
        title: "Open Forum",
        description: "This forum has no end time",
        question: "Is this forum open?",
        creatorName: "User",
        createdAt: { toDate: () => new Date() },
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

      expect(screen.getByText("Open")).toBeInTheDocument();
    });

    it("shows Closed status when endTime is in the past", () => {
      const pastDate = new Date();
      pastDate.setDate(pastDate.getDate() - 1); // Yesterday

      const mockForum = {
        id: "1",
        title: "Closed Forum",
        description: "This forum is closed",
        question: "Is this forum closed?",
        creatorName: "User",
        createdAt: { toDate: () => new Date("2023-01-01") },
        endTime: { toDate: () => pastDate },
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

      expect(screen.getByText("Closed")).toBeInTheDocument();
    });

    it("shows Open status when endTime is in the future", () => {
      const futureDate = new Date();
      futureDate.setDate(futureDate.getDate() + 1); // Tomorrow

      const mockForum = {
        id: "1",
        title: "Future Forum",
        description: "This forum is still open",
        question: "Will this forum close later?",
        creatorName: "User",
        createdAt: { toDate: () => new Date() },
        endTime: { toDate: () => futureDate },
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

      expect(screen.getByText("Open")).toBeInTheDocument();
    });
  });
});
