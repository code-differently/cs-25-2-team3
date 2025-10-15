"""
models/reaction.py
Defines the Reaction data model for message interactions.
"""

class Reaction:
    """Represents a user reaction to a message."""
    
    def __init__(self, id: int, message_id: int, user_id: int, reaction_type: str, timestamp: str):
        """Initialize a new reaction instance.
        
        Args:
            id: Unique identifier for the reaction
            message_id: ID of the message being reacted to
            user_id: ID of the user making the reaction
            reaction_type: Type of reaction (like, dislike, etc.)
            timestamp: When the reaction was created
        """
        pass

    def to_dict(self) -> dict:
        """Return a dictionary representation of the reaction.
        
        Returns:
            Dictionary containing reaction data
        """
        pass

    def is_valid_type(self) -> bool:
        """Validate if the reaction type is allowed.
        
        Returns:
            True if reaction type is valid, False otherwise
        """
        pass
