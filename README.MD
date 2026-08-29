# 🌑 Midnight L2 Dark Pool: Core Matching Engine

**Hackathon Track:** Cross-Chain (Shared Privacy Engine)

> 🔗 **Note:** This repository contains the **Core Java Matching Engine**. It works in tandem with the [Web3 ZKP Sidecar Node.js Service]([LINK_DO_SEU_REPO_NODE]) to form the complete Midnight Dark Pool architecture.

Traditional cross-chain swaps and AMMs expose order intent and volume to the public ledger, making institutional liquidity vulnerable to MEV extraction and front-running. This project introduces a **Zero-Knowledge Dark Pool L2**, utilizing the Midnight network as a shared privacy layer.

## 🏗️ Architecture & Engineering Principles

To achieve bank-grade throughput, we decoupled the high-frequency matching engine from the cryptographic settlement layer:

1. **In-Memory Core (Java):** A Zero-GC inspired Matching Engine operating entirely in-memory. It utilizes strict Single-Writer principles and lock-free data structures (`ConcurrentLinkedQueue`, `PriorityQueue`) to match Bid/Ask orders in nanoseconds.
2. **Autonomous Liquidity Agents:** `Runnable` instances operating on a dedicated `ExecutorService` thread pool to simulate cross-chain liquidity injections.
3. **Fire-and-Forget Settlement:** Once a match occurs, the payload is pushed to an outbound concurrent queue. The L2 thread instantly dispatches HTTP REST calls to the Sidecar without waiting for network responses, preventing Thread Starvation.

## 🚀 How to Run the Engine

**Prerequisites:**
You must have the [Web3 ZKP Sidecar]([https://github.com/joaobitencourt/MidnightSideCar]) running locally on port `3000` before starting the engine.

1. Clone this repository and open it in your Java IDE (Java 17+ recommended).
2. Run the `MatchingEngine.main()` method.
3. The Core Order Book will initialize, agents will inject liquidity, and the engine will instantly stream the matched settlement payloads to the Midnight Sidecar.