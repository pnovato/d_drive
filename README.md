# d_drive
Distributed Drive System

## Current Project Status

| Requirement | Status | Description                                                   |
|-----------|--------|---------------------------------------------------------------|
| R1 | DONE   | User registration and login via RMI                           |
| R2 | DONE   | Per-user directory structure (`local/` and `shared/`) |
| R3 (RMI) | DONE   | Synchronous update propagation using RMI callbacks  |

---

## Dynamically generated folder structure

    ├── users/
              ├── patrick/  ├── local/
                            │
                            └── partilhas/ 

              ├── ana/      ├── local/
                            │
                            └── partilhas/ │
                                           └── patrick_testeSync/


---

## Project Structure

### `common/` — Remote Interfaces

| Interface         | Purpose |
|------------------|---------|
| `LoginServiceRI` | Registers/logs in users and returns session objects |
| `FileManagerRI`  | Folder operations: create, list, delete, rename, share |
| `UserSessionRI`  | Represents an active user session: provides access to `FileManagerRI` and `PeerClientRI` |
| `PeerClientRI`   | Remote callback interface for user notifications via RMI |

---

### `server/` — Server-side Implementations

| Class                | Purpose |
|----------------------|---------|
| `LoginServiceImpl`   | Handles registration, login, and session creation |
| `UserSessionImpl`    | Represents the session state: includes file manager and client callback |
| `FileManagerImpl`    | Manages folder operations and RMI-based sharing logic |
| `User`               | Simple user model with username/password |
| `UserDatabase`       | In-memory database for user authentication |
| `LoginServer`        | Initializes and binds the login service to the RMI registry |
| `SessionManager`     | Static access point for managing active sessions |

---

### `client/` — Client-side Code

| Class             | Purpose |
|-------------------|---------|
| `LoginClient`     | Connects to the server, logs in, performs folder operations, and triggers sharing |
| `PeerClientImpl`  | Implements the RMI callback to receive notifications (e.g., when a folder is shared) |

---

## Shell Scripts for Execution `.sh`

| Script              | Description |
|---------------------|-------------|
| `runregistry.sh`    | Starts the `rmiregistry` on port 15679 |
| `runserver.sh`      | Launches the RMI server (`LoginServer`) |
| `runclient.sh`      | Executes the client (`LoginClient`) |

---

## Example Flow

1. User `patrick` logs in and creates folder `testeSync`
2. Shares it with `ana` via `shareFolder("patrick", "ana", "testeSync")`
3. This structure is created: `users/ana/shared/patrick_testeSync/`
4. If `ana` is online, she receives:


[Notification for ana] Change in 'testeSync': You received a new shared folder from patrick!

---

## Technical Notes
- All remote communication is implemented using **Java RMI**
- Session and callback references are stored in memory during execution
- Directory structure is created dynamically on the file system
- Implementation is cleanly separated into client/server/common layers

---

## Upcoming Tasks

- [ ] Implement **asynchronous update propagation** using **RabbitMQ** (publish/subscribe)
- [ ] Synchronize actual folder contents between shared users
- [ ] Add file upload/download functionality
---
