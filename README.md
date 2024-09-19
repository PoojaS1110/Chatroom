# Chatroom

# ChatRoom Application

## Overview
This project is a simple console-based chat application that allows users to create or join chat rooms, send and receive messages in real-time, and manage user activities. The application is designed to follow best practices in coding, including various software design patterns.

## Functional Requirements
1. Allow users to create/join chat rooms by entering a unique room ID.
2. Enable users to send and receive messages in real-time within a chat room.

## Design Patterns Followed
### Behavioral Patterns
1. **Observer Pattern**: To notify users about new messages.
2. **Command Pattern**: To encapsulate message-sending operations.

### Creational Patterns
1. **Singleton Pattern**: To manage the `ChatRoomManager` instance.
2. **Prototype Pattern**: For cloning message objects.

### Structural Patterns
1. **Adapter Pattern**: To support different message communication protocols.
2. **Decorator Pattern**: To add functionality (like logging) to message commands.


## Implementation 
- **Code Organization**: Each class adheres to naming conventions and global best practices.
- **Long-running Program**: The application is designed to run indefinitely while gathering inputs from users.
- **No Hardcoding of Flags**: The application uses structured commands without hardcoding boolean flags.
- **Logging and Exception Handling**: Implemented robust logging and exception handling mechanisms to ensure smooth operation.
- **Defensive Programming**: Validations are performed at all levels to handle user inputs effectively.
- **Performance Optimization**: The code is optimized for performance while maintaining clarity and readability.
