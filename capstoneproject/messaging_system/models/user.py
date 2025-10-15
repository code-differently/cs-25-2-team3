"""
models/user.py
Defines the User data model for messaging system participants.
"""

class User:
    """Represents a user in the messaging system."""
    
    def __init__(self, id: int, username: str, email: str, created_at: str):
        """Initialize a new user instance.
        
        Args:
            id: Unique identifier for the user
            username: User's display name
            email: User's email address
            created_at: When the user account was created
        """
        pass

    def to_dict(self) -> dict:
        """Return a dictionary representation of the user.
        
        Returns:
            Dictionary containing user data
        """
        pass

    def is_active(self) -> bool:
        """Check if the user account is active.
        
        Returns:
            True if user is active, False otherwise
        """
        pass
