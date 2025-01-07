package engine;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.BiConsumer;

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

public class ChessGame implements ChessController {

	private ChessView view;

	private final int WIDTH = 8;
	private final int HEIGHT = 8;
	private Piece[][] board = new Piece[WIDTH][HEIGHT];

	private final LinkedList<Move> history = new LinkedList<>();

	private Piece whiteKing;
	private Piece blackKing;
	private PlayerColor currentPlayerColor = PlayerColor.WHITE;

	@Override
	public void start(ChessView view) {
		this.view = view;
		view.startView();
	}

	@Override
	public boolean move(int fromX, int fromY, int toX, int toY) {

		Piece p = board[fromX][fromY];
		if (p == null) return false; // no piece to move at the start position
		if (p.getColor() != currentPlayerColor) return false;

		Position from = new Position(fromX, fromY);
		Position to = new Position(toX, toY);

		Move move = getMoveIfAllowed(p, from, to);
		if (move == null) return false;

		history.push(move);
		applyMove(move);

		if (!isStateValid()) {
			revertLastMove();
			fillBoardView();
			return false;
		}

		uppdateBoardView(move);

		if (move.pieceMoved.canBePromotedAt(move.to)) promote(move.to);

		nextTurn();
		return true;
	}

	public void promote (Position p) {
			Piece promoted = view.askUser("Promotion", "How do you want to promote your pawn ?",
			new Piece[]{
				new Bishop(currentPlayerColor, this),
				new Knight(currentPlayerColor, this),
				new Rook(currentPlayerColor, this),
				new Queen(currentPlayerColor, this)});
			board[p.x][p.y] = promoted;
			view.putPiece(promoted.getType(), currentPlayerColor, p.x, p.y);
	}

	public Move getMoveIfAllowed(Piece p, Position from, Position to) {
		return p.availableMoves().stream().filter(
			(Move m) -> m.to.equals(to)).findFirst().orElse(null);
	}

	private void applyMove (Move m) {
		board[m.to.x][m.to.y] = m.pieceMoved;
		board[m.from.x][m.from.y] = null;
		if (m.secondMove != null) applyMove(m.secondMove);
	}

	private void revertLastMove () {
		Move m = history.pop();
		revertMove(m);
	}

	private void revertMove(Move m) {
		if (m.secondMove != null) revertMove(m.secondMove);
		board[m.from.x][m.from.y] = m.pieceMoved;
		board[m.to.x][m.to.y] = m.pieceEaten;
	}

	private boolean isStateValid() {
		return !isThreatenend(currentPlayerKing());
	}

	private Piece currentPlayerKing() {
		return (currentPlayerColor == PlayerColor.WHITE) ? whiteKing : blackKing;
	}

	private void nextTurn() {
		System.out.println("Turn " + history.size() + " (" + currentPlayerColor.name() + " player) : " + history.getLast());
		currentPlayerColor = PlayerColor.values()[(currentPlayerColor.ordinal() + 1) % PlayerColor.values().length];
		if (isThreatenend(currentPlayerKing())) view.displayMessage("Check !");
	}

	public boolean isThreatenend(Piece p) {
		return Arrays.stream(board)
			.flatMap(Arrays::stream)
			.filter(piece -> piece != null)
			.flatMap(piece -> piece.availableMoves().stream())
			.anyMatch(m -> m.pieceEaten == p);
	}

	public boolean hasMoved(Piece p) {
		return history.stream().anyMatch(m -> m.pieceMoved == p);
	}

	@Override
	public void newGame() {
		resetBoard();
		board = ChessBoardInitializer.standardInitializedBoard(this);
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

	public boolean isOnBoard (Position p) {
		return p.x >= 0 
		&& p.x < WIDTH 
		&& p.y >= 0
		&& p.y < HEIGHT;
	}

	private void resetBoard() {
		for (int x=0; x < WIDTH; ++x) {
			for (int y=0; y < HEIGHT; ++y) {
				board[x][y] = null;
				view.removePiece(x, y);
			}
		}
	}

	private void fillBoardView() {
		this.foreach((x, y) -> {
			Piece p = board[x][y];
			if (p != null) view.putPiece(p.getType(), p.getColor(), x, y);});
	}

	private void uppdateBoardView(Move m) {
		view.putPiece(m.pieceMoved.getType(), m.pieceMoved.getColor(), m.to.x, m.to.y);
		view.removePiece(m.from.x, m.from.y);
		if (m.secondMove != null) uppdateBoardView(m.secondMove);
	}
	
	public void setWhiteKing(King whiteKing) {
		this.whiteKing = whiteKing;
	}
	public void setBlackKing(King blackKing) {
		this.blackKing = blackKing;
	}
	public int width() {
		return WIDTH;
	}
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
}