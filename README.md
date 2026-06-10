# 🎬 VINTY

VINTY is an application focused on the preservation and exhibition of retro and classic anime that have been lost in today's market. More than just a streaming player, this project was born as a personal laboratory to apply and validate advanced software engineering concepts in the Android ecosystem.

> **⚠️ Project Status: Paused (Hobby Project)**
> This was a hobby project, and active development is currently paused so I can focus on my main projects and startups. Because of this, it **does not reflect 100% of my current technical refinement in production**. There are UI/UX details to be optimized and algorithms awaiting full integration. However, the project features an **extremely robust and modular architectural base**, making it highly worthwhile to be "disassembled" and studied by anyone interested in scalable architectures.

## Main Features

Despite being a project in the refinement stage, VINTY already has complex logic implemented:

* **Custom Player with Save State:** Intelligent system that saves progress and allows the user to continue the video exactly where they left off (Cross-session).
* **Content Management:** Comprehensive listing organized by seasons and episodes.
* **My List (Watchlist):** Management of the user's favorite anime.
* **Local Recommendation Engine:** A recommendation algorithm structured within the domain layer, fully functional (currently awaiting the "hook" in the click-capture engine to feed the user database).

## Architecture and Engineering

The true heart of VINTY isn't just on the screen, but under the hood. The application was designed following a **Finalized Modular Base** approach, focused on extremely high decoupling and scalability.

* **Hexagonal Architecture (Ports and Adapters):** The core of the application (`domain`) is entirely isolated from external frameworks. The database and UI are mere implementation details.
* **Multi-Module Strategy:** The project is fragmented into dozens of modules with strict responsibilities:
    * `app` and `core`
    * `data` (analytics, auth, anime, progress)
    * `domain` (models, use cases, algorithms)
    * `feature` (auth, discovery, player, profile, watchlist)
    * `infra`
* **Design Patterns:** MVVM (Model-View-ViewModel) in the presentation layer, Dependency Injection, and strict adherence to the **SRP (Single Responsibility Principle)**.

## Screenshots
<img width="1024" height="500" alt="VINTY" src="https://github.com/user-attachments/assets/745e7bc8-6736-45d0-b902-17c3908e953f" />
<img width="1024" height="500" alt="VINTY2" src="https://github.com/user-attachments/assets/f9f03116-57e7-40f5-8277-8f4da2bb4538" />


## Next Steps (To-Do)
Should the project be resumed in the future, the action points will be:

- [ ] Connect the local algorithm to the click-capture engine.
- [ ] Design/UI refinements on secondary screens.
- [ ] Call performance optimization and list loading improvements.
- [ ] Finalize unit test coverage in the `feature` modules.

---
**Developed by Avenor Neto**
