
# d_drive
**Distributed Drive System – RMI + RabbitMQ**

## Project Status

| Requirement     | Status | Description                                                                 |
|-----------------|------|-----------------------------------------------------------------------------|
| R1              | DONE | User registration and login via RMI                                          |
| R2              | DONE | Dynamic per-user folder structure (`local/`, `partilhas/`)                   |
| R3 (RMI)        | DONE | Synchronous folder sharing via RMI + remote callbacks                        |
| R3 (RabbitMQ)   | DONE | Asynchronous folder sharing via RabbitMQ messaging                           |
| R4              | DONE | Folder synchronization (copy from shared)                                    |
| R5              | DONE | Persistent propagation: JSON tracking of shared folders                      |

---

## Generated Folder Structure

```bash
 users/
├── patrick/
│   ├── local/
│   └── partilhas/
│       ├── symbolic_links/
│       │   └── patrick_symbolic_link_testeSync/
│       └── real_copy_ptrck_pastaR5/
├── ana/
│   ├── local/
│   └── partilhas/
```
```bash
 data/
├── partilhas.json   # Persistent tracking of real shared folders (R5)
```

---

## Architecture

### `common/` — Interfaces & Utilities

| Interface         | Description                                                              |
|------------------|---------------------------------------------------------------------------|
| `LoginServiceRI`  | Remote interface for login/registration/session                          |
| `UserSessionRI`   | Represents a session: grants access to `FileManagerRI` and `PeerClientRI`|
| `FileManagerRI`   | Folder operations: create, delete, rename, share, sync                   |
| `PeerClientRI`    | Callback interface for RMI notifications                                 |

| Class             | Purpose                                                                  |
|------------------|---------------------------------------------------------------------------|
| `RabbitManager`   | Publishes/subscribes messages via RabbitMQ for async communication       |
| `PartilhaManager` | Uses JSON to persistently track folder sharing states (R5)               |

---

### `server/` — Service Logic

| Class               | Purpose                                                             |
|---------------------|---------------------------------------------------------------------|
| `LoginServiceImpl`  | Handles user registration/login and session creation               |
| `UserSessionImpl`   | Represents the authenticated session for a user                    |
| `FileManagerImpl`   | Implements all file/directory operations and sharing logic          |
| `UserDatabase`      | In-memory credential store                                          |
| `SessionManager`    | Keeps track of online users and their sessions                     |
| `LoginServer`       | Initializes and binds the RMI service on the specified registry     |

---

### `client/` — User Interaction

| Class              | Role                                                                  |
|--------------------|-----------------------------------------------------------------------|
| `LoginClient`      | Connects to RMI service, handles folder operations, shares folders    |
| `PeerClientImpl`   | Implements the callback to receive real-time notifications via RMI    |

---

## Shell Scripts for Execution

| Script             | Description                                                        |
|--------------------|--------------------------------------------------------------------|
| `runregistry.sh`   | Launches `rmiregistry` on default or specified port (e.g., 1099)   |
| `runserver.sh`     | Starts the RMI server and binds the service                        |
| `runclient.sh`     | Executes the client; prompts for user/pass and passes arguments    |
| `setenv.sh`        | Defines environment variables: hostnames, ports, classpath, etc.   |

---

## Example of Flow (R1 → R5)

1. `patrick` registers and logs in
2. Creates a folder: `/local/pastaR5`
3. Shares folder with `ana`
    - If **symbolic**: only a placeholder is created under `partilhas/symbolic_links/`
    - If **real**: full contents copied to `partilhas/real_copy_*`
    - If `ana` is **online**, RMI callback notifies her
    - Otherwise, RabbitMQ message is sent
4. Renaming/deleting the original folder updates all **real copies** (R5)
5. Shared folders are persistently tracked in `partilhas.json`

---

## Technologies

- **Java RMI** for synchronous, callback-based sharing and session handling
- **RabbitMQ** for async messaging and notifications (via Publish/Subscribe)
- **org.json** (json-20190722) for lightweight persistent tracking (R5)

---

## Final Considerations

- Project meets **all required specifications** (R1 → R5)
- Users can share folders both **transiently** (symbolic) and **persistently** (real copy)
- System supports both **online updates (RMI)** and **offline notifications (RabbitMQ)**
- JSON file ensures folder propagation is **trackable and consistent**

---

*This README was made by **pnovato***