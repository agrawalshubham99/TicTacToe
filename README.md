# Wear OS Tic Tac Toe Game

## Overview
Wear OS Tic Tac Toe is an interactive game app designed for Wear OS devices. Developed in Java with XML-based layouts, the app follows modern Material Design principles and supports both single-player (versus a smart CPU using the minimax algorithm) and two-player modes. The design emphasizes dynamic theming, interactive animations, and personalized avatar support.

## Features
- **Game Modes:**
  - **Single Player:** Play against a CPU that utilizes the minimax algorithm for optimal move selection.
  - **Two Player:** Compete with a friend with separate name inputs and symbol selections.
- **First-Move Selection:** Choose who makes the first move (Player1, Player2, or CPU).
- **Smart CPU (Minimax Algorithm):**  
  The CPU calculates the best move by evaluating all possible game outcomes. The algorithm assigns scores (+10 for a win, -10 for a loss, 0 for a draw) and factors in move depth to prefer quicker wins.
- **Dynamic Theming:** Supports DayNight mode to automatically adapt to system theme changes.
- **Interactive UI:**  
  - Modern Material design with CardView for the game board.
  - Subtle animations (e.g., scale "pop" on cell press and fade transitions).
- **Avatar Support:** Displays circular avatars generated from players’ initials.
- **Responsive Layout:** The game board is centrally positioned with safe margins to suit small, round screens.
