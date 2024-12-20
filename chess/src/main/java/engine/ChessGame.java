package engine;

import java.util.Arrays;
import java.util.LinkedList;

import chess.ChessController;
import chess.ChessView;
import chess.PlayerColor;
import engine.pieces.King;
import engine.pieces.Rook;
import engine.utils.ChessBoardInitializer;
import engine.utils.Position;

public class ChessGame implements ChessController {

	private ChessView view;

	private final int WIDTH = 8;
	private final int HEIGHT = 8;
	private Piece[][] board = new Piece[WIDTH][HEIGHT];

	private LinkedList<Move> history = new LinkedList<>();

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
		System.out.println(String.format("TO REMOVE : from (%d, %d) to (%d, %d)", fromX, fromY, toX, toY)); // TODO remove
		Piece p = board[fromX][fromY];
		if (p == null) return false; // no piece to move at the start position
		if (p.getColor() != currentPlayerColor) return false;

		Position from = new Position(fromX, fromY);
		Position to = new Position(toX, toY);

		Move move = getMoveIfAllowed(p, from, to);
		if (move == null) return false;

		applyMove(move);

		if (!isStateValid()) {
			revertLastMove();
			return false;
		}

		//TODO : check promotion

		uppdateBoardView(move);

		nextTurn();
		return true;
	}

	public Move getMoveIfAllowed(Piece p, Position from, Position to) {
		return new Move(p, new Rook(PlayerColor.WHITE, this), from, to); // test code
		/*for (Move m : p.availableMoves()) {
			if (m.from.equals(from) && m.to.equals(to)) {
				return m;
			}
		}
		return null;*/
	}

	private void applyMove (Move m) {
		history.add(m);
		board[m.to.x()][m.to.y()] = m.pieceMoved;
		board[m.from.x()][m.from.y()] = null;
	}

	private void revertLastMove () {
		Move m = history.pop();
		revertMove(m);
	}
	private void revertMove(Move m) {
		board[m.from.x()][m.from.y()] = board[m.to.x()][m.to.y()];
		board[m.to.x()][m.to.y()] = m.pieceEaten;
		if (m.secondMove != null) revertMove(m.secondMove);
	}

	private boolean isStateValid() {
		return isThreatenend((currentPlayerColor == PlayerColor.WHITE) ? whiteKing : blackKing);
	}

	private void nextTurn() {
		currentPlayerColor = PlayerColor.values()[(currentPlayerColor.ordinal() + 1) % PlayerColor.values().length];
		System.out.println("Turn " + history.size() + " (" + currentPlayerColor.name() + " player) : " + history.getLast());
	}

	public boolean isThreatenend(Piece p) { // TODO: remove if not used
		return Arrays.stream(board)
			.flatMap(Arrays::stream)
			.filter(piece -> piece != null)
			.flatMap(piece -> piece.availableMoves().stream())
			.anyMatch(m -> m.pieceEaten == p);
	}

	public boolean isThreatened(Position p) { // TODO: remove if not used
		return Arrays.stream(board)
			.flatMap(Arrays::stream)
			.filter(piece -> piece != null)
			.flatMap(piece -> piece.availableMoves().stream())
			.anyMatch(m -> m.to.equals(p));
	}

	public boolean hasMoved(Piece p) {
		return history.stream().anyMatch(m -> m.pieceMoved == p);
	}

	@Override
	public void newGame() {
		resetBoard();
		board = ChessBoardInitializer.standardInitializedBoard(this);
		updateBoardView();
	}

	/**
	 * Look at a position on the chess board, and get the Piece if there is one.
	 * @param p the position on the board we inspect
	 * @return null if there is no pieces at this position, the Piece if there is one.
	 */
	public Piece at(Position p) {
		if (!isOnBoard(p)) return null;
		return board[p.x()][p.y()];
	}

	public boolean isOnBoard (Position p) {
		return p.x() >= 0 
		&& p.x() < WIDTH 
		&& p.y() >= 0
		&& p.y() < HEIGHT;
	}

	private void resetBoard() {
		for (int x=0; x < WIDTH; ++x) {
			for (int y=0; y < HEIGHT; ++y) {
				board[x][y] = null;
				view.removePiece(x, y);
			}
		}
	}

	private void updateBoardView() {
		for (int x=0; x < WIDTH; ++x) {
			for (int y=0; y < HEIGHT; ++y) {
				Piece p = board[x][y];
				if (p != null) view.putPiece(p.getType(), p.getColor(), x, y);
			}
		}
	}

	private void uppdateBoardView(Move m) {
		view.removePiece(m.from.x(), m.from.y());
		if (m.pieceEaten != null) view.removePiece(m.to.x(), m.to.y()); // TODO: necessary ?
		view.putPiece(m.pieceMoved.getType(), m.pieceMoved.getColor(), m.to.x(), m.to.y());
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
}