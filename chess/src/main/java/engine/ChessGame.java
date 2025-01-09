package engine;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import chess.ChessController;
import chess.ChessView;
import chess.PlayerColor;
import engine.pieces.Bishop;
import engine.pieces.King;
import engine.pieces.Knight;
import engine.pieces.Queen;
import engine.pieces.Rook;
import engine.utils.ChessBoardInitializer;
import engine.utils.Position;

/**
 * Defines the class ChessGame that implements the interface ChessController
 * @author Bénédicte Vernet & Benoît Jaouen & Théo Pilet
 */
public class ChessGame implements ChessController {

	private ChessView view;

	private final int WIDTH = 8;
	private final int HEIGHT = 8;
	private Piece[][] board = new Piece[WIDTH][HEIGHT];

	private final LinkedList<Move> history = new LinkedList<>();

	private Piece whiteKing;
	private Piece blackKing;
	private PlayerColor currentPlayerColor = PlayerColor.WHITE;

	/**
     * Starts the game by initializing the view and setting up the board.
     * @param view the view interface used to interact with the player
     */
	@Override
	public void start(ChessView view) {
		this.view = view;
		view.startView();
	}

	/**
     * Attempts to move a piece from one position to another on the chessboard.
     * @param fromX the x-coordinate of the starting position
     * @param fromY the y-coordinate of the starting position
     * @param toX the x-coordinate of the destination position
     * @param toY the y-coordinate of the destination position
     * @return true if the move is valid and successfully executed, false otherwise
     */
	@Override
	public boolean move(int fromX, int fromY, int toX, int toY) {

		Piece p = board[fromX][fromY];
		if (p == null) return false; // no piece to move at the start position
		if (p.getColor() != currentPlayerColor) return false;

		Position to = new Position(toX, toY);
		Move move = getMoveIfAllowed(p, to);
		if (move == null) return false;

		if (!tryMove(move)) {
			System.out.println("Invalid move " + move);
			return false;}
		
		uppdateBoardView(move);
		if (move.pieceMoved.canBePromotedAt(move.to)) promote(move.to);

		nextTurn();

		return true;
	}

	/**
     * Validates and applies a move, checking if the resulting game state is valid.
     * @param move the move to attempt
     * @return  true if the move is valid, false otherwise
     */
	private boolean tryMove(Move move) {
		history.push(move);
		applyMove(move);

		if (!isStateValid()) {
			revertLastMove();
			fillBoardView(); // necessary ?
			return false;
		}
		return true;
	}

	/**
     * Promotes a pawn to another piece as chosen by the player.
     * @param p the position of the pawn to promote
     */
	private void promote (Position p) {
			Piece promoted = view.askUser("Promotion", "How do you want to promote your pawn ?",
			new Piece[]{
				new Bishop(currentPlayerColor, this),
				new Knight(currentPlayerColor, this),
				new Rook(currentPlayerColor, this),
				new Queen(currentPlayerColor, this)});
			board[p.x][p.y] = promoted;
			view.putPiece(promoted.getType(), currentPlayerColor, p.x, p.y);
	}

	/**
     * Retrieves a valid move for the specified piece and destination, if allowed.
     * @param p the piece to move
     * @param from the starting position of the piece
     * @param to the destination position of the piece
     * @return a valid Move object if allowed, null otherwise
     */
	private Move getMoveIfAllowed(Piece p, Position to) {
		for (Move m : p.availableMoves()) {
			System.out.println(m);
		}
		return p.availableMoves().stream().filter(
			(Move m) -> m.to.equals(to)).findFirst().orElse(null);
	}

	/**
     * Applies a move to the board.
     * @param m the move to apply
     */
	private void applyMove (Move m) {
		board[m.to.x][m.to.y] = m.pieceMoved;
		board[m.from.x][m.from.y] = null;
		if (m.secondMove != null) applyMove(m.secondMove);
	}

	/**
     * Reverts the last move made on the board.
     */
	private void revertLastMove () {
		Move m = history.pop();
		revertMove(m);
	}

