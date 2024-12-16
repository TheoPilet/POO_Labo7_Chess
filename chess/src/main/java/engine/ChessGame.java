package engine;

import java.util.LinkedList;

import chess.ChessController;
import chess.ChessView;
import chess.PlayerColor;
import engine.pieces.Bishop;
import engine.pieces.King;
import engine.pieces.Knight;
import engine.pieces.Queen;
import engine.pieces.Rook;
import engine.utils.Position;

public class ChessGame implements ChessController {

	private ChessView view;

	private final int WIDTH = 10;
	private final int HEIGHT = 10;
	private Piece[][] board = new Piece[WIDTH][HEIGHT];

	private LinkedList<Move> history = new LinkedList<>();

	@Override
	public void start(ChessView view) {
		this.view = view;
		view.startView();
	}

	@Override
	public boolean move(int fromX, int fromY, int toX, int toY) {
		System.out.println(String.format("TO REMOVE : from (%d, %d) to (%d, %d)", fromX, fromY, toX, toY)); // TODO remove
		return false; // TODO
	}

	@Override
	public void newGame() {
		resetBoard();
		initializeBoard();
		updateBoardView();
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

	public boolean isOnBoard (Position p) {
		return p.x >= 0 
		&& p.x < WIDTH 
		&& p.y >= 0
		&& p.y < HEIGHT;
	}

	private void initializeBoard () {
	PlayerColor white = PlayerColor.WHITE;
		PlayerColor black = PlayerColor.BLACK;

		Rook r1 = new Rook(white, this);
		Rook r2 = new Rook(white, this);
		Rook br1 = new Rook(black, this);
		Rook br2 = new Rook(black, this);

		LinkedList<Piece> whitePieces = new LinkedList<>(Arrays.asList(
			r1,
			new Knight(white, this),
			new Bishop(white, this),
			new King(white, this, r1, r2),
			new Queen(white, this),
			new Bishop(white, this),
			new Knight(white, this),
			r2
		));

		LinkedList<Piece> blackPieces = new LinkedList<>(Arrays.asList(
			br1,
			new Knight(black, this),
			new Bishop(black, this),
			new King(black, this, br1, br2),
			new Queen(black, this),
			new Bishop(black, this),
			new Knight(black, this),
			br2
		));

		for (int x=0, y=0; x < WIDTH; ++x) board[x][y] = whitePieces.pop();
		for (int x=0, y=1; x < WIDTH; ++x) board[x][y] = new Rook(white, this);
		for (int x=0, y=HEIGHT-1; x < WIDTH; ++x) board[x][y] = blackPieces.pop();
		for (int x=0, y=HEIGHT-2; x < WIDTH; ++x) board[x][y] = new Rook(white, this);
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
				view.putPiece(p.getType(), p.getColor(), x, y);
			}
		}
	}
}
