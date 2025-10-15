// Main component exports
export { default } from "./message";

// Model exports
export { Message } from './models/Message';
export { User } from './models/User';
export { Reaction, ReactionType } from './models/Reaction';
export type { MessageData, ReactionData } from './models/Message';
export type { UserData } from './models/User';

// Service exports
export { MessageService } from './services/MessageService';
export { ReactionService } from './services/ReactionService';
export { ModerationService } from './services/ModerationService';
export type { CreateMessageRequest, MessageFilters } from './services/MessageService';
export type { AddReactionRequest } from './services/ReactionService';
export type { ModerationResult } from './services/ModerationService';

// Component exports
export { MessageList } from './components/MessageList';
export { MessageItem } from './components/MessageItem';
export { MessageComposer } from './components/MessageComposer';

// Utility exports
export { Logger, LogLevel } from './utils/logger';
export { ApiClient } from './utils/apiClient';
export type { LogEntry } from './utils/logger';
export type { ApiConfig, ApiResponse } from './utils/apiClient';