	/**
     * Reverts a specified move on the board.
     * @param m the move to revert
     */
	private void revertMove(Move m) {
		if (m.secondMove != null) revertMove(m.secondMove);
		board[m.from.x][m.from.y] = m.pieceMoved;
		board[m.to.x][m.to.y] = m.pieceEaten;
	}

	/**
     * Checks if the current game state is valid, ensuring the current player's king is not in check.
     * @return true if the state is valid, false otherwise
     */
	private boolean isStateValid() {
		return !isThreatened(currentPlayerKing());
	}

	/**
     * Retrieves the current player's king piece.
     * @return the current player's king
     */
	private Piece currentPlayerKing() {
		return (currentPlayerColor == PlayerColor.WHITE) ? whiteKing : blackKing;
	}

	/**
     * Advances the turn to the next player.
     */
	private void nextTurn() {
		System.out.println("Turn " + history.size() + " (" + currentPlayerColor.name() + " player) : " + history.getLast());
		currentPlayerColor = PlayerColor.values()[(currentPlayerColor.ordinal() + 1) % PlayerColor.values().length];

		/*boolean threatened = isThreatened(currentPlayerKing());
		if (threatened) {
			view.displayMessage("Check !");
		}*/
		boolean hasValidMove = currentPlayerHasValidMove();
		if (!hasValidMove) {
			System.out.println("checkmated");
			view.displayMessage("Checkmate !");
		} else {

		}

		/*if (threatened) {
			if (hasValidMove) {
				view.displayMessage("Check !");
			} else {
				view.displayMessage("Checkmate !");
			}
		} else if (!hasValidMove) {
			view.displayMessage("PAT !");
		}*/

		//TODO:
		/*égalités par :
		- Manque de matériel :
			Roi contre roi
			Roi et fou contre roi
			Roi et cavalier contre roi
			Roi et deux cavaliers contre roi (sauf si l'adversaire a encore des pions)
		- Répétition de la position : Si la même position se répète trois fois avec le même joueur
		au trait et les mêmes possibilités de mouvement, la partie peut être déclarée nulle.
		- Règle des 50 coups : Si 50 coups consécutifs sont joués par chaque joueur sans qu'aucun 
		pion ne soit déplacé et sans qu'aucune pièce ne soit capturée, la partie est déclarée nulle. (je propose d'ignorer celle là :/)

		*/
	}

	/**
	 * Checks if the current player has at least one valid move.
	 * @return true if the current player has at least one valid move, false otherwise.
	 */
	private boolean currentPlayerHasValidMove() { //TODO: cette fonction a l'air de ne pas fonctionner correctement :()
		System.out.println("Before : " + history.size());
		return piecesPositions().stream().anyMatch(p -> (!board[p.x][p.y].getColor().equals(currentPlayerColor)) || board[p.x][p.y].availableMoves().stream().anyMatch((Move m) -> {
			if (tryMove(m)) {
				revertLastMove(); // si le move réussit, on l'efface et on retourne qu'il est réussi (true)
				System.out.println("After : " + history.size());
				return true;
			}
			System.out.println("After : " + history.size());
			return false; // sinon, on retourne false
		}));
	}

	/**
     * Checks if a specified piece is threatened by any opposing piece.
     * @param p the piece to check
     * @return true if the piece is threatened, false otherwise
     */
	public boolean isThreatened(Piece p) {
		return Arrays.stream(board)
			.flatMap(Arrays::stream)
			.filter(piece -> piece != null)
			.filter(piece -> !piece.color.equals(p.getColor()))
			.flatMap(piece -> piece.availableMoves().stream())
			.anyMatch(m -> m.pieceEaten == p);
	}

	/**
	 * @param color of the player that would be threatened
	 * @param p position that would be threatened
	 * @return if the position is threatened by the other player
	 */
	public boolean isThreatened(PlayerColor color, Position p) {
		return Arrays.stream(board)
			.flatMap(Arrays::stream)
			.filter(piece -> piece != null)
			.filter(piece -> !piece.color.equals(color))
			.flatMap(piece -> piece.availableMoves().stream())
			.anyMatch(m -> m.to.equals(p));
	}

