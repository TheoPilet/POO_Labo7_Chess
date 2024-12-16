package engine;

import java.util.LinkedList;

import chess.ChessController;
import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;
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
		view.displayMessage("new game (TO REMOVE)"); // TODO
		view.putPiece(PieceType.KING, PlayerColor.BLACK, 3, 4); // TODO exemple uniquement
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
}
