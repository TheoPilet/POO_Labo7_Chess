package engine;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.BiConsumer;

import chess.ChessController;
import chess.ChessView;
import chess.PieceType;
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

	private PlayerColor winner = null;
	private boolean draw = false;

	@Override
	public void start(ChessView view) {
		this.view = view;
		view.startView();
	}

	@Override
	public boolean move(int fromX, int fromY, int toX, int toY) {
		if (winner != null && !draw) return false; // the game is over if a winner exists or if a draw is declared

		Piece p = board[fromX][fromY];
		if (p == null) return false; // no piece to move at the start position
		if (p.getColor() != currentPlayerColor) return false;

		Position to = new Position(toX, toY);
		Move move = getMoveIfAllowed(p, to);
		if (move == null) return false;

		if (!tryMove(move)) {
			System.out.println("Invalid move " + move);
			return false;
		}
		
		uppdateBoardView(move);
		if (move.pieceMoved.canBePromotedAt(move.to)) promote(move.to);

		nextTurn();

		return true;
	}

	private boolean tryMove(Move move) {
		history.push(move);
		applyMove(move);

		if (!isStateValid()) {
			revertLastMove();
			fillBoardView();
			return false;
		}
		return true;
	}

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

	private Move getMoveIfAllowed(Piece p, Position to) {
		for (Move m : p.availableMoves()) {
			System.out.println(m);
		}
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
		return !isThreatened(currentPlayerKing());
	}

	private Piece currentPlayerKing() {
		return (currentPlayerColor == PlayerColor.WHITE) ? whiteKing : blackKing;
	}

	private void nextTurn() {
		// draw detection according to https://en.wikipedia.org/wiki/Chess#Draw
		LinkedList<Position> piecesPositions = piecesPositions();
		if (																								// DRAW since insufficient material :
			(piecesPositions.size() == 2) ||																	// 2 kings
			(piecesPositions.size() <= 3 && piecesPositions.stream().anyMatch((Position p) ->
				at(p).getType().equals(PieceType.KNIGHT) ||													// 2 kings and 1 knight
				at(p).getType().equals(PieceType.BISHOP))) ||												// 2 kings and 1 bishop
			(piecesPositions.size() == 4 && piecesPositions.stream().allMatch(
				p -> at(p).getType().equals(PieceType.KNIGHT) || at(p).getType().equals(PieceType.KING)))	// 2 kings and only 2 knights
		) {
			draw = true;
			view.displayMessage("Draw !");
			return;
		}

		PlayerColor previousColor = currentPlayerColor;
		currentPlayerColor = PlayerColor.values()[(currentPlayerColor.ordinal() + 1) % PlayerColor.values().length];

		// check, pat and checkmate detection
		Move validMove = currentPlayerHasValidMove();
		/*if (validMove == null) {
			if (isThreatened(currentPlayerKing())) {
				view.displayMessage("Checkmate !");
				winner = previousColor;
			} else {
				view.displayMessage("PAT !");
			}
		} else {
			if (isThreatened(currentPlayerKing())) {
				view.displayMessage("Check !");
			}
		}*/
	}

	private Move currentPlayerHasValidMove() { //TODO: cette fonction a l'air de ne pas fonctionner correctement :()
		return piecesPositions().stream().filter(																		// on ne prend que les pièces qui :
			p -> board[p.x][p.y].getColor().equals(currentPlayerColor) &&												// sont de la couleur du joueur actuel
			board[p.x][p.y].availableMoves().stream().anyMatch((Move move) -> {											// et ont dans leur availaible moves
				if (tryMove(move)) {																					// un move qui passe
					revertLastMove(); // si le move réussit, on l'efface et on retourne qu'il est réussi (true)
					System.out.println("Valid move : " + move);
					return true;
				}
				return false; // sinon, on retourne false
			})).findAny().map((Position p) -> at(p).availableMoves().peek()).orElse(null);
}

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
			.filter(piece -> at(p) != piece)
			.flatMap(piece -> piece.availableMoves().stream())
			.anyMatch(m -> m.to.equals(p));
	}

	public boolean hasMoved(Piece p) {
		return history.stream().anyMatch(m -> m.pieceMoved == p);
	}

	@Override
	public void newGame() {
		newGame(ChessBoardInitializer.standardInitializedBoard(this));
	}

	public void newGame(Piece[][] board) {
		resetBoard();
		this.board = board;
		currentPlayerColor = PlayerColor.WHITE;
		winner = null;
		draw = false;
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

	public LinkedList<Position> piecesPositions() {
		LinkedList<Position> piecesPositions = new LinkedList<>();
		for (int x=0; x < WIDTH; ++x)
			for (int y=0; y < HEIGHT; ++y)
				if (board[x][y] != null)
					piecesPositions.add(new Position(x, y));
		return piecesPositions;
	}
}