	/**
	 * Checks if a specific piece has moved during the game.
	 * @param p the piece to check
	 * @return true if the piece has moved at least once, false otherwise
	 */
	public boolean hasMoved(Piece p) {
		return history.stream().anyMatch(m -> m.pieceMoved == p);
	}

	/**
     * Starts a new game with the default board setup.
     */
	@Override
	public void newGame() {
		newGame(ChessBoardInitializer.standardInitializedBoard(this));
	}

	/**
     * Starts a new game with a specified board setup.
     * @param board the initial board setup
     */
	public void newGame(Piece[][] board) {
		resetBoard();
		this.board = board;
		currentPlayerColor = PlayerColor.WHITE;
		fillBoardView();
	}

	/**
	 * Look at a position on the chess board, and get the Piece if there is one.
	 * @param p the position on the board we inspect
	 * @return null if there is no pieces at this position, the Piece if there is one.
	 */
	public Piece at(Position p) {
		if (!isOnBoard(p)) return null;
		return board[p.x][p.y];
	}

	/**
	 * Look for a piece on the board and returns it's location.
	 * @param p the piece to look for
	 * @return the position of the piece, and null if it isn't on the board
	 */
	public Position where(Piece p) {
		for (int i = 0; i < WIDTH; ++i) {
			for (int j = 0; j < HEIGHT; ++j) {
				if (board[i][j] == p) return new Position(i, j);
			}
		}
		return null;
	}

	/**
	 * Checks if a given position is within the bounds of the chessboard.
	 * @param p the position to check
	 * @return true if the position is on the board, false otherwise
	 */
	public boolean isOnBoard (Position p) {
		return p.x >= 0 
		&& p.x < WIDTH 
		&& p.y >= 0
		&& p.y < HEIGHT;
	}

	/**
     * Resets the chessboard, clearing all pieces.
     */
	private void resetBoard() {
		for (int x=0; x < WIDTH; ++x) {
			for (int y=0; y < HEIGHT; ++y) {
				board[x][y] = null;
				view.removePiece(x, y);
			}
		}
	}

	/**
     * Fills the board view with the current state of the chessboard.
     */
	private void fillBoardView() {
		this.foreach((x, y) -> {
			Piece p = board[x][y];
			if (p != null) view.putPiece(p.getType(), p.getColor(), x, y);});
	}

	/**
     * Updates the board view after a move is made.
     * @param m the move that was made
     */
	private void uppdateBoardView(Move m) {
		view.putPiece(m.pieceMoved.getType(), m.pieceMoved.getColor(), m.to.x, m.to.y);
		view.removePiece(m.from.x, m.from.y);
		if (m.secondMove != null) uppdateBoardView(m.secondMove);
	}
	
	/**
     * Sets the white king piece for this game.
     * @param whiteKing the white king piece
     */
	public void setWhiteKing(King whiteKing) {
		this.whiteKing = whiteKing;
	}

	/**
     * Sets the black king piece for this game.
     * @param blackKing the black king piece
     */
	public void setBlackKing(King blackKing) {
		this.blackKing = blackKing;
	}

	/**
     * @return the width of the chessboard
     */
	public int width() {
		return WIDTH;
	}

	/**
     * @return the height of the chessboard
     */
	public int height() {
		return HEIGHT;
	}

	/**
	 * @return the last move played
	 */
	public Move lastMove () {
		return history.peek();
	}

	/**
	 * Iterate over all pieces on the board
	 * @param action the action to perform on each piece position
	 */
	public void foreach(BiConsumer<Integer, Integer> action) {
		for (int x=0; x < WIDTH; ++x)
			for (int y=0; y < HEIGHT; ++y)
				if (board[x][y] != null)
					action.accept(x, y);
	}

	/**
	 * Retrieves the positions of all pieces currently on the board.
	 * @return a LinkedList of Position objects representing the locations of all pieces on the board
	 */
	public LinkedList<Position> piecesPositions() {
		LinkedList<Position> piecesPositions = new LinkedList<>();
		for (int x=0; x < WIDTH; ++x)
			for (int y=0; y < HEIGHT; ++y)
				if (board[x][y] != null)
					piecesPositions.add(new Position(x, y));
		return piecesPositions;
	}
}