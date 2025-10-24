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

describe("ForumDetail Missing Fields", () => {
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

  it("handles forum with missing optional fields gracefully", () => {
    const forumWithMissingFields = {
      id: "1",
      title: "Forum with Missing Fields",
      description: "This forum has some missing optional fields",
      question: "What about missing fields?",
      creatorName: "Test User",
      createdAt: { toDate: () => new Date("2023-01-01") },
      // Missing upvotes, downvotes, commentCount, tags, endTime
    };

    mockUseForum.mockReturnValue({
      forum: forumWithMissingFields,
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    expect(screen.getByText("Forum with Missing Fields")).toBeInTheDocument();
    expect(screen.getByText("This forum has some missing optional fields")).toBeInTheDocument();
    expect(screen.getByText("What about missing fields?")).toBeInTheDocument();
    
    // Should display default values for missing fields
    expect(screen.getByText("0 responses")).toBeInTheDocument();
    expect(screen.getByText("↑ 0")).toBeInTheDocument();
    expect(screen.getByText("↓ 0")).toBeInTheDocument();
    
    // Should show Open status when no endTime
    expect(screen.getByText("Open")).toBeInTheDocument();
  });

  it("handles forum with missing createdAt gracefully", () => {
    const forumWithoutCreatedAt = {
      id: "1",
      title: "No Creation Date",
      description: "This forum has no creation date",
      question: "When was this created?",
      creatorName: "Test User",
      // Missing createdAt
      upvotes: 5,
      downvotes: 1,
      commentCount: 2,
    };

    mockUseForum.mockReturnValue({
      forum: forumWithoutCreatedAt,
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    expect(screen.getByText("No Creation Date")).toBeInTheDocument();
    expect(screen.getByText(/Unknown date/)).toBeInTheDocument();
  });

  it("displays tags when available", () => {
    const forumWithTags = {
      id: "1",
      title: "Forum with Tags",
      description: "This forum has tags",
      question: "What tags are shown?",
      creatorName: "Test User",
      createdAt: { toDate: () => new Date("2023-01-01") },
      tags: ["javascript", "react", "programming"],
      upvotes: 0,
      downvotes: 0,
      commentCount: 0,
    };

    mockUseForum.mockReturnValue({
      forum: forumWithTags,
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    expect(screen.getByText("Forum with Tags")).toBeInTheDocument();
    expect(screen.getByText("javascript")).toBeInTheDocument();
    expect(screen.getByText("react")).toBeInTheDocument();
    expect(screen.getByText("programming")).toBeInTheDocument();
  });

  it("handles forum with empty tags array", () => {
    const forumWithEmptyTags = {
      id: "1",
      title: "Forum with Empty Tags",
      description: "This forum has empty tags array",
      question: "Are there any tags?",
      creatorName: "Test User",
      createdAt: { toDate: () => new Date("2023-01-01") },
      tags: [],
      upvotes: 0,
      downvotes: 0,
      commentCount: 0,
    };

    mockUseForum.mockReturnValue({
      forum: forumWithEmptyTags,
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    expect(screen.getByText("Forum with Empty Tags")).toBeInTheDocument();
    // Should not crash and should not display any tag badges
    expect(screen.queryByText("javascript")).not.toBeInTheDocument();
  });

  it("displays end time information when present", () => {
    const futureDate = new Date();
    futureDate.setDate(futureDate.getDate() + 5);

    const forumWithEndTime = {
      id: "1",
      title: "Forum with End Time",
      description: "This forum has an end time",
      question: "When does this close?",
      creatorName: "Test User",
      createdAt: { toDate: () => new Date() },
      endTime: { toDate: () => futureDate },
      upvotes: 0,
      downvotes: 0,
      commentCount: 0,
    };

    mockUseForum.mockReturnValue({
      forum: forumWithEndTime,
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    expect(screen.getByText("Forum with End Time")).toBeInTheDocument();
    expect(screen.getByText(/Forum closes on/)).toBeInTheDocument();
  });

  it("handles expired forum correctly", () => {
    const pastDate = new Date();
    pastDate.setDate(pastDate.getDate() - 2); // Two days ago

    const expiredForum = {
      id: "1",
      title: "Expired Forum",
      description: "This forum has expired",
      question: "Is this forum closed?",
      creatorName: "Test User",
      createdAt: { toDate: () => new Date("2023-01-01") },
      endTime: { toDate: () => pastDate },
      upvotes: 10,
      downvotes: 2,
      commentCount: 5,
    };

    mockUseForum.mockReturnValue({
      forum: expiredForum,
      loading: false,
      error: null,
    });

    renderWithRouter(<ForumDetailPage />);

    expect(screen.getByText("Expired Forum")).toBeInTheDocument();
    expect(screen.getByText("Closed")).toBeInTheDocument();
    expect(screen.getByText(/Forum closed on/)).toBeInTheDocument();
  });
});
